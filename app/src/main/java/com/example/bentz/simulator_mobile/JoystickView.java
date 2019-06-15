package com.example.bentz.simulator_mobile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;


public class JoystickView extends View {
    private float x;
    private float y;
    private float radius;
    private float startWid;
    private float endWid;
    private float startHei;
    private float endHei;
    private RectF ovalRect;
    private float ovalWidth;
    private float ovalHeight;
    private Point center;
    private Boolean playMoving = false;
    private Connection tcp;

    public JoystickView(Context context,Connection tcp) {
        super(context);
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
        this.ovalWidth = (ovalRect.right - ovalRect.left)/2;
        this.ovalHeight = (ovalRect.top - ovalRect.bottom)/2;
        this.center = new Point(getWidth()/2,getHeight()/2);
    }

    public void returnDefualt() {
        this.x = (float)getWidth()/2;
        this.y = (float)getHeight()/2;
        tcp.sendCommands("set /controls/flight/aileron " + String.valueOf(0) + "\r\n");
        tcp.sendCommands("set /controls/flight/elevator "+ String.valueOf(0) + "\r\n");
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
        float dx = xVal -center.x ;
        float dy = yVal - center.y;
        return   (dx * dx) / (ovalWidth * ovalWidth) + (dy * dy) / (ovalHeight * ovalHeight)<=0.57;

    }


    public float normelizeAilron(float x) {
        float ret =1.47f* (x-((this.startWid+this.endWid)/2))/((this.endWid-this.startWid)/2);
        if(ret>=1){
            return 1;
        }else if(ret<=-1){
            return -1;
        }else{
            return ret;
        }
    }

    public float normelizeElevator(float y) {
        float ret = 1.42f*(y-((this.startHei+this.endHei)/2))/((this.startHei-this.endHei)/2);
        if(ret>=1){
            return 1;
        }else if(ret<=-1){
            return -1;
        }else{
            return ret;
        }
    }


}