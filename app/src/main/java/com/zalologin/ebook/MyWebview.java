package com.zalologin.ebook;

import android.content.Context;
import android.webkit.WebView;

/**
 * //Todo
 * <p>
 * Created by HOME on 9/15/2017.
 */

public class MyWebview extends WebView {
    public MyWebview(Context context) {
        super(context);
        setHorizontalFadingEdgeEnabled(true);

        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDisplayZoomControls(false);
        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setVerticalScrollBarEnabled(false);
        getSettings().setAllowFileAccess(true);
        getSettings().setAllowFileAccessFromFileURLs(true);
        getSettings().setAllowContentAccess(true);
        getSettings().setAllowUniversalAccessFromFileURLs(true);
    }


}
