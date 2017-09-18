package com.zalologin;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * //Todo
 * <p>
 * Created by HOME on 7/11/2017.
 */

public class CustomHeaderViewpager extends FrameLayout {
    public CustomHeaderViewpager(@NonNull Context context) {
        super(context);
    }

    public CustomHeaderViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHeaderViewpager(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
