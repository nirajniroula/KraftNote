package com.example.kraftnote.ui.note.editor.components;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.kraftnote.R;
import com.example.kraftnote.utils.watchers.TitleTextFormatWatcher;
import com.google.android.material.textfield.TextInputEditText;

public class NoteTitleText extends LinearLayout {
    private TextInputEditText noteTitleEditText;

    public NoteTitleText(Context context) {
        super(context);
        init(context);
    }

    public NoteTitleText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoteTitleText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public NoteTitleText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        final View view = inflate(getContext(), R.layout.component_note_title_text, this);

        initializeProperties(view);
        listenEvents();
    }

    private void initializeProperties(View view) {
        noteTitleEditText = view.findViewById(R.id.note_title_input_text);
    }

    private void listenEvents() {
        noteTitleEditText.setOnEditorActionListener((v, actionId, event) -> actionId == 0);
        noteTitleEditText.addTextChangedListener(new TitleTextFormatWatcher(noteTitleEditText));
    }

    public void setTitle(String title) {
        noteTitleEditText.setText(title);
    }

    public String getTitle() {
        final Editable text = noteTitleEditText.getText();

        return text != null ? text.toString() : "";
    }
}
