package com.example.kraftnote.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;

import java.util.List;

public class SecondFragment extends Fragment {
    private CategoryViewModel categoryViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_CategoryFragment_to_NoteFragment);
            }
        });

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);




        categoryViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                // Todo: update RecyclerView
                Toast.makeText(getContext(), "Category updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
