package com.example.kraftnote.utils;

import android.text.Spannable;
import android.text.Spanned;

import androidx.core.text.HtmlCompat;

public class HtmlParser {
    public static String toHtml(Spannable spannable) {
        if (spannable == null) return "";

        return HtmlCompat.toHtml(spannable, HtmlCompat.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL);
    }

    public static Spanned toSpanned(String html) {
        return HtmlCompat.fromHtml(
                html == null ? "" : html, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH);
    }
}
