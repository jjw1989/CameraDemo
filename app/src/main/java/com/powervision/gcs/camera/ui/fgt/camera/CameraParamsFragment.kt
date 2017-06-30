package com.powervision.gcs.camera.ui.fgt.camera

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.powervision.gcs.camera.R
import com.powervision.gcs.camera.adapter.CameraAdapter
import com.powervision.gcs.camera.adapter.OnRecyclerItemClickListener
import com.powervision.gcs.camera.model.CameraModel
import com.powervision.gcs.camera.network.RetrofitFactory
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


/**
 *相机参数页面
 */
class CameraParamsFragment : Fragment(), OnRecyclerItemClickListener {
    var paramsList: List<CameraModel>? = null
    var cameraAdapter: CameraAdapter? = null
    var mView: View? = null
    var recyleView: RecyclerView? = null
    var imgBack:ImageView?=null
    var dName:String?=null
    var softversion:String?=null
    var hardversion:String?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (null != mView) {//防止多次调用
//            var parent: ViewGroup = (mView?.parent as? ViewGroup)!!
//            if (null != parent) {
//                parent.removeView(mView)
//            }
        } else {
            mView = inflater!!.inflate(R.layout.fragment_camera_params, container, false)
            initView(mView)
        }
        return mView
    }

    private fun initView(mView: View?) {
        recyleView = mView?.findViewById(R.id.camera_rv) as RecyclerView
        imgBack=mView?.findViewById(R.id.imgBack) as ImageView
        requestCameraAllParams()
        initDate()
        initRecyleView()
        RxBus.get().register(this)
        imgBack?.setOnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
    }

    /**
     * 刷新参数
     */
    @Subscribe(tags = arrayOf(Tag("refreshParams")))
    fun refreshParams(name: String) {
        when (name) {
            "0" -> getRecordResolution()
            "1" -> getSlowRecordResolution()
            "2" -> getCircleRecordResolution()
            "3" -> getSeparateRecordTime()
            "4" -> getShortenRecordResolution()
            "5" -> getShortenRecordTimeInterval()
            "6" -> getRecordInPhotoResolution()
            "7" -> getRecordInPhotoTimeInterval()
            "8" -> getSinglePhotoResolution()
            "9" -> getPhotoMode()
            "10" -> getIso()
            "11" -> getEV()
            "12" -> getExplose()
            "13" -> getWhitebalance()
            "14" -> getMeteringMode()
            "15" -> getDelayPhotoResolution()
            "16" -> getDelayTime()
            "17" -> getRAWResolution()
            "18" -> getTimingPhotoResolution()
            "19" -> getTimingPhotoTime()
            "20" -> getContinuePhotoResolution()
            "21" -> getMulContinuePhotoResolution()
            "22" -> getMulContinuePhotoVelocity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.get().unregister(this)
    }

    /**
     * 请求相机所有参数
     */
    private fun requestCameraAllParams() {
        //1录像分辨率
        getRecordResolution()
        //2慢动作分辨率
        getSlowRecordResolution()
        //3循环录影分辨率
        getCircleRecordResolution()
        //4分段录影时间
        getSeparateRecordTime()
        //5缩时录影分辨率
        getShortenRecordResolution()
        //6缩时录影间隔
        getShortenRecordTimeInterval()
        //7录中拍分辨率
        getRecordInPhotoResolution()
        //8录中拍时间
        getRecordInPhotoTimeInterval()
        //9单张照片分辨率
        getSinglePhotoResolution()
        //10拍照模式
        getPhotoMode()
        //11ISO
        getIso()
        //12 EV
        getEV()
        //13曝光时间
        getExplose()
        //14白平衡
        getWhitebalance()
        //15测光模式
        getMeteringMode()
        //16延时拍照分辨率
        getDelayPhotoResolution()
        //17延时时间
        getDelayTime()
        //18RAW拍照分辨率
        getRAWResolution()
        //19定时拍照分辨率
        getTimingPhotoResolution()
        //20获取定时拍照时间
        getTimingPhotoTime()
        //21持续拍照分辨率
        getContinuePhotoResolution()
        //22多练拍分辨率
        getMulContinuePhotoResolution()
        //23多练拍速度
        getMulContinuePhotoVelocity()
        //24 sd卡信息
        getSDCapacity()
        //27获取相机当前时间
        getCameraCurrentDate()
        //
        requestDeviceInfo()
    }

    private fun getRecordResolution() {
        val recordResolution = RetrofitFactory.getInstance().requestGetRecordResolution()
        recordResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        val videoMode: String? = jsonobj.optString("videoMode")
                        videoMode?.let {
                            paramsList!![0].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            recordResolution.cancel()
                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                recordResolution.cancel()
            }
        })
    }

    private fun getSlowRecordResolution() {
        val slowRecordResolution = RetrofitFactory.getInstance().requestGetSlowRecordResolution()
        slowRecordResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        val videoMode: String? = jsonobj.optString("videoMode")
                        videoMode?.let {
                            paramsList!![1].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            slowRecordResolution.cancel()
                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                slowRecordResolution.cancel()
            }
        })
    }

    /**
     * 循环录影分辨率
     */
    private fun getCircleRecordResolution() {
        val circleRecordResolution = RetrofitFactory.getInstance().requestGetCircleRecordResolution()
        circleRecordResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        val videoMode: String? = jsonobj.optString("videoMode")
                        videoMode?.let {
                            paramsList!![2].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            circleRecordResolution.cancel()
                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                circleRecordResolution.cancel()
            }
        })
    }

    /**
     * 分段录影时间
     */
    private fun getSeparateRecordTime() {
        val separateRecordTime = RetrofitFactory.getInstance().requestGetSeparateRecordTime()
        separateRecordTime.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        //  Log.i("json","4分段录影时间="+json)
                        val videoMode: String? = jsonobj.optString("InterValString")
                        videoMode?.let {
                            paramsList!![3].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            separateRecordTime.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                separateRecordTime.cancel()
            }
        })
    }

    /**
     * 缩时录影分辨率
     */
    private fun getShortenRecordResolution() {
        val shortenRecordResolution = RetrofitFactory.getInstance().requestGetShortenRecordResolution()
        shortenRecordResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json","5缩时录影分辨率="+json)
                        val videoMode: String? = jsonobj.optString("videoMode")
                        videoMode?.let {
                            paramsList!![4].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            shortenRecordResolution.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                shortenRecordResolution.cancel()
            }
        })
    }

    /**
     * 6缩时录影间隔
     */
    private fun getShortenRecordTimeInterval() {
        val shortenRecordTimeInterval = RetrofitFactory.getInstance().requestGetShortenRecordTimeInterval()
        shortenRecordTimeInterval.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json","6缩时录影间隔="+json)
                        val videoMode: String? = jsonobj.optString("InterValString")
                        videoMode?.let {
                            paramsList!![5].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            shortenRecordTimeInterval.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                shortenRecordTimeInterval.cancel()
            }
        })
    }

    /**
     * 录中拍分辨率
     */
    private fun getRecordInPhotoResolution() {
        val recordInPhotoResolution = RetrofitFactory.getInstance().requestGetRecordInPhotoResolution()
        recordInPhotoResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json","7录中拍分辨率="+json)
                        val videoMode: String? = jsonobj.optString("videoMode")
                        videoMode?.let {
                            paramsList!![6].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            recordInPhotoResolution.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                recordInPhotoResolution.cancel()
            }
        })
    }

    /**
     * 录中拍时间
     */
    private fun getRecordInPhotoTimeInterval() {
        val recordInPhotoTimeInterval = RetrofitFactory.getInstance().requestGetRecordInPhotoTimeInterval()
        recordInPhotoTimeInterval.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json","8录中拍时间="+json)
                        val videoMode: String? = jsonobj.optString("InterValString")
                        videoMode?.let {
                            paramsList!![7].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            recordInPhotoTimeInterval.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                recordInPhotoTimeInterval.cancel()
            }
        })
    }

    /**
     *单张照片分辨率
     */
    private fun getSinglePhotoResolution() {
        val singlePhotoResolution = RetrofitFactory.getInstance().requestGetSinglePhotoResolution()
        singlePhotoResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        //Log.i("json","9单张照片分辨率="+json)
                        val videoMode: String? = jsonobj.optString("normal_photo_resolution")
                        videoMode?.let {
                            paramsList!![8].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            singlePhotoResolution.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                singlePhotoResolution.cancel()
            }
        })
    }

    /**
     * 拍照模式
     */
    private fun getPhotoMode() {
        val photoMode = RetrofitFactory.getInstance().requestGetPhotoMode()
        photoMode.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json","10拍照模式="+json)
                        val videoMode: String? = jsonobj.optString("normal_photo_scene")
                        videoMode?.let {
                            paramsList!![9].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            photoMode.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                photoMode.cancel()
            }
        })
    }

    /***获取ISO
     *
     */
    private fun getIso() {
        val iso = RetrofitFactory.getInstance().requestGetISO()
        iso.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        //Log.i("json","11ISO="+json)
                        val videoMode: String? = jsonobj.optString("normal_photo_exposure_iso")
                        videoMode?.let {
                            paramsList!![10].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            iso.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                iso.cancel()
            }
        })
    }

    /**
     * 获取EV值
     */
    private fun getEV() {
        val ev = RetrofitFactory.getInstance().requestGetEV()
        ev.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json","12 EV="+json)
                        val videoMode: String? = jsonobj.optString("normal_photo_exposure_ev")
                        videoMode?.let {
                            paramsList!![11].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            ev.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                ev.cancel()
            }
        })
    }

    /**
     * 曝光时间
     */
    private fun getExplose() {
        val explose = RetrofitFactory.getInstance().requestGetExplose()
        explose.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json","13曝光时间="+json)
                        val videoMode: String? = jsonobj.optString("normal_photo_exposure_time")
                        videoMode?.let {
                            paramsList!![12].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            explose.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                explose.cancel()
            }
        })
    }

    /**
     *获取白平衡
     */
    private fun getWhitebalance() {
        val whitebalance = RetrofitFactory.getInstance().requestGetWhitebalance()
        whitebalance.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        //Log.i("json","14白平衡="+json)
                        val videoMode: String? = jsonobj.optString("normal_photo_exposure_wb")
                        videoMode?.let {
                            paramsList!![13].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            whitebalance.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                whitebalance.cancel()
            }
        })
    }

    /**
     * 获取测光模式
     */
    private fun getMeteringMode() {
        val meteringMode = RetrofitFactory.getInstance().requestGetMeteringMode()
        meteringMode.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        //Log.i("json","15测光模式="+json)
                        val videoMode: String? = jsonobj.optString("normal_photo_metry")
                        videoMode?.let {
                            paramsList!![14].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            meteringMode.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                meteringMode.cancel()
            }
        })
    }

    /**
     * 获取延时拍照分辨率
     */
    private fun getDelayPhotoResolution() {
        val delayPhotoResolution = RetrofitFactory.getInstance().requestGetDelayPhotoResolution()
        delayPhotoResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        //Log.i("json","16延时拍照分辨率="+json)
                        val videoMode: String? = jsonobj.optString("timer_photo_resolution")
                        videoMode?.let {
                            paramsList!![15].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            delayPhotoResolution.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                delayPhotoResolution.cancel()
            }
        })
    }

    /**
     * 获取延时时间
     */
    private fun getDelayTime() {
        val delayTime = RetrofitFactory.getInstance().requestGetDelayTime()
        delayTime.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        //Log.i("json","17延时时间="+json)
                        val videoMode: String? = jsonobj.optString("timer_photo_InterValString")
                        videoMode?.let {
                            paramsList!![16].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            delayTime.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                delayTime.cancel()
            }
        })
    }

    /**
     *获取RAW拍照分辨率
     */
    private fun getRAWResolution() {
        val rAWResolution = RetrofitFactory.getInstance().requestGetRAWResolution()
        rAWResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        if (!json.contains("SvrFuncResult")) {
                            // Log.i("json", "18RAW拍照分辨率=" + json)
                            val jsonobj = JSONObject(json)
                            val videoMode: String? = jsonobj.optString("timer_photo_resolution")
                            videoMode?.let {
                                paramsList!![17].value = videoMode
                                cameraAdapter?.notifyDataSetChanged()
                                rAWResolution.cancel()
                            }
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                rAWResolution.cancel()
            }
        })
    }

    /**
     * 获取定时拍照分辨率
     */
    private fun getTimingPhotoResolution() {
        val timingPhotoResolution = RetrofitFactory.getInstance().requestGetTimingPhotoResolution()
        timingPhotoResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json","19定时拍照分辨率="+json)
                        val videoMode: String? = jsonobj.optString("multi_photo_lapse_resolution")
                        videoMode?.let {
                            paramsList!![18].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            timingPhotoResolution.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                timingPhotoResolution.cancel()
            }
        })
    }

    /**
     * 获取定时拍照时间
     */
    private fun getTimingPhotoTime() {
        val timingPhotoTime = RetrofitFactory.getInstance().requestGetTimingPhotoTime()
        timingPhotoTime.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        //Log.i("json","20获取定时拍照时间="+json)
                        val videoMode: String? = jsonobj.optString("multi_photo_lapse_InterValString")
                        videoMode?.let {
                            paramsList!![19].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            timingPhotoTime.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                timingPhotoTime.cancel()
            }
        })
    }

    /**
     * 获取持续拍照分辨率
     */
    private fun getContinuePhotoResolution() {
        val continuePhotoResolution = RetrofitFactory.getInstance().requestGetContinuePhotoResolution()
        continuePhotoResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json","21持续拍照分辨率="+json)
                        val videoMode: String? = jsonobj.optString("multi_photo_continus_resolution")
                        videoMode?.let {
                            paramsList!![20].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            continuePhotoResolution.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                continuePhotoResolution.cancel()
            }
        })
    }

    /**
     * 获取多练拍分辨率
     */
    private fun getMulContinuePhotoResolution() {
        val mulContinuePhotoResolution = RetrofitFactory.getInstance().requestGetMulContinuePhotoResolution()
        mulContinuePhotoResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        //Log.i("json","22多练拍分辨率="+json)
                        val videoMode: String? = jsonobj.optString("multi_photo_burst_resolution")
                        videoMode?.let {
                            paramsList!![21].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            mulContinuePhotoResolution.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                mulContinuePhotoResolution.cancel()
            }
        })
    }

    /**
     * 获取多连拍速度
     */
    private fun getMulContinuePhotoVelocity() {
        val mulContinuePhotoVelocity = RetrofitFactory.getInstance().requestGetMulContinuePhotoVelocity()
        mulContinuePhotoVelocity.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json","23多练拍速度="+json)
                        val videoMode: String? = jsonobj.optString("multi_photo_burst_type")
                        videoMode?.let {
                            paramsList!![22].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            mulContinuePhotoVelocity.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                mulContinuePhotoVelocity.cancel()
            }
        })
    }

    /**
     *获取SD卡的容量和使用容量
     */
    private fun getSDCapacity() {
        val sDCapacity = RetrofitFactory.getInstance().requestSDCapacity()
        sDCapacity.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        //Log.i("json", "24 sd卡信息=" + json)
                        val jsonobj = JSONObject(json)
                        val used: String? = jsonobj.optString("used")
                        val total: String? = jsonobj.optString("total")
                        total?.let {
                            used?.let {
                                paramsList!![23].value = used + "|" + total
                                cameraAdapter?.notifyDataSetChanged()
                                sDCapacity.cancel()
                            }

                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                sDCapacity.cancel()
            }
        })
    }

    /**
     * 获取相机当前时间
     */
    private fun getCameraCurrentDate() {
        val cameraCurrentDate = RetrofitFactory.getInstance().requestCameraCurrentDate()
        cameraCurrentDate.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        val jsonobj = JSONObject(json)
                        // Log.i("json", "27获取相机当前时间=" + json)
                        val videoMode: String? = jsonobj.optString("time")
                        videoMode?.let {
                            paramsList!![26].value = videoMode
                            cameraAdapter?.notifyDataSetChanged()
                            cameraCurrentDate.cancel()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                cameraCurrentDate.cancel()
            }
        })
    }

    /**
     * 获取设备信息
     */
    fun requestDeviceInfo(){
        val deviceAttribute = RetrofitFactory.getInstance().requestGetDeviceAttribute()
        deviceAttribute.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                         val jsonobj = JSONObject(json)
                         Log.i("json", "requestDeviceInfo=" + json)
                         dName=jsonobj.optString("name")
                         softversion=jsonobj.optString("softversion")
                         hardversion=jsonobj.optString("hardversion")
                        paramsList!![27].value = dName!!
                        paramsList!![28].value = softversion!!
                        paramsList!![29].value = hardversion!!
                        cameraAdapter?.notifyDataSetChanged()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                deviceAttribute.cancel()
            }
        })
    }
    override fun onItemClick(view: View?, position: Int) {
        when (position) {
            23 -> formatSdcard()
            24 -> factoryCamera()
            25 -> restartCamera()
            26,27,28,29,30->{}
            else -> commonFunction(position)
        }

    }

    /**
     * 格式化SD卡
     */
    fun formatSdcard() {
        val cameraCurrentDate = RetrofitFactory.getInstance().requestFormatSD()
        cameraCurrentDate.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "格式化SD卡=" + json)

                        if (json.equals("format sucess")) {
                            Toast.makeText(context, "格式化sd卡成功", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "格式化sd卡失败", Toast.LENGTH_SHORT).show()
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    /**
     * 恢复相机出厂设置
     */
    fun factoryCamera() {//恢复相机出厂设置不能反回数据
        val cameraCurrentDate = RetrofitFactory.getInstance().requestRestoreCamera()
        Toast.makeText(context, "请重新连接WIFI", Toast.LENGTH_SHORT).show()
        cameraCurrentDate.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    /**
     * 重启相机
     */
    fun restartCamera() {//重启相机不能反回数据
        Toast.makeText(context, "请重新连接WIFI", Toast.LENGTH_SHORT).show()
        val cameraCurrentDate = RetrofitFactory.getInstance().requestRebootCamera()
        cameraCurrentDate.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }

    /**
     *跳转通用子菜单
     */
    fun commonFunction(position: Int) {
        val cameraChildFragment = CameraChildFragment()
        var bundle: Bundle = Bundle()
        bundle.putInt("type", position)
        cameraChildFragment.arguments = bundle
        val manager = activity.supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out)
        transaction.replace(R.id.cameraRoot, cameraChildFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /**
     * 初始化数据
     */
    private fun initDate() {
        var model1 = CameraModel("录像分辨率", "", true)
        var model2 = CameraModel("慢动作分辨率", "", true)
        var model3 = CameraModel("循环录影分辨率", "", true)
        var model4 = CameraModel("分段录影时间", "", true)
        var model5 = CameraModel("缩时录影分辨率", "", true)
        var model6 = CameraModel("缩时录影间隔", "", true)
        var model7 = CameraModel("录中拍分辨率", "", true)
        var model8 = CameraModel("录中拍时间", "", true)
        var model9 = CameraModel("单张照片分辨率", "", true)
        var model10 = CameraModel("拍照模式", "", true)
        var model11 = CameraModel("ISO", "", true)
        var model12 = CameraModel("EV", "", true)
        var model13 = CameraModel("曝光时间", "", true)
        var model14 = CameraModel("白平衡", "", true)
        var model15 = CameraModel("测光模式", "", true)
        var model16 = CameraModel("延时拍照分辨率", "", true)
        var model17 = CameraModel("延时时间", "", true)
        var model18 = CameraModel("RAW拍照分辨率", "", true)
        var model19 = CameraModel("定时拍照分辨率", "", true)
        var model20 = CameraModel("定时拍照时间", "", true)
        var model21 = CameraModel("持续拍照分辨率", "", true)
        var model22 = CameraModel("多连拍分辨率", "", true)
        var model23 = CameraModel("多连拍速度", "", true)
        var model24 = CameraModel("格式化SD卡", "", true)
        var model25 = CameraModel("恢复相机出厂设置", "", true)
        var model26 = CameraModel("重启相机", "", true)
        var model27 = CameraModel("相机当前时间", "", true)
        var model28 = CameraModel("设备名称", "", true)
        var model29 = CameraModel("软件版本", "", true)
        var model30 = CameraModel("硬件版本", "", true)
        paramsList = mutableListOf<CameraModel>(model1, model2, model3, model4, model5,
                model6, model7, model8, model9, model10, model11, model12, model13, model14,
                model15, model16, model17, model18, model19, model20, model21, model22, model23, model24, model25, model26, model27,model28,model29,model30)

    }

    /**
     * 初始化列表控件
     */
    private fun initRecyleView() {
        recyleView?.layoutManager = LinearLayoutManager(context)
        cameraAdapter = CameraAdapter(paramsList)
        recyleView?.adapter = cameraAdapter
        cameraAdapter?.setOnRecyclerItemClickListener(this)
    }


}
