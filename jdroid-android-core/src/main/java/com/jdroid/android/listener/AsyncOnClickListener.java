package com.jdroid.android.listener;

import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.java.concurrent.ExecutorUtils;

/**
 * Asynchronous {@link OnClickListener}
 */
public abstract class AsyncOnClickListener implements OnClickListener {

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public final void onClick(final View view) {
		ExecutorUtils.execute(new Runnable() {

			@Override
			public void run() {
				onAsyncRun(view);
			}
		});
	}

	/**
	 * Called when a view has been clicked.
	 *
	 * @param view The view that was clicked.
	 */
	public abstract void onAsyncRun(View view);

}
