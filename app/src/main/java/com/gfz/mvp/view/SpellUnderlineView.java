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
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gfz.mvp.R;
import com.gfz.mvp.utils.CommUtilKt;
import com.gfz.mvp.utils.ScreenUtil;
import com.gfz.mvp.utils.TopLog;

/**
 * created by gaofengze on 2021/4/13
 */
public class SpellUnderlineView extends androidx.appcompat.widget.AppCompatTextView {
    // 画图数据
    float dottedLength = getPX(5);
    float dottedSpace = getPX(5);
    float underlineHeight = getPX(3);
    float dottedHeight = getPX(2);
    float dottedSum = dottedLength + dottedSpace;
    float[] interval = {
            getPX(0)
            , getPX(15) + dottedHeight
            , getPX(23) + dottedHeight
            , getPX(15) + underlineHeight
    };
    int[][] textSize = {{12,14,16},{14,16,18},{15,18,20},{16,18,20},{24,26,28},{34,36,40}};
    int height = 0;

    // 画图组件
    private Paint paint;
    private Paint paintDotted;
    PathEffect pathEffectNext = new DashPathEffect(new float[]{dottedLength, dottedSpace}, dottedLength / 2);
    Path path = new Path();
    boolean show = false;

    // 配置参数
    int fontLevel = 0;

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
        paintDotted.setColor(ContextCompat.getColor(context, R.color.col_94949B));
        paintDotted.setAlpha((int) (255 * 0.2));

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(underlineHeight);
        paint.setColor(ContextCompat.getColor(context, R.color.col_00b19b));
        paint.setAlpha((int) (255 * 0.2));

        updateSize(0);
    }

    /**
     * 设置是否显示
     * @param show
     */
    public void setShow(boolean show) {
        this.show = show;
        invalidate();
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);

        paint.setColor(color);
        paint.setAlpha((int) (255 * 0.2));
    }

    @Override
    public void setTextSize(int unit, float size) {
        int fontLevel = 0;
        int[] sizeArray = textSize[5];
        for (int i = 0; i < sizeArray.length; i++) {
            if(size == sizeArray[i]){
                fontLevel = i;
                break;
            }
        }
        updateSize(fontLevel);
        super.setTextSize(unit, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFormatLine(canvas);
        super.onDraw(canvas);
    }

    public void updateSize(int fontLevel){
        this.fontLevel = fontLevel;
        interval[0] = fontLevel == 2 ? dottedHeight + underlineHeight : dottedHeight;
        interval[2] = getPX(23 + fontLevel);

        height = 0;
        for(float h : interval){
            height += h;
        }
        height += underlineHeight;

        setMinHeight(height);
    }

    private void drawFormatLine(Canvas canvas){
        if (!show || getLineCount() > 1){
            return;
        }
        int width = getMeasuredWidth();

        float partWidth = width / 2f;
        float phase = dottedLength / 2f;

        float overLength = (int) ((partWidth + phase) % dottedSum);

        int cutDownLength = 0;
        //用户可以看到的最短线段 (0, dottedLength]
        if (overLength < dottedLength){
            cutDownLength = (int) (overLength + dottedSpace);
        }else if(overLength > dottedLength){
            cutDownLength = (int) (overLength - dottedLength);
        }

        float y = 0;
        for (int i = 0; i < 4; i++) {
            path.reset();

            y += interval[i];

            if (i != 2){
                paintDotted.setPathEffect(pathEffectNext);
                path.moveTo(width / 2f, y);
                path.lineTo(width - cutDownLength, y);
                path.moveTo(width / 2f, y);
                path.lineTo(cutDownLength, y);
                canvas.drawPath(path, paintDotted);
            }else{
                path.moveTo(cutDownLength, y);
                path.lineTo(width - cutDownLength, y);
                canvas.drawPath(path, paint);
            }
        }
    }

    private float getPX(int dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
