package com.example.kraftnote.ui.category.components;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kraftnote.R;
import com.example.kraftnote.persistence.entities.Category;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class CategoryTabLayout extends TabLayout {
    private Map<Integer, Category> categories = new HashMap<>();
    private Consumer<Category> onTabSelectedListener;
    private Category selected;

    public CategoryTabLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CategoryTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        removeAllTabs();
        addFirstTab();

        addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                selected = (Category) tab.getTag();
                onTabSelectedListener.accept(selected);
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }

    public Category getSelected() {
        return selected;
    }

    public void setOnTabSelectedListener(Consumer<Category> onTabSelectedListener) {
        this.onTabSelectedListener = onTabSelectedListener;
    }

    private void addFirstTab() {
        Tab allTab = newTab();
        allTab.setText(R.string.all);
        allTab.setTag(null);
        addTab(allTab);
    }


    public void sync(List<Category> categoryList) {
        List<Tab> tabsToRemove = new ArrayList<>();
        HashMap<Integer, Category> updatedCategories = new HashMap<>();

        for (int i = 0; i < getTabCount(); i++) {
            boolean categoryForTabExists = false;

            Tab tab = getTabAt(i);

            if (tab == null || tab.getTag() == null) continue;

            Category _category = (Category) tab.getTag();

            for (Category category : categoryList) {
                // update if match found
                if (Objects.equals(_category.getId(), category.getId())) {
                    tab.setText(category.getName());
                    tab.setTag(category);
                    categories.put(category.getId(), category);
                    updatedCategories.put(category.getId(), category);
                    categoryForTabExists = true;
                    break;
                }
            }

            // no match found remove category and add tag for removal
            if (!categoryForTabExists) {
                categories.remove(_category.getId());
                tabsToRemove.add(tab);
            }
        }

        // remove unwanted tabs
        for(Tab tab: tabsToRemove) {
            removeTab(tab);
        }

        // add new tabs
        for(Category category: categoryList) {
            // was category's tab updated
            if(updatedCategories.containsKey(category.getId()))  continue;

            // add new tab for the category
            Tab tab = newTab();
            tab.setText(category.getName());
            tab.setTag(category);
            addTab(tab);
            categories.put(category.getId(), category);
        }
    }
}
