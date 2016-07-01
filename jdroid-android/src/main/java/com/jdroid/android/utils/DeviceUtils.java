package com.jdroid.android.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import com.facebook.device.yearclass.YearClass;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.intent.IntentUtils;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.StringUtils;

public class DeviceUtils {

	private DeviceUtils(){}

	private static final String DEVICE_YEAR_CLASS = "DeviceYearClass";

	private static Integer deviceYearClass = YearClass.CLASS_UNKNOWN;

	public static Integer getDeviceYearClass() {
		if (deviceYearClass != null) {
			deviceYearClass = SharedPreferencesHelper.get().loadPreferenceAsInteger(DEVICE_YEAR_CLASS, YearClass.CLASS_UNKNOWN);
			//Try again if device was previously unknown.
			if (deviceYearClass == YearClass.CLASS_UNKNOWN) {
				deviceYearClass = YearClass.get(AbstractApplication.get());
				SharedPreferencesHelper.get().savePreferenceAsync(DEVICE_YEAR_CLASS, deviceYearClass);
			}
		}
		return deviceYearClass;
	}

	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	public static String getDeviceManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	public static String getDeviceName() {
		String manufacturer = getDeviceManufacturer();
		String model = getDeviceModel();
		if ((model != null) && model.startsWith(manufacturer)) {
			return StringUtils.capitalize(model);
		} else if (manufacturer != null) {
			return StringUtils.capitalize(manufacturer) + " " + model;
		} else {
			return "Unknown";
		}
	}

	public static Boolean hasCamera() {
		return IntentUtils.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE);
	}

	public static String getDeviceType() {
		if (ScreenUtils.is10Inches()) {
			return "10\" tablet";
		} else if (ScreenUtils.isBetween7And10Inches()) {
			return "7\" tablet";
		} else {
			return "phone";
		}
	}

	public static Boolean isEmulator() {
		return "google_sdk".equals(Build.PRODUCT);
	}

	public static String getAndroidId() {
		return Settings.Secure.getString(AbstractApplication.get().getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	public static String getNetworkOperatorName() {
		TelephonyManager manager = (TelephonyManager)AbstractApplication.get().getSystemService(
				Context.TELEPHONY_SERVICE);
		return manager.getNetworkOperatorName();
	}

	public static String getSimOperatorName() {
		TelephonyManager manager = (TelephonyManager)AbstractApplication.get().getSystemService(
				Context.TELEPHONY_SERVICE);
		return manager.getSimOperatorName();
	}

	@RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
	public static String getMacAddress() {
		WifiManager wifiManager = (WifiManager)AbstractApplication.get().getSystemService(Context.WIFI_SERVICE);
		return wifiManager.getConnectionInfo().getMacAddress();
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
		long size = (long)stat.getAvailableBlocks() * (long)stat.getBlockSize();
		return size / FileUtils.BYTES_TO_MB;
	}

	/**
	 * @return The total storage in MegaBytes
	 */
	@SuppressWarnings("deprecation")
	public static Long getTotalInternalDataSize() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		long size = (long)stat.getBlockCount() * (long)stat.getBlockSize();
		return size / FileUtils.BYTES_TO_MB;
	}

	public static Boolean isMediaMounted() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
}
