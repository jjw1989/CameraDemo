package com.powervision.gcs.camera.app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.powervision.camera.gen.DaoMaster
import com.powervision.camera.gen.DaoSession
import com.powervision.camera.gen.GreenDaoHelper
import com.powervision.gcs.camera.socket.service.CoreService

/**
 * Created by Sundy on 2017/6/27.
 */
class CameraApplication :MultiDexApplication() {
   var intent:Intent?=null

//    private var mDaoSession: DaoSession? = null
//    private var db: SQLiteDatabase? = null

    override fun onCreate() {
       intent= Intent(this@CameraApplication, CoreService::class.java)
        startService()

//        val helper = DaoMaster.DevOpenHelper(applicationContext, "media-db", null);
//         db = helper?.writableDatabase
//        val dm: DaoMaster = DaoMaster(db)
//        this.mDaoSession = dm.newSession();
        GreenDaoHelper.initDataBase(this)
        Log.w("lzq","初始化成功")

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    /**
     * 开启服务
     */
    fun startService(){
       applicationContext.startService(intent)
    }

    /**
     * 停止服务
     */
    fun stopService(){
        applicationContext.stopService(intent)
    }
//    fun getDaoSession(): DaoSession {
//        return mDaoSession!!
//    }
//
//    fun getDb(): SQLiteDatabase {
//        return db!!
//    }
    companion object {
        private val instances: CameraApplication? = CameraApplication()
        fun getIntance(): CameraApplication {
            return instances!!
        }
    }

}