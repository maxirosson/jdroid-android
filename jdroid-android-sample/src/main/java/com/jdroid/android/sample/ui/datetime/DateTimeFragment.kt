package com.jdroid.android.sample.ui.datetime

import android.os.Bundle
import android.view.View

import com.jdroid.android.date.DateButton
import com.jdroid.android.date.DatePickerDialogFragment
import com.jdroid.android.date.TimeButton
import com.jdroid.android.date.TimePickerDialogFragment
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.java.date.DateUtils

import java.util.Date

class DateTimeFragment : AbstractFragment(), DatePickerDialogFragment.OnDateSetListener, TimePickerDialogFragment.OnTimeSetListener {

    private lateinit var dateButton: DateButton
    private lateinit var timeButton: TimeButton

    override fun getContentFragmentLayout(): Int? {
        return R.layout.datetime_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateButton = findView(R.id.datePickerDialog)
        dateButton.init(this, DateUtils.now())

        timeButton = findView(R.id.timePickerDialog)
        timeButton.init(this, DateUtils.now())
    }

    override fun onTimeSet(time: Date, requestCode: Int) {
        timeButton.time = time
    }

    override fun onDateSet(date: Date, requestCode: Int) {
        dateButton.date = date
    }
}
