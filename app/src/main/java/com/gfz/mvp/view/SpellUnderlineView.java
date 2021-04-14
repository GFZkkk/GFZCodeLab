package com.gfz.mvp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gfz.mvp.utils.CommUtilKt;
import com.gfz.mvp.utils.ScreenUtil;
import com.gfz.mvp.utils.TopLog;

/**
 * created by gaofengze on 2021/4/13
 */
public class SpellUnderlineView extends androidx.appcompat.widget.AppCompatTextView {
    private Paint paint;
    private Paint paintDotted;
    float dottedLength = getPX(5);
    float dottedSpace = getPX(5);
    float underlineHeight = getPX(3);
    float dottedHeight = getPX(2);
    float dottedSum = dottedLength + dottedSpace;
    float[] interval = {getPX(0)
            , getPX(15) + dottedHeight
            , getPX(20) + dottedHeight
            , getPX(15) + underlineHeight
    };
    Context context;
    public SpellUnderlineView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SpellUnderlineView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public SpellUnderlineView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init(){

        paintDotted = new Paint();
        paintDotted.setStyle(Paint.Style.STROKE);
        paintDotted.setStrokeWidth(dottedHeight);
        paintDotted.setColor(Color.BLUE);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(underlineHeight);
        paint.setColor(Color.BLUE);
    }
    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        int width = getMeasuredWidth();

        float partWidth = width / 2f;
        float phase = dottedLength / 2f;
        int cutDownLength = Math.max((int) (((partWidth + phase) % dottedSum) - dottedLength), 0);

        Path path = new Path();
        float y = dottedHeight;
        for (int i = 0; i < 4; i++) {
            if (i > 0){
                path.reset();
            }

            y += interval[i];

            if (i == 2){
                path.moveTo(cutDownLength, y);
                path.lineTo(width - cutDownLength, y);
                canvas.drawPath(path, paint);

            }else{
                PathEffect pathEffectNext = new DashPathEffect(new float[]{dottedLength, dottedSpace}, dottedLength / 2);
                paintDotted.setPathEffect(pathEffectNext);
                path.moveTo(width / 2f, y);
                path.lineTo(width, y);
                path.moveTo(width / 2f, y);
                path.lineTo(0, y);
                canvas.drawPath(path, paintDotted);
            }

            TopLog.INSTANCE.e(CommUtilKt.toDP((int) y, context));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for(float h : interval){
            height += h;
        }
        height += dottedHeight + underlineHeight;
        TopLog.INSTANCE.e(CommUtilKt.toDP(height, context));
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float getPX(int dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
