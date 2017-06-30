//package com.powervision.gcs.camera.ui.aty.camera;
//
//import android.animation.ObjectAnimator;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.PixelFormat;
//import android.graphics.Point;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.CompoundButton;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.ToggleButton;
//
//import com.andview.refreshview.XRefreshView;
//import com.hisilicon.camplayer.HiCamPlayer;
//import com.hisilicon.camplayer.HiCamPlayer.HiCamPlayerState;
//import com.hisilicon.camplayer.HiCamPlayer.RecFrameInfo;
//import com.hisilicon.camplayer.HiCamPlayer.YuvFrameInfo;
//import com.powervision.gcs.camera.R;
//import com.powervision.gcs.camera.network.RetrofitFactory;
//import com.powervision.gcs.camera.ui.fgt.camera.CameraParamsFragment;
//import com.powervision.gcs.camera.view.CircleProgress;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.FileChannel;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
//public class HiCamPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback {
//    private static final String TAG = "HiCamPlayerActivity";
//    private static boolean sLoaded = false;
//    String mfilePath;
//    private int mWindowWidth = 0;
//    private int mWindowHeight = 0;
//    private static final int PLAYER_ASR_CHANGE = 0x01;
//    private final static int PLAYER_START_ERR = 29;
//    private final static int PLAYER_PLAY_ERR = 30;
//    private final static int SEEK_BAR_REFRESH = 31;
//    private final static int SEEK_BAR_DISPPEAR = 32;
//    private final static int SEEK_BAR_SETVISIBLE = 33;
//    private final static int SHOW_LOADINGVIEW = 34;
//    private final static int HIDE_LOADINGVIEW = 35;
//    private final static int PLAYER_ONPAUSED = 36;
//    private final static int PLAYER_ONPLAYED = 37;
//    private final static int UPDATE_LOADING_PERCENT = 38;
//    private final static int SEEK_BAR_DISPPEAR_TIME = 8000;// ms
//    private static final int MAX_VIDEO_FRAME_SIZE = 1024 * 1024;
//    private static final int MAX_YUV_FRAME_SIZE = 1920 * 1080 * 2;
//    private static final int MAX_AUDIO_FRAME_SIZE = 192000;
//    SurfaceView mMovieView;
//    HiCamPlayer player = null;
//    Context mContext;
//
//    boolean mbPaused = false;
//    boolean mbLiveStreamFlag = false;
//
//    ImageView imgBack;
//    ImageView imgMedia;
//    ImageView imgCameraSet;
//    FrameLayout cameraRootView;
//    ToggleButton btnStatus;
//    CircleProgress btnCircle;
//    CameraStatus cameraStatus;
//
//    ByteBuffer vidReadBuf;
//    ByteBuffer audReadBuf;
//    int recflag = 0;
//    int mAudflag = 0;
//    private PlayerRecordThread mRecordThread;
//
//
//    private void startVideoRecordThread() {
//        mRecordThread = new PlayerRecordThread();
//        if (mRecordThread != null) {
//            Log.d(TAG, "video record thread start");
//            mRecordThread.start();
//        }
//    }
//
//    private class PlayerRecordThread extends Thread {
//        private volatile boolean mbRecThreadExited;
//
//        @Override
//        public void run() {
//            FileChannel fout = null;
//            FileChannel audfout = null;
//            File saveRecPath = null;
//            File path = Environment.getExternalStorageDirectory();
//            saveRecPath = new File(path, "RECORD_DIR");
//            if (!saveRecPath.exists()) {
//                if (!saveRecPath.mkdir()) {
//                    Log.d(TAG, saveRecPath.getAbsolutePath() + " mkdir() failed");
//                }
//            }
//
//            File file = new File(saveRecPath, "video.h264");
//            File audfile = new File(saveRecPath, "audio.aac");
//            Log.d(TAG, saveRecPath.getAbsolutePath() + " save dir==================");
//            try {
//                fout = new FileOutputStream(file).getChannel();
//                audfout = new FileOutputStream(audfile).getChannel();
//                while (recflag == 1 && !Thread.interrupted()) {
//
//                    vidReadBuf.clear();
//
//                    try {
//                        RecFrameInfo frameInfo = player.getRecordVideo(vidReadBuf);
//
//                        if (null == frameInfo) {
//                            Log.d(TAG, "can't get frame  ");
//                            try {
//                                Thread.sleep(30);
//                            } catch (InterruptedException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                            continue;
//                        }
//                        if (frameInfo.frameSize < 0 || frameInfo.pts < 0) {
//                            Log.d(TAG, "error  frameInfo.frameSize < 0 || frameInfo.pts < 0  ");
//                            break;
//                        }
//                        vidReadBuf.flip();
//                        vidReadBuf.limit(frameInfo.frameSize);
//                        Log.d(TAG, "getRecordVideo size:" + frameInfo.frameSize + " pts: " + frameInfo.pts + " pt: " + frameInfo.payload);
//                        fout.write(vidReadBuf);
//                        vidReadBuf.clear();
//
//                        if (mAudflag == 1) {
//                            RecFrameInfo audframeInfo = player.getRecordAudio(audReadBuf);
//
//                            if (null == audframeInfo) {
//                                Log.d(TAG, "can't get frame  ");
//                                try {
//                                    Thread.sleep(30);
//                                } catch (InterruptedException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                                continue;
//                            }
//                            if (audframeInfo.frameSize < 0 || audframeInfo.pts < 0) {
//                                Log.d(TAG, "error audframeInfo.frameSize < 0  ||  audframeInfo.pts < 0   ");
//                                break;
//                            }
//                            audReadBuf.flip();
//                            audReadBuf.limit(audframeInfo.frameSize);
//                            Log.d(TAG, "getRecordAudio size:" + audframeInfo.frameSize + " pts: " + audframeInfo.pts + " pt: " + audframeInfo.payload);
//                            audfout.write(audReadBuf);
//                            audReadBuf.clear();
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e(TAG, "fail to write video data");
//                        break;
//                    }
//
//
//                }
//            } catch (FileNotFoundException e) {
//                Log.e(TAG, "fail to open record video file!");
//            } finally {
//                try {
//
//                    if (fout != null) {
//                        fout.close();
//                    }
//
//                } catch (IOException e) {
//                    Log.e(TAG, "fail to close record video file!");
//                }
//            }
//
//        }
//
//    }
//
//    ;
//
//    public void stopRecord() {
//        if (recflag == 1) {
//
//            recflag = 0;
//            if (null != mRecordThread) {
//                joinThread(mRecordThread);
//                mRecordThread = null;
//            }
//
//        }
//        Log.d(TAG, "stoprecord() end");
//    }
//
//    private void joinThread(Thread thread) {
//        // TODO Auto-generated method stub
//        if (null != thread && thread.isAlive()) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                Log.e(TAG, "fail to join");
//            }
//        }
//    }
//
//    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate()");
//        super.onCreate(savedInstanceState);
//        mContext = this;
//        Intent i = getIntent();
//        mfilePath = i.getStringExtra(getResources().getString(
//                R.string.input_file));
//        getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFormat(PixelFormat.RGB_565);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        Point winSize = new Point();
//        getWindowManager().getDefaultDisplay().getSize(winSize);
//        mWindowWidth = winSize.x;
//        mWindowHeight = winSize.y;
//        loadLibs();
//        setContentView(R.layout.hicamplayer_layout);
//
//        mMovieView = (SurfaceView) findViewById(R.id.playView);
//
//        mMovieView.getHolder().addCallback(this);
//
//        hideSeekBar();
//
//        imgBack = (ImageView) findViewById(R.id.imgBack);
//        imgMedia = (ImageView) findViewById(R.id.img_media);
//        imgCameraSet = (ImageView) findViewById(R.id.img_set);
//        btnStatus = (ToggleButton) findViewById(R.id.right_video_tb);
//        btnCircle = (CircleProgress) findViewById(R.id.video_camera_start);
//        cameraRootView = (FrameLayout) findViewById(R.id.cameraRoot);
//        cameraStatus = CameraStatus.ONSTART;
//        mMovieView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getSupportFragmentManager().popBackStack();
//            }
//        });
//        imgMedia.setOnClickListener(mediaListener);
//        imgCameraSet.setOnClickListener(cameraListener);
//        imgBack.setOnClickListener(backListener);
//
//        btnStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    switchTakephotoModel();
//                } else {
//                    switchRecordModel();
//
//                }
//            }
//        });
//        btnCircle.setOnClickListener(videoAnsCameraListener);
//
//        vidReadBuf = ByteBuffer.allocateDirect(MAX_VIDEO_FRAME_SIZE);
//        vidReadBuf.clear();
//        audReadBuf = ByteBuffer.allocateDirect(MAX_AUDIO_FRAME_SIZE);
//        audReadBuf.clear();
//        requestCameraStatus();
//
//    }
//
//    /**
//     * 切换拍照模式
//     */
//    private void switchTakephotoModel() {
//        Call<ResponseBody> call = RetrofitFactory.getInstance().requestSwitchPhotoMode();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    if(response.isSuccessful()){
//                        String json=response.body().string().trim();
//                        Log.i("qwert", "切换拍照模式:"+json);
//                        if (json.equals("Success") ) {
//                            Log.i("qwert", "onResponse: 111111111111111");
//                            cameraStatus = CameraStatus.CAMERA;
//                            btnCircle.setBackgroundResource(R.mipmap.camera_shot);
//                            call.cancel();
//                        }
//                    }else{
//                        Log.i("qwert", "onResponse: 切换拍照模式失败");
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }
//
//    /**
//     * 切换录像模式
//     */
//    private void switchRecordModel() {
//        Call<ResponseBody> call = RetrofitFactory.getInstance().requestSwitchRecordMode();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    if(response.isSuccessful()){
//                        String json=response.body().string().trim();
//                        if (json.equals("Success") ) {//录像模式
//                            Log.i("qwert", "录像模式: ");
//                            cameraStatus = CameraStatus.ONSTART;
//                            btnCircle.setBackgroundResource(R.mipmap.video_start);
//                            call.cancel();
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }
//
//    /**
//     * 正在录像中
//     */
//    private void recordingModel() {
//        cameraStatus = CameraStatus.END;
//        btnCircle.setBackgroundResource(R.mipmap.video_stop);
//    }
//
//    /**
//     * 请求相机状态
//     */
//    private void requestCameraStatus() {
//        Call<ResponseBody> call = RetrofitFactory.getInstance().requestCameraMode();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {//要加sd卡判断
//                    String json =response.body().string().trim();
//                    JSONObject jsonObject=new JSONObject(json);
//                    Log.i("qwert","相机状态="+json);
//                    if (jsonObject.getString("workmode").equals("20") || jsonObject.getString("workmode").equals("21") || jsonObject.getString("workmode").equals("23") || jsonObject.getString("workmode").equals("24")) {//录像模式
//                       // switchRecordModel();
//                        btnStatus.setChecked(false);
//                        Log.i("qwert", "相机状态: 录像模式" );
//                    } else if (jsonObject.getString("workmode").equals("00") || jsonObject.getString("workmode").equals("01") || jsonObject.getString("workmode").equals("02") || jsonObject.getString("workmode").equals("11")) {//拍照模式
//                       // switchTakephotoModel();
//                        btnStatus.setChecked(true);
//                        Log.i("qwert", "相机状态: 拍照模式" );
//                    }else if(jsonObject.getString("workmode").equals("recording")){
//                        recordingModel();
//                        btnStatus.setChecked(false);
//                        Log.i("qwert", "相机状态: 录像中" );
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                // call.cancel();
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }
//
//    protected void onPause() {
//        super.onPause();
//    }
//
//    protected void onResume() {
//        super.onResume();
//    }
//
//    protected void onDestroy() {
//        mPlayerHandler.removeMessages(SEEK_BAR_DISPPEAR);
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onStop() {
//        // TODO Auto-generated method stub
//        if (null != mRecordThread) {
//            recflag = 0;
//            stopRecord();
//        }
//
//        Log.d(TAG, "onStop");
//        super.onStop();
//    }
//
//    View.OnClickListener playClickListener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            if (player != null) {
//                if (!mbPaused)
//                    player.pause();
//                else
//                    player.start();
//            }
//        }
//    };
//
//    View.OnClickListener snapClickListener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            if (player != null) {
//                FileChannel fout = null;
//                File path = Environment.getExternalStorageDirectory();
//                File file = new File(path, "video.yuv");
//                try {
//                    fout = new FileOutputStream(file).getChannel();
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                ByteBuffer yuvbuffer = ByteBuffer.allocateDirect(MAX_YUV_FRAME_SIZE);
//                yuvbuffer.clear();
//                YuvFrameInfo yuvInfo = player.getSnapData(yuvbuffer);
//
//                if (null == yuvInfo) {
//                    Log.e(TAG, " player.getSnapData(yuvbuffer) get no data==================");
//                } else {
//                    Log.d(TAG, "getSnapData width:" + yuvInfo.width + " height: " + yuvInfo.height + " pts: " + yuvInfo.pts + " uoffset: " + yuvInfo.uoffset
//                            + " voffset: " + yuvInfo.voffset + " ypitch: " + yuvInfo.ypitch + " upitch: " + yuvInfo.upitch + " vpitch: " + yuvInfo.vpitch);
//                    yuvbuffer.flip();
//                    int limit = yuvInfo.height * yuvInfo.width * 3 / 2;
//                    yuvbuffer.limit(limit);
//                    try {
//                        fout.write(yuvbuffer);
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    yuvbuffer.clear();
//                }
//
//            }
//        }
//    };
//    View.OnClickListener videoAnsCameraListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (cameraStatus) {
//                case ONSTART:
//                    requestStartRecord();
//                    break;
//                case CAMERA:
//                    requestTakephoto();
//                    break;
//                case END:
//                    requestStopRecord();
//                    break;
//            }
//        }
//    };
//
//    /**
//     * 相机开始拍照
//     */
//    private void requestTakephoto() {
//        Call<ResponseBody> call = RetrofitFactory.getInstance().requestTakephoto();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    String json=response.body().string().trim();
//                    if(json.equals("Success")){
//                        btnCircle.startCartoom(0.5f);
//                        Toast.makeText(HiCamPlayerActivity.this,"拍照成功",Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(HiCamPlayerActivity.this,"拍照失败",Toast.LENGTH_SHORT).show();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }
//
//    /**
//     * 相机停止录像
//     */
//    private void requestStopRecord() {
//        Call<ResponseBody> call = RetrofitFactory.getInstance().requestStopRecord();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//
//                    String json=response.body().string().trim();
//                    if(json.equals("Success")){
//                        cameraStatus = CameraStatus.ONSTART;
//                        btnCircle.setBackgroundResource(R.mipmap.video_start);
//                        Toast.makeText(HiCamPlayerActivity.this,"结束录像",Toast.LENGTH_SHORT).show();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }
//     private Fragment[] fragment=new Fragment[4];
//    /**
//     * 相机开始录像
//     */
//    private void requestStartRecord() {
//        Call<ResponseBody> call = RetrofitFactory.getInstance().requestStartRecord();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    if (response.isSuccessful()) {
//                        String json=response.body().string().trim();
//                        if (json.equals("Success")) {
//                            cameraStatus = CameraStatus.END;
//                            btnCircle.setBackgroundResource(R.mipmap.video_stop);
//                            Toast.makeText(HiCamPlayerActivity.this,"开始录像",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }
//
//    View.OnClickListener mediaListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(HiCamPlayerActivity.this, MediaActivity.class);
//            startActivity(intent);
//        }
//    };
//    private boolean isOpenCameraStatus = true;
//    View.OnClickListener cameraListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (isOpenCameraStatus) {
//                CameraParamsFragment cameraParamsFragment = new CameraParamsFragment();
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out);
//                transaction.add(R.id.cameraRoot, cameraParamsFragment,"test");
//                transaction.addToBackStack(null);
//                transaction.commit();
//            } else {
//                getSupportFragmentManager().popBackStack();
//            }
//            isOpenCameraStatus = !isOpenCameraStatus;
//        }
//
//    };
//
//    public enum CameraStatus {
//        ONSTART, ONSTARTING, END, CAMERA
//    }
//
//    View.OnClickListener backListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            finish();
//        }
//    };
//
//
//    void hideSeekBar() {
//        mPlayerHandler.sendEmptyMessageDelayed(SEEK_BAR_DISPPEAR, SEEK_BAR_DISPPEAR_TIME);
//    }
//
//
//
//
//    ;
//
//    /**
//     * loads all native libraries
//     *
//     * @return true if all libraries was loaded, otherwise return false
//     */
//    private static boolean loadLibs() {
//        if (sLoaded) {
//            return true;
//        }
//        boolean err = false;
//        int version = android.os.Build.VERSION.SDK_INT;
//        //version = 13;
//        String model = String.valueOf(android.os.Build.MODEL);
//        Log.d(TAG, "phone_type:" + model);
//
//        System.loadLibrary("ffmpeg");
//
//        if (version < 14 || version == 18) {
//            Log.e(TAG, "load libhi_camplayer_ffmpeg.so");
//            System.loadLibrary("hi_camplayer_ffmpeg");
//        } else if (version < 19) {
//            Log.e(TAG, "load libhi_camplayer_ics.so");
//            System.loadLibrary("hi_camplayer_ics");
//        } else if (version < 21) {
//            Log.e(TAG, "load libhi_camplayer_kitkat.so");
//            System.loadLibrary("hi_camplayer_kitkat");
//        } else {
//            Log.e(TAG, "load libhi_camplayer_lollipop.so");
//            System.loadLibrary("hi_camplayer_lollipop");
//        }
//
//
//        if (!err) {
//            sLoaded = true;
//        }
//        return sLoaded;
//    }
//
//    public void updateSurfaceView() {
//        Log.e(TAG, "update_surface_view");
//        if (player != null) {
//            int width = player.getVideoWidth();
//            int height = player.getVideoHeight();
//            if (width != 0 && height != 0) {
//                Log.e(TAG, "player video width : " + width + "height:" + height);
//                mMovieView.getHolder().setFixedSize(width / 16 * 16, height);
//
//                LayoutParams lp = mMovieView.getLayoutParams();
//
//                lp.width = mWindowWidth;
//                lp.height = mWindowHeight;
//
//                mMovieView.setLayoutParams(lp);
//                mMovieView.invalidate();
//            }
//        }
//    }
//
//
//    HiCamPlayer.onSeekBufferingStateListener mSeekListener = new HiCamPlayer.onSeekBufferingStateListener() {
//
//        @Override
//        public void onSeekBufferingStart(HiCamPlayer mplayer) {
//            // TODO Auto-generated method stub
//            mPlayerHandler.sendEmptyMessage(SHOW_LOADINGVIEW);
//        }
//
//        @Override
//        public void onSeekBufferingLoadingPercent(HiCamPlayer mplayer, int percent) {
//            // TODO Auto-generated method stub
//            Message msg = new Message();
//            msg.arg1 = percent;
//            msg.what = UPDATE_LOADING_PERCENT;
//            mPlayerHandler.sendMessage(msg);
//        }
//
//        @Override
//        public void onSeekBufferingEnd(HiCamPlayer mplayer) {
//            // TODO Auto-generated method stub
//            Log.i(TAG, "onBufferingEnded");
//            mPlayerHandler.sendEmptyMessage(HIDE_LOADINGVIEW);
//        }
//    };
//
//
//    HiCamPlayer.HiCamPlayerStateListener mPlayerListener = new HiCamPlayer.HiCamPlayerStateListener() {
//
//        @Override
//        public void onStateChange(HiCamPlayer player, HiCamPlayerState state) {
//            // TODO Auto-generated method stub
//            Log.i(TAG, "onStateChange curstate: " + state);
//            if (state == HiCamPlayerState.HICAMPLAYER_STATE_PAUSE) {
//                mPlayerHandler.sendEmptyMessage(PLAYER_ONPAUSED);
//                mbPaused = true;
//            } else if (state == HiCamPlayerState.HICAMPLAYER_STATE_PLAY) {
//                mPlayerHandler.sendEmptyMessage(PLAYER_ONPLAYED);
//                mbPaused = false;
//            }
//        }
//
//        @Override
//        public void onFinish(HiCamPlayer player) {
//            // TODO Auto-generated method stub
//            Log.i(TAG, "onFinish");
//            mPlayerHandler.sendEmptyMessage(SEEK_BAR_SETVISIBLE);
//
//            //onStateChange(player,HiCamPlayerState.HICAMPLAYER_STATE_PAUSE);
//        }
//
//        @Override
//        public void onError(HiCamPlayer player, String msg, int extra) {
//            // TODO Auto-generated method stub
//            Message m = new Message();
//            m.what = PLAYER_PLAY_ERR;
//            Bundle data = new Bundle();
//            data.putCharSequence("MSG", msg);
//            m.setData(data);
//            mPlayerHandler.sendMessage(m);
//        }
//
//        @Override
//        public void onBufferingUpdate(HiCamPlayer player, int percent) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onASRChange(HiCamPlayer player) {
//            // TODO Auto-generated method stub
//            mPlayerHandler.sendEmptyMessage(PLAYER_ASR_CHANGE);
//        }
//    };
//
//    Handler mPlayerHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case PLAYER_ASR_CHANGE:
//                    Log.d(TAG, "changeASR ");
//                    updateSurfaceView();
//                    break;
//
//                case PLAYER_START_ERR:
//                    String tips = "create player unsuccessfully, please try again!";
////                    HiCamPlayerMessageBox.show(mContext, "", tips,
////                            new DialogInterface.OnClickListener() {
////
////                                @Override
////                                public void onClick(DialogInterface dialog,
////                                                    int which) {
////                                    // TODO Auto-generated method stub
////                                    finish();
////                                }
////                            });
//                    break;
//
//                case PLAYER_PLAY_ERR:
//                    if (null != mRecordThread) {
//                        stopRecord();
//                    }
//                    Bundle data = msg.getData();
//                    String Errstr = data.getCharSequence("MSG").toString();
////                    HiCamPlayerMessageBox.show(mContext, "", Errstr,
////                            new DialogInterface.OnClickListener() {
////
////                                @Override
////                                public void onClick(DialogInterface dialog,
////                                                    int which) {
////                                    // TODO Auto-generated method stub
////                                    finish();
////                                }
////                            });
//                    break;
//
//                case SEEK_BAR_REFRESH:
//                    refreshSeekBar();
//                    break;
//
//                case SEEK_BAR_DISPPEAR:
//                    Log.i(TAG, "set seekbar invisible");
//
//                    break;
//
//                case SEEK_BAR_SETVISIBLE:
//                    Log.i(TAG, "set seekbar visible");
//
//                    break;
//
//                case SHOW_LOADINGVIEW:
//                    showLoadingDialog(true);
//                    break;
//
//                case HIDE_LOADINGVIEW:
//                    showLoadingDialog(false);
//                    break;
//
//                case PLAYER_ONPAUSED:
//
//                    break;
//
//                case PLAYER_ONPLAYED:
//
//                    refreshSeekBar();
//                    break;
//
//                case UPDATE_LOADING_PERCENT:
//
//                    break;
//
//                default:
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//    private String seconds2String(int time) {
//        int hour, min, second;
//
//        hour = time / 3600;
//        time = time % 3600;
//        min = time / 60;
//        time = time % 60;
//        second = time;
//
//        return String.format("%02d:%02d:%02d", hour, min, second);
//    }
//
//    private void refreshSeekBar() {
//        Log.i(TAG, "refresh seekbar");
//        int curPostion = 0;
//        if (player != null)
//            curPostion = player.getCurrentPosition();
//
//        mPlayerHandler.sendEmptyMessageDelayed(SEEK_BAR_REFRESH, 500);
//    }
//
//    private void handlePlayerStartErr() {
//        mPlayerHandler.sendEmptyMessage(PLAYER_START_ERR);
//    }
//
//    Dialog mLoadingDialog = null;
//
//    private void showLoadingDialog(boolean show) {
//        if (mLoadingDialog == null) {
//            mLoadingDialog = new Dialog(this, R.style.ott_process_dialog);
//            mLoadingDialog.setContentView(mdialogView);
//
//            mLoadingDialog
//                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        public void onCancel(DialogInterface dialog) {
//                            finish();
//                        }
//                    });
//        }
//
//        if (show) {
//            if (!mLoadingDialog.isShowing()) {
//                mLoadingDialog.setCanceledOnTouchOutside(false);
//                mdialogText.setText(R.string.waiting);
//                mLoadingDialog.show();
//                Log.i(TAG, "loading dialog show");
//            }
//        } else {
//            if (mLoadingDialog.isShowing()) {
//                mLoadingDialog.dismiss();
//                Log.i(TAG, "loading dialog dismiss");
//            }
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        // TODO Auto-generated method stub
//        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
//            mLoadingDialog.dismiss();
//        }
//        super.onBackPressed();
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width,
//                               int height) {
//        // TODO Auto-generated method stub
//        Log.i(TAG, "surfaceChanged formate: " + format + " w: " + width
//                + " h: " + height);
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        // TODO Auto-generated method stub
//        Log.i(TAG, "surfaceCreated");
//        mMovieView.getHolder().setFormat(PixelFormat.RGB_565);
//
//        try {
//            player = new HiCamPlayer();
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//
//        try {
//            player.setDisplay(holder);
//            player.setHiCamPlayerListener(mPlayerListener);
//            player.setOnSeekBufferingStateListener(mSeekListener);
//            player.setDataSource(mfilePath);
//            player.prepare();
//            player.start();
//        } catch (IOException e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            handlePlayerStartErr();
//        } catch (IllegalStateException e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            handlePlayerStartErr();
//        } catch (IllegalArgumentException e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            handlePlayerStartErr();
//        }
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        // TODO Auto-generated method stub
//        Log.i(TAG, "surfaceDestroyed");
//        mPlayerHandler.removeMessages(SEEK_BAR_REFRESH);
//        player.reset();
//        player.release();
//        player = null;
//    }
//
//}
