package com.example.pokemomproj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class hp_bar extends View {
    private Paint paint;
    private int maxHp;
    private int currentHp;

    public hp_bar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        maxHp = 100;
        currentHp = 100;
    }

    public void setHp(int hp) {
        currentHp = hp;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float hpPercentage = (float) currentHp / maxHp;

        paint.setColor(Color.RED);
        canvas.drawRect(0, 0, width * hpPercentage, height, paint);

        paint.setColor(Color.GRAY);
        canvas.drawRect(width * hpPercentage, 0, width, height, paint);
    }
}