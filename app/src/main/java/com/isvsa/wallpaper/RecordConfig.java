package com.isvsa.wallpaper;

/**
 * 项目名称：VideoRecorder-master
 * 创建人：BenC Zhang zhangzhihua@yy.com
 * 类描述：TODO(这里用一句话描述这个方法的作用)
 * 创建时间：2017/11/29 19:10
 *
 * @version V1.0
 */
public class RecordConfig {
    public static int CAM_BACK = 0;
    public static int CAM_FRONT = 1;

    public static int Cam_Index = CAM_BACK;
    public static boolean Cam_Preview = false;

    public static void setCamIndex(int index) {
        Cam_Index = index;
    }
    public static int getCamIndex() {
        return Cam_Index ;
    }

    public static void setCamPrivew(boolean isPreview) {
        Cam_Preview = isPreview;
    }
    public static boolean isCamPreview() {
        return Cam_Preview ;
    }
}
