package com.jdroid.android.loading;

import com.jdroid.android.R;
import com.jdroid.android.fragment.FragmentIf;

public class NonBlockingLoading implements FragmentLoading {
	
	private Boolean isNonBlockingLoadingDisplayedByDefault = true;
	
	/**
	 * @see com.jdroid.android.loading.FragmentLoading#onViewCreated(com.jdroid.android.fragment.FragmentIf)
	 */
	@Override
	public void onViewCreated(FragmentIf fragmentIf) {
		// Do nothing
	}
	
	/**
	 * @see com.jdroid.android.loading.FragmentLoading#show(com.jdroid.android.fragment.FragmentIf)
	 */
	@Override
	public void show(final FragmentIf fragmentIf) {
		LoadingLayout loadingLayout = fragmentIf.findView(R.id.loadingLayout);
		loadingLayout.setLoading(isNonBlockingLoadingDisplayedByDefault);
		loadingLayout.showLoading(fragmentIf);
	}
	
	/**
	 * @see com.jdroid.android.loading.FragmentLoading#dismiss(com.jdroid.android.fragment.FragmentIf)
	 */
	@Override
	public void dismiss(final FragmentIf fragmentIf) {
		LoadingLayout loadingLayout = fragmentIf.findView(R.id.loadingLayout);
		loadingLayout.dismissLoading(fragmentIf);
	}
	
	/**
	 * @param isNonBlockingLoadingDisplayedByDefault the isNonBlockingLoadingDisplayedByDefault to set
	 */
	public void setIsNonBlockingLoadingDisplayedByDefault(Boolean isNonBlockingLoadingDisplayedByDefault) {
		this.isNonBlockingLoadingDisplayedByDefault = isNonBlockingLoadingDisplayedByDefault;
	}
}
