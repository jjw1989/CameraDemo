package com.appunite.ffmpeg;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by AndrewZJ on 2015/7/24.
 */
public class MGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener,FFmpegDisplay {
    private Context context;
//    private MediaPlayer mediaPlayer;
    private  SurfaceTexture surfaceTexture;
    private  NormalVideo normalVideo;
      Surface surface;
    private FFmpegPlayer mMpegPlayer = null;
    public MGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.e("MGLSurfaceView","lbg onSurfaceCreated");
        if(normalVideo==null && mMpegPlayer!=null) {
            normalVideo = new NormalVideo(context,mMpegPlayer.getVrmode());
            surfaceTexture = new SurfaceTexture(normalVideo.getTextureID());
            surfaceTexture.setOnFrameAvailableListener(this);

            surface = new Surface(surfaceTexture);
            mMpegPlayer.renderSurface(surface,0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {

        Log.e("MGLSurfaceView","lbg onResume");

        super.onResume();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if(surface==null && normalVideo!=null && mMpegPlayer!=null) {
            Log.e("MGLSurfaceView","lbg onSurfaceChanged create surface");
            surfaceTexture = new SurfaceTexture(normalVideo.getTextureID());
            surfaceTexture.setOnFrameAvailableListener(this);
            surface = new Surface(surfaceTexture);
                switch (mMpegPlayer.playstatus) {
                    case 0:
                    break;
                    case 1: {
                        mMpegPlayer.playstatus = 0;
                    }
                    break;
                    case 2:
                        mMpegPlayer.playstatus = 0;
                    break;
                }
            mMpegPlayer.renderSurface(surface,0);
        }
    }

    public void addSurface(){
        surfaceTexture = new SurfaceTexture(normalVideo.getTextureID());
        surfaceTexture.setOnFrameAvailableListener(this);
        surface = new Surface(surfaceTexture);
        mMpegPlayer.renderSurface(surface,1);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return
        Log.e("MGLSurfaceView","lbg faceDestroyed");
        mMpegPlayer.setGlSurfaceView(null);
        mMpegPlayer.pause();
        mMpegPlayer.renderStop();

        surface=null;
        super.surfaceDestroyed(holder);
    }
    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this){
            surfaceTexture.updateTexImage();
        }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        normalVideo.setShader(mMpegPlayer.getVrmode());
        if(mMpegPlayer.getVrmode()==2){

            GLES20.glViewport(-100, getHeight()/6, getWidth()/2+100, getHeight()/3*2);
            normalVideo.DrawSelf();

            GLES20.glViewport(getWidth()/2+100, getHeight()/6, getWidth()/2+300, getHeight()/3*2);
            normalVideo.DrawSelf();
        }
        else{
            GLES20.glViewport(0, 0, getWidth(), getHeight());
            normalVideo.DrawSelf();
        }

    }

    @Override
    public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.requestRender();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setConfig(Context context) {

        this.setEGLContextClientVersion(2);
        this.setPreserveEGLContextOnPause(true);
        this.setRenderer(this);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void setMpegPlayer(FFmpegPlayer fFmpegPlayer) {
        if (mMpegPlayer == null){
            setConfig(context);
        }
        this.mMpegPlayer = fFmpegPlayer;
    }
}