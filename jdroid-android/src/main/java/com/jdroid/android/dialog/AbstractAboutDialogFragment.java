package com.jdroid.android.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.DateUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractAboutDialogFragment extends AbstractDialogFragment {
	
	public void show(Activity activity) {
		FragmentManager fm = ((FragmentActivity)activity).getSupportFragmentManager();
		show(fm, getClass().getSimpleName());
	}
	
	/**
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View view = inflate(R.layout.about_dialog);
		dialogBuilder.setView(view);
		
		dialogBuilder.setTitle(R.string.about);
		
		dialogBuilder.setPositiveButton(getString(R.string.ok), null);
		
		TextView appName = (TextView)view.findViewById(R.id.appName);
		appName.setText(AbstractApplication.get().getAppName());
		
		TextView version = (TextView)view.findViewById(R.id.version);
		version.setText(getString(R.string.version, AndroidUtils.getVersionName()));
		
		TextView contactUsLabel = (TextView)view.findViewById(R.id.contactUsLabel);
		TextView contactUsEmail = (TextView)view.findViewById(R.id.contactUsEmail);
		final String contactUsEmailAddress = getContactUsEmail();
		if (contactUsEmailAddress != null) {
			contactUsEmail.setText(contactUsEmailAddress);
		} else {
			contactUsLabel.setVisibility(View.GONE);
			contactUsEmail.setVisibility(View.GONE);
		}
		
		TextView copyright = (TextView)view.findViewById(R.id.copyright);
		copyright.setText(getCopyRightLegend());
		
		TextView allRightsReservedLegend = (TextView)view.findViewById(R.id.allRightsReservedLegend);
		allRightsReservedLegend.setText(getAllRightsReservedLegend());
		
		return dialogBuilder.create();
	}
	
	protected String getAllRightsReservedLegend() {
		return getString(R.string.allRightsReservedLegend);
	}
	
	protected String getCopyRightLegend() {
		return getString(R.string.copyright, DateUtils.getYear(), AbstractApplication.get().getAppName());
	}
	
	protected String getContactUsEmail() {
		return null;
	}
	
}
