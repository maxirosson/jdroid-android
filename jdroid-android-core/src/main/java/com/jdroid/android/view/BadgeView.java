package com.jdroid.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * Text view used to show a badge with a number of notifications. If the notifications are less than zero, the badge
 * isn't visible.
 */
public class BadgeView extends AppCompatTextView {

	private static final int DEFAULT_MAXIMUM = 9;
	private int maximum = DEFAULT_MAXIMUM;

	public BadgeView(Context context) {
		this(context, null);
	}

	public BadgeView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (isInEditMode()) {
			setText("1");
		} else {
			// hide by default
			setVisibility(GONE);
		}

		// Adding styles
		setPadding(8, 4, 8, 4);
		setTextColor(Color.WHITE);
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		setTypeface(Typeface.DEFAULT_BOLD);
	}

	/**
	 * Sets a notification number in the badge.
	 *
	 * @param notifications
	 */
	@SuppressLint("SetTextI18n")
	public void setNotifications(Integer notifications) {

		if ((notifications != null) && (notifications > 0)) {
			setVisibility(VISIBLE);
			if (notifications > maximum) {
				this.setText(maximum + "+");
			} else {
				this.setText(notifications.toString());
			}
		} else {
			setVisibility(GONE);
		}
	}

	/**
	 * @param maximum the maximum to set
	 */
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

}
