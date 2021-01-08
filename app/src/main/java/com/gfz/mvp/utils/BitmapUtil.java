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

    public static final String[] alpha = {
            "FF","FC","FA","F7","F5","F2","F0","ED","EB","E8","E6","E3"
            ,"E0","DE","DB","D9","D6","D4","D1","CF","CC","C9"
            ,"C7","C4","C2","BF","BD","BA","B8","B5","B3","B0"
            ,"AD","AB","A8","A6","A3","A1","9E","9C","99","96"
            ,"94","91","8F","8C","8A","87","85","82","80","7D"
            ,"7A","78","75","73","73","70","6E","6B","69","66"
            ,"63","61","5E","5C","59","57","54","52","4F","4D"
            ,"4A","47","45","42","40","3D","3B","38","36","33"
            ,"30","2E","2B","29","26","24","21","1F","1C","1A"
            ,"17","14","12","0F","0D","0A","08","05","03","00"};
}
