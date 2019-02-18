package com.isvsa.wallpaper;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

import com.isvsa.ColorUtil;
import com.isvsa.OnColorStatusChange;
import com.isvsa.jni.ImageUtilEngine;

public class CameraTextureView extends TextureView implements TextureView.SurfaceTextureListener {

	private static String TAG = "CameraTextureView";
	private SurfaceTexture mSurfaceTexture = null;
	private Camera mCamera = null;
	private int mPreviewHeight = 800; // 预览显示高度
	private int mPreviewWidth = 480; // 预览显示宽度
	private int mWidth = 0, mHeight = 0;
	private OnColorStatusChange colorChange;
    private ImageUtilEngine imageEngine;
	int colorOld;

	public CameraTextureView(Context context) {
		super(context);
		//initHolder();
	}

	public CameraTextureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//initHolder();
	}

	public CameraTextureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//initHolder();
	}

	/**
	 * 初始化
	 */
	void initHolder() {
		Log.i(TAG, "going into initHolder");
		setSurfaceTextureListener(this);
	}

	/**
	 * 初始化Camera
	 */
	public void initCamera()// surfaceChanged中调用
	{
		Log.i(TAG, "going into initCamera");
		mCamera.stopPreview();// stopCamera();
		if (null != mCamera) {
			try {
				/* Camera Service settings */
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setFlashMode("off"); // 无闪光灯
				// 设置预览图片大小
				parameters.setPreviewSize(mPreviewHeight, mPreviewWidth); // 指定preview的大小
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//视频自动对焦
				// 横竖屏镜头自动调整
				if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
					parameters.set("orientation", "portrait"); //
					// parameters.set("rotation", 90); // 镜头角度转90度（默认摄像头是横拍）
					mCamera.setDisplayOrientation(90); // 在2.2以上可以使用
				} else// 如果是横屏
				{
					parameters.set("orientation", "landscape"); //
					mCamera.setDisplayOrientation(0); // 在2.2以上可以使用
				}
				/* 视频流编码处理 */
				// 添加对视频流处理函数
				// 设定配置参数并开启预览
				mCamera.setParameters(parameters); // 将Camera.Parameters设定予Camera
				mCamera.setPreviewCallback(new PreviewCallback() {
					@Override
					public void onPreviewFrame(byte[] data, Camera camera) {
						// TODO Auto-generated method stub
						mWidth = camera.getParameters().getPreviewSize().width;
						mHeight = camera.getParameters().getPreviewSize().height;
						int[] buf = imageEngine.decodeYUV420SP(data, mWidth, mHeight);
						Bitmap bitmap = Bitmap.createBitmap(buf, mWidth, mHeight, Config.RGB_565);
						int color = bitmap.getPixel(mWidth / 2, mHeight / 2);

						int [] pixelArr = new int[100 * 100] ;
						bitmap.getPixels(pixelArr,0,100,mWidth / 2 - 5, mHeight / 2 - 5, 100, 100);
						for (int i = 0; i < pixelArr.length; i++) {
							System.out.println("pixelArr:" + pixelArr[i]);
						}

						if (ColorUtil.compareSpan(colorOld, color) > 50) {
							if (colorChange != null && colorChange instanceof OnColorStatusChangeCallback) {
								((OnColorStatusChangeCallback)colorChange).onColorBitmapChange(color,
										Bitmap.createBitmap(pixelArr, 100, 100, Config.RGB_565));
							}
						}
						colorOld = color;
						//bitmap.recycle();
					}
				});
				mCamera.startPreview(); // 打开预览画面
				// 【调试】设置后的图片大小和预览大小以及帧率
				Camera.Size csize = mCamera.getParameters().getPreviewSize();
				mPreviewHeight = csize.height; //
				mPreviewWidth = csize.width;
				csize = mCamera.getParameters().getPictureSize();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setOnColorStatusChange(OnColorStatusChange colorChange) {
		this.colorChange = colorChange;
	}


	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		Log.i(TAG, "going into onSurfaceTextureAvailable");
        if (mCamera == null) {
            mCamera = Camera.open();// 开启摄像头（2.3版本后支持多摄像头,需传入参数）
            imageEngine = new ImageUtilEngine();
            try {
                mCamera.setPreviewCallback(new PreviewCallback() {

                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        mWidth = camera.getParameters().getPreviewSize().width;
                        mHeight = camera.getParameters().getPreviewSize().height;
                    }
                });
                mCamera.setPreviewTexture(surface);// 设置预览
            } catch (Exception ex) {
                ex.printStackTrace();
                if (null != mCamera) {
                    mCamera.release();
                    mCamera = null;
                }
            }
        }
		initCamera();
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
		Log.i(TAG, "going into onSurfaceTextureSizeChanged");
		if (surface == null) {
			return;
		}
		mSurfaceTexture = surface;
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		Log.v(TAG, "onSurfaceTextureDestroyed");
		release();
		return true;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {

	}

	public void release() {
		if (null != mCamera) {
			mCamera.setPreviewCallback(null); // ！！这个必须在前，不然退出出错
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
		mSurfaceTexture = null;
	}
}
