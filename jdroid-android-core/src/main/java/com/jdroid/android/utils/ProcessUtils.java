package com.jdroid.android.utils;

import android.app.ActivityManager;
import android.content.Context;

public class ProcessUtils {
	
	private static Boolean isMainProcess;
	private static ActivityManager.RunningAppProcessInfo processInfo;
	
	public static boolean isMainProcess(Context context) {
		init(context);
		return isMainProcess;
	}
	
	public static ActivityManager.RunningAppProcessInfo getProcessInfo(Context context) {
		init(context);
		return processInfo;
	}
	
	private static void init(Context context) {
		if (isMainProcess == null) {
			processInfo = getProcessName(context);
			isMainProcess = context.getPackageName().equals(processInfo.processName);
		}
	}
	
	private static ActivityManager.RunningAppProcessInfo getProcessName(Context context) {
		// get the current process name, you will get
		// name like "com.package.name" (main process name) or "com.package.name:remote"
		int mypid = android.os.Process.myPid();
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for(ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
			if (info.pid == mypid) {
				return info;
			}
		}
		return null;
	}
}
