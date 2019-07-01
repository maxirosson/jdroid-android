package com.jdroid.android.about;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdroid.android.about.feedback.RateAppStats;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.recycler.AbstractRecyclerFragment;
import com.jdroid.android.recycler.FooterRecyclerViewType;
import com.jdroid.android.recycler.RecyclerViewAdapter;
import com.jdroid.android.recycler.RecyclerViewContainer;
import com.jdroid.android.recycler.RecyclerViewType;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.ExternalAppsUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;

import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AboutFragment extends AbstractRecyclerFragment {

	private List<Object> aboutItems = Lists.newArrayList();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Header
		aboutItems.add("");

		final String website = getWebsite();
		if (website != null) {
			aboutItems.add(new AboutItem(R.drawable.jdroid_ic_website_black_24dp, R.string.jdroid_website) {
				@Override
				public void onSelected(Activity activity) {
					ExternalAppsUtils.openUrl(website);
				}
			});
		}

		final String contactUsEmailAddress = getContactUsEmail();
		if (contactUsEmailAddress != null) {
			aboutItems.add(new AboutItem(R.drawable.jdroid_ic_contact_us_black_24dp, R.string.jdroid_contactUs) {

				@Override
				public void onSelected(Activity activity) {
					if (ExternalAppsUtils.openEmail(contactUsEmailAddress)) {
						AboutAppModule.Companion.get().getModuleAnalyticsSender().trackContactUs();
					} else {
						// TODO Improve this adding a toast or something
					}
				}
			});
		}

		if (AboutAppModule.Companion.get().getAboutContext().getSpreadTheLoveFragmentClass() != null) {
			aboutItems.add(new AboutItem(R.drawable.jdroid_ic_spread_the_love_black_24dp, R.string.jdroid_spreadTheLove) {

				@Override
				public void onSelected(Activity activity) {
					ActivityLauncher.startActivity(activity, SpreadTheLoveActivity.class);
				}
			});
		}
		aboutItems.add(new AboutItem(R.drawable.jdroid_ic_libraries_black_24dp, R.string.jdroid_libraries) {

			@Override
			public void onSelected(Activity activity) {
				ActivityLauncher.startActivity(activity, LibrariesActivity.class);
			}
		});

		RemoteConfigParameter privacyPolicyUrlRemoteConfigParameter = AbstractApplication.get().getAppContext().getPrivacyPolicyUrl();
		if (privacyPolicyUrlRemoteConfigParameter != null) {
			String privacyPolicyUrl = AbstractApplication.get().getRemoteConfigLoader().getString(privacyPolicyUrlRemoteConfigParameter);
			aboutItems.add(new AboutItem(R.drawable.jdroid_ic_privacy_policy_black_24dp, R.string.jdroid_privacyPolicy) {

				@Override
				public void onSelected(Activity activity) {
					ExternalAppsUtils.openUrl(privacyPolicyUrl);
				}
			});
		}

		if (AboutAppModule.Companion.get().getAboutContext().isBetaTestingEnabled()) {
			aboutItems.add(new AboutItem(R.drawable.jdroid_ic_beta_black_24dp, R.string.jdroid_beta) {

				@Override
				public void onSelected(Activity activity) {
					ExternalAppsUtils.openUrl(AboutAppModule.Companion.get().getAboutContext().getBetaTestingUrl());
				}
			});
		}
		aboutItems.addAll(getCustomAboutItems());
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(Lists.newArrayList(new HeaderRecyclerViewType(), new AboutRecyclerViewType()), aboutItems);
		if (rateAppViewEnabled()) {
			recyclerViewAdapter.setFooter(new AboutFooterRecyclerViewType());
		}
		setAdapter(recyclerViewAdapter);
	}

	protected String getWebsite() {
		return AbstractApplication.get().getAppContext().getWebsite();
	}

	protected String getContactUsEmail() {
		return AbstractApplication.get().getAppContext().getContactUsEmail();
	}

	protected List<AboutItem> getCustomAboutItems() {
		return Lists.newArrayList();
	}

	@MainThread
	protected Boolean rateAppViewEnabled() {
		return RateAppStats.displayRateAppView();
	}

	@Override
	protected boolean isDividerItemDecorationEnabled() {
		return true;
	}

	public class HeaderRecyclerViewType extends RecyclerViewType<String, HeaderItemHolder> {

		@Override
		protected Class<String> getItemClass() {
			return String.class;
		}

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.jdroid_about_header_view;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			HeaderItemHolder holder = new HeaderItemHolder(view);
			holder.appIcon = findView(view, R.id.appIcon);
			holder.appName = findView(view, R.id.appName);
			holder.version = findView(view, R.id.version);
			return holder;
		}

		@Override
		public void fillHolderFromItem(String item, HeaderItemHolder holder) {
			holder.appIcon.setImageResource(AbstractApplication.get().getLauncherIconResId());
			holder.appName.setText(AbstractApplication.get().getAppName());
			holder.version.setText(getString(R.string.jdroid_version, AppUtils.getVersionName()));
			if (AbstractApplication.get().getAppContext().isDebugSettingsEnabled()) {
				holder.appIcon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AbstractApplication.get().getDebugContext().launchActivityDebugSettingsActivity(getActivity());
					}
				});
			}
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return AboutFragment.this;
		}
	}

	public static class HeaderItemHolder extends RecyclerView.ViewHolder {

		protected ImageView appIcon;
		protected TextView appName;
		protected TextView version;

		public HeaderItemHolder(View itemView) {
			super(itemView);
		}
	}

	public class AboutFooterRecyclerViewType extends FooterRecyclerViewType {

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.jdroid_about_footer_view;
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return AboutFragment.this;
		}
	}

	public class AboutRecyclerViewType extends RecyclerViewType<AboutItem, AboutItemHolder> {

		@Override
		protected Class<AboutItem> getItemClass() {
			return AboutItem.class;
		}

		@Override
		protected Integer getLayoutResourceId() {
			return R.layout.jdroid_default_item;
		}

		@Override
		public RecyclerView.ViewHolder createViewHolderFromView(View view) {
			AboutItemHolder holder = new AboutItemHolder(view);
			holder.image = findView(view, R.id.image);
			holder.name = findView(view, R.id.name);
			return holder;
		}

		@Override
		public void fillHolderFromItem(AboutItem item, AboutItemHolder holder) {
			holder.image.setImageResource(item.getIconResId());
			holder.name.setText(item.getNameResId());
		}

		@Override
		public void onItemSelected(AboutItem item, View view) {
			item.onSelected(getActivity());
		}

		@NonNull
		@Override
		public RecyclerViewContainer getRecyclerViewContainer() {
			return AboutFragment.this;
		}
	}

	public static class AboutItemHolder extends RecyclerView.ViewHolder {

		protected ImageView image;
		protected TextView name;

		public AboutItemHolder(View itemView) {
			super(itemView);
		}
	}
}
