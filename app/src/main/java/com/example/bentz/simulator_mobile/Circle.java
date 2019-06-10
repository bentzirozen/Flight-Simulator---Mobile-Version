package com.example.bentz.simulator_mobile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Circle extends View {

    Paint paint, paintSmall;
    private Point start = null;
    private Point cursorAtMouseDown = null;
    private Point startAtMouseDown = null;
    private Point endAtMouseDown = null;
    private boolean movingStart = false;
    private boolean movingEnd = false;
    private boolean movingLine = false;
    private RectF rect;

    public Circle(Context context) {
        super(context);
        init();
    }

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Circle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        start = new Point(100, 100);

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float left = canvas.getWidth()/10;
        float top = canvas.getHeight()/15;
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        rect = new RectF(left,top,left+(canvas.getWidth()/1.25f),top+(canvas.getHeight()/1.25f));
        canvas.drawOval(rect, paint);
        canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, 15, paint);

    }

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