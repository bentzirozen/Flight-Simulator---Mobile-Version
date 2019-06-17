package com.example.bentz.simulator_mobile;
import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
{
    private float x;
    private float y;
    private float ovalRadius;
    private float cursorRadius;
    private JoystickListener joystickCallback;
    private float startWid;
    private float endWid;
    private float endHei;
    private float startHei;
    private RectF ovalRect;


    private void setupDimensions()
    {
        x = getWidth() / 2;
        y = getHeight() / 2;
        this.startWid = (float)getWidth()/12;
        this.endWid = (float)getWidth()-((float)getWidth()/12);
        this.startHei = (float)getHeight()/5;
        this.endHei = getHeight()-((float)getHeight()/5);
        this.ovalRect = new RectF(this.startWid,this.startHei , this.endWid, this.endHei);
        ovalRadius = Math.min(getWidth(), getHeight()) / 3;
        cursorRadius = Math.min(getWidth(), getHeight()) / 10;
    }

    public JoystickView(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public JoystickView(Context context, AttributeSet attributes, int style)
    {
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public JoystickView (Context context, AttributeSet attributes)
    {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    private void drawJoystick(float newX, float newY)
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint paint = new Paint();
            myCanvas.drawColor(0xffffffff);
            //Draw the base first before shading
            paint.setARGB(255, 83, 40, 223);
            myCanvas.drawOval(ovalRect,paint);
            paint.setARGB(255,239,37, 37);
            myCanvas.drawCircle(newX, newY, cursorRadius, paint);
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        setupDimensions();
        drawJoystick(x, y);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public boolean onTouch(View v, MotionEvent e)
    {
        if(v.equals(this))
        {
            if(e.getAction() != e.ACTION_UP)
            {
                float dist = (float) Math.sqrt((Math.pow(e.getX() - x, 2)) + Math.pow(e.getY() - y, 2));
                if(dist < ovalRadius)
                {
                    drawJoystick(e.getX(), e.getY());
                    joystickCallback.onJoystickMoved((e.getX() - x)/ovalRadius, (e.getY() - y)/ovalRadius);
                }
                else
                {
                    float ratio = ovalRadius / dist;
                    float constrainedX = x + (e.getX() - x) * ratio;
                    float constrainedY = y + (e.getY() - y) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX-x)/ovalRadius, (constrainedY-y)/ovalRadius);
                }
            }
            else
                drawJoystick(x, y);

            if(e.getAction() == e.ACTION_UP)
                joystickCallback.onJoystickMoved(0,0);
        }
        return true;
    }

    public interface JoystickListener
    {
        void onJoystickMoved(float x, float y);
    }
}
