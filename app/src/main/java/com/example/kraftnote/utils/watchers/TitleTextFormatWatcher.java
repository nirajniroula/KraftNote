package com.example.kraftnote.utils.watchers;

import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;

public class TitleTextFormatWatcher implements TextWatcher {
    private static final String TAG = TitleTextFormatWatcher.class.getSimpleName();

    private final TextInputEditText editText;
    private final OnTextChangedListener listener;

    public TitleTextFormatWatcher(TextInputEditText editText, OnTextChangedListener listener) {
        this.editText = editText;
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        final Editable editable = editText.getText();

        if (editable == null) return;

        editText.removeTextChangedListener(this);

        final ParcelableSpan[] spans = editable.getSpans(0, editable.length() - 1, ParcelableSpan.class);

        for (ParcelableSpan span : spans) {
            editable.removeSpan(span);
        }

        String text = editable.toString();

        if (text.contains("\n")) {
            int cursor = editText.getSelectionStart();

            if(cursor == -1) cursor = 0;

            editText.setText(text.replaceAll("\n", " "));
            editText.setSelection(cursor);

            Log.d(TAG, String.format("New Line found - cursor at %d", cursor));
        }

        if(listener != null) {
            listener.onChanged(editText.getText().toString());
        }

        editText.addTextChangedListener(this);
    }
}
