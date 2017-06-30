/*
 * FFmpegSurfaceView.java
 * Copyright (c) 2012 Jacek Marchwicki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.appunite.ffmpeg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
//import android.view.SurfaceView;

@SuppressLint("NewApi")
public class FFmpegTextureView extends TextureView implements FFmpegDisplay,
		SurfaceTextureListener {

	public static enum ScaleType {
		CENTER_CROP, CENTER_INSIDE, FIT_XY
	}

	Surface surface;
	private FFmpegPlayer mMpegPlayer = null;
	private boolean mCreated = false;

	public FFmpegTextureView(Context context) {
		this(context, null, 0);
	}

	public FFmpegTextureView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public FFmpegTextureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setSurfaceTextureListener(this);//设置监听函数  重写4个方法
//
//		SurfaceHolder holder = getHolder();
//		holder.setFormat(PixelFormat.RGBA_8888);
//		holder.addCallback(this);
	}

	@Override
	public void setMpegPlayer(FFmpegPlayer fFmpegPlayer) {
		if (mMpegPlayer != null)
			throw new RuntimeException(
					"setMpegPlayer could not be called twice");

		this.mMpegPlayer = fFmpegPlayer;
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width,int height) {
		System.out.println("onSurfaceTextureAvailable onSurfaceTextureAvailable");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			surface=new Surface(surfaceTexture);
		}
		mMpegPlayer.renderSurface(surface,0);
		mCreated = true;

	}
	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,int height) {
		System.out.println("onSurfaceTextureSizeChanged onSurfaceTextureSizeChanged");
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//		this.mMpegPlayer.renderFrameStop();
		this.mMpegPlayer.pause();
		this.mMpegPlayer.renderStop();
		FFmpegListener fFmpegListener=this.mMpegPlayer.getMpegListener();
		if(fFmpegListener!=null){
			fFmpegListener.onFFPause(null);
		}
		mCreated = false;
		return true;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture mTextureView) {
		int width,height;
//		mMpegPlayer.getMpegListener().onFrameUpdate(0);
//		Bitmap bmp= getBitmap();
//		height=bmp.getHeight();
//		width=bmp.getWidth();
//		int []surface_pixels = new int[height * width];
////		if (surface_pixels == null){
////			surface_pixels = new int[height * width];
////		}
//
//		bmp.getPixels(surface_pixels, 0, width, 0, 0, width, height);
//		for(int i=0;i<height * width;i++){
//			surface_pixels[i]=0;
//		}
//		bmp.recycle();
	}


//	@Override
//	public void surfaceCreated(SurfaceHolder holder) {
//		if (mCreated  == true) {
//			surfaceDestroyed(holder);
//		}
//
//		Surface surface = holder.getSurface();
//		mMpegPlayer.render(surface);
//		mCreated = true;
//	}

//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		this.mMpegPlayer.renderFrameStop();
//		FFmpegListener fFmpegListener=this.mMpegPlayer.getMpegListener();
//		if(fFmpegListener!=null){
//			fFmpegListener.onFFStop();
//		}
//		mCreated = false;
//	}

}
