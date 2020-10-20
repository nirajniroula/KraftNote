package com.example.kraftnote.ui.note.contracts;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kraftnote.persistence.entities.Note;

import java.lang.ref.WeakReference;

public abstract class NoteEditorChildBaseFragment extends Fragment {
    private WeakReference<ViewPager2> viewPagerWeakRef;
    private Note note;

    public final void setViewPagerWeakRef(WeakReference<ViewPager2> viewPagerWeakRef) {
        this.viewPagerWeakRef = viewPagerWeakRef;
    }

    public final WeakReference<ViewPager2> getViewPagerWeakRef() {
        return viewPagerWeakRef;
    }

    public void onFragmentVisible() {
    }

    public final void updateViewPagerScrollBehaviour(boolean allowViewChangeOnSwipeGesture) {
        if (getViewPagerWeakRef().get() == null) return;

        Log.d("NoteEditor", "Allow Scroll " + allowViewChangeOnSwipeGesture);

        getViewPagerWeakRef().get().setUserInputEnabled(allowViewChangeOnSwipeGesture);
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }
}
