package com.zalologin;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * //Todo
 * <p>
 * Created by HOME on 6/27/2017.
 */

public class CustomTextView extends RelativeLayout {
    private static final String TAG = "Custom";
    private static final int MAX_LINE = 3;
    private TextView tvTitle;
    private TextView tvLabel;

    private int widthLabel;
    private int lineCount;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addText(context);
    }

    public void addText(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view, this, false);
        String str = "This is highlighted text. This is highlighted text. This is highlighted text. This is highlighted text. This is highlighted text. " +
                "This is highlighted text. This is highlighted text. This is highlighted text. This is highlighted text. This is highlighted text.";
        String str1 = "This is";
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvLabel = (TextView) view.findViewById(R.id.tvLabel);


        addView(view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: w = " + widthMeasureSpec + " : h = " + heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout: changed = " + changed);
        Log.d(TAG, "onLayout: left = " + l);
        Log.d(TAG, "onLayout: top = " + t);
        Log.d(TAG, "onLayout: right = " + r);
        Log.d(TAG, "onLayout: bottom = " + b);

        changeLayout(changed, l, t, r, b);
    }

    private void changeLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            widthLabel = tvLabel.getWidth();
            lineCount = tvTitle.getLineCount();

            Rect rect = new Rect();
            tvTitle.getLineBounds(lineCount - 1, rect);

            Log.d(TAG, "changeLayout: width = " + widthLabel);
            Log.d(TAG, "changeLayout: " + rect.width() + " : " + rect.height());

            String line = null;
            Layout layout = tvTitle.getLayout();
            Layout layout1 = tvLabel.getLayout();

            String text = tvTitle.getText().toString();
            int start = 0;
            int end = 0;
            for (int i = 0; i < lineCount; i++) {
                end = layout.getLineEnd(i);
                if (i == lineCount - 1) {
                    line = text.substring(start, end);
                }
                start = end;
            }

            Log.d(TAG, "changeLayout: " + line);

            Rect bounds = new Rect();

            tvTitle.getPaint().getTextBounds(line, 0, line.length(), bounds);

            Log.d(TAG, "changeLayout:1 " + bounds.width() + " : " + bounds.height());

            TextView textView = new TextView(getContext());
            textView.setText(tvLabel.getText().toString());
            textView.setPadding(3, 0, 3, 0);
            textView.setBackgroundResource(R.drawable.bg_text_label);


            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            if (lineCount < MAX_LINE) {
                if (widthLabel + 10 + bounds.width() > getWidth()) {
                    Log.d(TAG, "MAX_LINE:1 " + line);

                    layoutParams.setMargins(0, b, 0, 0);
                    tvLabel.setLayoutParams(layoutParams);
                    this.addView(textView, layoutParams);

//                    tvLabel.setLeft(0);
//                    tvLabel.setBottom(b);
                } else {
                    Log.d(TAG, "MAX_LINE:2 " + line);

                    layoutParams.setMargins(bounds.width() + 10, b - rect.height(), 0, 0);
                    tvLabel.setLayoutParams(layoutParams);
                    this.addView(textView, layoutParams);

//                    tvLabel.setRight(bounds.width() + 10);
//                    tvLabel.setBottom(b - rect.height());
                }
            } else {
                if (widthLabel + 10 + bounds.width() > getWidth()) {
                    Log.d(TAG, "MAX_LINE:3 " + line);
//                    layoutParams.setMargins(getWidth() - 10 - bounds.width(), b - rect.height(), 0, 0);
//                    tvLabel.setLayoutParams(layoutParams);
                    tvLabel.setRight(getWidth() - 10 - bounds.width());
                    tvLabel.setBottom(b - rect.height());
                } else {
                    Log.d(TAG, "MAX_LINE:4 " + line);
//                    layoutParams.setMargins(bounds.width() + 10, b - rect.height(), 0, 0);
//                    tvLabel.setLayoutParams(layoutParams);
                    tvLabel.setRight(bounds.width() + 10);
                    tvLabel.setBottom(b - rect.height());
                }
            }

            tvLabel.setX(bounds.width());
            tvLabel.setY(b - rect.height());
//            requestLayout();
        }


    }

    public void setTvTitle(String title) {
        tvTitle.setText(title);
        requestLayout();
    }

    public void setTvLabel(String label) {
        tvLabel.setText(label);
        requestLayout();
    }

    private void setMultilineEllipsize(TextView view, int maxLines, TextUtils.TruncateAt where) {
        if (maxLines >= view.getLineCount()) {
            return;
        }
        float avail = 0.0f;
        for (int i = 0; i < maxLines; i++) {
            avail += view.getLayout().getLineMax(i);
        }
        CharSequence ellipsizedText = TextUtils.ellipsize(view.getText(), view.getPaint(), avail, where);
        view.setText(ellipsizedText);
    }
}
