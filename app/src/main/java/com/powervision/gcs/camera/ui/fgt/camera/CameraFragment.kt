package com.powervision.gcs.camera.ui.fgt.camera

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.appunite.ffmpeg.FFmpegError
import com.appunite.ffmpeg.FFmpegListener
import com.appunite.ffmpeg.FFmpegStreamInfo
import com.appunite.ffmpeg.NotPlayingException
import com.powervision.gcs.camera.R
import com.powervision.gcs.camera.ui.aty.camera.VideoPlayActivity
import kotlinx.android.synthetic.main.fragment_camera.*

/**
 * 输入图传地址页
 */
class CameraFragment : Fragment() , FFmpegListener {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnPlay.setOnClickListener {
//            val mMpegPlayer = FFmpegPlayer.getInstance(context)
//            mMpegPlayer.mpegListener = this@CameraFragment
//            mMpegPlayer.connServer( inputUrl.text.toString())
            var intent = Intent(context, VideoPlayActivity::class.java)
            intent.putExtra(resources.getString(R.string.input_file), inputUrl.text.toString())
            context.startActivity(intent)
        }
    }
    override fun onFFPause(err: NotPlayingException?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFFStop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFFUpdateTime(mCurrentTimeUs: Long, mVideoDurationUs: Long, isFinished: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFFSeeked(result: NotPlayingException?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFFConnStatus(status: Int) {
        when (status) {
            0 -> Log.e("conn", "conn ok")
            1 -> Log.e("conn", "reconn...")
            else -> {
            }
        }
    }

    override fun onFFRenderStatus(status: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFFRecordStatus(status: Int) {
        when (status) {
            0 -> Log.e("test", "录制停止")
            2 -> Log.e("test", "正在录制")
            3 -> Log.e("test", "录制停止")
            else -> {
            }
        }
    }

    override fun onFrameUpdate(status: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFFResume(result: NotPlayingException?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFFDataSourceLoaded(err: FFmpegError?, streams: Array<out FFmpegStreamInfo>?) {
        if (err != null) {
            val format = resources.getString(R.string.main_could_not_open_stream)
            val message = String.format(format, err.message)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        }
    }

}