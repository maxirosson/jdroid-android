package com.jdroid.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.R;
import com.jdroid.android.ad.AdLoader;
import com.jdroid.android.exception.DefaultExceptionHandler;

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
		fragment.setRetainInstance(getFragmentIf().shouldRetainInstance());
	}
	
	public Boolean shouldRetainInstance() {
		return true;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.v(TAG, "Executing onViewCreated on " + fragment);
		
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
		if (getFragmentIf().goBackOnError()) {
			DefaultExceptionHandler.markAsGoBackOnError(runtimeException);
		} else {
			DefaultExceptionHandler.markAsNotGoBackOnError(runtimeException);
		}
		getFragmentIf().dismissLoadingOnUIThread();
		throw runtimeException;
	}
}
