package com.gfz.mvp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {

    /**
     * 将view的内容绘制成Bitmap
     */
    public static Bitmap convertViewToBitmap(View view){
        if (view == null){
            return null;
        }
        int w = view.getMeasuredWidth();
        int h = view.getMeasuredHeight();
        if (w <= 0 || h <= 0){
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);
        //把view中的内容绘制在画布上
        view.draw(canvas);
        return bitmap;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp){
        return bmpToByteArray(bmp, 100);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, int quality){
        if (bmp == null){
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG , quality , output);
        bmp.recycle();

        byte[] result = output.toByteArray();
        try{
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将view的内容绘制成bit数组
     */
    public static byte[] convertViewToByteArray(View view, int quality){
        return bmpToByteArray(convertViewToBitmap(view), quality);
    }


    /**
     * 高斯模糊
     * radius范围为0-25
     */
    public static void blur(Context context, Bitmap bitmap, int radius){
        if (context == null || bitmap == null || radius < 0 || radius > 25){
            return;
        }
        RenderScript rs = RenderScript.create(context);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, bitmap);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(bitmap);
        rs.destroy();
    }

    /**
     * 获取矢量图
     */
    public static Bitmap getVectorBitmap(Context context, int resId){
        Bitmap bitmap = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = null;
            vectorDrawable = context.getDrawable(resId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else{
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        }
        return bitmap;
    }
}
