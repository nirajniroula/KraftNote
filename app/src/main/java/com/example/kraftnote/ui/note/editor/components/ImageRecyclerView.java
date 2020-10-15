package com.example.kraftnote.ui.note.editor.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.ImageCardBinding;
import com.example.kraftnote.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;

public class ImageRecyclerView extends RecyclerView {
    private List<String> images = new ArrayList<>();
    private ImageAdapter adapter;
    private FileHelper fileHelper;

    public ImageRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ImageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        adapter = new ImageAdapter();
        fileHelper = new FileHelper(context);

        setAdapter(adapter);
        setLayoutManager(new LinearLayoutManager(context));
        setHasFixedSize(true);

        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());

        addItemDecoration(new FirstLastItemSpacesDecoration(space));
    }

    public void setImages(List<String> images) {
        this.images = images;
        adapter.syncImages(images);
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {
        private List<String> images = new ArrayList<>();

        @NonNull
        @Override
        public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_card, parent, false);

            ImageHolder holder = new ImageHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
            Bitmap image = fileHelper.readImage(images.get(position));
            holder.binding.image.setImageBitmap(image);
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        public void syncImages(List<String> images) {
            this.images = images;
            notifyDataSetChanged();
        }
    }

    private static class ImageHolder extends RecyclerView.ViewHolder {
        private ImageCardBinding binding;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            binding = ImageCardBinding.bind(itemView);
        }
    }

    private static class FirstLastItemSpacesDecoration extends RecyclerView.ItemDecoration {

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
}
