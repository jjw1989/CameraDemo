package com.powervision.gcs.camera.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.powervision.gcs.camera.R
import com.powervision.gcs.camera.adapter.hodler.BaseViewHolder
import com.powervision.gcs.camera.adapter.hodler.MediaNetHolder
import com.powervision.gcs.camera.api.ApiUrl
import com.powervision.gcs.camera.model.MediaModel

/**
 * 网络媒体库适配器
 * Created by Sundy on 2017/6/21.
 */
class MediaNetAdapter(mDatas: MutableList<MediaModel>?) : CommonAdapter<MediaModel>(mDatas) {

    override fun createView(viewGroup: ViewGroup?, i: Int): View {
        var mView:View=LayoutInflater.from(viewGroup?.context).inflate(R.layout.camera_media_item_layout,viewGroup,false)
        return mView
    }

    override fun createViewHolder(view: View?): BaseViewHolder {
       return MediaNetHolder(view,true)
    }

    override fun bindViewAndDatas(holder: BaseViewHolder?, t: MediaModel?) {
       var itemHolder:MediaNetHolder= holder as MediaNetHolder
        Log.i("item","bindViewAndDatas,,,,,,,,,,,,,,,,,,,,,,")
        Glide.with(holder.itemView.context).asBitmap().load(ApiUrl.SCHEME+t?.path).into( itemHolder.imgBg)
    }
}