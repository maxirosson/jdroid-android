package com.jdroid.android.listener;

import android.view.View;
import android.view.View.OnLongClickListener;

public class DoNothingOnLongClickListener implements OnLongClickListener {
	
	/**
	 * @see android.view.View.OnLongClickListener#onLongClick(android.view.View)
	 */
	@Override
	public boolean onLongClick(View v) {
		// Do nothing to avoid context menu on the input, so there's no way to do a copy - paste
		return true;
	}
	
}