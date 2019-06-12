package com.example.bentz.simulator_mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class Circle extends View {
    Paint oval,movingCircle ;
    private Point start = null;
    private Point cursorAtMouseDown = null;
    private Point startAtMouseDown = null;
    private Point endAtMouseDown = null;
    private boolean movingStart = false;
    private boolean movingEnd = false;
    private boolean movingLine = false;
    private RectF rect;
    private Canvas canvas;
    private Display display;
    private static int counter;

    public Circle(Context context) {
        super(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        init();
    }

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        init();
    }

    public Circle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        init();
    }

    private void init() {
        counter=0;
        int width = display.getWidth();
        int height = display.getHeight();
        start = new Point(width/12,height/15);
        oval = new Paint();
        movingCircle = new Paint();
        oval.setColor(Color.BLUE);
        oval.setStrokeWidth(10);
        oval.setStyle(Paint.Style.STROKE);
        movingCircle.setColor(Color.RED);
        movingCircle.setStrokeWidth(10);
        movingCircle.setStyle(Paint.Style.STROKE);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       oval.setStyle(Paint.Style.STROKE);
       rect = new RectF(canvas.getWidth()/10, canvas.getHeight()/15, (canvas.getWidth() / 1.25f), (canvas.getHeight() / 1.25f));
       canvas.drawOval(rect, oval);
        canvas.drawCircle(start.x*6, start.y*7.5f, 15, movingCircle);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("Inside On Touch", "");
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                if (movingStart || movingEnd || movingLine) {
                    invalidate();
                }

                movingStart = false;
                movingEnd = false;
                movingLine = false;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                if (movingStart || movingEnd || movingLine) {
                    invalidate();
                }

                movingStart = false;
                movingEnd = false;
                movingLine = false;
                break;
            case MotionEvent.ACTION_MOVE:

                Log.d("Inside On Touch", "ACTION_MOVE");

                if (movingStart) {
                    start.x = (int) event.getX();
                    start.y = (int) event.getY();
                    invalidate();
                    Log.d("Inside On Touch", "--movingStart=" + movingStart);
                    return true;
                } else if (movingEnd) {
                    start.x = (int) event.getX();
                    start.y = (int) event.getY();
                    invalidate();
                    Log.d("Inside On Touch", "--movingEnd=" + movingEnd);
                    return true;
                } else if (movingLine) {
                    Log.d("Inside On Touch", "--movingLine=" + movingLine);
                    if (cursorAtMouseDown != null) {
                        // double diffX = event.getX() - cursorAtMouseDown.x;
                        // double diffY = event.getY() - cursorAtMouseDown.y;
                        // start.x = (int) (startAtMouseDown.x + diffX);
                        // start.y = (int) (startAtMouseDown.y + diffY);

                        start = cursorAtMouseDown;

                        invalidate();
                        return true;
                    }

                }
                return false;

            case MotionEvent.ACTION_DOWN:
                cursorAtMouseDown = new Point((int) event.getX(),
                        (int) event.getY());

                if (cursorAtMouseDown.equals(start)) {

                }

                if (isCircleCenterChaged(cursorAtMouseDown)) {
                    movingLine = true;
                }

                return true;

            default:
                return super.onTouchEvent(event);

        }
        return false;
    }

    private boolean isCircleCenterChaged(Point cursorAtMouseDown) {
        return true;
    }


}