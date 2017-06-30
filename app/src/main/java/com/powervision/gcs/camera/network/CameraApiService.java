package com.powervision.gcs.camera.network;

import android.content.Intent;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 相机指令
 * Created by Sundy on 2017/6/12.
 */

public interface CameraApiService {
    /**
     * 相机开始录像
     * @return
     */
    @GET("record.cgi?record&-cmd=start")
    Call<ResponseBody> requestStartRecord();

    /**
     *相机停止录像
     * @return
     */
    @GET("record.cgi?record&-cmd=stop")
    Call<ResponseBody> requestStopRecord();

    /**
     * 相机拍照
     * @return
     */
    @GET("photo.cgi?&-type=common&-cmd=start")
    Call<ResponseBody> requestTakephoto();

    /**
     * 相机停止拍照（当相机处于连拍时，可以结束相机连拍状态）
     * @return
     */
    @GET("")
    Call<ResponseBody> requestStopTakephoto();

    /**
     * 相机文件数量
     * @return
     */
    @GET("getfilecount.cgi")
    Call<ResponseBody> requestCameraFileCount();

    /**
     * 获取相机文件列表
     * @return
     */
    @GET("getfilelist.cgi?getfilelist&")
    Call<ResponseBody> requestCameraFileList(@Query("-start")int page,@Query("-end") int count);

    /**
     * 获取相机单个文件信息
     * @return
     */
    @GET("")
    Call<ResponseBody> requestCameraFileInfo();

    /**
     * 删除单个文件
     * @return
     */
    @GET("deletefile.cgi?deletefile&")
    Call<ResponseBody> requestDeleteCameraFile(@Query("-name") String path);
    /**
     * 删除所有文件
     * @return
     */
    @GET("deleteallfiles.cgi?")
    Call<ResponseBody> requestDeleteAllCameraFile();
    /**
     *
     * 切换相机为录像模式
     * @return
     */
    @GET("setworkmode.cgi?&-act=set&-workmode=20 ")
    Call<ResponseBody> requestSwitchRecordMode();

    /**
     * 切换相机为拍照模式
     * @return
     */
    @GET("setworkmode.cgi?&-act=set&-workmode=00")
    Call<ResponseBody> requestSwitchPhotoMode();

    /**
     *获取相机模式（录像模式，拍照模式，多连拍模式）
     * @return
     */
    @GET("getworkmode.cgi?&-act=get")
    Call<ResponseBody> requestCameraMode();

    /**
     * 下载文件
     * @return
     */
    Call<ResponseBody> requestDownloadFile();

    /**
     * 设置录像分辨率
     * @param {0 1 2 4 7 15 17 }
     * @return
     */
    @GET("setparameter.cgi?&-act=set&-workmode=20&-type=0&")
    Call<ResponseBody> requestSetRecordResolution(@Query("-value") int type);

    /**
     * 获取录像分辨率
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=20&-type=0")
    Call<ResponseBody> requestGetRecordResolution();

    /**
     *设置慢动作分辨率
     * @param {0 1 2 4  }
     * @return
     */
    @GET("setparameter.cgi?&-act=set&-workmode=24&-type=0&")
    Call<ResponseBody> requestSetSlowRecordResolution(@Query("-value") int type);

    /**
     *
     * 获取慢动作分辨率
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=24&-type=0")
    Call<ResponseBody> requestGetSlowRecordResolution();

    /**
     * 设置循环分辨率
     * @param {0 1 2 4}
     * @return
     */
     @GET("setparameter.cgi?&-act=set&-workmode=21&-type=0&-value")
    Call<ResponseBody> requestSetCircleRecordResolution(@Query("-value") int type);

    /**
     * 获取循环分辨率
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=21&-type=0")
    Call<ResponseBody> requestGetCircleRecordResolution();

    /**
     * http://192.168.1.10/cgi-bin/hi3559/setparameter.cgi?&-act=set&-workmode=21&-type=7&-value=0

     * 设置分段录制时间
     * @param {0 1 2 3 }
     * @return
     */
    @GET("setparameter.cgi?&-act=set&-workmode=21&-type=7&")
    Call<ResponseBody> requestSetSeparateRecordTime(@Query("-value") int type);

    /**
     * 获取分段录制时间
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=21&-type=7")
    Call<ResponseBody> requestGetSeparateRecordTime();

    /**
     *设置缩时录影分辨率
     * @param {0 1 2 4}
     * @return
     *
     */
    @GET("setparameter.cgi?&-act=set&-workmode=22&-type=0&")
    Call<ResponseBody> requestSetShortenRecordResolution(@Query("-value") int type);

    /**
     * 获取缩时录影分辨率
     * @return
     *
     */
    @GET("getparameter.cgi?getworkmode&-act=get&-workmode=22&-type=0")
    Call<ResponseBody> requestGetShortenRecordResolution();

    /**
     * 设置缩时录影间隔
     * @param {500 1000 2000 5000 10000 30000 60000 }
     * @return
     */
    @GET("setparameter.cgi?&-act=set&-workmode=22&-type=1&")
    Call<ResponseBody> requestSetShortenRecordTimeInterval(@Query("-value") int type);

    /**
     * 获取缩时录影间隔
     * @return
     *
     */
    @GET("getparameter.cgi?&-act=get&-workmode=22&-type=1")
    Call<ResponseBody> requestGetShortenRecordTimeInterval();

    /**
     * 设置录中拍分辨率
     * @param {3 16 18 8 }
     * @return
     */
    @GET("setparameter.cgi?&-act=set&-workmode=23&-type=6&")
    Call<ResponseBody> requestSetRecordInPhotoResolution(@Query("-value") int type);

    /**
     * 获取录中拍分辨率
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=23&-type=6")
    Call<ResponseBody> requestGetRecordInPhotoResolution();

    /**
     *设置录中拍时间间隔
     * @param {3000 5000 10000 30000 60000 300000 }
     * @return
     */
    @GET("setparameter.cgi?&-act=set&-workmode=23&-type=1&")
    Call<ResponseBody> requestSetRecordInPhotoTimeInterval(@Query("-value") int type);

    /**
     * 获取录中拍时间间隔
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=23&-type=1")
    Call<ResponseBody> requestGetRecordInPhotoTimeInterval();

    /**
     * 设置单张照片分辨率
     * @param {9 10}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=00&-type=0&")
    Call<ResponseBody> requestSetSinglePhotoResolution(@Query("-value")int type);

    /**
     * 获取单张照片分辨率
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=00&-type=0")
    Call<ResponseBody> requestGetSinglePhotoResolution();

    /**
     *设置拍照模式
     * @param {0 1 2 3}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=00&-type=2&")
    Call<ResponseBody> requestSetPhotoMode(@Query("-value")int type);

    /**
     *  获取拍照模式
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=00&-type=2")
    Call<ResponseBody> requestGetPhotoMode();

    /**
     *  设置ISO
     *  @param {0 100 200 400 800 1600 3200 6400}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=00&-type=6&")
    Call<ResponseBody> requestSetISO(@Query("-value") int type);

    /**
     * 获取ISO
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=00&-type=6")
    Call<ResponseBody> requestGetISO();

    /**
     * 设置EV
       @param {1 2 3 4 5 5 6 7 8 9}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=00&-type=3&")
    Call<ResponseBody> requestSetEV(@Query("-value") int type);

    /**
     * 获取EV
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=00&-type=3")
    Call<ResponseBody> requestGetEV();

    /**
     *  设置曝光时间
     * @param {0 2 5 10 15 20 30 }
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=00&-type=7&")
    Call<ResponseBody> requestSetExplose(@Query("-value")int type);

    /**
     * 获取曝光时间
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=00&-type=7")
    Call<ResponseBody> requestGetExplose();

    /**
     * 设置白平衡
     * @param {0 1 2 3 4 5}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=00&-type=5&")
    Call<ResponseBody> requestSetWhitebalance(@Query("-value") int type);

    /**
     * 获取白平衡
     * @return
     */
    @GET("getparameter.cgi?@-act=get&-workmode=00&-type=5")
    Call<ResponseBody> requestGetWhitebalance();

    /**
     * 设置测光模式
     * @param {0 1}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=00&-type=4&")
    Call<ResponseBody> requestSetMeteringMode(@Query("-value") int type);

    /**
     * 获取测光模式
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=00&-type=4")
    Call<ResponseBody> requestGetMeteringMode();

    /**
     *  设置延时拍照分辨率
     *  @param {9  10}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=01&-type=0&")
    Call<ResponseBody> requestSetDelayPhotoResolution(@Query("-value") int type);

    /**
     *  获取延时拍照分辨率
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=01&-type=0")
    Call<ResponseBody> requestGetDelayPhotoResolution();

    /**
     * 设置延时拍照
     * @param {1 3 5 10 30}

     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=01&-type=1&")
    Call<ResponseBody> requestSetDelayTime(@Query("-value")int type);

    /**
     *  获取延时拍照
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=01&-type=1")
    Call<ResponseBody> requestGetDelayTime();

    /**
     *设置RAW拍照模式
     * @return
     */
    Call<ResponseBody> requestSetRAWPhotoMode();

    /**
     * 设置RAW拍照分辨率
     * @@param {9 10}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=02&-type=0&")
    Call<ResponseBody> requestSetRAWResolution(@Query("-value")  int type);

    /**
     *  获取RAW拍照分辨率
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=02&-type=0")
    Call<ResponseBody> requestGetRAWResolution();

    /**
     * 设置定时拍照分辨率
     * @param {9 10}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=11&-type=0&")
    Call<ResponseBody> requestSetTimingPhotoResolution(@Query("-value") int type);

    /**
     *获取定时拍照分辨率
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=11&-type=0")
    Call<ResponseBody> requestGetTimingPhotoResolution();

    /**
     *  设置定时拍照时间
     *  @param {500 1000 2000 3000 5000 10000 30000 60000 300000}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=11&-type=2&")
    Call<ResponseBody> requestSetTimingPhotoTime(@Query("-value") int type);

    /**
     * 获取定时拍照时间
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=11&-type=2")
    Call<ResponseBody> requestGetTimingPhotoTime();

    /**
     *  设置持续拍照分辨率
     *  @param {12 13}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=12&-type=0&")
    Call<ResponseBody> requestSetContinuePhotoResolution(@Query("-value") int type);

    /**
     *获取持续拍照分辨率
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=12&-type=0")
    Call<ResponseBody> requestGetContinuePhotoResolution();

    /**
     * 设置多连拍分辨率
     * @param {3 5 10 12 13 }
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=10&-type=0&")
    Call<ResponseBody> requestSetMulContinuePhotoResolution(@Query("-value") int type);

    /**
     * 获取多连拍分辨率
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=10&-type=0")
    Call<ResponseBody> requestGetMulContinuePhotoResolution();

    /**
     * 设置多连拍速度
     * @param {0 1 2 3 4 5 6 7}
     * @return
     */
    @GET("setparameter.cgi?&act=set&-workmode=10&-type=1&")
    Call<ResponseBody> requestSetMulContinuePhotoVelocity(@Query("-value") int type);

    /**
     *获取多连拍速度
     * @return
     */
    @GET("getparameter.cgi?&-act=get&-workmode=10&-type=1")
    Call<ResponseBody> requestGetMulContinuePhotoVelocity();

    /**
     *  获取SD存在照片张数和剩余可拍照数量
     获取当前录像时间和可录像总时间
     * @return
     */
    @GET("getremain.cgi?&-cmd=get")
    Call<ResponseBody> requestGetResidualPhotoAndResidualRecordTime();

    /**
     * 格式化SD卡
     * @return
     */
    @GET("sdcommand.cgi?&-format")
    Call<ResponseBody> requestFormatSD();

    /**
     * 获取SD卡容量
     * @return
     */
    @GET("getsdstate.cgi")
    Call<ResponseBody> requestSDCapacity();

    /**
     * 获取相机时间
     * @return
     */
    @GET("getsystime.cgi?")
    Call<ResponseBody> requestCameraCurrentDate();

    /**
     * 恢复默认值
     * @return
     */
    @GET("reset.cgi? ")
    Call<ResponseBody> requestRestoreCamera();

    /**
     * 获取设备信息
     * @return
     */
    @GET("getdeviceattr.cgi?&-act=get")
    Call<ResponseBody> requestGetDeviceAttribute();

    /**
     * 重启相机
     * @return
     */
    @GET("reboot.cgi?")
    Call<ResponseBody> requestRebootCamera();

    /**
     * 获取相机信息
     * @return
     */
    @GET("")
    Call<ResponseBody> requestGetCameraInfo();

    /**
     * 设置相机时间
     * @return
     */
    @GET("setsystime.cgi?setsystime&-act=set&")
    Call<ResponseBody> requestSetCameraTime(@Query("-time")int time,@Query("-timeformat") int format,@Query("-timezone")int timezone);
}
