package com.zalologin;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utilities for getting screen size or convert "dp" to "px".
 */
public final class ScreenUtil {
    private static final String TAG = ScreenUtil.class.getSimpleName();

    private ScreenUtil() {
        // no instance
    }

    /**
     * This method is used to get height of screen.
     *
     * @param context context
     * @return return height screen in pixel
     */
    public static int getHeightScreen(Context context) {
        return getScreenSize(context).getHeight();
    }

    /**
     * This method is used to get width of screen.
     *
     * @param context context
     * @return return width of screen in pixel
     */
    public static int getWidthScreen(Context context) {
        return getScreenSize(context).getWidth();
    }

    public static ScreenSize getScreenSize(Context context) {

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        // For JellyBean 4.2 (API 17) and onward
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getRealMetrics(displayMetrics);
            return new ScreenSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
        }

        Method getRawH;
        Method getRawW;
        try {
            getRawH = Display.class.getMethod("getRawHeight");
            getRawW = Display.class.getMethod("getRawWidth");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "NoSuchMethodException error: ", e);
            return new ScreenSize(0, 0);
        }

        try {
            return new ScreenSize((int) getRawW.invoke(display), (int) getRawH.invoke(display));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Log.e(TAG, "error: ", e);
            return new ScreenSize(0, 0);
        }
    }

    /**
     * @param context is current context
     * @param spValue is value you want to convert for
     * @return return value in pixel
     */
    public static int convertSPtoPX(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return Math.round(spValue * fontScale);
    }

    /**
     * Convert dp to pixel
     *
     * @param dp      dp value
     * @param context content
     * @return return value in pixel
     */
    public static int convertDpToPixel(Context context, int dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    /**
     * Class that manage the size of screen.
     */
    public static class ScreenSize {
        private int width;
        private int height;

        ScreenSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
