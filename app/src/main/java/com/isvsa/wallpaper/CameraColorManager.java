package com.isvsa.wallpaper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * 项目名称：ColorWithCamera
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2018/9/14 13:58
 *
 * @version V1.0
 */
public class CameraColorManager {

    private WindowManager mWindowManager;
    private CameraColorLayout mIShowView;
    private LayoutParams mWmLp;
    private boolean mIsShown = false;
    private Context mContext;

    private volatile static CameraColorManager singleton;

    private CameraColorManager(Context context) {
        if (context != null) {
            init(context);
        }
    }

    public static CameraColorManager getInstance(Context context) {
        if (singleton == null) {
            synchronized (CameraColorManager.class) {
                if (singleton == null) {
                    singleton = new CameraColorManager(context);
                }
            }
        }
        return singleton;
    }

    private void init(Context context) {
        mIsShown = false;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mContext = context;
        mWmLp = createWindowManagerLayoutParams(context);
    }

    public synchronized void show(CameraColorLayout view) {
        if (!mIsShown) {
            if (mIShowView == null) {
                mIShowView = view;
            }
            if (mIShowView.getParent() == null && mIShowView.getWindowToken() == null) {
                try {
                    mWindowManager.addView(mIShowView, mWmLp);
                    mIsShown = true;
                } catch (Exception e) {
                    mIsShown = false;
                }
            }
        }
    }

    public synchronized void unShow() {
        if (mWindowManager != null && mIShowView != null && mIsShown) {
            mIShowView.onStop();
            mIShowView.onDestroy();
            if (mIShowView.getParent() != null) {
                mWindowManager.removeViewImmediate(mIShowView);
            }
            mIShowView = null;
            mIsShown = false;
        }
    }

    public boolean isShown() {
        return mIsShown;
    }

    public void screenOn() {
        if (mIShowView != null) {
            mIShowView.onResume();
        }
    }

    public void screenOff() {
        if (mIShowView != null) {
            mIShowView.onStop();
        }
    }

    private static WindowManager.LayoutParams createWindowManagerLayoutParams(Context context) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        //设置悬浮窗的位置,默认居中
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        int flags = LayoutParams.FLAG_FULLSCREEN |
                LayoutParams.FLAG_TURN_SCREEN_ON |
                LayoutParams.FLAG_NOT_FOCUSABLE |
                LayoutParams.FLAG_NOT_TOUCHABLE |
                LayoutParams.FLAG_NOT_TOUCH_MODAL |
                LayoutParams.FLAG_LAYOUT_NO_LIMITS |//limit border
                LayoutParams.FLAG_SHOW_WHEN_LOCKED;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                    | LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        }
        params.flags = flags;
        params.format = PixelFormat.TRANSLUCENT;
        if (!RecordConfig.isCamPreview()) {
            params.width = 1;
            params.height = 1;

        } else {
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
            params.width = 1080;
            params.height = 1920;
        }
        return params;
    }

}