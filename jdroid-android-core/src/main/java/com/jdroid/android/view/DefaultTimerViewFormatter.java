package com.jdroid.android.view;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DefaultTimerViewFormatter implements TimerViewFormatter {

	@Override
	public String formatDuration(long duration) {
		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

		StringBuilder builder = new StringBuilder();
		if (hours > 0) {
			builder.append(String.format(Locale.getDefault(), "%1$02d", hours));
			builder.append(":");
		}
		builder.append(String.format(Locale.getDefault(), "%1$02d", minutes));
		builder.append(":");
		builder.append(String.format(Locale.getDefault(), "%1$02d", seconds));
		return builder.toString();
	}

}
