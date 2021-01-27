package com.gfz.mvp.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.gfz.mvp.utils.TopLog;

/**
 * created by gaofengze on 2021/1/20
 */
public class InterpolatorView extends View implements ValueAnimator.AnimatorUpdateListener {

    private Paint paint;
    private Path path;

    private float progressX;
    private float progressY;

    public InterpolatorView(Context context) {
        super(context);
        init();
    }

    public InterpolatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InterpolatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public InterpolatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    AnimatorSet animatorSet;
    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        path = new Path();
        ObjectAnimator animator = ObjectAnimator.ofFloat(this,"progressX", 0,1);
        animator.setInterpolator(new LinearInterpolator());
//        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(this);

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(this,"progressY", 0,1);
//        animator1.setRepeatCount(ValueAnimator.INFINITE);
        animator1.setInterpolator(new EaseCubicInterpolator(0.66f,0,0.34f, 1));
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, animator1);

    }

    public void start(){
        animatorSet.setDuration(2000);
        animatorSet.start();
    }

    public void setProgressX(float progressX) {
        this.progressX = progressX;
    }

    public void setProgressY(float progressY) {
        this.progressY = progressY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = 1000;
        path.lineTo( width * progressX, width - width * progressY);
        canvas.drawPath(path, paint);
        TopLog.INSTANCE.e(progressY);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        invalidate();
    }
}
