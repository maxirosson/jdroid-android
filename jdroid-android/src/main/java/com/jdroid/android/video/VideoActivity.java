package com.jdroid.android.video;

import java.io.File;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.widget.VideoView;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractActivity;

/**
 * This activity reproduces full-screen video. <br />
 * Extra parameters to determine the source:
 * <table border="1">
 * <tr>
 * <td>LOCAL</td>
 * <td>SD</td>
 * <td>URI</td>
 * </tr>
 * <tr>
 * <td>R.raw.some_video</td>
 * <td>video.mp4 (on video folder in SD card)</td>
 * <td>http://www.site.com/video/some_video.mp4</td>
 * </tr>
 * </table>
 * 
 */
@SuppressLint("Registered")
public class VideoActivity extends AbstractActivity {
	
	private VideoView video;
	
	public static final String LOCAL = "LOCAL";
	public static final String SD = "SD";
	public static final String URI = "URI";
	
	private static final String SD_VIDEOS_DIR = "video";
	private static final String ANDROID_RES = "android.resource://";
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return R.layout.video_activity;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		video = findView(R.id.video);
		showLoading();
		
		String path = null;
		if (getIntent().getExtras().containsKey(LOCAL)) {
			path = ANDROID_RES + getPackageName() + File.separator + getExtra(LOCAL);
		} else if (getIntent().getExtras().containsKey(SD)) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SD_VIDEOS_DIR
					+ File.separator + getExtra(SD);
		} else if (getIntent().getExtras().containsKey(URI)) {
			path = (String)getExtra(URI);
		}
		video.setVideoPath(path);
		
		video.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				dismissLoading();
				video.start();
				video.requestFocus();
			}
		});
		
		video.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				VideoActivity.this.finish();
			}
		});
	}
	
	/**
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		finish();
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractActivity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		finish();
	}
}
