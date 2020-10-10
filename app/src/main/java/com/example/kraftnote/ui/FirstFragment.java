package com.example.kraftnote.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;
import com.example.kraftnote.ui.category.CategoryTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FirstFragment extends Fragment {
    private CategoryViewModel categoryViewModel;
    private List<Category> categories = new ArrayList<>();
    private int count = 0;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);

    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final HashMap<Integer, Category> map = new HashMap<>();
        final Random random = new Random();

        final CategoryTabLayout categoryTabLayout = view.findViewById(R.id.tabs);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_CategoryFragment_to_NoteFragment);
            }
        });

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Category category = categories.get(random.nextInt(categories.size()));
                categoryViewModel.delete(category);
            }
        });

        view.findViewById(R.id.button_third).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Category category = categories.get(random.nextInt(categories.size()));
                category.setName("Category Updated" + " " + (count ++));
                categoryViewModel.update(category);
            }
        });

        view.findViewById(R.id.button_forth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryViewModel.insert(new Category("New Category" + " " + (count ++)));
            }
        });

        categoryViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                FirstFragment.this.categories = categories;

                categoryTabLayout.sync(categories);

                Toast.makeText(getContext(), "Category updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
