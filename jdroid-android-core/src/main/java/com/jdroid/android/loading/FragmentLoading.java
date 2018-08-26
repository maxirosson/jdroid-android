package com.jdroid.android.loading;

import com.jdroid.android.fragment.FragmentIf;

public interface FragmentLoading {

	public void onViewCreated(final FragmentIf fragmentIf);

	public void show(FragmentIf fragmentIf);

	public void dismiss(FragmentIf fragmentIf);

}
