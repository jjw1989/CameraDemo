package com.powervision.camera.gen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.identityscope.IdentityScopeType;

/**
 * Created by ttxs on 2017/6/29.
 */

public class GreenDaoHelper
{
    private static final String JEFF_DB = "jeff_db";
    private static SQLiteDatabase db;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    public static void initDataBase(Context context){
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper
                (context, JEFF_DB);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        //数据动态更新，不使用缓存，如果需要缓存，改成Session
        mDaoSession = mDaoMaster.newSession(IdentityScopeType.None);
    }
    public static SQLiteDatabase getDb(){
        return db;
    }
    public static DaoMaster getDaoMaster(){
        return mDaoMaster;
    }
    public static DaoSession getDaoSession(){
        return mDaoSession;
    }
}
