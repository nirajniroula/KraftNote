package com.example.kraftnote.ui.note;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.FragmentAddEditNoteBinding;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;
import com.example.kraftnote.ui.note.contracts.ViewPagerControlledFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorImageFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorRecordingFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorReminderFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorTitleBodyFragment;
import com.example.kraftnote.ui.note.editor.NoteEditorTodoFragment;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.ref.WeakReference;
import java.util.List;

public class AddEditNoteFragment extends Fragment {
    private static final String TAG = AddEditNoteFragment.class.getSimpleName();

    private FragmentAddEditNoteBinding binding;

    private NavController navController;
    private CategoryViewModel categoryViewModel;
    private FragmentCollectionAdapter fragmentCollectionAdapter;

    // This callback will only be called when AddUpdateNoteFragment is at least started
    private final
    OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            AddEditNoteFragment.this.gotoNoteFragment();
        }
    };

    private final
    ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            binding.viewpager.setUserInputEnabled(true);
            binding.viewpager.setOffscreenPageLimit(5);
            fragmentCollectionAdapter.getFragment(position).onFragmentVisible();
        }
    };

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), onBackPressedCallback);

        View root = inflater.inflate(R.layout.fragment_add_edit_note, container, false);
        binding = FragmentAddEditNoteBinding.bind(root);

        return root;
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        fragmentCollectionAdapter = new FragmentCollectionAdapter(this);
        navController = NavHostFragment.findNavController(this);

        binding.viewpager.setAdapter(fragmentCollectionAdapter);

        new TabLayoutMediator(
                binding.tabs, binding.viewpager,
                (tab, position) ->
                        tab.setText(fragmentCollectionAdapter.getFragmentName(position))
        ).attach();

        binding.viewpager.registerOnPageChangeCallback(onPageChangeCallback);
    }

    private void listenEvents() {
        categoryViewModel.getAll().observe(getViewLifecycleOwner(), this::categoriesMutated);

        binding.closeEditorButton.setOnClickListener(v -> gotoNoteFragment());
    }

    private void categoriesMutated(List<Category> categories) {
        Log.d(TAG, "Category Mutated " + categories.size());
    }

    private void gotoNoteFragment() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder
                .setTitle(R.string.confirmation_required)
                .setMessage(R.string.do_you_want_to_discard_the_changes)
                .setCancelable(false)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, (dialog, which) -> navController.navigate(R.id.action_AddUpdateNoteFragment_to_NoteFragment))
                .show();
    }

    private final class FragmentCollectionAdapter extends FragmentStateAdapter {
        private NameAndFragmentTuple[] nameAndFragmentTupleList = new NameAndFragmentTuple[]{
                new NameAndFragmentTuple(R.string.note, new NoteEditorTitleBodyFragment()),
                new NameAndFragmentTuple(R.string.todos, new NoteEditorTodoFragment()),
                new NameAndFragmentTuple(R.string.images, new NoteEditorImageFragment()),
                new NameAndFragmentTuple(R.string.reminders, new NoteEditorReminderFragment()),
                new NameAndFragmentTuple(R.string.recordings, new NoteEditorRecordingFragment()),
        };

        public FragmentCollectionAdapter(@NonNull Fragment fragment) {
            super(fragment);

            final WeakReference<ViewPager2> viewPager2WeakRef = new WeakReference<>(binding.viewpager);

            for (NameAndFragmentTuple tuple : nameAndFragmentTupleList) {
                tuple.getFragment().setViewPagerWeakRef(viewPager2WeakRef);
            }
        }

        public int getFragmentName(int position) {
            return nameAndFragmentTupleList[position].getNameResId();
        }

        @NonNull
        @Override
        public ViewPagerControlledFragment createFragment(int position) {
            return getFragment(position);
        }

        public ViewPagerControlledFragment getFragment(int position) {
            return nameAndFragmentTupleList[position].getFragment();
        }

        @Override
        public int getItemCount() {
            return nameAndFragmentTupleList.length;
        }
    }

    private final static class NameAndFragmentTuple {
        private final int nameResId;
        private final ViewPagerControlledFragment fragment;

        public NameAndFragmentTuple(int nameResId, ViewPagerControlledFragment fragment) {
            this.nameResId = nameResId;
            this.fragment = fragment;
        }

        public final int getNameResId() {
            return nameResId;
        }

        public final ViewPagerControlledFragment getFragment() {
            return fragment;
        }
    }
}
