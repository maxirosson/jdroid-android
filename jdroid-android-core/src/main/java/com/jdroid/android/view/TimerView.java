package com.jdroid.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimerView extends AppCompatTextView implements Handler.Callback {

	private static final int MESSAGE_CODE = IdGenerator.getIntId();
	private static final long HANDLER_DELAY = TimeUnit.SECONDS.toMillis(1);

	private static final Logger LOGGER = LoggerUtils.getLogger(TimerView.class);

	private Handler handler;
	private Boolean visible = false;

	public enum Status {
		INITIAL,
		STARTED,
		STOPPED,
		PAUSED;
	}

	private long durationBeforePause = 0L;
	private long startTime = 0L;
	private long stopTime = 0L;
	private Status status = Status.INITIAL;

	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler = new Handler(this);
	}

	public TimerView(Context context) {
		this(context, null);
	}

	@Override
	public boolean handleMessage(Message msg) {
		updateTime();
		return true;
	}

	@SuppressLint("SetTextI18n")
	private void updateTime() {
		if (visible != null && visible) {
			setText(formatDuration(getValue()));
			if (status.equals(Status.STARTED)) {
				handler.sendMessageDelayed(Message.obtain(handler, MESSAGE_CODE), HANDLER_DELAY);
			} else {
				handler.removeMessages(MESSAGE_CODE);
			}
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

	public void start() {
		if (status.equals(Status.INITIAL)) {
			LOGGER.info("Starting timer");
			durationBeforePause = 0L;
			startTime = DateUtils.nowMillis();
			stopTime = 0L;
			status = Status.STARTED;
			updateTime();
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("The TimerView should be on initial status to start. Current status: " + status);
		}
	}

	public void pause() {
		if (status.equals(Status.STARTED)) {
			LOGGER.info("Pausing timer");
			stopTime = DateUtils.nowMillis();
			if (startTime > 0) {
				durationBeforePause = durationBeforePause + stopTime - startTime;
				startTime = 0L;
				stopTime = 0L;
			}
			status = Status.PAUSED;
			updateTime();
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("The TimerView should be on started status to pause. Current status: " + status);
		}
	}

	public void unpause() {
		if (status.equals(Status.PAUSED)) {
			LOGGER.info("Unpausing timer");
			startTime = DateUtils.nowMillis();
			stopTime = 0L;
			status = Status.STARTED;
			updateTime();
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("The TimerView should be on paused status to unpause. Current status: " + status);
		}
	}

	public void stop() {
		if (status.equals(Status.STARTED)) {
			LOGGER.info("Stopped timer");
			stopTime = DateUtils.nowMillis();
			status = Status.STOPPED;
			updateTime();
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("The TimerView should be on started status to stop. Current status: " + status);
		}
	}

	public void reset() {
		LOGGER.info("Reseted timer");
		durationBeforePause = 0L;
		startTime = 0L;
		stopTime = 0L;
		status = Status.INITIAL;
		updateTime();
	}

	public Long getValue() {
		if (startTime > 0 && stopTime > 0) {
			return durationBeforePause + stopTime - startTime;
		} else if (startTime > 0) {
			return durationBeforePause + DateUtils.nowMillis() - startTime;
		} else {
			return 0L;
		}
	}

	private String formatDuration(long duration) {
		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - (hours * 60);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - (hours * 60 * 60) - (minutes * 60);

		StringBuilder builder = new StringBuilder();
		builder.append(String.format(Locale.getDefault(), "%1$02d", minutes));
		builder.append(":");
		builder.append(String.format(Locale.getDefault(), "%1$02d", seconds));
		return builder.toString();
	}

	public Status getStatus() {
		return status;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		return new SavedState(superState, durationBeforePause, startTime, stopTime, status);
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState)state;
		super.onRestoreInstanceState(ss.getSuperState());
		durationBeforePause = ss.durationBeforePause;
		startTime = ss.startTime;
		stopTime = ss.stopTime;
		status = ss.status;
	}

	/**
	 * Class for managing state storing/restoring.
	 */
	private static class SavedState extends BaseSavedState {

		private Long durationBeforePause;
		private Long startTime;
		private Long stopTime;
		private Status status;

		private SavedState(Parcelable superState, Long durationBeforePause, Long startTime, Long stopTime, Status status) {
			super(superState);
			this.durationBeforePause = durationBeforePause;
			this.startTime = startTime;
			this.stopTime = stopTime;
			this.status = status;
		}

		private SavedState(Parcel in) {
			super(in);
			durationBeforePause = in.readLong();
			startTime = in.readLong();
			stopTime = in.readLong();
			status = (Status)in.readSerializable();
		}

		@Override
		public void writeToParcel(@NonNull Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeLong(durationBeforePause);
			dest.writeLong(startTime);
			dest.writeLong(stopTime);
			dest.writeSerializable(status);
		}

		@SuppressWarnings({ "hiding", "unused" })
		public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}