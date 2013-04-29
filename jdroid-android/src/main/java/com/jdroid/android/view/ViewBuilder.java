package com.jdroid.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.jdroid.android.R;

/**
 * 
 * @author Maxi Rosson
 */
public class ViewBuilder {
	
	public static TextView buildSectionTitle(Context context, int titleId, Object... args) {
		TextView textView = (TextView)LayoutInflater.from(context).inflate(R.layout.section_title, null);
		textView.setText(context.getString(titleId, args));
		return textView;
	}
	
}
