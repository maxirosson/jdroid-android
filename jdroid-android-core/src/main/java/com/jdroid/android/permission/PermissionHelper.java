package com.jdroid.android.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.dialog.AppInfoDialogFragment;

/**
 * Helper class for permissions.
 *
 * When using an PermissionHelper instance, it is required to set the listener to receive callbacks using the method
 * {@link #setOnRequestPermissionsResultListener(OnRequestPermissionsResultListener)}, and to call the methods
 * {@link #onResume()} and {@link #onRequestPermissionsResult(int, String[], int[])} from the respective activity/fragment methods.
 *
 * If you prefer to use the static methods to chek permissions like {@link #checkPermission(Fragment, int, int, String, int)},
 * Your activity/Fragment has to implement the "OnRequestPermissionsResultCallback" method (
 * {@link android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])} or
 * {@link android.support.v4.app.Fragment#onRequestPermissionsResult(int, String[], int[])})
 *
 */
public class PermissionHelper {

	public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
	public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
	public static final String ACCOUNTS_PERMISSION = Manifest.permission.GET_ACCOUNTS;
	public static final String WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;

	public static final int LOCATION_PERMISSION_REQUEST_CODE = 91;
	public static final int CAMERA_PERMISSION_REQUEST_CODE = 92;
	public static final int ACCOUNTS_PERMISSION_REQUEST_CODE = 93;
	public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 94;

	private PermissionDelegate permissionDelegate;
	private int permissionRequestCode;
	private String permission;
	private int permissionRationaleMessageResId;
	private int appInfoDialogMessageResId;
	private int permissionRationaleTitleResId;
	private int appInfoDialogTitleResId;
	private OnRequestPermissionsResultListener onRequestPermissionsResultListener;
	private boolean showAppInfoDialogEnabled = true;
	private boolean requestPermissionOnStart = false;
	private boolean pendingAppInfoDialog;
	private Boolean previouslyShouldShowRequestPermissionRationale;
	private boolean firstTime;

	public static PermissionHelper createLocationPermissionHelper(Fragment fragment) {
		return new PermissionHelper(fragment, LOCATION_PERMISSION, LOCATION_PERMISSION_REQUEST_CODE);
	}

	public static PermissionHelper createCameraPermissionHelper(Fragment fragment) {
		return new PermissionHelper(fragment, CAMERA_PERMISSION, CAMERA_PERMISSION_REQUEST_CODE);
	}

	public static PermissionHelper createAccountsPermissionHelper(Fragment fragment) {
		return new PermissionHelper(fragment, ACCOUNTS_PERMISSION, ACCOUNTS_PERMISSION_REQUEST_CODE);
	}

	public static PermissionHelper createWriteExternalStoragePermissionHelper(Fragment fragment) {
		return new PermissionHelper(fragment, WRITE_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
	}

	public static Boolean checkPermission(FragmentActivity fragmentActivity, @StringRes int titleResId, @StringRes int messageResId, String permission, int permissionRequestCode) {
		return checkPermission(createPermissionDelegate(fragmentActivity), titleResId, messageResId, permission, permissionRequestCode);
	}

	public static Boolean checkPermission(FragmentActivity fragmentActivity, String title, CharSequence message, String permission, int permissionRequestCode) {
		return checkPermission(createPermissionDelegate(fragmentActivity), title, message, permission, permissionRequestCode);
	}

	public static Boolean checkPermission(FragmentActivity fragmentActivity, String permission, int permissionRequestCode) {
		return checkPermission(createPermissionDelegate(fragmentActivity), permission, permissionRequestCode);
	}

	public static Boolean shouldShowRequestPermissionRationale(FragmentActivity fragmentActivity, String permission) {
		return shouldShowRequestPermissionRationale(createPermissionDelegate(fragmentActivity), permission);
	}

	public static Boolean checkPermission(Fragment fragment, @StringRes int titleResId, @StringRes int messageResId, String permission, int permissionRequestCode) {
		return checkPermission(createPermissionDelegate(fragment), titleResId, messageResId, permission, permissionRequestCode);
	}

	public static Boolean checkPermission(Fragment fragment, String title, CharSequence message, String permission, int permissionRequestCode) {
		return checkPermission(createPermissionDelegate(fragment), title, message, permission, permissionRequestCode);
	}

	public static Boolean checkPermission(Fragment fragment, String permission, int permissionRequestCode) {
		return checkPermission(createPermissionDelegate(fragment), permission, permissionRequestCode);
	}

	public static Boolean shouldShowRequestPermissionRationale(Fragment fragment, String permission) {
		return shouldShowRequestPermissionRationale(createPermissionDelegate(fragment), permission);
	}

	private static boolean checkPermission(PermissionDelegate permissionDelegate, @StringRes int titleResId, @StringRes int messageResId, String permission, int permissionRequestCode) {
		return checkPermission(permissionDelegate, permissionDelegate.getActivity().getString(titleResId), permissionDelegate.getActivity().getText(messageResId), permission, permissionRequestCode);
	}

	private static boolean checkPermission(PermissionDelegate permissionDelegate, String title, CharSequence message, String permission, int permissionRequestCode) {
		// Try catch added for exception occurred when checking permissions on some Lenovo 7" tablets
		boolean hasPermission;
		try {
			if (permissionDelegate.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
				if (permissionDelegate.shouldShowRequestPermissionRationale(permission)) {
					permissionDelegate.showPermissionDialogFragment(title, message, permission, permissionRequestCode);
				} else {
					permissionDelegate.requestPermissions(new String[]{permission}, permissionRequestCode);
				}
				hasPermission = false;
			} else {
				hasPermission = true;
			}
		} catch (Exception e) {
			hasPermission = false;
			AbstractApplication.get().getExceptionHandler().logWarningException("Exception checking permission", e);
		}
		return hasPermission;
	}

	private static boolean checkPermission(PermissionDelegate permissionDelegate, String permission, int permissionRequestCode) {
		// Try catch added for exception occurred when checking permissions on some Lenovo 7" tablets
		boolean hasPermission;
		try {
			if (permissionDelegate.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
				permissionDelegate.requestPermissions(new String[] {permission}, permissionRequestCode);
				hasPermission = false;
			} else {
				hasPermission = true;
			}
		} catch (Exception e) {
			hasPermission = false;
			AbstractApplication.get().getExceptionHandler().logWarningException("Exception checking permission", e);
		}
		return hasPermission;
	}

	private static boolean shouldShowRequestPermissionRationale(PermissionDelegate permissionDelegate, String permission) {
		return permissionDelegate.shouldShowRequestPermissionRationale(permission);
	}

	public static Boolean verifyPermission(Context context, String permission) {
		// Try catch added for exception occurred when checking permissions on some Lenovo 7" tablets
		Boolean hasPermission;
		try {
			hasPermission = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
		} catch (Exception e) {
			hasPermission = false;
			AbstractApplication.get().getExceptionHandler().logWarningException("Exception checking permission", e);
		}
		return hasPermission;
	}

	protected static PermissionDelegate createPermissionDelegate(@NonNull FragmentActivity fragmentActivity) {
		return new ActivityPermissionDelegate(fragmentActivity);
	}

	protected static PermissionDelegate createPermissionDelegate(@NonNull Fragment fragment) {
		return new FragmentPermissionDelegate(fragment);
	}



	public PermissionHelper(@NonNull FragmentActivity fragmentActivity, @NonNull String permission, int permissionRequestCode) {
		this(new ActivityPermissionDelegate(fragmentActivity), permission, permissionRequestCode);
	}

	public PermissionHelper(@NonNull Fragment fragment, @NonNull String permission, int permissionRequestCode) {
		this(new FragmentPermissionDelegate(fragment), permission, permissionRequestCode);
	}

	private PermissionHelper(PermissionDelegate permissionDelegate, String permission, int permissionRequestCode) {
		this.permissionDelegate = permissionDelegate;
		this.permission = permission;
		this.permissionRequestCode = permissionRequestCode;
	}

	/**
	 * Sets the message to be displayed in the permission rationale dialog, from a resource.
	 * If not set, the permission rationale dialog will not be shown.
	 *
	 * @param permissionRationaleMessageResId  the resource id for the message
	 */
	public void setPermissionRationaleMessageResId(@StringRes int permissionRationaleMessageResId) {
		this.permissionRationaleMessageResId = permissionRationaleMessageResId;
	}

	/**
	 * Sets the title to be displayed in the permission rationale dialog, from a resource.
	 * If not set, the default title is used {@link R.string#jdroid_requiredPermission} is used.
	 *
	 * @param permissionRationaleTitleResId  the resource id for the title
	 */
	public void setPermissionRationaleTitleResId(int permissionRationaleTitleResId) {
		this.permissionRationaleTitleResId = permissionRationaleTitleResId;
	}

	/**
	 * Sets the message to be displayed in the App Info dialog, from a resource.
	 * If not set and {@link #isShowAppInfoDialogEnabled()} is true, the message specified in {@link #setPermissionRationaleMessageResId(int)} is used.
	 *
	 * @param appInfoDialogMessageResId  the resource id for the message
	 */
	public void setAppInfoDialogMessageResId(@StringRes int appInfoDialogMessageResId) {
		this.appInfoDialogMessageResId = appInfoDialogMessageResId;
	}

	/**
	 * Sets the title to be displayed in the App Info dialog, from a resource.
	 * If not set and {@link #isShowAppInfoDialogEnabled()} is true, the title specified in {@link #setPermissionRationaleTitleResId(int)} or the default title is used.
	 *
	 * @param appInfoDialogTitleResId  the resource id for the title
	 */
	public void setAppInfoDialogTitleResId(int appInfoDialogTitleResId) {
		this.appInfoDialogTitleResId = appInfoDialogTitleResId;
	}

	/**
	 * Sets the listener to be called for the result from requesting permissions.
	 *
	 * @see OnRequestPermissionsResultListener
	 */
	public void setOnRequestPermissionsResultListener(OnRequestPermissionsResultListener onRequestPermissionsResultListener) {
		this.onRequestPermissionsResultListener = onRequestPermissionsResultListener;
	}

	public boolean isShowAppInfoDialogEnabled() {
		return showAppInfoDialogEnabled;
	}

	/**
	 * Enables or disables the App Info dialog. When it is enabled, the App Info dialog will be shown
	 * if {@link #checkPermission()} is called and the user previously was denied the permission with
	 * the option "Never ask again".
	 * The default value for showAppInfoDialogEnabled is true.
	 *
	 * @param showAppInfoDialogEnabled true to enable the App Info dialog, false otherwise.
	 */
	public void setShowAppInfoDialogEnabled(boolean showAppInfoDialogEnabled) {
		this.showAppInfoDialogEnabled = showAppInfoDialogEnabled;
	}

	public boolean isRequestPermissionOnStart() {
		return requestPermissionOnStart;
	}

	/**
	 * Sets whether the permission to the user will be requested when the activity/fragment starting.
	 * The default value is false.
	 *
	 * @param requestPermissionOnStart true if the permission will be requested when starting, false otherwise
	 */
	public void setRequestPermissionOnStart(boolean requestPermissionOnStart) {
		this.requestPermissionOnStart = requestPermissionOnStart;
	}

	/**
	 * This method must be called from the respective method of the fragment/activity when requestPermissionOnStart is true.
	 *
	 * @see #setRequestPermissionOnStart(boolean)
	 */
	public void onCreate(Bundle savedInstanceState) {
		firstTime = savedInstanceState==null;
	}

	/**
	 * This method must be called from the respective method of the fragment/activity.
	 *
	 */
	public void onResume() {
		if(requestPermissionOnStart && firstTime) {
			checkPermission(false);
			firstTime = false;
		} else if(pendingAppInfoDialog) {
			pendingAppInfoDialog = false;
			showAppInfoDialog();
		}
	}

	/**
	 * This method must be called from the respective method of the fragment/activity.
	 */
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == permissionRequestCode) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				if (onRequestPermissionsResultListener != null) {
					onRequestPermissionsResultListener.onRequestPermissionsGranted();
				}
			} else {
				if (onRequestPermissionsResultListener != null) {
					onRequestPermissionsResultListener.onRequestPermissionsDenied();
				}

				if (previouslyShouldShowRequestPermissionRationale != null && !previouslyShouldShowRequestPermissionRationale && !permissionDelegate.shouldShowRequestPermissionRationale(permission)) {
					//Note: onRequestPermissionsResult(...) is called immediately before onResume() (like onActivityResult(...)),
					// if the dialog is show immediately in this case an exception occurs ("java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState").
					pendingAppInfoDialog = true;
				}
			}
		}
	}

	/**
	 * 	Check wheter the app has the permission. If the app does not have the permission, the permission will be request
	 * 	to the user using the default Android UI for accepting it. If the user previously was denied this permission
	 * 	a dialog with the "Permission Rationale Message" will be showed. By last, if the user previously was denied
	 * 	this permission with the option "Never ask again" the AppInfoDialog could be showed.
	 * 	After the user has accepted or rejected the requested permission the OnRequestPermissionsResultListener
	 * 	will receive a callback.
	 *
	 * 	@return true if the app already has the permission, false otherwise.
	 *
	 * 	@see #setPermissionRationaleMessageResId(int)
	 * 	@see #setPermissionRationaleTitleResId(int)
	 * 	@see #setAppInfoDialogMessageResId(int)
	 * 	@see #setAppInfoDialogTitleResId(int)
	 * 	@see #setShowAppInfoDialogEnabled(boolean)
	 *
	 * 	@see #setOnRequestPermissionsResultListener(OnRequestPermissionsResultListener)
	 *
	 */
	public boolean checkPermission() {
		return checkPermission(showAppInfoDialogEnabled);
	}

	/**
	 * Similar to {@link #checkPermission()}, but it allows the AppInfoDialog will be enabled or disabled for this call.
	 *
	 * @param showAppInfoDialogEnabled true if the AppInfoDialog is enabled, false to disabled it.
	 */
	public boolean checkPermission(boolean showAppInfoDialogEnabled) {
		boolean hasPermission;
		if(permissionRationaleMessageResId != 0) {
			hasPermission = checkPermission(permissionDelegate, getPermissionRationaleTitleResId(), permissionRationaleMessageResId, permission, permissionRequestCode);
		} else {
			hasPermission = checkPermission(permissionDelegate, permission, permissionRequestCode);
		}
		if(!hasPermission && showAppInfoDialogEnabled) {
			previouslyShouldShowRequestPermissionRationale = permissionDelegate.shouldShowRequestPermissionRationale(permission);
		} else {
			previouslyShouldShowRequestPermissionRationale = null;
		}
		return hasPermission;
	}

	/**
	 * @return true whether you should show UI with rationale for requesting a permission., false otherwise
	 */
	public boolean shouldShowRequestPermissionRationale() {
		return permissionDelegate.shouldShowRequestPermissionRationale(permission);
	}

	/**
	 * @return true if the app has the permission, false otherwise
	 */
	public boolean verifyPermission() {
		return verifyPermission(permissionDelegate.getActivity(), permission);
	}

	private int getAppInfoDialogMessageResId() {
		if(appInfoDialogMessageResId != 0) {
			return appInfoDialogMessageResId;
		}
		return permissionRationaleMessageResId;
	}

	private int getAppInfoDialogTitleResId() {
		if(appInfoDialogTitleResId != 0) {
			return appInfoDialogTitleResId;
		}
		return getPermissionRationaleTitleResId();
	}

	private int getPermissionRationaleTitleResId() {
		if(permissionRationaleTitleResId != 0) {
			return permissionRationaleTitleResId;
		}
		return R.string.jdroid_requiredPermission;
	}

	private void showAppInfoDialog() {
		int appInfoDialogMessageResId = getAppInfoDialogMessageResId();
		if(appInfoDialogMessageResId != 0) {
			AppInfoDialogFragment.show(permissionDelegate.getActivity(), getAppInfoDialogTitleResId(), appInfoDialogMessageResId, permission);
		}
	}


	// Nested classes -------------------------
	/**
	 * Listener for the result from requesting permissions.
	 * The listener will be called when the fragment/activity onRequestPermissionsResult(...) is called and this can occur immediately before onResume() (like onActivityResult(...)).
	 */
	public interface OnRequestPermissionsResultListener {
		/**
		 * Called when the permission is granted.
		 */
		void onRequestPermissionsGranted();

		/**
		 * Called when the permission is denied.
		 */
		void onRequestPermissionsDenied();
	}


	protected static abstract class PermissionDelegate {

		public abstract FragmentActivity getActivity();

		public abstract void requestPermissions(@NonNull String[] permissions, int requestCode);

		public abstract boolean shouldShowRequestPermissionRationale(@NonNull String permission);

		public abstract void showPermissionDialogFragment(String title, CharSequence message, String permission, int permissionRequestCode);

		public int checkSelfPermission(@NonNull String permission) {
			return ContextCompat.checkSelfPermission(getActivity(), permission);
		}

	}


	protected static class ActivityPermissionDelegate extends PermissionDelegate {

		private FragmentActivity fragmentActivity;

		public ActivityPermissionDelegate(FragmentActivity fragmentActivity) {
			this.fragmentActivity = fragmentActivity;
		}

		@Override
		public FragmentActivity getActivity() {
			return fragmentActivity;
		}

		@Override
		public void requestPermissions(@NonNull String[] permissions, int requestCode) {
			ActivityCompat.requestPermissions(fragmentActivity, permissions,requestCode);
		}

		@Override
		public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
			return ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permission);
		}

		@Override
		public void showPermissionDialogFragment(String title, CharSequence message, String permission, int permissionRequestCode) {
			PermissionDialogFragment.show(fragmentActivity, title, message, permission, permissionRequestCode);
		}

	}

	protected static class FragmentPermissionDelegate extends PermissionDelegate {
		private Fragment fragment;

		public FragmentPermissionDelegate(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public FragmentActivity getActivity() {
			return fragment.getActivity();
		}

		@Override
		public void requestPermissions(@NonNull String[] permissions, int requestCode) {
			fragment.requestPermissions(permissions, requestCode);
		}

		@Override
		public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
			return fragment.shouldShowRequestPermissionRationale(permission);
		}

		@Override
		public void showPermissionDialogFragment(String title, CharSequence message, String permission, int permissionRequestCode) {
			PermissionDialogFragment.show(fragment, title, message, permission, permissionRequestCode);
		}

	}

}
