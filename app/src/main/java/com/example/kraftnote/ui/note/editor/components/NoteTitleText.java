package com.example.kraftnote.ui.note.editor.components;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.kraftnote.R;
import com.example.kraftnote.databinding.ComponentNoteTitleTextBinding;
import com.example.kraftnote.utils.watchers.TitleTextFormatWatcher;
import com.google.android.material.textfield.TextInputEditText;

public class NoteTitleText extends LinearLayout {
    private ComponentNoteTitleTextBinding binding;
    private MutableLiveData<String> title;

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
        binding = ComponentNoteTitleTextBinding.bind(view);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        title = new MutableLiveData<>("");
    }

    private void listenEvents() {
        binding.noteTitleInputText.setOnEditorActionListener((v, actionId, event) -> actionId == 0);

        binding.noteTitleInputText.addTextChangedListener(new TitleTextFormatWatcher(binding.noteTitleInputText, title -> {
            this.title.setValue(title);
        }));
    }

    public void setTitle(String title) {
        binding.noteTitleInputText.setText(title);
    }

    public LiveData<String> getTitle() {
        return title;
    }
}
