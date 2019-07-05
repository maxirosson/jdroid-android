package com.jdroid.android.date;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.jdroid.android.R;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.java.date.DateUtils;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DatePickerDialogFragment extends AbstractDialogFragment implements DatePicker.OnDateChangedListener {

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		defaultDate = getArgument(DEFAULT_DATE_EXTRA);
		titleResId = getArgument(TITLE_EXTRA);
		minDate = getArgument(MIN_DATE_EXTRA);
		maxDate = getArgument(MAX_DATE_EXTRA);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View view = inflate(R.layout.jdroid_date_picker_dialog_fragment);
		dialogBuilder.setView(view);

		final DatePicker datePicker = view.findViewById(R.id.datePicker);
		datePicker.init(DateUtils.INSTANCE.getYear(defaultDate), DateUtils.INSTANCE.getMonth(defaultDate), DateUtils.INSTANCE.getDayOfMonth(defaultDate), this);

		if (titleResId != null) {
			dialogBuilder.setTitle(titleResId);
		}

		Boolean disableMinMaxDate = disableMinMaxDate();

		if ((minDate != null) && !disableMinMaxDate) {
			datePicker.setMinDate(minDate.getTime());
		}
		if ((maxDate != null) && !disableMinMaxDate) {
			datePicker.setMaxDate(maxDate.getTime());
		}

		dialogBuilder.setPositiveButton(getString(R.string.jdroid_ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				Date date = com.jdroid.java.date.DateUtils.INSTANCE.getDate(datePicker.getYear(), datePicker.getMonth(),
					datePicker.getDayOfMonth());
				int requestCode = getTargetRequestCode();
				((OnDateSetListener)getTargetFragment()).onDateSet(date, requestCode);
			}
		});

		dialogBuilder.setNegativeButton(getString(R.string.jdroid_cancel), null);

		return dialogBuilder.create();
	}

	private Boolean disableMinMaxDate() {

		// Disable the min/max date feature on devices where it crashes
		if ((AndroidUtils.INSTANCE.getApiLevel() >= Build.VERSION_CODES.JELLY_BEAN)
			&& (AndroidUtils.INSTANCE.getApiLevel() <= Build.VERSION_CODES.JELLY_BEAN_MR1)) {
			String model = DeviceUtils.INSTANCE.getDeviceModel();
			if ((model != null)
				&& (model.equals("Nexus 7") || model.contains("ST26i") || model.contains("ST26a")
				|| model.contains("Galaxy Nexus") || model.contains("Amazon Kindle Fire"))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// Do nothing
	}
}
