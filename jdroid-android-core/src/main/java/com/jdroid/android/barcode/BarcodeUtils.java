package com.jdroid.android.barcode;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.jdroid.android.R;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.google.GooglePlayUtils;
import com.jdroid.java.utils.RandomUtils;

/**
 * <p>
 * A utility class which helps ease integration with Barcode Scanner via {@link Intent}s. This is a simple way to invoke
 * barcode scanning and receive the result, without any need to integrate, modify, or learn the project's source code.
 * </p>
 *
 * <p>
 * It does require that the Barcode Scanner application is installed. The {@link #initiateScan(FragmentActivity)} method will prompt the
 * user to download the application, if needed.
 * </p>
 */
public final class BarcodeUtils {

	private static final int REQUEST_CODE = RandomUtils.get16BitsInt();

	private static final String PACKAGE = "com.google.zxing.client.android";

	private static final String SCAN_RESULT = "SCAN_RESULT";
	private static final String SCAN_RESULT_FORMAT = "SCAN_RESULT_FORMAT";

	public static void initiateScan(@Nullable FragmentActivity activity) {
		Intent intentScan = new Intent(PACKAGE + ".SCAN");
		intentScan.setPackage(PACKAGE);
		intentScan.addCategory(Intent.CATEGORY_DEFAULT);

		try {
			ActivityLauncher.startActivityForResult(activity, intentScan, REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
			GooglePlayUtils.showDownloadDialog(activity, R.string.jdroid_barcodeScanner, PACKAGE);
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
	 * @return null if the event handled here was not related to this class, or else an {@link BarcodeIntentResult}
	 * 	containing the result of the scan. If the user cancelled scanning, the fields will be null.
	 */
	public static BarcodeIntentResult parseActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			String contents = intent.getStringExtra(SCAN_RESULT);
			String formatName = intent.getStringExtra(SCAN_RESULT_FORMAT);
			return new BarcodeIntentResult(contents, formatName);
		}
		return null;
	}
}
