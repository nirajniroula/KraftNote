package com.example.kraftnote.ui.note.editor.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;


/**
 * An Android EditText, that reacts to parent scroll events. If clicked,
 * intercepts parent event and scrolls itself as long as the text inside
 * is scrollable. If not, gives back the control of a scroll event to a parent.
 */
public class InputText extends TextInputEditText {
    public InputText(@NonNull Context context) {
        super(context);
        addOnTouchListener();
    }

    public InputText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnTouchListener();
    }

    public InputText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addOnTouchListener();
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addOnTouchListener() {
        this.setOnTouchListener(new ScrollableOnTouchListener());
    }

    private class ScrollableOnTouchListener implements OnTouchListener {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() != InputText.this.getId()) {
                return false;
            }
            v.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_MOVE:
                    if (checkIfEditTextOnTopAndScrollUp(v, event)) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    if (checkIfEditTextOnBottomAndScrollDown(v, event)) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        }

        private boolean checkIfEditTextOnTopAndScrollUp(View v, MotionEvent event) {
            if (v.getScrollY() == 0) {
                if (event.getHistorySize() > 0 && event.getHistoricalY(0) <= event.getY()) {
                    return true;
                }
            }
            return false;
        }

        private boolean checkIfEditTextOnBottomAndScrollDown(View v, MotionEvent event) {
            float scrollY = v.getScrollY();
            if (scrollY == 0) {
                return true;
            }
            float maxHeight = computeVerticalScrollRange() - getLineHeight() * getMaxLines();
            float topScrolledPosition = scrollY / maxHeight;
            float roundedVal = topScrolledPosition * 1000;
            roundedVal = Math.round(roundedVal) + 20 / getContext().getResources().getDisplayMetrics().scaledDensity;
            roundedVal = roundedVal / 1000;
            if (roundedVal >= 1) {
                if (event.getHistorySize() > 0 && event.getHistoricalY(0) >= event.getY()) {
                    return true;
                }
            }
            return false;
        }
    }
}
