package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;

import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ToastUtils;

public class CustomActivityLoadingFragment extends BlockingLoadingFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActivityIf().setLoading(new ActivityLoading() {

			@Override
			public void show(ActivityIf activityIf) {
				ToastUtils.showToast(R.string.showLoading);

			}

			@Override
			public void dismiss(ActivityIf activityIf) {
				ToastUtils.showToast(R.string.dismissLoading);
			}
		});
	}
}
