package com.example.kraftnote.ui.note.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kraftnote.R;

public class TitleBodyEditorComponent extends RelativeLayout {
    private static final String TAG = TitleBodyEditorComponent.class.getSimpleName();

    private LinearLayout noteRemindersLinearLayout;

    private TextView locationReminderTextView;
    private TextView datetimeReminderTextView;

    private NoteTitleText noteTitleInputTextLayout;
    private NoteBodyText noteBodyTextInputText;

    private View view;

    public TitleBodyEditorComponent(Context context) {
        super(context);
        init(context);
    }

    public TitleBodyEditorComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBodyEditorComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TitleBodyEditorComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        view = inflate(getContext(), R.layout.component_title_body_editor, this);

        initializeProperties();
        listenEvents();
    }

    private void initializeProperties() {
        noteRemindersLinearLayout = view.findViewById(R.id.note_reminders_linear_layout);
        locationReminderTextView = view.findViewById(R.id.location_reminder_text_view);
        datetimeReminderTextView = view.findViewById(R.id.datetime_reminder_text_view);
        noteTitleInputTextLayout = view.findViewById(R.id.note_title);
        noteBodyTextInputText = view.findViewById(R.id.editor_body_text);
    }

    private void listenEvents() {

    }
}
