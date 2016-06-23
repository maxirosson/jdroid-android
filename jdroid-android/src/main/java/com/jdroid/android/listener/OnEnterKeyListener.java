package com.jdroid.android.listener;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.concurrent.ExecutorUtils;

public abstract class OnEnterKeyListener implements OnKeyListener {
	
	private Boolean async;
	
	public OnEnterKeyListener() {
		this(true);
	}
	
	/**
	 * @param async
	 */
	public OnEnterKeyListener(Boolean async) {
		this.async = async;
	}
	
	/**
	 * @see android.view.View.OnKeyListener#onKey(android.view.View, int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKey(final View v, int keyCode, KeyEvent event) {
		
		if (matchKeyCode(keyCode) && (event.getAction() == KeyEvent.ACTION_UP)) {
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					AppUtils.hideSoftInput(v);
					onRun(v);
				}
			};
			if (async) {
				ExecutorUtils.execute(runnable);
			} else {
				runnable.run();
			}
			return true;
		}
		return false;
	}
	
	protected Boolean matchKeyCode(int keyCode) {
		return keyCode == KeyEvent.KEYCODE_ENTER;
	}
	
	/**
	 * Called when the search key on the keyword is released.
	 * 
	 * @param view The view the key has been dispatched to.
	 */
	public abstract void onRun(View view);
	
}
