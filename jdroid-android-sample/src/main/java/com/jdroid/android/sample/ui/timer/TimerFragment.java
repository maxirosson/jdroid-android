package com.jdroid.android.sample.ui.timer;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.view.TimerView;

public class TimerFragment extends AbstractFragment {

	private TimerView timerView;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.timer_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		timerView = findView(R.id.timer);

		findView(R.id.start).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timerView.start();
			}
		});
		findView(R.id.stop).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timerView.stop();
			}
		});
		findView(R.id.reset).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timerView.reset();
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

		if (timerView.getStatus().equals(TimerView.Status.PAUSED)) {
			timerView.unpause();
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		if (timerView.getStatus().equals(TimerView.Status.STARTED)) {
			timerView.pause();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (timerView.getStatus().equals(TimerView.Status.STARTED)) {
			timerView.pause();
		}
	}
}
