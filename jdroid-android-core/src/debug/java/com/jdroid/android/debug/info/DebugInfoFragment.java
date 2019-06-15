package com.jdroid.android.debug.info;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.firebase.performance.FirebasePerformanceAppContext;
import com.jdroid.android.leakcanary.LeakCanaryHelper;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.strictmode.StrictModeHelper;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.android.utils.ScreenUtils;
import com.jdroid.java.collections.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;


public class DebugInfoFragment extends AbstractRecyclerFragment {

	private List<Pair<String, Object>> properties = Lists.newArrayList();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppContext appContext = AbstractApplication.get().getAppContext();

		properties.add(new Pair<>("Build Type", AppUtils.getBuildType()));
		properties.add(new Pair<>("Build Time", AppUtils.getBuildTime()));

		properties.add(new Pair<>("Installation Source", appContext.getInstallationSource()));

		properties.add(new Pair<>("Screen Width Dp", ScreenUtils.getScreenWidthDp()));
		properties.add(new Pair<>("Screen Height Dp", ScreenUtils.getScreenHeightDp()));
		properties.add(new Pair<>("Screen Density", ScreenUtils.getScreenDensity()));
		properties.add(new Pair<>("Screen Density DPI", ScreenUtils.getDensityDpi()));

		properties.add(new Pair<>("Git Branch", AbstractApplication.get().getGitContext().getBranch()));
		properties.add(new Pair<>("Git Sha", AbstractApplication.get().getGitContext().getSha()));

		properties.add(new Pair<>("Application Id", AppUtils.getApplicationId()));
		properties.add(new Pair<>("Package Name", AbstractApplication.get().getManifestPackageName()));
		properties.add(new Pair<>("Version Name", AppUtils.getVersionName()));
		properties.add(new Pair<>("Version Code", AppUtils.getVersionCode()));
		properties.add(new Pair<>("SO API Level", AndroidUtils.getApiLevel()));
		properties.add(new Pair<>("Installer Package Name", AppUtils.getInstallerPackageName()));

		properties.add(new Pair<>("Device Manufacturer", DeviceUtils.getDeviceManufacturer()));
		properties.add(new Pair<>("Device Model", DeviceUtils.getDeviceModel()));
		properties.add(new Pair<>("Device Year Class", DeviceUtils.getDeviceYearClass()));

		properties.add(new Pair<>("Network Operator Name", DeviceUtils.getNetworkOperatorName()));
		properties.add(new Pair<>("Sim Operator Name", DeviceUtils.getSimOperatorName()));

		properties.add(new Pair<>("App Loads", UsageStats.getAppLoads()));

		properties.add(new Pair<>("Strict Mode Enabled", StrictModeHelper.isStrictModeEnabled()));
		properties.add(new Pair<>("Leak Canary Enabled", LeakCanaryHelper.INSTANCE.isLeakCanaryEnabled()));

		properties.add(new Pair<>("Firebase Performance Enabled", FirebasePerformanceAppContext.INSTANCE.isFirebasePerformanceEnabled()));

		for (DebugInfoAppender each : DebugInfoHelper.getDebugInfoAppenders()) {
			properties.addAll(each.getDebugInfoProperties());
		}

		properties.addAll(AbstractApplication.get().getDebugContext().getCustomDebugInfoProperties());
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Collections.sort(properties, new Comparator<Pair<String, Object>>() {
			@Override
			public int compare(Pair<String, Object> o1, Pair<String, Object> o2) {
				return o1.first.compareTo(o2.first);
			}
		});

		setAdapter(new RecyclerViewAdapter(new PairItemRecyclerViewType() {

			@Override
			protected Boolean isTextSelectable() {
				return true;
			}

			@NonNull
			@Override
			public RecyclerViewContainer getRecyclerViewContainer() {
				return DebugInfoFragment.this;
			}
		}, properties));
	}
}
