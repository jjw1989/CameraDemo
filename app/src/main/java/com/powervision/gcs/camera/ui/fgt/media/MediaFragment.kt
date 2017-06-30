package com.powervision.gcs.camera.ui.fgt.media

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.powervision.gcs.camera.R
import com.powervision.gcs.camera.adapter.MediaAdapter
import com.powervision.gcs.camera.view.LinepagerIndicator
import com.powervision.gcs.camera.view.NoScrollViewPager

/**
 * 显示已经下载的图片和视频
 */
class MediaFragment : Fragment() {
    private var rootView: View? = null
    private var viewPage: NoScrollViewPager? = null
    private var indicatorMedia: LinepagerIndicator? = null
    private var adapter: MediaAdapter?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_media, container, false)
        viewPage = rootView?.findViewById(R.id.view_pager) as NoScrollViewPager?
        indicatorMedia = rootView?.findViewById(R.id.indicator_media) as LinepagerIndicator?
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter= MediaAdapter(context,activity.supportFragmentManager)
         viewPage?.adapter=adapter
        indicatorMedia?.setViewPager(viewPage)
    }

}
