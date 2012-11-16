package com.jdroid.android.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.utils.FileUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class AndroidUtils {
	
	public static Boolean isEmulator() {
		return "google_sdk".equals(Build.PRODUCT);
	}
	
	public static String getAndroidId() {
		return Secure.getString(AbstractApplication.get().getContentResolver(), Secure.ANDROID_ID);
	}
	
	/**
	 * @return The version name of the application
	 */
	public static String getVersionName() {
		return getPackageInfo().versionName;
	}
	
	/**
	 * @return The version code of the application
	 */
	public static Integer getVersionCode() {
		return getPackageInfo().versionCode;
	}
	
	/**
	 * @return The package name of the application
	 */
	public static String getPackageName() {
		return getPackageInfo().packageName;
	}
	
	/**
	 * @return The name of the application
	 */
	public static String getApplicationName() {
		Context context = AbstractApplication.get();
		ApplicationInfo applicationInfo = AndroidUtils.getApplicationInfo();
		return context.getPackageManager().getApplicationLabel(applicationInfo).toString();
	}
	
	public static PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			Context context = AbstractApplication.get();
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// Do Nothing
		}
		return info;
	}
	
	public static ApplicationInfo getApplicationInfo() {
		ApplicationInfo info = null;
		try {
			Context context = AbstractApplication.get();
			info = context.getPackageManager().getApplicationInfo(context.getPackageName(),
				PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// Do Nothing
		}
		return info;
	}
	
	public static void hideSoftInput(View view) {
		((InputMethodManager)AbstractApplication.get().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
			view.getWindowToken(), 0);
	}
	
	/**
	 * Gets the {@link WindowManager} from the context.
	 * 
	 * @return {@link WindowManager} The window manager.
	 */
	public static WindowManager getWindowManager() {
		return (WindowManager)AbstractApplication.get().getSystemService(Context.WINDOW_SERVICE);
	}
	
	/**
	 * @return The HEAP size in MegaBytes
	 */
	public static Integer getHeapSize() {
		return ((ActivityManager)AbstractApplication.get().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
	}
	
	/**
	 * @return The available storage in MegaBytes
	 */
	public static Long getAvailableInternalDataSize() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		long size = stat.getAvailableBlocks() * stat.getBlockSize();
		return size / FileUtils.BYTES_TO_MB;
	}
	
	/**
	 * @return The total storage in MegaBytes
	 */
	public static Long getTotalInternalDataSize() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		long size = stat.getBlockCount() * stat.getBlockSize();
		return size / FileUtils.BYTES_TO_MB;
	}
	
	public static Boolean isMediaMounted() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}
	
	public static Integer getApiLevel() {
		return android.os.Build.VERSION.SDK_INT;
	}
	
	public static String getPlatformVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static Boolean isGoogleTV() {
		return AbstractApplication.get().getPackageManager().hasSystemFeature("com.google.android.tv");
	}
	
	public static Boolean hasCamera() {
		return IntentUtils.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE);
	}
	
	public static Boolean hasGallery() {
		return !isGoogleTV();
	}
	
	public static Boolean isLargeScreenOrBigger() {
		int screenSize = AbstractApplication.get().getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return (screenSize != Configuration.SCREENLAYOUT_SIZE_SMALL)
				&& (screenSize != Configuration.SCREENLAYOUT_SIZE_NORMAL);
	}
	
	public static Boolean supportsContextualActionBar() {
		return (AndroidUtils.getApiLevel() >= 11) && !AndroidUtils.isGoogleTV();
	}
}
