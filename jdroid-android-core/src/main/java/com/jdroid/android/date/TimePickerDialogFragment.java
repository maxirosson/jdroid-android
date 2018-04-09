package com.jdroid.android.date;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import com.jdroid.android.R;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.jdroid.java.date.DateUtils;

import java.util.Date;

public class TimePickerDialogFragment extends AbstractDialogFragment {
	
	private static final String DEFAULT_TIME_EXTRA = "defaultTime";
	
	public static void show(Fragment targetFragment, Date defaultTime) {
		TimePickerDialogFragment.show(targetFragment, defaultTime, 1);
	}
	
	public static void show(Fragment targetFragment, Date defaultTime, int requestCode) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		TimePickerDialogFragment fragment = new TimePickerDialogFragment();
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(DEFAULT_TIME_EXTRA, defaultTime);
		fragment.setArguments(bundle);
		
		fragment.setTargetFragment(targetFragment, requestCode);
		fragment.show(fm, TimePickerDialogFragment.class.getSimpleName());
	}
	
	private Date defaultTime;
	
	public interface OnTimeSetListener {
		
		public void onTimeSet(Date time, int requestCode);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		defaultTime = getArgument(DEFAULT_TIME_EXTRA);
	}
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View view = inflate(R.layout.jdroid_time_picker_dialog_fragment);
		dialogBuilder.setView(view);
		
		final TimePicker timePicker = (TimePicker)view.findViewById(R.id.timePicker);
		timePicker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
		timePicker.setCurrentHour(DateUtils.getHour(defaultTime, true));
		timePicker.setCurrentMinute(DateUtils.getMinute(defaultTime));
		
		dialogBuilder.setPositiveButton(getString(R.string.jdroid_ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				Date time = DateUtils.getTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
				int requestCode = getTargetRequestCode();
				((OnTimeSetListener)getTargetFragment()).onTimeSet(time, requestCode);
			}
		});
		
		dialogBuilder.setNegativeButton(getString(R.string.jdroid_cancel), null);
		
		return dialogBuilder.create();
	}
}
