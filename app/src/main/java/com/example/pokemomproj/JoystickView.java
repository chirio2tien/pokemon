package com.example.pokemomproj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class JoystickView extends View {
    private float centerX, centerY, baseRadius, hatRadius;
    private float joystickX, joystickY;
    private JoystickListener joystickListener;

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initJoystick();
    }

    private void initJoystick() {
        Paint paint = new Paint();
        paint.setColor(Color.argb(128, 128, 128, 128));
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 3;
        hatRadius = Math.min(getWidth(), getHeight()) / 6;
        joystickX = centerX;
        joystickY = centerY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.argb(128, 128, 128, 128));
        canvas.drawCircle(centerX, centerY, baseRadius, paint);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(joystickX, joystickY, hatRadius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP) {
            float displacement = (float) Math.sqrt(Math.pow(event.getX() - centerX, 2) + Math.pow(event.getY() - centerY, 2));
            if (displacement < baseRadius) {
                joystickX = event.getX();
                joystickY = event.getY();
            } else {
                float ratio = baseRadius / displacement;
                joystickX = centerX + (event.getX() - centerX) * ratio;
                joystickY = centerY + (event.getY() - centerY) * ratio;
            }
            if (joystickListener != null) {
                joystickListener.onJoystickMoved((joystickX - centerX) / baseRadius, (joystickY - centerY) / baseRadius);
            }
        } else {
            joystickX = centerX;
            joystickY = centerY;
            if (joystickListener != null) {
                joystickListener.onJoystickMoved(0, 0);
            }
        }
        invalidate();
        return true;
    }

    public void setJoystickListener(JoystickListener listener) {
        this.joystickListener = listener;
    }

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent);
    }
}