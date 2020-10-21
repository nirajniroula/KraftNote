package com.example.kraftnote.ui.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class FirstLastItemSpacesDecoration extends RecyclerView.ItemDecoration {

    private final int spaces;

    public FirstLastItemSpacesDecoration(int spaces) {
        this.spaces = spaces;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            outRect.bottom = spaces * 10;
        }
    }
}
