package com.hennacis.visualintervaltimer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;

public class CircleView extends View {

    private Paint mCirclePaint = new Paint();
    private ValueAnimator valueAnimator;
    private int[] mColors = {
            Color.parseColor("#D500F9"),// purple
            Color.parseColor("#CDDC39"),// lime
            Color.parseColor("#2979FF"),// blue
            Color.parseColor("#FFC400"),// amber
            Color.parseColor("#607D8B"),// grey
            Color.parseColor("#f44336"),// red
            Color.parseColor("#E1D7AA"),// beige
    };
    private int count = 0;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(count >= mColors.length){
            count = 0;
        }

        mCirclePaint.setColor(mColors[count]);
        canvas.drawCircle(getWidth()/2,getHeight()/2,1, mCirclePaint);
    }

    public void start(final int secs){
        stop();
        setVisibility(VISIBLE);

        valueAnimator = ValueAnimator.ofFloat(0f, getWidth());
        valueAnimator.setInterpolator(null);
        valueAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                float animatedValue = (float)updatedAnimation.getAnimatedValue();
                CircleView.this.setScaleX(animatedValue);
                CircleView.this.setScaleY(animatedValue);
                //mCanvas.drawPaint(mCirclePaint);
                //CircleView.super.animate().scaleY(animatedValue).setDuration(TimeUnit.SECONDS.toMillis(secs));
                //CircleView.super.animate().scaleX(animatedValue).setDuration(TimeUnit.SECONDS.toMillis(secs));
            }
        });
        valueAnimator.start();
        count++;
    }

    public void stop(){
        if (valueAnimator != null && valueAnimator.isRunning()){
            valueAnimator.pause();
            valueAnimator = null;
            setVisibility(INVISIBLE);
//            invalidate();
        }
        invalidate();
    }

    public int getColor(){
        return mColors[count];
    }
}