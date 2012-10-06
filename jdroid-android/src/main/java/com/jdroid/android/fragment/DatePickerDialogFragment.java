package com.jdroid.android.fragment;

import java.util.Date;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import com.jdroid.android.R;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.DateUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class DatePickerDialogFragment extends AbstractDialogFragment {
	
	private static final String DEFAULT_DATE_EXTRA = "defaultDate";
	
	public static void show(Fragment targetFragment, Date date) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		DatePickerDialogFragment fragment = new DatePickerDialogFragment(date);
		fragment.setTargetFragment(targetFragment, 1);
		fragment.show(fm, DatePickerDialogFragment.class.getSimpleName());
	}
	
	private DatePicker datePicker;
	private Date defaultDate;
	
	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateSetListener {
		
		public void onDateSet(Date date);
	}
	
	public DatePickerDialogFragment() {
	}
	
	public DatePickerDialogFragment(Date defaultDate) {
		this.defaultDate = defaultDate;
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(DEFAULT_DATE_EXTRA, defaultDate);
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
			defaultDate = (Date)args.getSerializable(DEFAULT_DATE_EXTRA);
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.date_picker_dialog_fragment, container, false);
		
		datePicker = (DatePicker)view.findViewById(R.id.datePicker);
		datePicker.init(DateUtils.getYear(defaultDate), DateUtils.getMonth(defaultDate), DateUtils.getDay(defaultDate),
			null);
		if (AndroidUtils.getApiLevel() > 10) {
			datePicker.getCalendarView().setShowWeekNumber(false);
		}
		getDialog().setTitle(R.string.selectDate);
		
		Button ok = (Button)view.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date date = DateUtils.getDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
				((OnDateSetListener)getTargetFragment()).onDateSet(date);
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
