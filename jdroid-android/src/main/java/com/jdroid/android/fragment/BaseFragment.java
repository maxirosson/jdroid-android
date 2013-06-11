package com.jdroid.android.fragment;

import org.slf4j.Logger;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.R;
import com.jdroid.android.ad.AdLoader;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class BaseFragment {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(BaseFragment.class);
	
	private Fragment fragment;
	
	public BaseFragment(Fragment fragment) {
		this.fragment = fragment;
	}
	
	public FragmentIf getFragmentIf() {
		return (FragmentIf)fragment;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		LOGGER.trace("Executing onCreate on " + fragment);
		fragment.setRetainInstance(getFragmentIf().shouldRetainInstance());
	}
	
	public Boolean shouldRetainInstance() {
		return true;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		LOGGER.trace("Executing onViewCreated on " + fragment);
		
		AdLoader.loadAd(fragment.getActivity(), (ViewGroup)(fragment.getView().findViewById(R.id.adViewContainer)),
			getFragmentIf().getAdSize());
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		LOGGER.trace("Executing onActivityCreated on " + fragment);
	}
	
	public void onStart() {
		LOGGER.trace("Executing onStart on " + fragment);
	}
	
	public void onResume() {
		LOGGER.trace("Executing onResume on " + fragment);
	}
	
	public void onPause() {
		LOGGER.trace("Executing onPause on " + fragment);
	}
	
	public void onStop() {
		LOGGER.trace("Executing onStop on " + fragment);
	}
	
	public void onDestroyView() {
		LOGGER.trace("Executing onDestroyView on " + fragment);
	}
	
	public void onDestroy() {
		LOGGER.trace("Executing onDestroy on " + fragment);
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
	
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			if (fragmentIf.goBackOnError(runtimeException)) {
				DefaultExceptionHandler.markAsGoBackOnError(runtimeException);
			} else {
				DefaultExceptionHandler.markAsNotGoBackOnError(runtimeException);
			}
			fragmentIf.dismissLoadingOnUIThread();
		}
		throw runtimeException;
	}
}
