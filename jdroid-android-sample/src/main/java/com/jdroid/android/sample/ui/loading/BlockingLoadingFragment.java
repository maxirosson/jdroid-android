package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.usecases.SampleUseCase;
import com.jdroid.android.usecase.UseCaseHelper;
import com.jdroid.android.usecase.UseCaseTrigger;
import com.jdroid.android.usecase.listener.ActivityLoadingUseCaseListener;

public class BlockingLoadingFragment extends AbstractFragment {

	private SampleUseCase sampleUseCase;
	private ActivityLoadingUseCaseListener sampleUseCaseListener;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.blocking_loading_fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sampleUseCase = new SampleUseCase();
		sampleUseCase.setDelayInSeconds(5);
		sampleUseCaseListener = new ActivityLoadingUseCaseListener() {
			@Override
			protected FragmentIf getFragmentIf() {
				return BlockingLoadingFragment.this;
			}
		};
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Button fail = findView(R.id.fail);
		fail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sampleUseCase.setFail(true);
				UseCaseHelper.executeUseCase(sampleUseCase);
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		UseCaseHelper.registerUseCase(sampleUseCase, sampleUseCaseListener, UseCaseTrigger.ONCE);
	}

	@Override
	public void onStop() {
		super.onStop();
		UseCaseHelper.unregisterUseCase(sampleUseCase, sampleUseCaseListener);
	}
}
