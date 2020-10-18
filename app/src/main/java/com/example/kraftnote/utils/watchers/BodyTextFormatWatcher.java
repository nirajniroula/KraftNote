package com.example.kraftnote.utils.watchers;

import android.text.Editable;
import android.text.Html;
import android.text.ParcelableSpan;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import androidx.core.text.HtmlCompat;

import com.example.kraftnote.utils.HtmlParser;
import com.google.android.material.textfield.TextInputEditText;

public class BodyTextFormatWatcher implements TextWatcher {
    private final TextInputEditText editText;
    private final OnTextChangedListener listener;


    public BodyTextFormatWatcher(TextInputEditText editText, OnTextChangedListener listener) {
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

        final Object[] spans = editable.getSpans(0, editable.length() - 1, Object.class);

        for (Object span : spans) {
            if (span instanceof ParcelableSpan) {

                if (
                        span instanceof StyleSpan
                                || span instanceof UnderlineSpan
                                || span instanceof StrikethroughSpan
                ) {
                    continue;
                }

                editable.removeSpan(span);
            }
        }

        if (listener != null) {
            listener.onChanged(HtmlParser.toHtml(editText.getText()));
        }

        editText.addTextChangedListener(this);
    }
}
