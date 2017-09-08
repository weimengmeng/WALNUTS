package com.njjd.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.njjd.walnuts.R;

/**
 * Created by mrwim on 17/8/28.
 */

public class TipButton extends RadioButton {

    private boolean mTipOn = false;

    private Dot mDot;

    private class Dot {

        int color;

        int radius;

        int marginTop;
        int marginRight;

        Dot(int n) {
            float density = getContext().getResources().getDisplayMetrics().density;
            radius = (int) (5 * density);
            marginTop = (int) (n* density);
            marginRight = (int) (3* density);

            color = getContext().getResources().getColor(R.color.red);
        }
        Dot() {
            float density = getContext().getResources().getDisplayMetrics().density;
            radius = (int) (5 * density);
            marginTop = (int) (3* density);
            marginRight = (int) (3* density);

            color = getContext().getResources().getColor(R.color.red);
        }
    }

    public TipButton(Context context) {
        super(context);
        init();
    }

    public TipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TipButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mDot = new Dot();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTipOn) {
            float cx = getWidth() - mDot.marginRight - mDot.radius;
            float cy = mDot.marginTop + mDot.radius;

            Drawable drawableTop = getCompoundDrawables()[1];
            if (drawableTop != null) {
                int drawableTopWidth = drawableTop.getIntrinsicWidth();
                if (drawableTopWidth > 0) {
                    int dotLeft = getWidth() / 2 + drawableTopWidth / 2;
                    cx = dotLeft + mDot.radius;
                }
            }


            Paint paint = getPaint();
            //save
            int tempColor = paint.getColor();

            paint.setColor(mDot.color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, mDot.radius, paint);

            //restore
            paint.setColor(tempColor);
        }
    }

    public void setTipOn(boolean tip,int n) {
        this.mTipOn = tip;
        mDot=new Dot(9);
        invalidate();
    }
    public void setTipOn(boolean tip) {
        this.mTipOn = tip;
        mDot=new Dot();
        invalidate();
    }
    public boolean isTipOn() {
        return mTipOn;
    }
}