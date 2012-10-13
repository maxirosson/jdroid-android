package com.jdroid.android.view;

import java.util.Date;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.jdroid.android.fragment.TimePickerDialogFragment;
import com.jdroid.android.utils.AndroidDateUtils;
import com.jdroid.java.utils.DateUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class TimeButton extends Button {
	
	private Date time;
	
	public TimeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			setText("11:13 PM");
		}
	}
	
	public void init(final Fragment fragment, final Date defaultTime) {
		setTime(defaultTime);
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimePickerDialogFragment.show(fragment, time);
			}
		});
	}
	
	public void setTime(Date time) {
		this.time = time;
		setText(AndroidDateUtils.formatTime(time));
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
		public void writeToParcel(Parcel dest, int flags) {
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
