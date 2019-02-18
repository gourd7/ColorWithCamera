package com.isvsa.wallpaper;

import android.graphics.Bitmap;

import com.isvsa.OnColorStatusChange;

/**
 * Created by zhy on 2014/10/15.
 */
public interface OnColorStatusChangeCallback extends OnColorStatusChange{
    public void onColorBitmapChange(int color, Bitmap bitmap);
}
