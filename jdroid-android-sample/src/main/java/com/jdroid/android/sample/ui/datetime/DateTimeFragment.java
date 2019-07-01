package com.jdroid.android.sample.ui.datetime;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.date.DateButton;
import com.jdroid.android.date.DatePickerDialogFragment;
import com.jdroid.android.date.TimeButton;
import com.jdroid.android.date.TimePickerDialogFragment;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.java.date.DateUtils;

import java.util.Date;

public class DateTimeFragment extends AbstractFragment implements DatePickerDialogFragment.OnDateSetListener, TimePickerDialogFragment.OnTimeSetListener {

	private DateButton dateButton;
	private TimeButton timeButton;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.datetime_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		dateButton = findView(R.id.datePickerDialog);
		dateButton.init(this, DateUtils.INSTANCE.now());

		timeButton = findView(R.id.timePickerDialog);
		timeButton.init(this, DateUtils.INSTANCE.now());
	}

	@Override
	public void onTimeSet(Date time, int requestCode) {
		timeButton.setTime(time);
	}

	@Override
	public void onDateSet(Date date, int requestCode) {
		dateButton.setDate(date);
	}
}
