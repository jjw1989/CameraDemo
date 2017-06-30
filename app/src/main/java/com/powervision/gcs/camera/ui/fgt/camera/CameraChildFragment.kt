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
import com.hwangjr.rxbus.RxBus
import com.powervision.gcs.camera.R
import com.powervision.gcs.camera.adapter.CameraAdapter
import com.powervision.gcs.camera.adapter.OnRecyclerItemClickListener
import com.powervision.gcs.camera.model.CameraModel
import com.powervision.gcs.camera.network.RetrofitFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * 相机子菜单
 */
class CameraChildFragment : Fragment(), OnRecyclerItemClickListener {
    var mView: View? = null
    var type: Int = -1
    var dataControllrt: DataController? = null
    var cameraAdapter: CameraAdapter? = null
    var recylerView: RecyclerView? = null
    var imgBack:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments.getInt("type")
        dataControllrt = DataController.newInstance(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(R.layout.fragment_camera_child, container, false)
        recylerView = mView?.findViewById(R.id.camera_child_rv) as RecyclerView
        imgBack=mView?.findViewById(R.id.imgBack) as ImageView
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        imgBack?.setOnClickListener({
            activity.supportFragmentManager.popBackStack()
        })
    }

    /**
     * 初始化View
     */
    private fun initView() {
        cameraAdapter = CameraAdapter(getData(type))
        recylerView?.layoutManager = LinearLayoutManager(context)
        recylerView?.adapter = cameraAdapter
        cameraAdapter?.setOnRecyclerItemClickListener(this@CameraChildFragment)

    }

    /**
     * 获取子菜单所有数据
     */
    fun getData(type: Int): List<CameraModel>? {
        when (type) {
            0 -> return dataControllrt?.recordResolution()
            1 -> return dataControllrt?.slowResolution()
            2 -> return dataControllrt?.cyclicResolution()
            3 -> return dataControllrt?.sectionTime()
            4 -> return dataControllrt?.shrinkTimeResolution()
            5 -> return dataControllrt?.shrinkTimeInterval()
            6 -> return dataControllrt?.recordingResolution()
            7 -> return dataControllrt?.recordTime()
            8 -> return dataControllrt?.slngleResolution()
            9 -> return dataControllrt?.takePhotosModel()
            10 -> return dataControllrt?.iso()
            11 -> return dataControllrt?.ev()
            12 -> return dataControllrt?.exTime()
            13 -> return dataControllrt?.whiteBalance()
            14 -> return dataControllrt?.photometryModel()
            15 -> return dataControllrt?.delayResolution()
            16 -> return dataControllrt?.delayTime()
            17 -> return dataControllrt?.rawResolution()
            18 -> return dataControllrt?.timingResolution()
            19 -> return dataControllrt?.timingPhotoTime()
            20 -> return dataControllrt?.continueResolution()
            21 -> return dataControllrt?.moreResolution()
            22 -> return dataControllrt?.moreSpeed()
            else -> return null
        }

    }

    /**
     * item 按类型处理数据
     */
    override fun onItemClick(view: View?, position: Int) {
        when (type) {
            0 -> setRecordResolution(position)
            1 -> setSlowRecordResolution(position)
            2 -> setCircleRecordResolution(position)
            3 -> setSeparateRecordTime(position)
            4 -> setShortenRecordResolution(position)
            5 -> setShortenRecordTimeInterval(position)
            6 -> setRecordInPhotoResolution(position)
            7 -> setRecordInPhotoTimeInterval(position)
            8 -> setSinglePhotoResolution(position)
            9 -> setPhotoMode(position)
            10 -> setISO(position)
            11 -> setEV(position)
            12 -> setExplose(position)
            13 -> setWhitebalance(position)
            14 -> setMeteringMode(position)
            15 -> setDelayPhotoResolution(position)
            16 -> setDelayTime(position)
            17 -> setRAWResolution(position)
            18 -> setTimingPhotoResolution(position)
            19 -> setTimingPhotoTime(position)
            20 -> setContinuePhotoResolution(position)
            21 -> setMulContinuePhotoResolution(position)
            22 -> setMulContinuePhotoVelocity(position)
        }
        activity.supportFragmentManager.popBackStack()
        RxBus.get().post("refreshParams",type.toString())
    }


    /**
     * 0设置录像分辨率
     */
    private fun setRecordResolution(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 1
            2 -> id = 2
            3 -> id = 4
            4 -> id = 7
            5 -> id = 15
            6 -> id = 17
        }
        val recordResolution = RetrofitFactory.getInstance().requestSetRecordResolution(id)
        recordResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "0设置录像分辨率=" + json)
                        recordResolution.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                recordResolution.cancel()
            }
        })
    }

    /**
     * 1设置慢动作分辨率
     */
    private fun setSlowRecordResolution(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 1
            2 -> id = 2
            3 -> id = 4
        }
        val recordResolution = RetrofitFactory.getInstance().requestSetSlowRecordResolution(id)
        recordResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "1设置慢动作分辨率=" + json)
                        recordResolution.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                recordResolution.cancel()
            }
        })
    }

    /**
     * 2设置循环录影分辨率
     */
    private fun setCircleRecordResolution(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 1
            2 -> id = 2
            3 -> id = 4
            4 -> id = 7
            5 -> id = 15
            6 -> id = 17
        }
        val recordResolution = RetrofitFactory.getInstance().requestSetCircleRecordResolution(id)
        recordResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "2设置循环录影分辨率=" + json)
                        recordResolution.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                recordResolution.cancel()
            }
        })
    }

    /**
     * 3分段录影时间
     */
    private fun setSeparateRecordTime(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 1
            2 -> id = 2
            3 -> id = 3
        }
        val separateRecordTime = RetrofitFactory.getInstance().requestSetSeparateRecordTime(id)
        separateRecordTime.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "3分段录影时间=" + json)
                        separateRecordTime.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                separateRecordTime.cancel()
            }
        })
    }

    /**
     * 4缩时录影分辨率
     */
    private fun setShortenRecordResolution(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 1
            2 -> id = 2
            3 -> id = 4
        }
        val shortenRecordResolution = RetrofitFactory.getInstance().requestSetShortenRecordResolution(id)
        shortenRecordResolution.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "4缩时录影分辨率=" + json)
                        shortenRecordResolution.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                shortenRecordResolution.cancel()
            }
        })
    }

    /**
     * 5缩时录影间隔
     */
    private fun setShortenRecordTimeInterval(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 500
            1 -> id = 1000
            2 -> id = 2000
            3 -> id = 5000
            4 -> id = 10000
            5 -> id = 30000
            6 -> id = 60000
        }
        val shortenRecordTimeInterval = RetrofitFactory.getInstance().requestSetShortenRecordTimeInterval(id)
        shortenRecordTimeInterval.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "5缩时录影间隔=" + json)
                        shortenRecordTimeInterval.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                shortenRecordTimeInterval.cancel()
            }
        })
    }

    /**
     * 6录中拍分辨率
     */
    private fun setRecordInPhotoResolution(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 3
            1 -> id = 16
            2 -> id = 18
            3 -> id = 8
        }
        val callBack = RetrofitFactory.getInstance().requestSetRecordInPhotoResolution(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "6录中拍分辨率=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 7录中拍间隔
     */
    private fun setRecordInPhotoTimeInterval(position: Int) {
        var id: Int = -1
        when (position) {//3000 5000 10000 30000 60000 300000
            0 -> id = 3000
            1 -> id = 5000
            2 -> id = 10000
            3 -> id = 30000
            4 -> id = 60000
            5 -> id = 300000
        }
        val callBack = RetrofitFactory.getInstance().requestSetRecordInPhotoTimeInterval(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "7录中拍间隔=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     *8单张照片分辨率
     */
    private fun setSinglePhotoResolution(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 9
            1 -> id = 10
        }
        val callBack = RetrofitFactory.getInstance().requestSetSinglePhotoResolution(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "8单张照片分辨率=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 9设置拍照模式
     */
    private fun setPhotoMode(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 1
            2 -> id = 2
            3 -> id = 3
        }
        val callBack = RetrofitFactory.getInstance().requestSetPhotoMode(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "9设置拍照模式=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 10设置ISO
     */
    private fun setISO(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 100
            2 -> id = 200
            3 -> id = 400
            4 -> id = 800
            5 -> id = 1600
            6 -> id = 3200
            7 -> id = 6400
        }
        val callBack = RetrofitFactory.getInstance().requestSetISO(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "10设置ISO=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 11设置EV
     */
    private fun setEV(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 9
            2 -> id = 8
            3 -> id = 7
            4 -> id = 6
            5 -> id = 5
            6 -> id = 4
            7 -> id = 3
            8 -> id = 2
            9 -> id = 1
        }
        val callBack = RetrofitFactory.getInstance().requestSetEV(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "11设置EV=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 12设置曝光时间
     */
    private fun setExplose(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 2
            2 -> id = 5
            3 -> id = 10
            4 -> id = 15
            5 -> id = 20
            6 -> id = 30
        }
        val callBack = RetrofitFactory.getInstance().requestSetExplose(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "12设置曝光时间=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 13设置白平衡
     */
    private fun setWhitebalance(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 1
            2 -> id = 2
            3 -> id = 3
            4 -> id = 4
            5 -> id = 5
        }
        val callBack = RetrofitFactory.getInstance().requestSetWhitebalance(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "13设置白平衡=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 14设置测光模式
     */
    private fun setMeteringMode(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 1
        }
        val callBack = RetrofitFactory.getInstance().requestSetMeteringMode(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "14设置测光模式=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 15设置延迟拍照分辨率
     */
    private fun setDelayPhotoResolution(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 9
            1 -> id = 10
        }
        val callBack = RetrofitFactory.getInstance().requestSetDelayPhotoResolution(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "15设置延迟拍照分辨率=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 16设置延时拍照
     */
    private fun setDelayTime(position: Int) {
        var id: Int = -1
        when (position) {//{1 3 5 10 30}
            0 -> id = 1
            1 -> id = 3
            2 -> id = 5
            3 -> id = 10
            4 -> id = 30
        }
        val callBack = RetrofitFactory.getInstance().requestSetDelayTime(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "16设置延时拍照=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 17RAW拍照分辨率
     */
    private fun setRAWResolution(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 9
            1 -> id = 10
        }
        val callBack = RetrofitFactory.getInstance().requestSetRAWResolution(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "17RAW拍照分辨率=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     *18设置定时拍照分辨率
     */
    private fun setTimingPhotoResolution(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 9
            1 -> id = 10
        }
        val callBack = RetrofitFactory.getInstance().requestSetTimingPhotoResolution(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "18设置定时拍照分辨率=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 19设置定时拍照时间
     */
    private fun setTimingPhotoTime(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 500
            1 -> id = 1000
            2 -> id = 2000
            3 -> id = 3000
            4 -> id = 5000
            5 -> id = 10000
            6 -> id = 30000
            7 -> id = 60000
            8 -> id = 300000
        }
        val callBack = RetrofitFactory.getInstance().requestSetTimingPhotoTime(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "19设置定时拍照时间=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 20设置持续拍照分辨率
     */
    private fun setContinuePhotoResolution(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 12
            1 -> id = 13

        }
        val callBack = RetrofitFactory.getInstance().requestSetContinuePhotoResolution(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "20设置持续拍照分辨率=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 21设置多连拍分辨率
     */
    private fun setMulContinuePhotoResolution(position: Int) {
        var id: Int = -1
        when (position) {//3 5 10 12 13 }
            0 -> id = 12
            1 -> id = 13
        }
        val callBack = RetrofitFactory.getInstance().requestSetMulContinuePhotoResolution(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "21设置多连拍分辨率=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }

    /**
     * 22设置多连拍速度
     */
    private fun setMulContinuePhotoVelocity(position: Int) {
        var id: Int = -1
        when (position) {
            0 -> id = 0
            1 -> id = 1
            2 -> id = 2
            3 -> id = 3
            4 -> id = 4
            5 -> id = 5
            6 -> id = 6
            7 -> id = 7

        }
        val callBack = RetrofitFactory.getInstance().requestSetMulContinuePhotoVelocity(id)
        callBack.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    json.let {
                        Log.i("json", "22设置多连拍速度=" + json)
                        callBack.cancel()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    //activity.supportFragmentManager.popBackStack()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callBack.cancel()
            }
        })
    }
}
