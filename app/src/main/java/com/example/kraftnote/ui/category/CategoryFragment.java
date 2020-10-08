package com.example.kraftnote.ui.category;

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

import com.example.kraftnote.persistence.entities.Category;
import com.example.kraftnote.persistence.viewmodels.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment {
    private CategoryViewModel categoryViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);




        categoryViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                // Todo: update RecyclerView
                Toast.makeText(getContext(), "Category updated", Toast.LENGTH_SHORT).show();
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
