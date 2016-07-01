package com.jdroid.android.sample.ui.toasts;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.concurrent.ExecutorUtils;

public class ToastsFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.toasts_fragment;
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.displayToastFromUIThread).setOnClickListener(v -> ToastUtils.showToast(R.string.toastFromUIThread));

		findView(R.id.displayToastFromWorkerThread).setOnClickListener(v ->
				ExecutorUtils.execute(() -> ToastUtils.showToastOnUIThread(R.string.toastFromWorkerThread)));
	}
}
