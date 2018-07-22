package com.jdroid.android.debug.info;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.strictmode.StrictModeHelper;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.android.utils.ScreenUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.StringUtils;

import java.util.List;

public class DebugInfoFragment extends AbstractRecyclerFragment {

	private List<Pair<String, Object>> properties = Lists.newArrayList();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppContext appContext = AbstractApplication.get().getAppContext();

		properties.add(new Pair<String, Object>("Build Type", AppUtils.getBuildType()));
		properties.add(new Pair<String, Object>("Build Time", AppUtils.getBuildTime()));

		properties.add(new Pair<String, Object>("Installation Source", appContext.getInstallationSource()));

		properties.add(new Pair<String, Object>("Screen Width Dp", ScreenUtils.getScreenWidthDp()));
		properties.add(new Pair<String, Object>("Screen Height Dp", ScreenUtils.getScreenHeightDp()));
		properties.add(new Pair<String, Object>("Screen Density", ScreenUtils.getScreenDensity()));
		properties.add(new Pair<String, Object>("Screen Density DPI", ScreenUtils.getDensityDpi()));

		properties.add(new Pair<String, Object>("Git Branch", AbstractApplication.get().getGitContext().getBranch()));
		properties.add(new Pair<String, Object>("Git Sha", AbstractApplication.get().getGitContext().getSha()));

		properties.add(new Pair<String, Object>("Application Id", AppUtils.getApplicationId()));
		properties.add(new Pair<String, Object>("Package Name", AbstractApplication.get().getManifestPackageName()));
		properties.add(new Pair<String, Object>("Version Name", AppUtils.getVersionName()));
		properties.add(new Pair<String, Object>("Version Code", AppUtils.getVersionCode()));
		properties.add(new Pair<String, Object>("SO API Level", AndroidUtils.getApiLevel()));

		properties.add(new Pair<String, Object>("Device Manufacturer", DeviceUtils.getDeviceManufacturer()));
		properties.add(new Pair<String, Object>("Device Model", DeviceUtils.getDeviceModel()));
		properties.add(new Pair<String, Object>("Device Year Class", DeviceUtils.getDeviceYearClass()));

		properties.add(new Pair<String, Object>("Network Operator Name", DeviceUtils.getNetworkOperatorName()));
		properties.add(new Pair<String, Object>("Sim Operator Name", DeviceUtils.getSimOperatorName()));

		properties.add(new Pair<String, Object>("App Loads", UsageStats.getAppLoads()));
		
		properties.add(new Pair<String, Object>("Strict Mode Enabled", StrictModeHelper.isStrictModeEnabled()));

		for (DebugInfoAppender each : DebugInfoHelper.getDebugInfoAppenders()) {
			properties.addAll(each.getDebugInfoProperties());
		}
		
		properties.addAll(AbstractApplication.get().getDebugContext().getCustomDebugInfoProperties());
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		List<Pair<String, Object>> filteredProperties = Lists.newArrayList();
		for(Pair<String, Object> each: properties) {
			if (each.second != null && StringUtils.isNotBlank(each.second.toString())) {
				filteredProperties.add(each);
			}
		}
		setAdapter(new RecyclerViewAdapter(new PairItemRecyclerViewType() {
			
			@Override
			protected Boolean isTextSelectable() {
				return true;
			}
			
			@Override
			public AbstractRecyclerFragment getAbstractRecyclerFragment() {
				return DebugInfoFragment.this;
			}
		}, filteredProperties));
	}
}
