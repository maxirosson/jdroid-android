package com.jdroid.android.fragment;

import java.util.Date;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import com.jdroid.android.R;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.DateUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class TimePickerDialogFragment extends AbstractDialogFragment {
	
	private static final String DEFAULT_TIME_EXTRA = "defaultTime";
	
	public static void show(Fragment targetFragment, Date defaultTime) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		TimePickerDialogFragment fragment = new TimePickerDialogFragment(defaultTime);
		fragment.setTargetFragment(targetFragment, 1);
		fragment.show(fm, TimePickerDialogFragment.class.getSimpleName());
	}
	
	private TimePicker timePicker;
	private Date defaultTime;
	
	public interface OnTimeSetListener {
		
		public void onTimeSet(Date time);
	}
	
	public TimePickerDialogFragment() {
	}
	
	public TimePickerDialogFragment(Date defaultTime) {
		this.defaultTime = defaultTime;
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(DEFAULT_TIME_EXTRA, defaultTime);
		setArguments(bundle);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Google TV is not displaying the title of the dialog.
		if (AndroidUtils.isGoogleTV()) {
			setStyle(STYLE_NO_TITLE, 0);
		}
		
		Bundle args = getArguments();
		if (args != null) {
			defaultTime = (Date)args.getSerializable(DEFAULT_TIME_EXTRA);
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.time_picker_dialog_fragment, container, false);
		
		getDialog().setTitle(R.string.selectTime);
		timePicker = (TimePicker)view.findViewById(R.id.timePicker);
		timePicker.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
		timePicker.setCurrentHour(DateUtils.getHour(defaultTime, true));
		timePicker.setCurrentMinute(DateUtils.getMinute(defaultTime));
		
		Button ok = (Button)view.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date time = DateUtils.getTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
				((OnTimeSetListener)getTargetFragment()).onTimeSet(time);
				dismiss();
			}
		});
		
		Button cancel = (Button)view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		return view;
	}
}
