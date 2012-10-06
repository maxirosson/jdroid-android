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
import com.jdroid.android.fragment.DatePickerDialogFragment;
import com.jdroid.android.utils.AndroidDateUtils;
import com.jdroid.java.utils.DateUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class DateButton extends Button {
	
	private Date date;
	
	public DateButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void init(final Fragment fragment, final Date defaultDate) {
		setDate(defaultDate);
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatePickerDialogFragment.show(fragment, date);
			}
		});
	}
	
	public void setDate(Date date) {
		this.date = date;
		setText(AndroidDateUtils.formatDate(date));
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
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(DateUtils.getYear(date));
			dest.writeInt(DateUtils.getMonth(date));
			dest.writeInt(DateUtils.getDay(date));
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
