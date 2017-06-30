package com.powervision.gcs.camera.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.powervision.gcs.camera.R
import com.powervision.gcs.camera.ui.fgt.camera.CameraFragment
import com.powervision.gcs.camera.ui.fgt.media.MediaFragment
import kotlinx.android.synthetic.main.activity_menu.*

/**
 * Create by sundy
 * AP03测试主类
 */
class MenuActivity: AppCompatActivity() {
    private var ransaction: FragmentTransaction? = null
    private var fragmentManager: FragmentManager? = null
    private var cameraFragment: CameraFragment? = null
    private var mediaFragment: MediaFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        initFragment()
    }
    fun initFragment() {
        cameraFragment = CameraFragment()
        mediaFragment = MediaFragment()
        fragmentManager = supportFragmentManager
        ransaction = fragmentManager?.beginTransaction()
        ransaction?.replace(R.id.container, cameraFragment)?.commit()
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var rt=fragmentManager?.beginTransaction()
        when (item.itemId) {
            R.id.navigation_home -> {
                rt?.replace(R.id.container, cameraFragment)?.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                rt?.replace(R.id.container, mediaFragment)?.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
