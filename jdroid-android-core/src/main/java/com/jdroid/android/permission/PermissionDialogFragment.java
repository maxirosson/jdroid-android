package com.jdroid.android.permission;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.jdroid.android.R;
import com.jdroid.android.dialog.AlertDialogFragment;

public class PermissionDialogFragment extends AlertDialogFragment {

	public static final String PERMISSION_EXTRA = "permissionExtra";
	public static final String PERMISSION_REQUEST_CODE_EXTRA = "permissionRequestCodeExtra";

	private String permission;
	private int permissionRequestCode;

	public static void show(FragmentActivity fragmentActivity, String title, CharSequence message, String permission, int permissionRequestCode) {
		show(fragmentActivity, null, title, message, permission, permissionRequestCode);
	}

	public static void show(Fragment targetFragment, String title, CharSequence message, String permission, int permissionRequestCode) {
		show(targetFragment.getActivity(), targetFragment, title, message, permission, permissionRequestCode);
	}

	private static void show(FragmentActivity fragmentActivity, Fragment targetFragment, String title, CharSequence message, String permission, int permissionRequestCode) {
		PermissionDialogFragment fragment = new PermissionDialogFragment();
		fragment.addParameter(PermissionDialogFragment.PERMISSION_EXTRA, permission);
		fragment.addParameter(PermissionDialogFragment.PERMISSION_REQUEST_CODE_EXTRA, permissionRequestCode);
		if(targetFragment!=null) {
			fragment.setTargetFragment(targetFragment,0);
		}

		String screenViewName = PermissionDialogFragment.class.getSimpleName() + "-" + permission;

		AlertDialogFragment.show(fragmentActivity, fragment, null, title, message, fragmentActivity.getString(R.string.jdroid_cancel), null,
				fragmentActivity.getString(R.string.jdroid_ok), true, screenViewName, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		permission = getArgument(PERMISSION_EXTRA);
		permissionRequestCode = getArgument(PERMISSION_REQUEST_CODE_EXTRA);
	}

	@Override
	protected void onPositiveClick() {
		Fragment targetFragment = this.getTargetFragment();
		if (targetFragment != null) {
			targetFragment.requestPermissions(new String[] {permission}, permissionRequestCode);
		} else {
			ActivityCompat.requestPermissions(getActivity(), new String[] {permission}, permissionRequestCode);
		}
	}
}
