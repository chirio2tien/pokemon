package com.example.pokemomproj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class mana_bar extends View {
    private Paint paint;
    private int maxMana;
    private int currentMana;

    public mana_bar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        maxMana = 100;
        currentMana = 100;
    }

    public void setMana(int mana) {
        currentMana = mana;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float manaPercentage = (float) currentMana / maxMana;

        paint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, width * manaPercentage, height, paint);

        paint.setColor(Color.GRAY);
        canvas.drawRect(width * manaPercentage, 0, width, height, paint);
    }
}