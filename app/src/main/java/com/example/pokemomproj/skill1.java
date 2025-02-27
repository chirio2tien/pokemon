
package com.example.pokemomproj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.Objects;

public class skill1 extends View {
    private static final String TAG = "FireballSkill";
    private float fireballX;
    private float fireballY;
    private float directionX;
    private float directionY;
    private AnimationDrawable fireballDrawable;

    public skill1(Context context, float startX, float startY, float directionX, float directionY) {
        super(context);
        this.fireballX = startX;
        this.fireballY = startY;
        this.directionX = directionX;
        this.directionY = directionY;
        Log.d(TAG, "FireballSkill created at position: (" + startX + ", " + startY + ") with direction: (" + directionX + ", " + directionY + ")");

        initFireballAnimation(context);
        startAnimation();
    }

    private void initFireballAnimation(Context context) {
        fireballDrawable = new AnimationDrawable();

        for (int i = 0; i < 3; i++) {
           int resId = context.getResources().getIdentifier("giratina_d_" + i, "drawable", context.getPackageName());
            if (resId != 0) {
                fireballDrawable.addFrame(ContextCompat.getDrawable(context, resId), 100);
            } else {
                Log.e(TAG, "Drawable resource not found: fireball_" + i);
            }
        }
        fireballDrawable.setOneShot(false);
        fireballDrawable.start();
    }

    private void startAnimation() {
        post(new Runnable() {
            @Override
            public void run() {
                fireballX += directionX * 10;
                fireballY += directionY * 10;
                invalidate();
                postDelayed(this, 1000 / 60); // 60 FPS
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (fireballDrawable != null) {
            canvas.save();
            canvas.translate(fireballX - fireballDrawable.getIntrinsicWidth() / 2, fireballY - fireballDrawable.getIntrinsicHeight() / 2);
            fireballDrawable.setBounds(0, 0, fireballDrawable.getIntrinsicWidth(), fireballDrawable.getIntrinsicHeight());
            fireballDrawable.draw(canvas);
            canvas.restore();
        }
    }
}