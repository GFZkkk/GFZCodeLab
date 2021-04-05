package com.gfz.mvp.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.gfz.mvp.R;

import java.util.Random;


/**
 * 彩带动画
 * created by gaofengze on 2021/1/6
 */
public class FireworksView extends View implements ValueAnimator.AnimatorUpdateListener {
    //绘制范围
    private int width = getPX(375);
    private int height = getPX(150);

    private Paint paint;
    private PathMeasure pathMeasure;
    private Matrix matrix;

    //彩片路径点
    private final int[][][][] point = {
            {{{186,100}},{{17,22},{129,38}}},
            {{{186,100}},{{14,88},{96,81}}},
            {{{186,100}},{{40,67},{130,60}}},
            {{{186,100}},{{80,14},{145,26}}},
            {{{186,100}},{{91,39},{143,39}}},
            {{{186,100}},{{106,13},{163,47}}},
            {{{186,100}},{{168,31},{179,55}}},
            {{{186,100}},{{57,115},{81,77}}},
            {{{186,100}},{{26,140},{67,89}}},
            {{{186,100}},{{118,136},{127,95}}},
            {{{193,100}},{{262,14},{218,20}}},
            {{{193,100}},{{288,36},{229,42}}},
            {{{193,100}},{{338,15},{281,7}}},
            {{{193,100}},{{365,39},{319,29}}},
            {{{193,100}},{{330,45},{270,23}}},
            {{{193,100}},{{305,67},{280,50}}},
            {{{193,100}},{{337,75},{311,48}}},
            {{{193,100}},{{363,80},{336,57}}},
            {{{193,100}},{{329,113},{321,88}}},
            {{{193,100}},{{360,118},{345,89}}},
            {{{193,100}},{{289,131},{263,99}}}
    };

    //彩带路径点
    private final int[][][][] point1 = {
            {
                    //起点
                    {{217,102}},
                    //终点 和 多个控制点
                    {{252,61},{220,63}},
                    {{292,94},{274,59},{291,71}},
                    {{256,127},{293,108},{281,130}},
                    {{368,60},{197,120},{212,2}},
            },
            {
                    {{186,80}},
                    {{169,100},{188,101}},
                    {{128,107},{149,100},{140,93}},
                    {{91,119},{115,122},{108,126}},
                    {{52,121},{68,106}}
            },
            {
                    {{169,100}},
                    {{164,74},{164,96}},
                    {{131,55},{164,64},{153,52}},
                    {{98,32},{116,57},{98,48}},
                    {{65,1},{98,4}}
            }
    };

    private int[] rotate = new int[point.length];

    //彩片路径点
    private final Path[] paths = new Path[point.length];
    //彩带路径
    private final Path[] paths1 = new Path[point1.length];

    //彩片绘制点
    private final float[][] dpoint = new float[paths.length][2];
    //彩带绘制路径
    private final Path[] dPath = new Path[paths1.length];

    //彩片图案
    private Bitmap[] bitmaps = null;
    //彩带颜色
    private final int[] ribbonColor = {
            Color.parseColor("#FF0202"),
            Color.parseColor("#956FE7"),
            Color.parseColor("#FFDA00")
    };
    //彩带长度
    private final float[] colorLength = {0.2f, 0.2f, 0.3f};
    //当前进度
    private float progress;
    private float scaleProgress;

    public FireworksView(Context context) {
        super(context);
        init();
    }

    public FireworksView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FireworksView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FireworksView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    ObjectAnimator a1;
    ObjectAnimator scale;
    ObjectAnimator alphaStar;
    ObjectAnimator alphaEnd;
    AnimatorSet animatorSet;

    public void start(){
        end();
        Random random = new Random();

        for (int i = 0; i < rotate.length; i++) {
            rotate[i] = random.nextBoolean() ? 1 : -1;
        }

        animatorSet.start();
    }

    public void end(){
        animatorSet.end();
    }

    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        matrix = new Matrix();
        paint.setStrokeWidth(getPX(3));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        pathMeasure = new PathMeasure();

        loadResource();

        buildPath();
        initAnimator();
    }

    private void initAnimator(){
        animatorSet = new AnimatorSet();

        //控制运动和旋转
        a1 = ObjectAnimator.ofFloat(this,"progress",0f,1f)
                .setDuration(1400);
        a1.setInterpolator(new DecelerateInterpolator(1f));
        a1.addUpdateListener(this);

        LinearInterpolator linearInterpolator = new LinearInterpolator();
        //缩放
        scale = ObjectAnimator.ofFloat(this,"scaleProgress",0f,1f)
                .setDuration(400);
        scale.setInterpolator(linearInterpolator);
        scale.addUpdateListener(this);

        //开始阶段透明度动画
        alphaStar = ObjectAnimator.ofFloat(FireworksView.this,"alpha", 0f,1f).setDuration(400);
        alphaStar.setInterpolator(linearInterpolator);

        //结束阶段透明度动画
        alphaEnd = ObjectAnimator.ofFloat(FireworksView.this,"alpha", 1f,0f).setDuration(200);
        alphaEnd.setStartDelay(1200);
        alphaEnd.setInterpolator(linearInterpolator);

        animatorSet.playTogether(a1,scale,alphaStar,alphaEnd);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //彩片位置
        for (int i = 0; i < paths.length; i++) {
            pathMeasure.setPath(paths[i],false);
            pathMeasure.getPosTan(pathMeasure.getLength() * progress, dpoint[i],null);
        }
        //彩带位置
        for (int i = 0; i < paths1.length; i++) {
            dPath[i] = new Path();
            pathMeasure.setPath(paths1[i],false);
            pathMeasure.getSegment(
                    pathMeasure.getLength() * (progress - colorLength[i]),
                    pathMeasure.getLength() * progress,
                    dPath[i], true);
        }
        //更新视图
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dPath[0] != null){
            //绘制彩片
            for (int i = 0; i < paths.length; i++) {
                float dx = (bitmaps[i].getWidth() / 2f);
                float dy = (bitmaps[i].getHeight() / 2f);
                matrix.reset();
                matrix.setRotate(360 * progress * rotate[i], dx, dy);
                matrix.postScale(scaleProgress, scaleProgress, dx, dy);
                matrix.postTranslate(dpoint[i][0] - dx, dpoint[i][1] - dy);
                canvas.drawBitmap(bitmaps[i], matrix, paint);
            }
            //绘制彩带
            for (int i = 0; i < paths1.length; i++) {
                paint.setColor(ribbonColor[i]);
                canvas.drawPath(dPath[i], paint);
            }
        }
    }

    /**
     * 加载图片到数组
     */
    private void loadResource(){
        //加载图片
        Bitmap star = BitmapUtil.INSTANCE.getVectorBitmap(getContext(), R.drawable.ic_word_salute_star);
        Bitmap heart = BitmapUtil.INSTANCE.getVectorBitmap(getContext(), R.drawable.ic_word_salute_heart);
        Bitmap round = BitmapUtil.INSTANCE.getVectorBitmap(getContext(), R.drawable.ic_word_salute_round);
        Bitmap triangle = BitmapUtil.INSTANCE.getVectorBitmap(getContext(), R.drawable.ic_word_salute_triangle);
        Bitmap yellowdiamond = BitmapUtil.INSTANCE.getVectorBitmap(getContext(), R.drawable.ic_word_salute_yellowdiamond);
        Bitmap bluediamond = BitmapUtil.INSTANCE.getVectorBitmap(getContext(), R.drawable.ic_word_salute_bluediamond);
        bitmaps = new Bitmap[]{heart, round, triangle, heart, yellowdiamond
                , star, triangle, star, bluediamond, heart, round, bluediamond, star
                , round, heart, triangle, yellowdiamond, heart, heart, star, yellowdiamond};

    }

    /**
     * 生成路径
     */
    private void buildPath(){
        for (int i = 0; i < point.length; i++) {
            Path path = new Path();
            buildBezierPath(point[i], path);
            paths[i] = path;
        }

        for (int i = 0; i < point1.length; i++) {
            Path path = new Path();
            buildBezierPath(point1[i], path);
            paths1[i] = path;
        }

    }

    /**
     * 生成连续贝塞尔曲线
     */
    private void buildBezierPath(int[][][] points, Path path){
        path.moveTo(getPX(points[0][0][0]), getPX(points[0][0][1]));
        for (int j = 1; j < points.length; j++) {
            addBezierPath(points[j], path);
        }
    }

    /**
     * 给路径添加二阶或三阶贝塞尔曲线
     */
    private void addBezierPath(int[][] points, Path path){
        if (points.length == 2){
            //二阶
            path.quadTo(getPX(points[1][0]), getPX(points[1][1]),
                    getPX(points[0][0]), getPX(points[0][1]));
        }else if(points.length == 3){
            //三阶
            path.cubicTo(getPX(points[1][0]), getPX(points[1][1]),
                    getPX(points[2][0]), getPX(points[2][1]),
                    getPX(points[0][0]), getPX(points[0][1]));
        }
    }

    //设置动画进度
    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setScaleProgress(float scaleProgress) {
        this.scaleProgress = scaleProgress;
    }

    private int getPX(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}















