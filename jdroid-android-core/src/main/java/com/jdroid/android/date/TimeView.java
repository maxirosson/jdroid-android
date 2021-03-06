package com.jdroid.android.date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jdroid.java.utils.IdGenerator;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * A {@link TextView} that displays the current time
 */
public class TimeView extends AppCompatTextView implements Callback {

	private static final int MESSAGE_CODE = IdGenerator.INSTANCE.getIntId();
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

	@Override
	public boolean handleMessage(Message msg) {
		updateTime();
		return true;
	}

	@SuppressLint("SetTextI18n")
	private void updateTime() {
		if ((visible != null) && (visible)) {
			if (isInEditMode()) {
				setText("09:34 PM");
			} else {
				setText(AndroidDateUtils.INSTANCE.formatTime());
			}
			handler.sendMessageDelayed(Message.obtain(handler, MESSAGE_CODE), HANDLER_DELAY);
		} else {
			handler.removeMessages(MESSAGE_CODE);
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		visible = visibility == VISIBLE;
		updateTime();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		visible = false;
		updateTime();
	}

}
