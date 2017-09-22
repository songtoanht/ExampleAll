package com.zalologin.ebook;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * //Todo
 * <p>
 * Created by HOME on 9/22/2017.
 */

public class LoadPageScript {
    @JavascriptInterface
    public void onLoad() {
        Log.d("ttt", "Yes");
//        onLoadCompleted();
    }
}
