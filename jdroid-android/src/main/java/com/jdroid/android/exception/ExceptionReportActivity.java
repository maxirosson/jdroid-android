package com.jdroid.android.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import org.slf4j.Logger;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.NotificationUtils;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class ExceptionReportActivity extends Activity {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(ExceptionReportActivity.class);
	
	private static final String EXCEPTION_EXTRA = "exceptionExtra";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_NoDisplay);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.exceptionReportDialogTitle, AndroidUtils.getApplicationName()));
		dialog.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
		dialog.setMessage(getString(R.string.exceptionReportDialogText, AndroidUtils.getApplicationName()));
		dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				throw (RuntimeException)(getIntent().getSerializableExtra(EXCEPTION_EXTRA));
			}
		});
		dialog.show();
	}
	
	/**
	 * Sends an error report.
	 * 
	 * @param thread The thread where the exception occurred (e.g. {@link java.lang.Thread#currentThread()})
	 * @param ex The exception
	 */
	public static void reportException(Thread thread, Throwable ex) {
		
		try {
			Context context = AbstractApplication.get();
			
			Writer writer = new StringWriter();
			ex.printStackTrace(new PrintWriter(writer));
			
			Bundle bundle = new Bundle();
			bundle.putSerializable(EXCEPTION_EXTRA, ex);
			
			String notificationTitle = context.getString(R.string.exceptionReportNotificationTitle,
				AndroidUtils.getApplicationName());
			String notificationText = context.getString(R.string.exceptionReportNotificationText);
			
			NotificationUtils.sendNotification(IdGenerator.getRandomIntId(), android.R.drawable.stat_notify_error,
				notificationTitle, notificationTitle, notificationText, ExceptionReportActivity.class,
				DateUtils.now().getTime(), bundle);
		} catch (Exception e) {
			LOGGER.error("Unexepected error from the exception reporter", e);
		}
	}
}
