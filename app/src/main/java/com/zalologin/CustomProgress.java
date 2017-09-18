package com.zalologin;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HOME on 5/10/2017.
 */

public class CustomProgress extends View {
    public interface OnCompleteLoadProgressListener {
        void OnCompleteProgress();
    }

    private static final int DELAY_TIME = 100;
    private Context mContext;
    private int mBackgroundColor;
    private int mPrimaryColor;
    private int mBackgroundWidth;
    private int mPrimaryWidth;
    private int mTargetLength;
    private int mDirector;

    private Paint mArcPaintBackground;
    private Paint mArcPaintPrimary;
    private Paint mTextPaint;

    private float mPadding;
    private float mProgress;

    private RectF mDrawingRect;

    private static double M_PI_2 = Math.PI / 2;
    private int mTimer;
    private boolean updateView = false;
    private OnCompleteLoadProgressListener mOnCompleteLoadProgressListener;

    public void setOnCompleteLoadProgressListener(OnCompleteLoadProgressListener onCompleteLoadProgressListener) {
        this.mOnCompleteLoadProgressListener = onCompleteLoadProgressListener;
    }

    private UpdateViewRunnable updateViewRunnable = new UpdateViewRunnable();

    private class UpdateViewRunnable implements Runnable {
        public void run() {
            if (mProgress < 100) {
                mProgress += 100 * DELAY_TIME / 1000 / mTimer;
                invalidate();
            } else {
                updateView = false;
                if (mOnCompleteLoadProgressListener != null) {
                    mOnCompleteLoadProgressListener.OnCompleteProgress();
                }
            }

            if (updateView) {
                postDelayed(this, DELAY_TIME);
            }
        }
    }

    public void start() {
        if (updateView) {
            postDelayed(updateViewRunnable, DELAY_TIME);
        }
    }

    public CustomProgress(Context context) {
        this(context, null);
    }

    public CustomProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        typeAttribute(context, attrs);
        init(context);
    }

    private void typeAttribute(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomProgress, 0, 0);
        try {
            mTimer = ta.getInt(R.styleable.CustomProgress_timer, 5);
            mPrimaryWidth = ta.getDimensionPixelSize(R.styleable.CustomProgress_widthStroke, 4);
            mBackgroundWidth = mPrimaryWidth;
            mDirector = ta.getInt(R.styleable.CustomProgress_director, 1);
        } finally {
            ta.recycle();
        }
    }

    public void setPrimaryWidth(int primaryWidth) {
        this.mPrimaryWidth = primaryWidth;
        mBackgroundColor = primaryWidth;
        invalidate();
        requestLayout();
    }

    public void setDirector(int mDirector) {
        this.mDirector = mDirector;
        invalidate();
        requestLayout();
    }

    private void init(Context ctx) {
        mContext = ctx;
        Resources res = ctx.getResources();
        final float density = res.getDisplayMetrics().density;

        mBackgroundColor = ContextCompat.getColor(mContext, android.R.color.black);
        mPrimaryColor = ContextCompat.getColor(mContext, android.R.color.white);
        mTargetLength = (int) (mPrimaryWidth * 1.50); // 20% longer than arc line width

        mArcPaintBackground = new Paint() {
            {
                setDither(true);
                setStyle(Style.FILL_AND_STROKE);
                setAntiAlias(true);
            }
        };
        mArcPaintBackground.setColor(mBackgroundColor);
        mArcPaintBackground.setStrokeWidth(mBackgroundWidth);

        mArcPaintPrimary = new Paint() {
            {
                setDither(true);
                setStyle(Style.STROKE);
                setStrokeCap(Cap.BUTT);
                setStrokeJoin(Join.BEVEL);
                setAntiAlias(true);
            }
        };
        mArcPaintPrimary.setColor(mPrimaryColor);
        mArcPaintPrimary.setStrokeWidth(mPrimaryWidth);

        mTextPaint = new Paint() {
            {
                setDither(true);
                setStyle(Style.FILL);
                setTextSize(14 * density);
                setAntiAlias(true);
                setColor(Color.WHITE);
            }
        };

        // get widest drawn element to properly pad the rect we draw inside
        float maxW = (mTargetLength >= mBackgroundWidth) ? mTargetLength : mBackgroundWidth;
        // arc is drawn with it's stroke center at the rect size provided, so we have to pad
        // it by half to bring it inside our bounding rect
        mPadding = maxW / 2;
        mProgress = 0;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
        requestLayout();
    }

    public void setTimer(int timer) {
        mTimer = timer;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // full circle (start at 270, the "top")
        canvas.drawArc(mDrawingRect, 270, 360, false, mArcPaintBackground);

        // draw starting at top of circle in the negative (counter-clockwise) direction
        canvas.drawArc(mDrawingRect, 270, mDirector * (360 * (mProgress / 100f)), false, mArcPaintPrimary);

        canvas.drawText((int)(mTimer - mTimer * mProgress / 100) + "" , getWidth() / 2 - 20, getHeight() / 2 + 20, mTextPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (0 == height) height = width; // 0 vertical space, make it square
        if (0 == width) width = height; // 0 horizontal space, make it square

        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // set the dimensions
        int size = 0;
        if (widthWithoutPadding > heigthWithoutPadding) {
            size = heigthWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // bound our drawable arc to stay fully within our canvas
        mDrawingRect = new RectF(mPadding + getPaddingLeft(),
                mPadding + getPaddingTop(),
                w - mPadding - getPaddingRight(),
                h - mPadding - getPaddingBottom());
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateView = true;
    }

    @Override
    public void onDetachedFromWindow() {
        updateView = false;
        super.onDetachedFromWindow();
    }
}
