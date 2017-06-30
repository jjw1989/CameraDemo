/*
 * FFmpegPlayer.java
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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFmpegPlayer {

//	private GPUImageFilterTools mFilterTools;
//	GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
	public int playstatus=-1;//-1 init 0:play 1:pause ,2:stop
	/**
	 * -1是初始状态，0是连接状态，1是重连状态
	 * */
	public int connstatus=0;
	private Handler m_Timer = new Handler();

	public int getRecordStatus() {
		return record_status;
	}

	private String playurl;
	private static FFmpegPlayer m_intance=null;

	boolean listenThread=false;
	/**
	 * @param recordStatus
	 * 0是初始化，2是录制，3是播放停止
     */
	public void setRecordStatus(int recordStatus) {
		this.record_status = recordStatus;
		if (getMpegListener() != null) {
            getMpegListener().onFFRecordStatus(record_status);
		}
	}
	public Context context;
	private int record_status;//0是初始化，2是录制，3是播放停止

	private Handler handler;
	public int getVrmode() {
		return vrmode;
	}
	private int vrmode=1;//1是普通模式，2是vr模式
	public void setVrmode(int vrmode) {
		this.vrmode = vrmode;
	}


	private FFmpegPlayer(Context context) {

		setIsharddecode(false);//ifHardecode());
		this.context=context;
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case 0:
//						setInterrupt(1);

						setDataSource(playurl, null, UNKNOWN_STREAM, UNKNOWN_STREAM, NO_STREAM);
						break;
					default:
						break;
				}
			}
		};
		int error = initNative();
		Pattern p = Pattern.compile("[^0-9]");
		Matcher m = p.matcher(Build.HARDWARE);
		System.out.println( m.replaceAll("").trim());
		setGputype(2);
//		if(changshang.toLowerCase().contains("huawei")){
//			if(hardware_mode<3640){
//				setIsharddecode(true);//ifHardecode());
//				setGputype(1);
//			}else{
//				setGputype(2);
//			}
//
//		}else if(changshang.toLowerCase().contains("meizu")){
//
//			if(hardware_mode<6600) {
////				setIsharddecode(true);//ifHardecode());
//			}
//			setGputype(2);
//		}else if(changshang.toLowerCase().contains("gionee")){
//			setGputype(2);
//		}else if(changshang.toLowerCase().contains("samsung")){
//			setGputype(0);
//		}
//		else{
//			setGputype(2);
//		}

		if (error != 0) {
			Log.e("FFmpegPlayer","initNative failed");
			throw new RuntimeException(String.format("Could not initialize player: %d", error));
		}
	}
	synchronized public static FFmpegPlayer getInstance(Context context){
		if(m_intance==null){
			m_intance=new FFmpegPlayer(context);
		}
		return m_intance;
	}
	private Runnable runconn = new Runnable() {
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
		public void run() {
			lasttime = System.currentTimeMillis();
			HashMap<String, String> params = new HashMap<String, String>();
			// set font for ass
//			File assFont = new File(Environment.getExternalStorageDirectory(),
//					"DroidSansFallback.ttf");
//			params.put("ass_default_font_path", assFont.getAbsolutePath());

//		params.put("-r", "20");//-vcodec libx264
//			String url="udp://0.0.0.0:60430?pkt_size=65536";
			if(playstatus==1|| playstatus==0){
			//播放或者暂停时可以重连
				setDataSource(playurl, params, FFmpegPlayer.UNKNOWN_STREAM, -1,	-1);
			}
		}
	};



	private static class StopTask extends AsyncTask<Void, Void, Void> {

		private final FFmpegPlayer player;

		public StopTask(FFmpegPlayer player) {
			this.player = player;
		}

		@Override
		protected Void doInBackground(Void... params) {
			player.stopNative();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (player.mpegListener != null)
				player.mpegListener.onFFStop();
			player.playstatus = 2;
		}

	}

	private  class SetDataSourceTaskResult {
		FFmpegError error;
		FFmpegStreamInfo[] streams;
	}

	private  class SetDataSourceTask extends
			AsyncTask<Object, Void, SetDataSourceTaskResult> {

		private final FFmpegPlayer player;

		public SetDataSourceTask(FFmpegPlayer player) {
			this.player = player;
		}

		@Override
		protected SetDataSourceTaskResult doInBackground(Object... params) {
			String url = (String) params[0];
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) params[1];
			Integer videoStream = (Integer) params[2];
			Integer audioStream = (Integer) params[3];
			Integer subtitleStream = (Integer) params[4];

			int videoStreamNo = videoStream == null ? -1 : videoStream.intValue();
			int audioStreamNo = audioStream == null ? -1 : audioStream.intValue();
			int subtitleStreamNo = subtitleStream == null ? -1 : subtitleStream.intValue();
			//真正连接服务器代码在这
			int err = player.setDataSourceNative(url, map, videoStreamNo, audioStreamNo, subtitleStreamNo);
			SetDataSourceTaskResult result = new SetDataSourceTaskResult();
			if (err < 0) {
				result.error = new FFmpegError(err);
				result.streams = null;
				if(playstatus==1||playstatus==0)
				m_Timer.postDelayed(runconn, 400);
			} else {
				if(!listenThread) {
					Log.e("FFmpegPlayer", "java listenThread start");
					Thread t = new Thread(new Runnable() {
						public void run() {
							while (playstatus != 2) {
								if (lasttime > 0) {
									if ((System.currentTimeMillis() - lasttime) > 4000f) {
										Log.e("FFmpegPlayer", " timeout");
//										setInterrupt(1);
										if (connstatus == 0) {
											handler.sendEmptyMessage(0);
										}
										if (record_status == 2) {
											recordStop();//stop停止录制
											record_status = 0;
											if (player.mpegListener != null) {
												player.mpegListener.onFFRecordStatus(0);
											}
										}
										if (mpegListener != null) {
											connstatus = 1;
											player.mpegListener.onFFConnStatus(connstatus);
										}
										lasttime = System.currentTimeMillis();
									}
								}
								try {
								Thread.sleep(300);
								} catch (InterruptedException e) {
									Log.getStackTraceString(e);
									e.printStackTrace();
								}
							}
						}
					});
					t.start();
					listenThread=true;
				}
				result.error = null;
				result.streams = player.getStreamsInfo();
			}
			return result;
		}

		@Override
		protected void onPostExecute(SetDataSourceTaskResult result) {
			if (player.mpegListener != null)
				player.mpegListener.onFFDataSourceLoaded(result.error,
						result.streams);
		}

	}

	private static class SeekTask extends
			AsyncTask<Long, Void, NotPlayingException> {

		private final FFmpegPlayer player;

		public SeekTask(FFmpegPlayer player) {
			this.player = player;
		}

		@Override
		protected NotPlayingException doInBackground(Long... params) {
			try {
				player.seekNative(params[0].longValue());
			} catch (NotPlayingException e) {
				return e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(NotPlayingException result) {
			if (player.mpegListener != null)
				player.mpegListener.onFFSeeked(result);
		}

	}

	private static class PauseTask extends
			AsyncTask<Void, Void, NotPlayingException> {

		private final FFmpegPlayer mMpegPlayer;

		public PauseTask(FFmpegPlayer player) {
			this.mMpegPlayer = player;
		}

		@Override
		protected NotPlayingException doInBackground(Void... params) {
			try {
				mMpegPlayer.pauseNative();
				return null;
			} catch (NotPlayingException e) {
				return e;
			}
		}

		@Override
		protected void onPostExecute(NotPlayingException result) {
			if (mMpegPlayer.mpegListener != null)
				mMpegPlayer.mpegListener.onFFPause(result);
			mMpegPlayer.playstatus = 1;
		}

	}

	private static class ResumeTask extends
			AsyncTask<Void, Void, NotPlayingException> {

		private final FFmpegPlayer player;

		public ResumeTask(FFmpegPlayer player) {
			this.player = player;
		}

		@Override
		protected NotPlayingException doInBackground(Void... params) {
			try {
				player.resumeNative();
				return null;
			} catch (NotPlayingException e) {
				return e;
			}
		}

		@Override
		protected void onPostExecute(NotPlayingException result) {
			if (player.mpegListener != null)
				player.mpegListener.onFFResume(result);
            player.playstatus = 0;
		}

	}

	static {
		NativeTester nativeTester = new NativeTester();
		if (nativeTester.isNeon()) {
			System.loadLibrary("ffmpeg-neon");
			System.loadLibrary("ffmpeg-jni-neon");
		} else {
			System.loadLibrary("ffmpeg");
			System.loadLibrary("ffmpeg-jni");
		}
	}


	public static final int UNKNOWN_STREAM = -1;
	public static final int NO_STREAM = -2;
	private FFmpegListener mpegListener = null;
	private final RenderedFrame mRenderedFrame = new RenderedFrame();

	private int mNativePlayer;
	private long mCurrentTimeUs;
	private long mVideoDurationUs;
	private FFmpegStreamInfo[] mStreamsInfos = null;

	static class RenderedFrame {
		public Bitmap bitmap;
		public int height;
		public int width;
	}


	private boolean ifHardecode() {
		//读取系统配置文件/system/etc/media_codecc.xml
		File file = new File("/system/etc/media_codecs.xml");
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (in == null) {
			Log.i("xp", "in == null");
		} else {
			Log.i("xp", "in != null");
		}


		boolean isHardcode = false;
		XmlPullParserFactory pullFactory;
		try {
			pullFactory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = pullFactory.newPullParser();
			xmlPullParser.setInput(in, "UTF-8");
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = xmlPullParser.getName();
				switch (eventType) {
					case XmlPullParser.START_TAG:
						if ("MediaCodec".equals(tagName)) {
							String componentName = xmlPullParser.getAttributeValue(0);
							Log.i("xp", componentName);
							if (componentName.startsWith("OMX.")) {
								if (!componentName.startsWith("OMX.google.")) {
									isHardcode = true;
								}
							}
						}
				}
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isHardcode;
	}
	@Override
	protected void finalize() throws Throwable {
		deallocNative();
		super.finalize();
	}
	private native int setHardecode(int value);
	private native int setInterrupt(int value);
	public native int setGputype(int value);//0是分割帧 1是不要P帧 2是不分割


	/**
	* isdebug 1是打印数据包日志，0是不打印日志
	* framecount是每隔framecount发一个连接通知
	*/
	public native int setDebugLevel(int isdebug,int framecount);
	private native int initNative();

	private native void deallocNative();

	private native int setDataSourceNative(String url,
										   Map<String, String> dictionary, int videoStreamNo,
										   int audioStreamNo, int subtitleStreamNo);

	private native void stopNative();

	boolean renderingStop=false;

	public  void renderStop(){
		if(!renderingStop) {
			renderFrameStop();
			renderingStop=true;
		}
	}

	private native void renderFrameStop();

	public native int recordStart();

	public native int recordStop();
	native int screenshotNative();

	int number=0;
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public  void screenshot(){

		if(isharddecode()){
			final Bitmap content = surfaceView.getBitmap();
			Thread t = new Thread(new Runnable(){
				public void run(){
					screenShot(content,number++);
				}
			});
			t.start();
		}else{
			screenshotNative();
		}

	}

	public void screenShot(Bitmap bitmap, int number) {
		FileOutputStream out = null;
		try {
			//判断sd卡是否存在
			boolean sdCardExist = Environment.getExternalStorageState()
					.equals(Environment.MEDIA_MOUNTED);
			if(sdCardExist){
				//获取sdcard的根目录
				String sdPath = Environment.getExternalStorageDirectory().getPath();

				//创建图片文件
				File file = new File(sdPath + File.separator+File.separator+ "screen"+number + ".png");
				if(!file.exists()){
					file.createNewFile();
				}

//        image.setImageBitmap(bitmap);
				out = new FileOutputStream(file);
				if (out != null) {
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
					out.flush();
					out.close();
				}
			}


		} catch (Exception e) {
			Log.e("FFmpegPlayer", "cause for "+e.getMessage());
		}
	}
	private native void seekNative(long positionUs) throws NotPlayingException;

	private native long getVideoDurationNative();

	public void renderSurface(Surface surface,int changedecode){
		renderingStop=false;
		playstatus=0;
		render(surface,changedecode);
	}
	private native void render(Surface surface,int changedecode);

	//param streamsInfos could be null

	private void setStreamsInfo(FFmpegStreamInfo[] streamsInfos) {
		this.mStreamsInfos = streamsInfos;
	}

	//return return streams info after successful setDataSource or null
	protected FFmpegStreamInfo[] getStreamsInfo() {
		return mStreamsInfos;
	}

	public void stop() {
		playstatus=2;
		listenThread=false;
		new StopTask(this).execute();
	}

	private native void pauseNative() throws NotPlayingException;

	private native void resumeNative() throws NotPlayingException;

	public void pause() {
		new PauseTask(this).execute();
	}

	public void seek(long positionUs) {
		new SeekTask(this).execute(Long.valueOf(positionUs));
	}

	public void resume() {
		new ResumeTask(this).execute();
	}

	//jni会调用这个：
	private void onUpdateTime(long currentUs, long maxUs, int isFinished) {

		lasttime = System.currentTimeMillis();

		if (getMpegListener() != null) {
			connstatus=0;
            getMpegListener().onFFConnStatus(connstatus);
		}
		this.mCurrentTimeUs = currentUs;
		this.mVideoDurationUs = maxUs;
		if(isFinished==1){
			Log.e("onUpdateTime","isFinished");
			if(null!=getMpegListener())
			getMpegListener().onFFRenderStatus(0);
			m_Timer.postDelayed(runconn, 1000);
		}else if(isFinished==2){
			connstatus=0;
			if(null!=getMpegListener())
			getMpegListener().onFFRenderStatus(1);
		}
	}

	private AudioTrack prepareAudioTrack(int sampleRateInHz,
										 int numberOfChannels) {

		for (;;) {
			int channelConfig;
			if (numberOfChannels == 1) {
				channelConfig = AudioFormat.CHANNEL_OUT_MONO;
			} else if (numberOfChannels == 2) {
				channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
			} else if (numberOfChannels == 3) {
				channelConfig = AudioFormat.CHANNEL_OUT_FRONT_CENTER
						| AudioFormat.CHANNEL_OUT_FRONT_RIGHT
						| AudioFormat.CHANNEL_OUT_FRONT_LEFT;
			} else if (numberOfChannels == 4) {
				channelConfig = AudioFormat.CHANNEL_OUT_QUAD;
			} else if (numberOfChannels == 5) {
				channelConfig = AudioFormat.CHANNEL_OUT_QUAD
						| AudioFormat.CHANNEL_OUT_LOW_FREQUENCY;
			} else if (numberOfChannels == 6) {
				channelConfig = AudioFormat.CHANNEL_OUT_5POINT1;
			} else if (numberOfChannels == 8) {
				channelConfig = AudioFormat.CHANNEL_OUT_7POINT1;
			} else {
				channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
			}
			try {
				int minBufferSize = AudioTrack.getMinBufferSize(sampleRateInHz,
						channelConfig, AudioFormat.ENCODING_PCM_16BIT);
				AudioTrack audioTrack = new AudioTrack(
						AudioManager.STREAM_MUSIC, sampleRateInHz,
						channelConfig, AudioFormat.ENCODING_PCM_16BIT,
						minBufferSize, AudioTrack.MODE_STREAM);
				return audioTrack;
			} catch (IllegalArgumentException e) {
				if (numberOfChannels > 2) {
					numberOfChannels = 2;
				} else if (numberOfChannels > 1) {
					numberOfChannels = 1;
				} else {
					throw e;
				}
			}
		}
	}

	public void connServer(String url) {

		this.playurl=url;
		if(playstatus==2||playstatus==-1) {
			//初始化或者关闭后可以连接
			//setDataSource("udp://0.0.0.0:60430?pkt_size=65536", null, UNKNOWN_STREAM, UNKNOWN_STREAM, NO_STREAM);
			setDataSource(url, null, UNKNOWN_STREAM, UNKNOWN_STREAM, NO_STREAM);
			playstatus=1;
		}
	}

	public void setDataSource(String url, Map<String, String> dictionary,
							  int videoStream, int audioStream, int subtitlesStream) {
//		if(url.indexOf("?")<0)
//			url+="?pkt_size=60000";
		new SetDataSourceTask(this).execute(url, dictionary,
				Integer.valueOf(videoStream), Integer.valueOf(audioStream),
				Integer.valueOf(subtitlesStream));
	}

	public FFmpegListener getMpegListener() {
		return mpegListener;
	}

	public void setMpegListener(FFmpegListener mpegListener) {
		this.mpegListener = mpegListener;
	}
	private Bitmap prepareFrame(int width, int height) {
		// Bitmap bitmap =
		// Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		this.mRenderedFrame.height = height;
		this.mRenderedFrame.width = width;
		return bitmap;
	}

	private volatile long    lasttime;
	public boolean isharddecode() {
		return isharddecode;
	}

	public void setIsharddecode(boolean isharddecode) {

		if(isharddecode!=this.isharddecode) {
			this.isharddecode = isharddecode;
			if(null!=mGLSurfaceView){
				if (isharddecode) {
					mGLSurfaceView.addSurface();
				} else {
					if (mGLSurfaceView.surface != null)
						renderSurface(mGLSurfaceView.surface, 2);
				}
			}
			setHardecode(isharddecode ? 1 : 0);
		}
	}

	private boolean isharddecode;

	public FFmpegTextureView getSurfaceView() {
		return surfaceView;
	}

	public void setSurfaceView(FFmpegTextureView surfaceView) {
		this.surfaceView = surfaceView;
		surfaceView.setMpegPlayer(this);
	}

	private MGLSurfaceView mGLSurfaceView=null;

	public void setGlSurfaceView(MGLSurfaceView surfaceView) {
		mGLSurfaceView=surfaceView;

		if(surfaceView==null){
			return;
		}
//		mGPUImage = new GPUImage(surfaceView,context,this);
//		mFilterTools=new GPUImageFilterTools();
//
//		mFilter=mFilterTools.createFilterForType(GPUImageFilterTools.FilterType.FILTER_GROUP1);
//		mGPUImage.setFilter(mFilter);
//		mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);

//		mGPUImage.setGLSurfaceView(surfaceView);
		surfaceView.setMpegPlayer(this);
	}
	public void setMyGlSurfaceView(MyGLSurfaceView surfaceView) {
		surfaceView.setMpegPlayer(this);
	}


	private FFmpegTextureView surfaceView;
}
