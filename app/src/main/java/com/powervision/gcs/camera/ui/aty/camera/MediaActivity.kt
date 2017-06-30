package com.powervision.gcs.camera.ui.aty.camera

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.andview.refreshview.XRefreshView
import com.andview.refreshview.XRefreshViewFooter
//import com.anye.greendao.gen.ImgDbDao
//import com.anye.greendao.gen.VideoDbDao
import com.flyco.dialog.widget.ActionSheetDialog
import com.google.gson.Gson
import com.powervision.camera.gen.GreenDaoHelper
import com.powervision.camera.gen.ImgDbDao
//import com.powervision.camera.gen.ImgDbDao
//import com.powervision.camera.gen.VideoDbDao
import com.powervision.gcs.camera.R
import com.powervision.gcs.camera.adapter.MediaSdcardAdapter
import com.powervision.gcs.camera.adapter.OnRecyclerItemClickListener

import com.powervision.gcs.camera.anim.NoAlphaItemAnimator

import com.powervision.gcs.camera.model.ImgDb

import com.powervision.gcs.camera.model.MediaModel
import com.powervision.gcs.camera.network.RetrofitFactory
import com.powervision.gcs.camera.view.DownLoadWindow
import kotlinx.android.synthetic.main.activity_media.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


/**
 * 相机媒体库主页：用来显示相机SDcard的图片和视频
 */
class MediaActivity : AppCompatActivity(), OnRecyclerItemClickListener {
    /**
     * 定义一个可变的集合
     */
    var datas: MutableList<MediaModel>? = mutableListOf()
    var fileNumber: Int = 0
    var netAdapter: MediaSdcardAdapter? = null
    var page: Int = 1
    var count: Int = 20
    var headView: View? = null
    var tvNum: TextView? = null
    var imgDao: ImgDbDao? = null
//    var videoDao: VideoDbDao?= null

    /**
     * item 单击事件处理
     */
    override fun onItemClick(view: View?, positionOut: Int) {

        Log.i("position","position ="+positionOut)
        val stringItems = arrayOf("删除制定文件", "下载","删除所有文件")
        val dialog = ActionSheetDialog(this@MediaActivity, stringItems, null)
        dialog.title("提示\r\n文件信息")//
                .titleTextSize_SP(14.5f)//
                .show()
        dialog.setOnOperItemClickL { parent, view, position, id ->
            when (position) {
                0 -> deteleFile(positionOut)
                1 -> downloadFile(positionOut)
                2->deteleAllFile()
            }
            dialog.dismiss()
        }
    }

    /**
     * 删除所有文件
     */
    private fun deteleAllFile() {
        val deleteFile = RetrofitFactory.getInstance().requestDeleteAllCameraFile()
        deleteFile.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        if(json.equals("Success")){
                            Toast.makeText(this@MediaActivity.applicationContext,"删除所有文件成功",Toast.LENGTH_SHORT).show()
                            netAdapter?.removeAllItem()
                            requestFileNumber()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                deleteFile.cancel()
            }
        })
    }

    /**
     * 删除文件
     */
    fun deteleFile(position: Int) {
        var item: MediaModel = datas!!.get(position)
        val deleteFile = RetrofitFactory.getInstance().requestDeleteCameraFile(item.path)
        deleteFile.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        //Log.i("json","删除文件="+json)
                        if (json.equals("Success")) {
                            Toast.makeText(this@MediaActivity.applicationContext, "删除成功", Toast.LENGTH_SHORT).show()
                            //xrefreshView.startRefresh()
                            netAdapter?.removeItem(position)
                            requestFileNumber()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                deleteFile.cancel()
            }
        })
    }

    /**
     * 下载文件
     */
    fun downloadFile(position: Int) {
        var item: MediaModel = datas!!.get(position)
        var pop = DownLoadWindow(this)
        pop.showAtLocation(media_content, Gravity.CENTER, 0, -100)
    }

    /**
     * 获取网络文件数量
     */
    fun requestFileNumber() {
        val fileCount = RetrofitFactory.getInstance().requestCameraFileCount()
        fileCount.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        fileNumber = jsonobj.optInt("count")
                        tvNum?.text = "SD卡文件总数：$fileNumber"
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                fileCount.cancel()
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        initRecycleView()
        requestFileNumber()
        requestNetData(page, count)


    }

    /**
     * 初始化控件和适配器
     */
    private fun initRecycleView() {
        mediaRv?.layoutManager = GridLayoutManager(this@MediaActivity, 3) as RecyclerView.LayoutManager?
        mediaRv.itemAnimator.changeDuration=0
        xrefreshView.setPinnedTime(1000)
        xrefreshView.setMoveForHorizontal(true)
        netAdapter = MediaSdcardAdapter(null)
        headView = netAdapter?.setHeaderView(R.layout.camera_top_layout, mediaRv)
        tvNum = headView?.findViewById(R.id.tv_num) as TextView
        mediaRv?.adapter = netAdapter
        netAdapter?.customLoadMoreView = XRefreshViewFooter(this)
        xrefreshView.setXRefreshViewListener(listener)
        netAdapter?.setOnRecyclerItemClickListener(this)
        xrefreshView.setAutoLoadMore(false)
        xrefreshView.setPinnedTime(1000)
        xrefreshView.setMoveForHorizontal(true)
        xrefreshView.pullLoadEnable = true
    }

    /**
     * 刷新和加载更多回调接口
     */

    internal var listener: XRefreshView.SimpleXRefreshListener = object : XRefreshView.SimpleXRefreshListener() {
        override fun onRefresh(isPullDown: Boolean) {
            super.onRefresh(isPullDown)
            requestNetData(page, count)
            Handler().postDelayed({
                xrefreshView.stopRefresh()
            }, 1000)
        }

        override fun onLoadMore(isSilence: Boolean) {
            super.onLoadMore(isSilence)
        }
    }

    /**
     * 请求相机图片个视频数据
     */
    private fun requestNetData(page: Int, count: Int) {
        val mediatDates = RetrofitFactory.getInstance().requestCameraFileList(page, count)
        mediatDates.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    Log.i("json", "json=" + json)
                    json.let {
                        json.split(";").filter {
                            item ->
                            !item.equals("")
                        }.forEach {
                            item ->
                            getData(item)
                        }.apply {
                            datas?.sortBy { it.creattime }
                            netAdapter?.addNewsItems(datas!!)
                            mediatDates.cancel()
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                mediatDates.cancel()
            }
        })
    }

    /**
     * 数据转化
     */
    fun getData(item: String) {
        val gson: Gson = Gson()
        val model = gson.fromJson(item, MediaModel::class.java)
        datas?.add(model)
    }
}
