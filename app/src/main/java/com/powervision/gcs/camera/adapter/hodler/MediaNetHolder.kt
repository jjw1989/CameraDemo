package com.powervision.gcs.camera.adapter.hodler

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.powervision.gcs.camera.R

/**
 * 网络媒体库holder
 * Created by Sundy on 2017/6/21.
 */
class MediaNetHolder(itemView: View?, isItem:Boolean) : BaseViewHolder(itemView) {
    var imgBg:ImageView?=null
    var isVideo:ImageView?=null
    var tvSize:TextView?=null
    init {
        if(isItem) {
            imgBg = itemView?.findViewById(R.id.img_bg) as ImageView
            isVideo=itemView?.findViewById(R.id.img_video) as ImageView
            tvSize=itemView?.findViewById(R.id.tv_sise) as TextView
        }
    }

}

