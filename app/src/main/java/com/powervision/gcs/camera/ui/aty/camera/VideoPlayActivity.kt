package com.powervision.gcs.camera.ui.aty.camera

import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import com.appunite.ffmpeg.*
import com.powervision.gcs.camera.R
import com.powervision.gcs.camera.network.RetrofitFactory
import com.powervision.gcs.camera.ui.fgt.camera.CameraParamsFragment
import kotlinx.android.synthetic.main.activity_video_play.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * 自研图传
 * Create by Sundy On 2017/6/28
 */
class VideoPlayActivity : AppCompatActivity(), FFmpegListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private var mMpegPlayer: FFmpegPlayer? = null
    private var url: String? = null
    private var cameraStatus: CameraStatus? = null
    private var isOpenCameraStatus = true

    override fun onCreate(savedInstanceState: Bundle?) {
        initWindow()
        super.onCreate(savedInstanceState)
        cameraStatus = CameraStatus.ONSTART
        setContentView(R.layout.activity_video_play)
        getIntent(intent)
        initVideo()
        setListener()
        requestCameraStatus()
    }

    /**
     * 初始化窗口属性
     */
    private fun initWindow() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFormat(PixelFormat.RGB_565)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * 请求相机状态
     */
    private fun requestCameraStatus() {
        val call = RetrofitFactory.getInstance().requestCameraMode()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {//要加sd卡判断
                    val json = response.body()!!.string().trim { it <= ' ' }
                    val jsonObject = JSONObject(json)
                    Log.i("qwert", "相机状态=" + json)
                    if (jsonObject.getString("workmode") == "20" || jsonObject.getString("workmode") == "21" || jsonObject.getString("workmode") == "23" || jsonObject.getString("workmode") == "24") {//录像模式
                        // switchRecordModel();
                        right_video_tb.setChecked(false)
                        Log.i("qwert", "相机状态: 录像模式")
                    } else if (jsonObject.getString("workmode") == "00" || jsonObject.getString("workmode") == "01" || jsonObject.getString("workmode") == "02" || jsonObject.getString("workmode") == "11") {//拍照模式
                        // switchTakephotoModel();
                        right_video_tb.setChecked(true)
                        Log.i("qwert", "相机状态: 拍照模式")
                    } else if (jsonObject.getString("workmode") == "recording") {
                        recordingModel()
                        right_video_tb.setChecked(false)
                        Log.i("qwert", "相机状态: 录像中")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                call.cancel()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
            }
        })
    }

    /**
     * 正在录像模式
     */
    private fun recordingModel() {
        cameraStatus = CameraStatus.END
        video_camera_start.setBackgroundResource(R.mipmap.video_stop)
    }

    /**
     * 设置监听
     */
    private fun setListener() {
        videoView.setOnClickListener(this)
        imgBack.setOnClickListener(this)
        right_video_tb.setOnCheckedChangeListener(this)
        video_camera_start.setOnClickListener(this)
        img_media.setOnClickListener(this)
        img_set.setOnClickListener(this)
    }

    /**
     * 获取上个界面的数据
     */
    private fun getIntent(intent: Intent) {
        if (intent != null) {
            url = intent.getStringExtra(resources.getString(R.string.input_file))
        }
    }

    /**
     * 初始化图传类
     */
    private fun initVideo() {
        mMpegPlayer = FFmpegPlayer.getInstance(applicationContext)
        mMpegPlayer?.mpegListener = this
        mMpegPlayer?.connServer(url)

        mMpegPlayer?.setGlSurfaceView(videoView as MGLSurfaceView?)
        mMpegPlayer?.setVrmode(1)
        mMpegPlayer?.setDebugLevel(1, 10)
        mMpegPlayer?.setIsharddecode(false)


    }

    /**
     * 各个控件点击事件的处理
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.videoView -> {
                var fragmentNum: Int = supportFragmentManager.backStackEntryCount
                Log.i("count1","count="+fragmentNum)
                if (fragmentNum != 0) {
                    supportFragmentManager.popBackStack()
                    isOpenCameraStatus = false
                } else {
                    isOpenCameraStatus = true
                }
            }//fragment按栈返回
            R.id.imgBack -> {
                finish()
            }
            R.id.video_camera_start -> {
                when (cameraStatus) {
                    CameraStatus.ONSTART -> requestStartRecord()
                    CameraStatus.CAMERA -> requestTakephoto()
                    CameraStatus.END -> requestStopRecord()
                }
            }
            R.id.img_media -> {
                val intent = Intent(this@VideoPlayActivity, MediaActivity::class.java)
                startActivity(intent)
            }
            R.id.img_set -> cameraParamsView()
        }

    }

    /**
     * 添加相机参数视图
     */
    private fun cameraParamsView() {
        if (isOpenCameraStatus) {
            val cameraParamsFragment = CameraParamsFragment()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out)
            transaction.add(R.id.cameraRoot, cameraParamsFragment, "test")
            transaction.addToBackStack(null)
            transaction.commit()
        } else {
            supportFragmentManager.popBackStack()
        }
        isOpenCameraStatus = !isOpenCameraStatus
    }

    /**
     * 相机停止录像
     */
    private fun requestStopRecord() {
        val call = RetrofitFactory.getInstance().requestStopRecord()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {

                    val json = response.body()!!.string().trim { it <= ' ' }
                    if (json == "Success") {
                        cameraStatus = CameraStatus.ONSTART
                        video_camera_start.setBackgroundResource(R.mipmap.video_start)
                        Toast.makeText(this@VideoPlayActivity.applicationContext, "结束录像", Toast.LENGTH_SHORT).show()
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
     * 相机开始拍照
     */
    private fun requestTakephoto() {
        val call = RetrofitFactory.getInstance().requestTakephoto()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val json = response.body()!!.string().trim { it <= ' ' }
                    if (json == "Success") {
                        video_camera_start.startCartoom(0.5f)
                        Toast.makeText(this@VideoPlayActivity.applicationContext, "拍照成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@VideoPlayActivity.applicationContext, "拍照失败", Toast.LENGTH_SHORT).show()
                    }
                    call.cancel()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
            }
        })
    }

    /**
     * 相机开始录像
     */
    private fun requestStartRecord() {
        val call = RetrofitFactory.getInstance().requestStartRecord()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val json = response.body()!!.string().trim { it <= ' ' }
                        if (json == "Success") {
                            cameraStatus = CameraStatus.END
                            video_camera_start.setBackgroundResource(R.mipmap.video_stop)
                            Toast.makeText(this@VideoPlayActivity.applicationContext, "开始录像", Toast.LENGTH_SHORT).show()
                            call.cancel()
                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
            }
        })
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            switchTakephotoModel()
        } else {
            switchRecordModel()
        }
    }

    /**
     * 切换拍照模式
     */
    private fun switchTakephotoModel() {
        val call = RetrofitFactory.getInstance().requestSwitchPhotoMode()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val json = response.body()!!.string().trim { it <= ' ' }
                        // Log.i("qwert", "切换拍照模式:" + json)
                        if (json == "Success") {
                            Toast.makeText(this@VideoPlayActivity.applicationContext, "切换拍照模式成功", Toast.LENGTH_SHORT).show()
                            cameraStatus = CameraStatus.CAMERA
                            video_camera_start.setBackgroundResource(R.mipmap.camera_shot)
                            call.cancel()
                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
            }
        })
    }

    /**
     * 切换录像模式
     */
    private fun switchRecordModel() {
        val call = RetrofitFactory.getInstance().requestSwitchRecordMode()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val json = response.body()!!.string().trim { it <= ' ' }
                        if (json == "Success") {//录像模式
                            Toast.makeText(this@VideoPlayActivity.applicationContext, "录像模式式成功", Toast.LENGTH_SHORT).show()
                            cameraStatus = CameraStatus.ONSTART
                            video_camera_start.setBackgroundResource(R.mipmap.video_start)
                            call.cancel()
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

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        this.mMpegPlayer?.setMpegListener(null)
        super.onDestroy()
    }

    override fun onBackPressed() {
        mMpegPlayer?.pause()
        mMpegPlayer?.renderStop()
        super.onBackPressed()
    }

    enum class CameraStatus {
        ONSTART, ONSTARTING, END, CAMERA
    }

    override fun onFFDataSourceLoaded(err: FFmpegError?, streams: Array<out FFmpegStreamInfo>?) {

    }

    override fun onFFResume(result: NotPlayingException?) {

    }

    override fun onFFPause(err: NotPlayingException?) {

    }

    override fun onFFStop() {

    }

    override fun onFFUpdateTime(mCurrentTimeUs: Long, mVideoDurationUs: Long, isFinished: Boolean) {

    }

    override fun onFFSeeked(result: NotPlayingException?) {

    }

    override fun onFFConnStatus(status: Int) {

    }

    override fun onFFRenderStatus(status: Int) {

    }

    override fun onFFRecordStatus(status: Int) {

    }

    override fun onFrameUpdate(status: Int) {

    }
}
