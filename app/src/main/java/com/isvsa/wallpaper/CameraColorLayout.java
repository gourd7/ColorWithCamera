package com.isvsa.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.RelativeLayout;

import com.isvsa.OnColorStatusChange;
import com.isvsa.R;


public class CameraColorLayout extends RelativeLayout implements IViewLife{

    private RelativeLayout mCameraLayout;
    private CameraSurfaceView mSurfaceView;
    private CameraTextureView mTextureView;
    private SurfaceHolder mSurfaceHolder;

    public CameraColorLayout(Context context) {
        this(context, null);
    }

    public CameraColorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
        if (mTextureView != null) {
            mTextureView.release();
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_surface_view, this);

        mSurfaceView = findViewById(R.id.surface_view);
        //mSurfaceView.setOnColorStatusChange(mOnColorStatusChange);
        //mSurfaceView.initHolder();

        mTextureView = findViewById(R.id.texture_view);
        mTextureView.setOnColorStatusChange(mOnColorStatusChange);
        mTextureView.initHolder();
        mTextureView.setAlpha(0.5f);
    }

    private OnColorStatusChangeCallback mOnColorStatusChange = new OnColorStatusChangeCallback() {
        @Override
        public void onColorChange(int color) {
            if (mSurfaceHolder != null) {
                Utils.onDrawFrame(mSurfaceHolder, color, null);
            }
        }

        @Override
        public void onColorBitmapChange(int color, Bitmap bitmap) {
            if (mSurfaceHolder != null) {
                Utils.onDrawFrame(mSurfaceHolder, color, bitmap);
            }
        }
    };

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
    }
}
