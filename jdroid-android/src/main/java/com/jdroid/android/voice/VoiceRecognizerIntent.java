package com.jdroid.android.voice;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.java.utils.RandomUtils;

import java.util.List;

/**
 * <p>
 * An utility class which helps ease integration with Voice Search via {@link Intent}s. This is a simple way to invoke
 * voice recognition and receive the result, without any need to integrate, modify, or learn the project's source code.
 * </p>
 * 
 * <p>
 * It does require that the Google Voice Search application is installed. The {@link #initiateRecord()} method will
 * prompt the user to download the application, if needed.
 * </p>
 */
public class VoiceRecognizerIntent {

	private VoiceRecognizerIntent(){}
	
	private static final int REQUEST_CODE = RandomUtils.get16BitsInt();
	private static final String PACKAGE = "com.google.android.voicesearch";
	
	public static void initiateRecord() {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		
		try {
			activity.startActivityForResult(intent, REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
			GooglePlayUtils.showDownloadDialog(R.string.voiceSearch, PACKAGE);
		}
	}
	
	/**
	 * <p>
	 * Call this from your {@link Activity}'s {@link Activity#onActivityResult(int, int, Intent)} method.
	 * </p>
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param intent
	 * 
	 * @return null if the event handled here was not related to this class, or else a {@link String} containing the
	 *         result of the voice record. If the user canceled recording, the fields will be null.
	 */
	public static String parseActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				
				String result = null;
				List<String> matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				if (!matches.isEmpty()) {
					result = matches.get(0);
				}
				return result;
			}
		}
		return null;
	}
	
}
