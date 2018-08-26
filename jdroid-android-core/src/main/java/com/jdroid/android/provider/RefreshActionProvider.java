package com.jdroid.android.provider;

import android.content.Context;

import com.jdroid.android.R;

public class RefreshActionProvider extends TwoStateActionProvider {

	public RefreshActionProvider(Context context) {
		super(context);
	}

	@Override
	protected Integer getFirstStateImageResId() {
		return R.drawable.jdroid_ic_refresh_white_24dp;
	}

	@Override
	protected Integer getFirstStateCheatSheetResId() {
		return R.string.jdroid_refresh;
	}
}
