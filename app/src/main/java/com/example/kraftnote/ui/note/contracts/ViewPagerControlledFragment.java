package com.example.kraftnote.ui.note.contracts;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.lang.ref.WeakReference;

public abstract class ViewPagerControlledFragment extends Fragment {
    private WeakReference<ViewPager2> viewPagerWeakRef;

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
}
