package com.powervision.gcs.camera.ui.fgt.camera

import android.content.Context
import com.powervision.gcs.camera.R
import com.powervision.gcs.camera.model.CameraModel

/**
 * 相机子菜单
 * Created by Sundy on 2017/6/19.
 */
open class DataController(context: Context) {
    val mContext = context
    /**
     * type=0
     * 录像分辨率
     */
    fun recordResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.resolution_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.resolution_2), "", false)
        val item3 = CameraModel(mContext.getString(R.string.resolution_3), "", false)
        val item4 = CameraModel(mContext.getString(R.string.resolution_4), "", false)
        val item5 = CameraModel(mContext.getString(R.string.resolution_5), "", false)
        val item6 = CameraModel(mContext.getString(R.string.resolution_6), "", false)
        val item7 = CameraModel(mContext.getString(R.string.resolution_7), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6, item7)
        return items
    }

    /**
     * type=1
     *慢动作分辨率
     */
    fun slowResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.resolution_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.resolution_2), "", false)
        val item3 = CameraModel(mContext.getString(R.string.resolution_3), "", false)
        val item4 = CameraModel(mContext.getString(R.string.resolution_4), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4)
        return items
    }

    /**
     * type=2
     *循环录影分辨率
     */
    fun cyclicResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.resolution_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.resolution_2), "", false)
        val item3 = CameraModel(mContext.getString(R.string.resolution_3), "", false)
        val item4 = CameraModel(mContext.getString(R.string.resolution_4), "", false)
        val item5 = CameraModel(mContext.getString(R.string.resolution_5), "", false)
        val item6 = CameraModel(mContext.getString(R.string.resolution_6), "", false)
        val item7 = CameraModel(mContext.getString(R.string.resolution_7), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6, item7)
        return items
    }

    /**
     * type=3
     *分段录影时间
     */
    fun sectionTime(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.time1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.time2), "", false)
        val item3 = CameraModel(mContext.getString(R.string.time3), "", false)
        val item4 = CameraModel(mContext.getString(R.string.time4), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4)
        return items
    }

    /**
     * type=4
     *缩时录影分辨率
     */
    fun shrinkTimeResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.resolution_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.resolution_2), "", false)
        val item3 = CameraModel(mContext.getString(R.string.resolution_3), "", false)
        val item4 = CameraModel(mContext.getString(R.string.resolution_4), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4)
        return items
    }

    /**
     * type=5
     *缩时录影间隔
     */
    fun shrinkTimeInterval(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.time5), "", false)
        val item2 = CameraModel(mContext.getString(R.string.time6), "", false)
        val item3 = CameraModel(mContext.getString(R.string.time7), "", false)
        val item4 = CameraModel(mContext.getString(R.string.time9), "", false)
        val item5 = CameraModel(mContext.getString(R.string.time10), "", false)
        val item6 = CameraModel(mContext.getString(R.string.time12), "", false)
        val item7 = CameraModel(mContext.getString(R.string.time13), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6, item7)
        return items
    }

    /**
     * type=6
     * 录中拍分辨率
     */
    fun recordingResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.resolution_3), "", false)
        val item2 = CameraModel(mContext.getString(R.string.resolution_6), "", false)
        val item3 = CameraModel(mContext.getString(R.string.resolution_7), "", false)
        val item4 = CameraModel(mContext.getString(R.string.resolution_5), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4)
        return items
    }

    /**
     * type=7
     * 录中拍时间
     */
    fun recordTime(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.time8), "", false)
        val item2 = CameraModel(mContext.getString(R.string.time9), "", false)
        val item3 = CameraModel(mContext.getString(R.string.time10), "", false)
        val item4 = CameraModel(mContext.getString(R.string.time12), "", false)
        val item5 = CameraModel(mContext.getString(R.string.time1), "", false)
        val item6 = CameraModel(mContext.getString(R.string.time3), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6)
        return items
    }

    /**
     * type=8
     * 单张照片分辨率
     */
    fun slngleResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.burs_resolution_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.burs_resolution_2), "", false)
        val items = mutableListOf<CameraModel>(item1, item2)
        return items
    }

    /**
     * type=9
     * 拍照模式
     */
    fun takePhotosModel(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.take_photos_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.take_photos_2), "", false)
        val item3 = CameraModel(mContext.getString(R.string.take_photos_3), "", false)
        val item4 = CameraModel(mContext.getString(R.string.take_photos_4), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4)
        return items
    }

    /**
     * type=10
     * ISO
     */
    fun iso(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.iso_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.iso_2), "", false)
        val item3 = CameraModel(mContext.getString(R.string.iso_3), "", false)
        val item4 = CameraModel(mContext.getString(R.string.iso_4), "", false)
        val item5 = CameraModel(mContext.getString(R.string.iso_5), "", false)
        val item6 = CameraModel(mContext.getString(R.string.iso_6), "", false)
        val item7 = CameraModel(mContext.getString(R.string.iso_7), "", false)
        val item8 = CameraModel(mContext.getString(R.string.iso_8), "", false)

        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6, item7, item8)
        return items
    }

    /**
     * type=11
     * EV
     */
    fun ev(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.ev_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.ev_2), "", false)
        val item3 = CameraModel(mContext.getString(R.string.ev_3), "", false)
        val item4 = CameraModel(mContext.getString(R.string.ev_4), "", false)
        val item5 = CameraModel(mContext.getString(R.string.ev_5), "", false)
        val item6 = CameraModel(mContext.getString(R.string.ev_6), "", false)
        val item7 = CameraModel(mContext.getString(R.string.ev_7), "", false)
        val item8 = CameraModel(mContext.getString(R.string.ev_8), "", false)
        val item9 = CameraModel(mContext.getString(R.string.ev_9), "", false)
        val item10 = CameraModel(mContext.getString(R.string.ev_10), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10)
        return items
    }

    /**
     * type=12
     * 曝光时间
     */
    fun exTime(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.time14), "", false)
        val item2 = CameraModel(mContext.getString(R.string.time7), "", false)
        val item3 = CameraModel(mContext.getString(R.string.time9), "", false)
        val item4 = CameraModel(mContext.getString(R.string.time10), "", false)
        val item5 = CameraModel(mContext.getString(R.string.time15), "", false)
        val item6 = CameraModel(mContext.getString(R.string.time11), "", false)
        val item7 = CameraModel(mContext.getString(R.string.time12), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6, item7)
        return items
    }

    /**
     * type=13
     * 白平衡
     */
    fun whiteBalance(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.wb_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.wb_2), "", false)
        val item3 = CameraModel(mContext.getString(R.string.wb_3), "", false)
        val item4 = CameraModel(mContext.getString(R.string.wb_4), "", false)
        val item5 = CameraModel(mContext.getString(R.string.wb_5), "", false)
        val item6 = CameraModel(mContext.getString(R.string.wb_6), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6)
        return items
    }

    /**
     * type=14
     * 测光模式
     */
    fun photometryModel(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.photometry_model_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.photometry_model_2), "", false)

        val items = mutableListOf<CameraModel>(item1, item2)
        return items
    }

    /**
     * type=15
     * 延时拍照分辨率
     */
    fun delayResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.burs_resolution_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.burs_resolution_2), "", false)
        val items = mutableListOf<CameraModel>(item1, item2)
        return items
    }

    /**
     * type=16
     * 延时时间
     */
    fun delayTime(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.time6), "", false)
        val item2 = CameraModel(mContext.getString(R.string.time8), "", false)
        val item3 = CameraModel(mContext.getString(R.string.time9), "", false)
        val item4 = CameraModel(mContext.getString(R.string.time10), "", false)
        val item5 = CameraModel(mContext.getString(R.string.time12), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5)
        return items
    }

    /**
     * type=17
     * RAW拍照分辨率
     */
    fun rawResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.burs_resolution_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.burs_resolution_2), "", false)
        val items = mutableListOf<CameraModel>(item1, item2)
        return items
    }

    /**
     * type=18
     * 定时拍照分辨率
     */
    fun timingResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.burs_resolution_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.burs_resolution_2), "", false)
        val items = mutableListOf<CameraModel>(item1, item2)
        return items
    }

    /**
     * type=19
     * 定时拍照时间
     */
    fun timingPhotoTime(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.time5), "", false)
        val item2 = CameraModel(mContext.getString(R.string.time6), "", false)
        val item3 = CameraModel(mContext.getString(R.string.time7), "", false)
        val item4 = CameraModel(mContext.getString(R.string.time8), "", false)
        val item5 = CameraModel(mContext.getString(R.string.time9), "", false)
        val item6 = CameraModel(mContext.getString(R.string.time10), "", false)
        val item7 = CameraModel(mContext.getString(R.string.time12), "", false)
        val item8 = CameraModel(mContext.getString(R.string.time1), "", false)
        val item9 = CameraModel(mContext.getString(R.string.time2), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6, item7, item8, item9)
        return items
    }

    /**
     * type=20
     * 持续拍照分辨率
     */
    fun continueResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.burs_resolution_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.burs_resolution_2), "", false)
        val items = mutableListOf<CameraModel>(item1, item2)
        return items
    }

    /**
     * type=21
     * 多连拍分辨率
     */
    fun moreResolution(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.burs_resolution_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.burs_resolution_2), "", false)
        val items = mutableListOf<CameraModel>(item1, item2)
        return items
    }

    /**
     * type=22
     * 多连拍速度
     */
    fun moreSpeed(): List<CameraModel> {
        val item1 = CameraModel(mContext.getString(R.string.burs_mode_1), "", false)
        val item2 = CameraModel(mContext.getString(R.string.burs_mode_2), "", false)
        val item3 = CameraModel(mContext.getString(R.string.burs_mode_3), "", false)
        val item4 = CameraModel(mContext.getString(R.string.burs_mode_4), "", false)
        val item5 = CameraModel(mContext.getString(R.string.burs_mode_5), "", false)
        val item6 = CameraModel(mContext.getString(R.string.burs_mode_6), "", false)
        val item7 = CameraModel(mContext.getString(R.string.burs_mode_7), "", false)
        val item8 = CameraModel(mContext.getString(R.string.burs_mode_8), "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6, item7, item8)
        return items
    }

    /**
     * type=27
     * 相机当前时间
     */
    fun test24(): List<CameraModel> {
        val item1 = CameraModel("4K2K30", "", false)
        val item2 = CameraModel("720P120", "", false)
        val item3 = CameraModel("1080P60", "", false)
        val item4 = CameraModel("1440P30", "", false)
        val item5 = CameraModel("2.7KP30", "", false)
        val item6 = CameraModel("1080P120", "", false)
        val item7 = CameraModel("720P240", "", false)
        val items = mutableListOf<CameraModel>(item1, item2, item3, item4, item5, item6, item7)
        return items
    }

    companion object {
        fun newInstance(context: Context): DataController {
            val data = DataController(context)
            return data
        }
    }
}