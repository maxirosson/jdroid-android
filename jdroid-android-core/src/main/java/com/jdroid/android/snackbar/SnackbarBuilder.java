package com.jdroid.android.snackbar;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.jdroid.android.R;
import com.jdroid.android.utils.LocalizationUtils;

public class SnackbarBuilder {

	private int parentLayoutId = R.id.fragmentContainer;
	private int duration = Snackbar.LENGTH_LONG;
	private int actionTextResId;
	private View.OnClickListener onClickListener;
	private String description;

	public Snackbar build(Activity activity) {
		Snackbar snackbar = Snackbar.make(activity.findViewById(parentLayoutId), description, duration);
		if (onClickListener != null) {
			snackbar.setAction(actionTextResId, onClickListener);
		}
		return snackbar;
	}

	public void setActionTextResId(@StringRes int actionTextResId) {
		this.actionTextResId = actionTextResId;
	}

	public void setOnClickListener(View.OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public void setParentLayoutId(@IdRes int parentLayoutId) {
		this.parentLayoutId = parentLayoutId;
	}

	public void setDuration(@Snackbar.Duration int duration) {
		this.duration = duration;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDescription(@StringRes int descriptionResId) {
		this.description = LocalizationUtils.getString(descriptionResId);
	}
}
