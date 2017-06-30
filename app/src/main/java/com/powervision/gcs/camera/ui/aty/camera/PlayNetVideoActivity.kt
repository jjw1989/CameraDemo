package com.powervision.gcs.camera.ui.aty.camera

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.powervision.gcs.camera.R
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
import kotlinx.android.synthetic.main.activity_play_net_video.*

/**
 * 播放网络视频
 */
class PlayNetVideoActivity : AppCompatActivity() {
    var url:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_net_video)
        Log.i("url","url="+url)
        url=intent.getStringExtra("url")
        mVideoView.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL)
        mVideoView.startVideo()
    }
}
