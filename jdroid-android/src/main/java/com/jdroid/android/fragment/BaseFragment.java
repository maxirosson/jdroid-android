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
		onResumeUseCase(useCase, listener, UseCaseTrigger.MANUAL);
	}
	
	public void onResumeUseCase(final DefaultAbstractUseCase useCase, final DefaultUseCaseListener listener,
			final UseCaseTrigger useCaseTrigger) {
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				useCase.addListener(listener);
				if (useCase.isNotified()) {
					if (useCaseTrigger.equals(UseCaseTrigger.ALWAYS)) {
						useCase.run();
					}
				} else {
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
					} else if (useCase.isNotInvoked()
							&& (useCaseTrigger.equals(UseCaseTrigger.ONCE) || useCaseTrigger.equals(UseCaseTrigger.ALWAYS))) {
						useCase.run();
					}
				}
			}
		});
	}
	
	public enum UseCaseTrigger {
		MANUAL,
		ONCE,
		ALWAYS;
	}
	
	public void onPauseUseCase(final DefaultAbstractUseCase userCase, final DefaultUseCaseListener listener) {
		if (userCase != null) {
			userCase.removeListener(listener);
		}
	}
	
	public <E> E getArgument(String key) {
		return getArgument(key, null);
	}
	
	@SuppressWarnings("unchecked")
	public <E> E getArgument(String key, E defaultValue) {
		Bundle arguments = fragment.getArguments();
		E value = (arguments != null) && arguments.containsKey(key) ? (E)arguments.get(key) : null;
		return value != null ? value : defaultValue;
	}
}
