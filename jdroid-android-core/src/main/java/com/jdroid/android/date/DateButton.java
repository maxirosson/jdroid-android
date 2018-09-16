package com.jdroid.android.date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import com.jdroid.java.date.DateTimeFormat;
import com.jdroid.java.date.DateUtils;

import java.util.Date;

public class DateButton extends AppCompatButton {

	private Date date;

	@SuppressLint("SetTextI18n")
	public DateButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			setText("Sat, Dec 15, 2012");
		}
	}

	public void init(Fragment targetFragment, final Date defaultDate) {
		init(targetFragment, defaultDate, null);
	}

	public void init(Fragment targetFragment, Date defaultDate, Integer titleResId) {
		init(targetFragment, defaultDate, titleResId, null);
	}

	public void init(Fragment targetFragment, Date defaultDate, Integer titleResId, Date minDate) {
		init(targetFragment, defaultDate, titleResId, minDate, null);
	}

	public void init(Fragment targetFragment, Date defaultDate, Integer titleResId, Date minDate, Date maxDate) {
		init(targetFragment, defaultDate, titleResId, minDate, maxDate, 1);
	}

	public void init(final Fragment targetFragment, final Date defaultDate, final Integer titleResId,
					 final Date minDate, final Date maxDate, final int requestCode) {
		setDate(defaultDate);
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialogFragment.show(targetFragment, defaultDate, titleResId, minDate, maxDate, requestCode);
			}
		});
	}

	public void setDate(Long timestamp) {
		setDate(DateUtils.getDate(timestamp));
	}

	public void setDate(Date date) {
		this.date = date;
		setText(DateUtils.format(date, DateTimeFormat.EEMMMDYYYY));
	}

	public Date getDate() {
		return date;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		return new SavedState(superState, date);
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState)state;
		super.onRestoreInstanceState(ss.getSuperState());
		setDate(ss.date);
	}

	/**
	 * Class for managing state storing/restoring.
	 */
	private static class SavedState extends BaseSavedState {

		private Date date;

		/**
		 * Constructor called from {@link DatePicker#onSaveInstanceState()}
		 */
		private SavedState(Parcelable superState, Date date) {
			super(superState);
			this.date = date;
		}

		/**
		 * Constructor called from {@link #CREATOR}
		 */
		private SavedState(Parcel in) {
			super(in);
			date = DateUtils.getDate(in.readInt(), in.readInt(), in.readInt());
		}

		@Override
		public void writeToParcel(@NonNull Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(DateUtils.getYear(date));
			dest.writeInt(DateUtils.getMonth(date));
			dest.writeInt(DateUtils.getDayOfMonth(date));
		}

		@SuppressWarnings({ "hiding", "unused" })
		public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
