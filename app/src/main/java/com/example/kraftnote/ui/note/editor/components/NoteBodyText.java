package com.example.kraftnote.ui.note.editor.components;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.ComponentNoteBodyTextBinding;
import com.example.kraftnote.utils.watchers.BodyTextFormatWatcher;
import com.google.android.material.textfield.TextInputLayout;

public class NoteBodyText extends TextInputLayout {
    private static final String TAG = NoteBodyText.class.getSimpleName();

    private ComponentNoteBodyTextBinding binding;

    public NoteBodyText(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NoteBodyText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoteBodyText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        final View view = inflate(getContext(), R.layout.component_note_body_text, this);
        binding = ComponentNoteBodyTextBinding.bind(view);

        initializeProperties(view);
        listenEvents(context);
    }

    private void initializeProperties(View view) {
        binding.bodyTextarea.setScroller(new Scroller(getContext()));
        binding.bodyTextarea.setVerticalScrollBarEnabled(true);
    }

    private void listenEvents(Context context) {
        binding.bodyTextarea.setCustomSelectionActionModeCallback(actionModeCallback);
        binding.bodyTextarea.addTextChangedListener(new BodyTextFormatWatcher(binding.bodyTextarea));
    }

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.note_editor_context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            final Editable editable = binding.bodyTextarea.getText();

            if (editable == null) return false;

            int startIndex = binding.bodyTextarea.getSelectionStart();
            int endIndex = binding.bodyTextarea.getSelectionEnd();

            if (startIndex < 0) return false;

            if (item.getItemId() == R.id.note_editor_context_menu_bold) {
                editable.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.d(TAG, "BOLD");
            } else if (item.getItemId() == R.id.note_editor_context_menu_italic) {
                editable.setSpan(new StyleSpan(Typeface.ITALIC), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.d(TAG, "ITALIC");
            } else if (item.getItemId() == R.id.note_editor_context_menu_normal) {
                Object[] spans = editable.getSpans(startIndex, endIndex, Object.class);

                for (Object span : spans) {
                    if (span instanceof ParcelableSpan) {
                        editable.removeSpan(span);
                    }
                }

                Log.d(TAG, "NORMAL");
            } else if (item.getItemId() == R.id.note_editor_context_menu_strikethrough) {
                editable.setSpan(new StrikethroughSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.d(TAG, "STRIKE-THROUGH");
            } else if (item.getItemId() == R.id.note_editor_context_menu_underline) {
                editable.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.d(TAG, "UNDERLINE");
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            ActionMode actionMode = null;
        }
    };
}
