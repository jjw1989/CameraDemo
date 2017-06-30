package com.powervision.gcs.camera.view

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import com.powervision.gcs.camera.R

/**
 * Created by ZDY on 2017/6/29.
 */
class DownLoadWindow(activity: Activity) : PopupWindow() {
    init {
        val rootview = activity.layoutInflater.inflate(R.layout.layout_download, null)
        contentView = rootview
        width = RelativeLayout.LayoutParams.WRAP_CONTENT
        height = RelativeLayout.LayoutParams.WRAP_CONTENT
    }
}