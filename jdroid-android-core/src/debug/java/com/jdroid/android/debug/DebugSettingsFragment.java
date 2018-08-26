package com.jdroid.android.debug;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class DebugSettingsFragment extends AbstractRecyclerFragment {

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		DebugContext debugContext = AbstractApplication.get().getDebugContext();

		List<PreferencesAppender> appenders = Lists.newArrayList();
		addAppender(appenders, debugContext.createServersDebugPrefsAppender());
		addAppender(appenders, debugContext.createHttpMocksDebugPrefsAppender());
		addAppender(appenders, debugContext.createNavDrawerDebugPrefsAppender());
		addAppender(appenders, debugContext.createHttpCacheDebugPrefsAppender());
		addAppender(appenders, debugContext.createExceptionHandlingDebugPrefsAppender());
		addAppender(appenders, debugContext.createInfoDebugPrefsAppender());
		addAppender(appenders, debugContext.createUsageStatsDebugPrefsAppender());
		addAppender(appenders, debugContext.createUriMapperPrefsAppender());
		addAppender(appenders, debugContext.createNotificationsDebugPrefsAppender());

		for (PreferencesAppender preferencesAppender : DebugSettingsHelper.getPreferencesAppenders()) {
			addAppender(appenders, preferencesAppender);
		}

		appenders.addAll(debugContext.getCustomPreferencesAppenders());

		setAdapter(new RecyclerViewAdapter(new PreferencesAppenderRecyclerViewType(), Lists.newArrayList(appenders)));
	}

	private void addAppender(List<PreferencesAppender> appenders, PreferencesAppender preferencesAppender) {
		if (preferencesAppender != null && preferencesAppender.isEnabled()) {
			appenders.add(preferencesAppender);
		}
	}

	public class PreferencesAppenderRecyclerViewType extends RecyclerViewType<PreferencesAppender, PreferenceAppenderHolder> {

		@Override
		protected Class getItemClass() {
			return PreferencesAppender.class;
		}

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.jdroid_default_item;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			PreferenceAppenderHolder holder = new PreferenceAppenderHolder(view);
			holder.name = findView(view, R.id.name);
			return holder;
		}

		@Override
		public void fillHolderFromItem(PreferencesAppender item, PreferenceAppenderHolder holder) {
			holder.name.setText(item.getNameResId());
		}

		@Override
		public void onItemSelected(PreferencesAppender item, View view) {
			PreferenceAppenderActivity.startActivity(getActivity(), item);
		}

		@Override
		public AbstractRecyclerFragment getAbstractRecyclerFragment() {
			return DebugSettingsFragment.this;
		}
	}

	public static class PreferenceAppenderHolder extends RecyclerView.ViewHolder {

		protected TextView name;

		public PreferenceAppenderHolder(View itemView) {
			super(itemView);
		}
	}
}