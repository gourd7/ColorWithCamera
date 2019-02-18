package com.isvsa.wallpaper;

/**
 * 项目名称：ColorWithCamera
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2018/9/13 18:03
 *
 * @version V1.0
 */

import android.content.Context;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class CameraColorLiveWallpaper extends WallpaperService {
    private static final String TAG = "LiveWallpaper";
    // 实现WallpaperService必须实现的抽象方法
    private Context mContext;

    public Engine onCreateEngine() {
        // 返回自定义的CameraEngine
        mContext = this.getApplicationContext();
        return new CameraEngine();
    }


    class CameraEngine extends Engine{
        private SurfaceHolder mSurfaceHolder;
        private CameraColorLayout mCameraColorLayout;

        public CameraEngine() {
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            Log.e(TAG, "onCreate surfaceHolder: " + surfaceHolder);
            super.onCreate(surfaceHolder);
            mSurfaceHolder = surfaceHolder;
            // 设置处理触摸事件
            setTouchEventsEnabled(true);
            mCameraColorLayout = new CameraColorLayout(mContext);
            mCameraColorLayout.setSurfaceHolder(mSurfaceHolder);
            CameraColorManager.getInstance(mContext).show(mCameraColorLayout);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            // 时间处理:点击拍照,长按拍照
        }

        @Override
        public void onDestroy() {
            Log.e(TAG, "onDestroy");
            super.onDestroy();
            CameraColorManager.getInstance(mContext).unShow();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            Log.e(TAG, "onVisibilityChanged");
            if (visible) {
                mCameraColorLayout = new CameraColorLayout(mContext);
                mCameraColorLayout.setSurfaceHolder(mSurfaceHolder);
                CameraColorManager.getInstance(mContext).show(mCameraColorLayout);
            } else {
                CameraColorManager.getInstance(mContext).unShow();
            }
        }
    }
}