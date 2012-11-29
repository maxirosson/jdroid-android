package com.jdroid.android.fragment;

import roboguice.RoboGuice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.R;
import com.jdroid.android.ad.AdLoader;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;
import com.jdroid.java.utils.ExecutorUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class BaseFragment {
	
	private final static String TAG = BaseFragment.class.getSimpleName();
	
	private Fragment fragment;
	
	public BaseFragment(Fragment fragment) {
		this.fragment = fragment;
	}
	
	public FragmentIf getFragmentIf() {
		return (FragmentIf)fragment;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "Executing onCreate on " + fragment);
		RoboGuice.getInjector(fragment.getActivity()).injectMembersWithoutViews(fragment);
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.v(TAG, "Executing onViewCreated on " + fragment);
		RoboGuice.getInjector(fragment.getActivity()).injectViewMembers(fragment);
		
		AdLoader.loadAd(fragment.getActivity(), (ViewGroup)(fragment.getView().findViewById(R.id.adViewContainer)),
			getFragmentIf().getAdSize());
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.v(TAG, "Executing onActivityCreated on " + fragment);
	}
	
	public void onStart() {
		Log.v(TAG, "Executing onStart on " + fragment);
	}
	
	public void onResume() {
		Log.v(TAG, "Executing onResume on " + fragment);
	}
	
	public void onPause() {
		Log.v(TAG, "Executing onPause on " + fragment);
	}
	
	public void onStop() {
		Log.v(TAG, "Executing onStop on " + fragment);
	}
	
	public void onDestroyView() {
		Log.v(TAG, "Executing onDestroyView on " + fragment);
	}
	
	public void onDestroy() {
		Log.v(TAG, "Executing onDestroy on " + fragment);
	}
	
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		onResumeUseCase(useCase, listener, false);
	}
	
	public void onResumeUseCase(final DefaultAbstractUseCase useCase, final DefaultUseCaseListener listener,
			final Boolean initUseCase) {
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				useCase.addListener(listener);
				if (!useCase.isNotified()) {
					if (useCase.isInProgress()) {
						listener.onStartUseCase();
					} else if (useCase.isFinishSuccessful()) {
						listener.onFinishUseCase();
						useCase.markAsNotified();
					} else if (useCase.isFinishFailed()) {
						try {
							listener.onFinishFailedUseCase(useCase.getRuntimeException());
						} finally {
							useCase.markAsNotified();
						}
					} else if (useCase.isNotInvoked() && initUseCase) {
						useCase.run();
					}
				}
			}
		});
	}
	
	public void onPauseUseCase(final DefaultAbstractUseCase userCase, final DefaultUseCaseListener listener) {
		if (userCase != null) {
			userCase.removeListener(listener);
		}
	}
}
