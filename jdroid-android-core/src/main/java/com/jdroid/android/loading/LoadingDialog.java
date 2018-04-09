package com.jdroid.android.loading;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;
import com.jdroid.android.R;

/**
 * {@link Dialog} that displays a {@link ProgressBar} on indeterminate mode
 * 
 */
public class LoadingDialog extends Dialog {
	
	/**
	 * Constructor
	 * 
	 * @param context The Context in which the Dialog should run.
	 */
	public LoadingDialog(Context context) {
		this(context, R.style.jdroid_customDialog);
	}
	
	/**
	 * Constructor
	 * 
	 * @param context The Context in which the Dialog should run.
	 * @param theme A style resource describing the theme to use for the window.
	 */
	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.jdroid_loading_dialog);
	}
}
