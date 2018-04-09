package com.jdroid.android.utils;

import org.slf4j.Logger;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

public class AlarmUtils {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AlarmUtils.class);
	
	public static void scheduleRtcWakeUpAlarm(long triggerAtTime, PendingIntent operation) {
		getAlarmManager().set(AlarmManager.RTC_WAKEUP, triggerAtTime, operation);
		log("RTC_WAKEUP", triggerAtTime);
	}
	
	public static void scheduleRtcAlarm(long triggerAtTime, PendingIntent operation) {
		getAlarmManager().set(AlarmManager.RTC, triggerAtTime, operation);
		log("RTC", triggerAtTime);
	}
	
	public static void scheduleElapsedRealtimeWakeUpAlarm(long triggerAtTime, PendingIntent operation) {
		getAlarmManager().set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, operation);
		log("ELAPSED_REALTIME_WAKEUP", triggerAtTime);
	}
	
	public static void scheduleElapsedRealtimeAlarm(long triggerAtTime, PendingIntent operation) {
		getAlarmManager().set(AlarmManager.ELAPSED_REALTIME, triggerAtTime, operation);
		log("ELAPSED_REALTIME", triggerAtTime);
	}
	
	private static void log(String alarmType, long triggerAtTime) {
		LOGGER.debug("Created " + alarmType + " alarm for "
				+ DateUtils.formatDateTime(DateUtils.getDate(triggerAtTime)));
	}
	
	public static void cancelAlarm(PendingIntent operation) {
		getAlarmManager().cancel(operation);
	}
	
	private static AlarmManager getAlarmManager() {
		Context context = AbstractApplication.get();
		return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}
}
