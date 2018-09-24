package com.jdroid.android.sample.ui.navdrawer;

import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.sample.R;

public class LeftSampleNavDrawer extends NavDrawer {

	public LeftSampleNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		super(activity, appBar);
	}

	@Override
	public View createContentView() {
		return getActivity().findViewById(R.id.drawer);
	}
}
