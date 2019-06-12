package com.example.bentz.simulator_mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class JoystickView extends View {
    Paint oval,movingCircle ;
    private float x;
    private float y;
    private float radius;
    private float startWid;
    private float endWid;
    private float startHei;
    private float endHei;
    private RectF ovalRect;
    private Boolean playMoving = false;
    private Connection tcp;
    private Bitmap playerImage;

    public JoystickView(Context context,Connection tcp) {
        super(context);
        Resources res = getResources();
        playerImage = BitmapFactory.decodeResource(res, R.drawable.check);
        this.radius = 100;
        this.tcp = tcp;
    }


    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        //canvas.drawBitmap(playerImage,null,ovalRect,null);
        Paint cursorPaint = new Paint();
        cursorPaint.setColor(Color.rgb(0, 0, 0));
        cursorPaint.setStrokeWidth(10);

        Paint ovalPaint = new Paint();
        ovalPaint.setColor(Color.rgb(0, 0, 100));
        ovalPaint.setStrokeWidth(10);
        canvas.drawOval(this.ovalRect, ovalPaint);
        canvas.drawCircle(this.x, this.y, this.radius, cursorPaint);

    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        returnDefualt();
        this.startWid = (float)getWidth()/8;
        this.endWid = (float)getWidth()-((float)getWidth()/8);
        this.startHei = (float)getHeight()/8;
        this.endHei = getHeight()-((float)getHeight()/8);
        this.ovalRect = new RectF(this.startWid,this.startHei , this.endWid, this.endHei);
    }

    public void returnDefualt() {
        this.x = (float)getWidth()/2;
        this.y = (float)getHeight()/2;
        tcp.sendCommands("set /controls/flight/aileron " + String.valueOf(normelizeAilron(this.x)) + "\r\n");
        tcp.sendCommands("set /controls/flight/elevator "+ String.valueOf(normelizeElevator(this.y)) + "\r\n");
    }


    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if(CheckIfInside(event.getX(), event.getY())) {
                    this.playMoving = true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (!this.playMoving)
                    return true;
                if (CheckForLimit(event.getX(), event.getY())) {
                    this.x = event.getX();
                    this.y = event.getY();
                    tcp.sendCommands("set /controls/flight/aileron "+ String.valueOf(normelizeAilron(this.x)) + "\r\n");
                    tcp.sendCommands("set /controls/flight/elevator "+ String.valueOf(normelizeElevator(this.y)) + "\r\n");
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_UP :
                this.playMoving = false;
                returnDefualt();
                invalidate();
        }
        return true;
    }

    Boolean CheckIfInside(float xVal, float yVal) {
        double distance = Math.sqrt((this.x-xVal)*(this.x-xVal) + (this.y-yVal)*(this.y-yVal));
        return (distance <= this.radius);
    }

    Boolean CheckForLimit(float xVal, float yVal) {
        return (this.ovalRect.contains(xVal,yVal)&&
                this.ovalRect.contains(xVal, yVal+radius) &&
                this.ovalRect.contains(xVal, yVal-radius) &&
                this.ovalRect.contains(xVal+radius, yVal) &&
                this.ovalRect.contains(xVal-radius, yVal));
    }


    public float normelizeAilron(float x) {
        return (x-((this.startWid+this.endWid)/2))/((this.endWid-this.startWid)/2);
    }

    public float normelizeElevator(float y) {
        return (y-((this.startHei+this.endHei)/2))/((this.startHei-this.endHei)/2);
    }


}