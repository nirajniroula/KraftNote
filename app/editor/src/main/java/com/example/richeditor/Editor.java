package com.example.richeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

final public class Editor extends WebView {
    public static final String EDITOR_ASSET_URL = "file:///android_asset/editor.html";
    private final WebSettings WEB_SETTINGS = getSettings();

    private boolean editable = false;
    private boolean isReady = false;
    private String htmlContent = "";

    public Editor(@NonNull Context context) {
        this(context, null);
    }

    public Editor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Editor,
                0, 0);

        try {
            editable = a.getBoolean(R.styleable.Editor_editable, false);
        } finally {
            a.recycle();
        }

        setup(context);
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void setup(Context context) {
        WEB_SETTINGS.setJavaScriptEnabled(true);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(getClass().toString(), String.format("%s @ %d: %s", cm.message(),
                        cm.lineNumber(), cm.sourceId()));

                return super.onConsoleMessage(cm);
            }
        });

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isReady = true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final String url = request.getUrl().toString();

                return super.shouldOverrideUrlLoading(view, request);
            }
        });

//        webSettings.setAllowFileAccess(true);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setAllowUniversalAccessFromFileURLs(true);

        addJavascriptInterface(new OnTextChangeListener() {
            @Override
            @JavascriptInterface
            public void onChance(String html) {
                htmlContent = html;
                Log.d(this.getClass().toString(), html);
            }
        }, "EditorEventListener");

        loadUrl(EDITOR_ASSET_URL);
    }

    public boolean isEditable() {
        return editable;
    }

    public String getHTML() {
        return htmlContent;
    }

    public void setHTML(String html) {
        html = (html != null) ? html : "";

        try {
            htmlContent = URLEncoder.encode(html, "UTF-8");
            runJavaScript("setHtml", htmlContent);
        } catch (UnsupportedEncodingException e) { /** (╯°□°)╯︵ ┻━┻ **/}
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        runJavaScript("setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
                + "px');");
    }

    public void setPlaceholder(String placeholder) {
        runJavaScript("setPlaceholder", placeholder);
    }

    public void setInputEnabled(Boolean inputEnabled) {
        editable = inputEnabled;
        runJavaScript("setInputEnabled", inputEnabled);
    }

    public void undo() {
        runJavaScript("undo");
    }

    public void redo() {
        runJavaScript("redo");
    }

    public void setBold() {
        runJavaScript("setBold");
    }

    public void setItalic() {
        runJavaScript("setItalic");
    }

    public void setSubscript() {
        runJavaScript("setSubscript");
    }

    public void setSuperscript() {
        runJavaScript("setSuperscript");
    }

    public void setStrikeThrough() {
        runJavaScript("setStrikeThrough");
    }

    public void setUnderline() {
        runJavaScript("setUnderline");
    }

    public void setHeading(int heading) {
        runJavaScript("setHeading", heading);
    }

    public void setAlignLeft() {
        runJavaScript("setJustifyLeft");
    }

    public void setAlignCenter() {
        runJavaScript("setJustifyCenter");
    }

    public void setAlignRight() {
        runJavaScript("setJustifyRight");
    }

    public void setBlockQuote() {
        runJavaScript("setBlockQuote");
    }

    public void insertLink(String href, String title) {
        runJavaScript("prepareInsert");
        runJavaScript("insertLink", href, title);
    }

    public void focusEditor() {
        requestFocus();
        runJavaScript("focus");
    }

    public void clearFocusEditor() {
        runJavaScript("blurFocus");
    }

    private void runJavaScript(final String methodName, final Object... params) {
        if (isReady) {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("javascript:try { ");
            stringBuilder.append("Editor.").append(methodName);
            stringBuilder.append("(");
            String separator = "";

            for (Object param : params) {
                stringBuilder.append(separator);
                separator = ",";

                if (param instanceof String) {
                    stringBuilder.append("'");
                }

                stringBuilder.append(param.toString().replace("'", "\\'"));

                if (param instanceof String) {
                    stringBuilder.append("'");
                }

            }

            stringBuilder.append(")}catch(error){console.error(error.message);}");

            final String script = stringBuilder.toString();

            evaluateJavascript(script, null);

            Log.i(getClass().toString(), "Run JS Method: " + methodName);

        } else {
            postDelayed(() -> runJavaScript(methodName, params), 100);
        }
    }

    private interface OnTextChangeListener {
        @JavascriptInterface
        void onChance(String html);
    }
}
