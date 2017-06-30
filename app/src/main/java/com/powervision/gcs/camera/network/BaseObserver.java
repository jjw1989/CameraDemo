package com.powervision.gcs.camera.network;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Sundy on 2017/6/12.
 */

public abstract class BaseObserver implements Observer {
    private static final String TAG = "BaseObserver";
    private Context mContext;

    protected BaseObserver(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object o) {

    }
    //    @Override
//    public void onNext(BaseEntity<T> value) {
//        if (value.getMessage().equals("Success")) {
//            if(value.getJson()!=null){
//                T t = value.getJson();
//                onHandleSuccess(t);
//            }else {
//                onHandleError(value.getMessage());
//            }
//
//
//
//        } else {
//            onHandleError(value.getMessage());
//        }
//    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "error:" + e.toString());
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete");
    }


    protected abstract void onHandleSuccess(Object obj);

    protected void onHandleError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
