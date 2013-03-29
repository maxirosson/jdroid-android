package com.jdroid.android.date;

import java.util.Date;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import com.jdroid.android.R;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.jdroid.android.utils.AndroidUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class DatePickerDialogFragment extends AbstractDialogFragment implements OnDateChangedListener {
	
	private static final String DEFAULT_DATE_EXTRA = "defaultDate";
	private static final String MIN_DATE_EXTRA = "minDate";
	private static final String MAX_DATE_EXTRA = "maxDate";
	private static final String TITLE_EXTRA = "title";
	
	public static void show(Fragment targetFragment, Date defaultDate) {
		DatePickerDialogFragment.show(targetFragment, defaultDate, null);
	}
	
	public static void show(Fragment targetFragment, Date defaultDate, Integer titleResId) {
		DatePickerDialogFragment.show(targetFragment, defaultDate, titleResId, null);
	}
	
	public static void show(Fragment targetFragment, Date defaultDate, Integer titleResId, Date minDate) {
		DatePickerDialogFragment.show(targetFragment, defaultDate, titleResId, minDate, null);
	}
	
	public static void show(Fragment targetFragment, Date defaultDate, Integer titleResId, Date minDate, Date maxDate) {
		DatePickerDialogFragment.show(targetFragment, defaultDate, titleResId, minDate, maxDate, 1);
	}
	
	public static void show(Fragment targetFragment, Date defaultDate, Integer titleResId, Date minDate, Date maxDate,
			int requestCode) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		DatePickerDialogFragment fragment = new DatePickerDialogFragment();
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(DEFAULT_DATE_EXTRA, defaultDate);
		if (titleResId != null) {
			bundle.putInt(TITLE_EXTRA, titleResId);
		}
		bundle.putSerializable(MIN_DATE_EXTRA, minDate);
		bundle.putSerializable(MAX_DATE_EXTRA, maxDate);
		fragment.setArguments(bundle);
		
		fragment.setTargetFragment(targetFragment, requestCode);
		fragment.show(fm, DatePickerDialogFragment.class.getSimpleName());
	}
	
	private Date defaultDate;
	private Date minDate;
	private Date maxDate;
	private Integer titleResId;
	
	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateSetListener {
		
		public void onDateSet(Date date, int requestCode);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		defaultDate = getArgument(DEFAULT_DATE_EXTRA);
		titleResId = getArgument(TITLE_EXTRA);
		minDate = getArgument(MIN_DATE_EXTRA);
		maxDate = getArgument(MAX_DATE_EXTRA);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.date_picker_dialog_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		final DatePicker datePicker = findView(R.id.datePicker);
		datePicker.init(com.jdroid.java.utils.DateUtils.getYear(defaultDate),
			com.jdroid.java.utils.DateUtils.getMonth(defaultDate), com.jdroid.java.utils.DateUtils.getDay(defaultDate),
			this);
		
		updateTitle(defaultDate);
		
		if (titleResId != null) {
			getDialog().setTitle(titleResId);
		}
		if (AndroidUtils.getApiLevel() > Build.VERSION_CODES.GINGERBREAD_MR1) {
			if (minDate != null) {
				datePicker.setMinDate(minDate.getTime());
			}
			if (maxDate != null) {
				datePicker.setMaxDate(maxDate.getTime());
			}
			
			if (AndroidUtils.getApiLevel() > Build.VERSION_CODES.HONEYCOMB) {
				datePicker.getCalendarView().setShowWeekNumber(false);
			}
		}
		
		findView(R.id.ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date date = com.jdroid.java.utils.DateUtils.getDate(datePicker.getYear(), datePicker.getMonth(),
					datePicker.getDayOfMonth());
				int requestCode = getTargetRequestCode();
				((OnDateSetListener)getTargetFragment()).onDateSet(date, requestCode);
				dismiss();
			}
		});
		
		findView(R.id.cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		if (titleResId == null) {
			updateTitle(year, monthOfYear, dayOfMonth);
		}
	}
	
	private void updateTitle(int year, int month, int day) {
		Date date = com.jdroid.java.utils.DateUtils.getDate(year, month, day);
		updateTitle(date);
	}
	
	private void updateTitle(Date date) {
		Dialog dialog = getDialog();
		if (dialog != null) {
			String title = DateUtils.formatDateTime(getDialog().getContext(), date.getTime(),
				DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_YEAR
						| DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_ABBREV_WEEKDAY);
			dialog.setTitle(title);
		}
	}
}
