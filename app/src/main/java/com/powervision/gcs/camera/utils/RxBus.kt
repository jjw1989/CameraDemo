//package com.powervision.gcs.camera.utils
//
//import io.reactivex.Flowable
//import io.reactivex.annotations.NonNull
//import io.reactivex.processors.PublishProcessor
//import io.reactivex.processors.FlowableProcessor
//
//
//
///**
// * Created by Sundy on 2017/6/29.
// */
//open class RxBus {
//    private var mBus: FlowableProcessor<Any>
//    init {
//        mBus = PublishProcessor.create<Any>().toSerialized()
//    }
//
//    companion object {
//        @Volatile
//        var instance: RxBus? = null
//        fun instance(): RxBus {
//            if (instance == null) {
//                synchronized(RxBus::class) {
//                    if (instance == null) {
//                        instance = RxBus()
//                    }
//                }
//            }
//            return instance!!
//        }
//    }
//
//
//
//    fun post(@NonNull obj: Any) {
//        mBus.onNext(obj)
//    }
//
//    fun <T> register(clz: Class<T>): Flowable<T> {
//        return mBus.ofType(clz)
//    }
//
//    fun unregisterAll() {
//        //会将所有由mBus 生成的 Flowable 都置  completed 状态  后续的 所有消息  都收不到了
//        mBus.onComplete()
//    }
//
//    fun hasSubscribers(): Boolean {
//        return mBus.hasSubscribers()
//    }
//
//}