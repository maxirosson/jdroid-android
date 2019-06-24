package com.jdroid.android.date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import com.jdroid.java.date.DateUtils;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

public class TimeButton extends AppCompatButton {

	private Date time;

	@SuppressLint("SetTextI18n")
	public TimeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			setText("11:13 PM");
		}
	}

	public void init(final Fragment fragment, final Date defaultTime, final int requestCode) {
		setTime(defaultTime);
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerDialogFragment.show(fragment, time, requestCode);
			}
		});
	}

	public void init(Fragment fragment, Date defaultTime) {
		init(fragment, defaultTime, 1);
	}

	public void setTime(Long timestamp) {
		setTime(DateUtils.getDate(timestamp));
	}

	public void setTime(Date time) {
		this.time = time;
		setText(AndroidDateUtils.INSTANCE.formatTime(time));
	}

	public Date getTime() {
		return time;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		return new SavedState(superState, time);
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState)state;
		super.onRestoreInstanceState(ss.getSuperState());
		setTime(ss.time);
	}

	/**
	 * Class for managing state storing/restoring.
	 */
	private static class SavedState extends BaseSavedState {

		private Date time;

		/**
		 * Constructor called from {@link DatePicker#onSaveInstanceState()}
		 */
		private SavedState(Parcelable superState, Date time) {
			super(superState);
			this.time = time;
		}

		/**
		 * Constructor called from {@link #CREATOR}
		 */
		private SavedState(Parcel in) {
			super(in);
			time = DateUtils.getTime(in.readInt(), in.readInt());
		}

		@Override
		public void writeToParcel(@NonNull Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(DateUtils.getHour(time, true));
			dest.writeInt(DateUtils.getMinute(time));
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
