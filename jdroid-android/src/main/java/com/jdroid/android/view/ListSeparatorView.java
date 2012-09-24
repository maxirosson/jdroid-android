package com.jdroid.android.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import com.jdroid.android.R;

/**
 * {@link TextView} used to display a section title
 * 
 * @author Maxi Rosson
 */
public class ListSeparatorView extends TextView {
	
	public ListSeparatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTextColor(Color.WHITE);
		setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.listSeparatorTextSize));
		setTypeface(Typeface.DEFAULT_BOLD);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.list_separator_background));
		setPadding(20, 3, 20, 3);
	}
	
	public ListSeparatorView(Context context, String title) {
		this(context, (AttributeSet)null);
		setText(title);
	}
	
	public ListSeparatorView(Context context, int titleResId) {
		this(context, context.getString(titleResId));
	}
}