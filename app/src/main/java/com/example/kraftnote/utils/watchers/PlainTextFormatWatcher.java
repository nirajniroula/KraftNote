package com.example.kraftnote.utils.watchers;

import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.TextWatcher;
import android.widget.EditText;

public class PlainTextFormatWatcher implements TextWatcher {

    private final EditText edittext;
    private final Runnable callback;

    public PlainTextFormatWatcher(EditText editText, Runnable callback) {
        this.edittext = editText;
        this.callback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(callback == null) return;

        callback.run();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Editable editable = edittext.getText();

        if (editable == null) return;

        edittext.removeTextChangedListener(this);

        ParcelableSpan[] spans = editable.getSpans(0, editable.toString().length(), ParcelableSpan.class);

        for (ParcelableSpan span : spans) {
            editable.removeSpan(span);
        }

        edittext.addTextChangedListener(this);

        if(callback == null) return;

        callback.run();
    }
}
