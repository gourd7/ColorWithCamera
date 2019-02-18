package com.isvsa.wallpaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.SurfaceHolder;

/**
 * 项目名称：ColorWithCamera
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2018/9/14 11:02
 *
 * @version V1.0
 */
public class Utils {

    public static void onDrawFrame(SurfaceHolder surfaceHolder, int color, Bitmap bitmap) {
        Canvas canvas = null;
        Paint painter = new Paint();
        painter.setStyle(Style.FILL);
        painter.setAntiAlias(true);
        painter.setFilterBitmap(true);
        try {
            canvas = surfaceHolder.lockCanvas();
            System.out.println("====onDrawFrame===" + canvas);
            if (canvas == null) {
                return;
            }
            synchronized (surfaceHolder) {
                canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
                canvas.drawColor(color);
                //canvas.drawBitmap(bitmap,100, 100, painter);
            }
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
