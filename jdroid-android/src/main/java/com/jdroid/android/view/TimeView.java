package com.jdroid.android.view;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;
import com.jdroid.android.utils.AndroidDateUtils;
import com.jdroid.java.utils.IdGenerator;

/**
 * A {@link TextView} that displays the current time
 * 
 * @author Maxi Rosson
 */
public class TimeView extends TextView implements Callback {
	
	private static final int MESSAGE_CODE = IdGenerator.getIntId();
	private static final int HANDLER_DELAY = 1000;
	
	private Handler handler;
	private Boolean visible = false;
	
	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler = new Handler(this);
	}
	
	public TimeView(Context context) {
		this(context, null);
	}
	
	/**
	 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
	 */
	@Override
	public boolean handleMessage(Message msg) {
		updateTime();
		return true;
	}
	
	private void updateTime() {
		if ((visible != null) && (visible == true)) {
			if (isInEditMode()) {
				setText("09:34 PM");
			} else {
				setText(AndroidDateUtils.formatTime());
			}
			handler.sendMessageDelayed(Message.obtain(handler, MESSAGE_CODE), HANDLER_DELAY);
		} else {
			handler.removeMessages(MESSAGE_CODE);
		}
	}
	
	/**
	 * @see android.view.View#onWindowVisibilityChanged(int)
	 */
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		visible = visibility == VISIBLE;
		updateTime();
	}
	
	/**
	 * @see android.widget.TextView#onDetachedFromWindow()
	 */
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		visible = false;
		updateTime();
	}
	
}
