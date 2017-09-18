package service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zalologin.R;
import com.zalologin.animator.MainActivity;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/28/2017.
 */

public class FloatingService extends Service implements View.OnClickListener {
    private WindowManager windowManager;
    private View floatingView;
    private RelativeLayout rlZoom;
    private RelativeLayout rlNormal;
    private TextView tvClear;
    private TextView tvZoom;
    private TextView tvCollapse;
    private ImageView imgWidget;
    private ImageView imgWidgetZoom;

    private float x;
    private float y;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public FloatingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget, null);
        rlNormal = (RelativeLayout) floatingView.findViewById(R.id.rlNormal);
        rlZoom = (RelativeLayout) floatingView.findViewById(R.id.rlZoom);

        tvClear = (TextView) floatingView.findViewById(R.id.tvClear);
        tvZoom = (TextView) floatingView.findViewById(R.id.tvZoom);
        tvCollapse = (TextView) floatingView.findViewById(R.id.tvCollapse);

        imgWidget = (ImageView) floatingView.findViewById(R.id.imgWidget);
        imgWidgetZoom = (ImageView) floatingView.findViewById(R.id.imgWidgetZoom);

        tvClear.setOnClickListener(this);
        tvZoom.setOnClickListener(this);
        tvCollapse.setOnClickListener(this);
        imgWidgetZoom.setOnClickListener(this);

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);

        imgWidget.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        Log.d("ttt", "ACTION_DOWN: " + initialTouchX + " : " + initialTouchY);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        Log.d("ttt", "ACTION_MOVE: ");
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        windowManager.updateViewLayout(floatingView, params);
                        return true;

                    case MotionEvent.ACTION_UP:
//                        int Xdiff = (int) (event.getRawX() - initialTouchX);
//                        int Ydiff = (int) (event.getRawY() - initialTouchY);
//
//                        if (Xdiff < 10 && Ydiff < 10) {
//                            if (isViewCollapsed()) {
//                                //When user clicks on the image view of the collapsed layout,
//                                //visibility of the collapsed layout will be changed to "View.GONE"
//                                //and expanded view will become visible.
//                                collapsedView.setVisibility(View.GONE);
//                                expandedView.setVisibility(View.VISIBLE);
//                            }
//                        }
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClear:
                stopSelf();
                break;
            case R.id.tvZoom:
                rlZoom.setVisibility(View.VISIBLE);
                rlNormal.setVisibility(View.GONE);
                break;
            case R.id.tvCollapse:
                rlZoom.setVisibility(View.GONE);
                rlNormal.setVisibility(View.VISIBLE);
                break;
            case R.id.imgWidgetZoom:
                Intent intent = new Intent(FloatingService.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                stopSelf();
                break;
        }
    }
}
