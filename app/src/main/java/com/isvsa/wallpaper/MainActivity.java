package com.isvsa.wallpaper;

/**
 * 项目名称：ColorWithCamera
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2018/9/13 18:04
 *
 * @version V1.0
 */
import android.Manifest;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.isvsa.R;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CAMERA = 454;
    private Context mContext;
    static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private boolean pickWallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findViewById(R.id.text)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkSelfPermission();
                    }
                });

    }


    /**
     * 检查权限
     */
    void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(mContext, PERMISSION_CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{PERMISSION_CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
        } else {
            startWallpaper();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startWallpaper();

                } else {
                    Toast.makeText(mContext, getString(R.string._lease_open_permissions), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * 选择壁纸
     */
    void startWallpaper() {
        if (pickWallpaper) {
            final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
            Intent chooser = Intent.createChooser(pickWallpaper, getString(R.string.choose_wallpaper));
            startActivity(chooser);
        } else {
            setTransparentWallpaper(); 
        }
    }

    /**
     * 不需要手动启动服务
     */
    void setTransparentWallpaper() {
        Intent localIntent = new Intent();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            localIntent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            localIntent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getPackageName(),
                    CameraColorLiveWallpaper.class.getCanonicalName()));
        } else {
            localIntent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        }
        this.startActivityForResult(localIntent, 1002);
    }
}