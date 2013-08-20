package com.jdroid.android.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.StringUtils;

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
	@SuppressWarnings("deprecation")
	public static Long getAvailableInternalDataSize() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		long size = stat.getAvailableBlocks() * stat.getBlockSize();
		return size / FileUtils.BYTES_TO_MB;
	}
	
	/**
	 * @return The total storage in MegaBytes
	 */
	@SuppressWarnings("deprecation")
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
	
	public static Boolean isPreHoneycomb() {
		return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB;
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
	
	public static Boolean isSmallScreen() {
		int screenSize = AbstractApplication.get().getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return screenSize == Configuration.SCREENLAYOUT_SIZE_SMALL;
	}
	
	public static Boolean isNormalScreen() {
		int screenSize = AbstractApplication.get().getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL;
	}
	
	public static Boolean isLargeScreen() {
		int screenSize = AbstractApplication.get().getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	public static Boolean isXLargeScreen() {
		int screenSize = AbstractApplication.get().getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}
	
	public static Boolean isLargeScreenOrBigger() {
		int screenSize = AbstractApplication.get().getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return (screenSize != Configuration.SCREENLAYOUT_SIZE_SMALL)
				&& (screenSize != Configuration.SCREENLAYOUT_SIZE_NORMAL);
	}
	
	public static Boolean isXLargeScreenOrBigger() {
		int screenSize = AbstractApplication.get().getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		return (screenSize != Configuration.SCREENLAYOUT_SIZE_SMALL)
				&& (screenSize != Configuration.SCREENLAYOUT_SIZE_NORMAL)
				&& (screenSize != Configuration.SCREENLAYOUT_SIZE_LARGE);
	}
	
	public static String getScreenSize() {
		String screenSize = StringUtils.EMPTY;
		if (AndroidUtils.isSmallScreen()) {
			screenSize = "small";
		} else if (AndroidUtils.isNormalScreen()) {
			screenSize = "normal";
		} else if (AndroidUtils.isLargeScreen()) {
			screenSize = "large";
		} else if (AndroidUtils.isXLargeScreen()) {
			screenSize = "xlarge";
		}
		return screenSize;
	}
	
	public static Boolean isLdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_LOW;
	}
	
	public static Boolean isMdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM;
	}
	
	public static Boolean isHdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_HIGH;
	}
	
	public static Boolean isXhdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static Boolean isTVdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_TV;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static Boolean isXXhdpiDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi == DisplayMetrics.DENSITY_XXHIGH;
	}
	
	public static String getScreenDensity() {
		String density = StringUtils.EMPTY;
		if (AndroidUtils.isLdpiDensity()) {
			density = "ldpi";
		} else if (AndroidUtils.isMdpiDensity()) {
			density = "mdpi";
		} else if (AndroidUtils.isHdpiDensity()) {
			density = "hdpi";
		} else if (AndroidUtils.isXhdpiDensity()) {
			density = "xhdpi";
		} else if (AndroidUtils.isTVdpiDensity()) {
			density = "tvdpi";
		} else if (AndroidUtils.isXXhdpiDensity()) {
			density = "xxhdpi";
		}
		return density;
	}
	
	public static Boolean supportsContextualActionBar() {
		return !AndroidUtils.isPreHoneycomb() && !AndroidUtils.isGoogleTV();
	}
	
	public static void startSkypeCall(String username) {
		Intent skypeIntent = new Intent("android.intent.action.VIEW");
		skypeIntent.setData(Uri.parse("skype:" + username));
		AbstractApplication.get().startActivity(skypeIntent);
	}
}
