package com.powervision.gcs.camera.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter
import com.bumptech.glide.Glide
import com.powervision.gcs.camera.R
import com.powervision.gcs.camera.adapter.hodler.MediaNetHolder
import com.powervision.gcs.camera.api.ApiUrl
import com.powervision.gcs.camera.model.MediaModel
import com.powervision.gcs.camera.ui.aty.camera.PlayNetVideoActivity

/**
 * Created by Sundy on 2017/6/22.
 */
class MediaSdcardAdapter(var datas: MutableList<MediaModel>?) : BaseRecyclerAdapter<MediaNetHolder>() {

    var context: Context? = null

    /**
     * 初始化资源
     */
    init {
        if (datas == null) {
            datas = mutableListOf()
        }
    }


    private var onRecyclerItemClickListener: OnRecyclerItemClickListener? = null
    /**
     * 设置Itme的点击监听
     */
    fun setOnRecyclerItemClickListener(onRecyclerItemClickListener: OnRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener
    }

    override fun getViewHolder(view: View?): MediaNetHolder {
        return MediaNetHolder(view, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int, isItem: Boolean): MediaNetHolder {
        var mView: View = LayoutInflater.from(parent?.context).inflate(R.layout.camera_media_item_layout, parent, false)
        context = parent?.context
        return MediaNetHolder(mView, true)
    }

    /**
     * 数据和视图绑定
     */
    override fun onBindViewHolder(holder: MediaNetHolder?, position: Int, isItem: Boolean) {
        val itemHolder: MediaNetHolder = holder as MediaNetHolder
        val item: MediaModel = datas!!.get(position)
        val num: Int = item.size.toInt() / 1024 / 1024
        itemHolder.tvSize?.text = "${num}M"
        if (item.filename.equals("mp4")) {
            itemHolder.isVideo?.visibility = View.VISIBLE
            val playUrl = ApiUrl.SCHEME + item.path
            var url: String = ApiUrl.SCHEME + item.path.replace("MP4", "THM")
            Glide.with(holder.itemView.context).asBitmap().load(url).into(itemHolder.imgBg)
            itemHolder.isVideo?.setOnClickListener({
                var intent: Intent = Intent(context, PlayNetVideoActivity::class.java)
                intent.putExtra("url", playUrl)
                context?.startActivity(intent)
            })
        } else {
            itemHolder.isVideo?.visibility = View.GONE
            Glide.with(holder.itemView.context).asBitmap().load(ApiUrl.SCHEME + item?.path).into(itemHolder.imgBg)
        }
        holder.itemView.setOnClickListener {
            item ->
            listener(item, position)
        }
    }

    fun listener(view: View?, position: Int) {
        if (onRecyclerItemClickListener != null) {
            onRecyclerItemClickListener?.onItemClick(view, position)
        }
    }

    override fun getAdapterItemCount(): Int {
        return datas!!.size
    }


    fun changeDate(datas: MutableList<MediaModel>) {
        this.datas = datas
        notifyDataSetChanged()
    }
    fun addNewsItems(datas: MutableList<MediaModel>){
        this.datas?.addAll(0,datas)
        this.notifyItemRangeInserted(0,datas.size)
    }

    /**
     * 在删除数据时要小心，是此控件的bug，要做好处理
     */
    fun removeItem(position: Int){
        this.datas?.removeAt(position)
        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(0,itemCount)

    }
    fun removeAllItem(){
        this.notifyItemMoved(0,datas!!.size)
        clear()
    }
    fun clear() {
        datas?.clear()
    }
}