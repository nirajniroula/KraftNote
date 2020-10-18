package com.example.kraftnote.utils.watchers;

import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.TextWatcher;
import android.widget.EditText;

public class PlainTextFormatWatcher implements TextWatcher {

    private final EditText edittext;

    public PlainTextFormatWatcher(EditText editText) {
        this.edittext = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Editable editable = edittext.getText();

        if (editable == null) return;

        edittext.removeTextChangedListener(this);

        Object[] spans = editable.getSpans(0, editable.toString().length(), Object.class);

        for (Object span : spans) {
            if (span instanceof ParcelableSpan) {
                editable.removeSpan(span);
            }
        }

        edittext.addTextChangedListener(this);
    }
}
