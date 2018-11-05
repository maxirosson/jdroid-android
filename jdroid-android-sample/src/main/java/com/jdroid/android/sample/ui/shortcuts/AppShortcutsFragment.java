package com.jdroid.android.sample.ui.shortcuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.home.HomeItem;
import com.jdroid.android.shortcuts.AppShortcutsHelper;
import com.jdroid.android.utils.LocalizationUtils;

import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.graphics.drawable.IconCompat;

public class AppShortcutsFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.app_shortcuts_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.requestPinShortcutWithCallback).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (AppShortcutsHelper.isRequestPinShortcutSupported()) {
					HomeItem item = HomeItem.APP_SHORTCUTS;
					Intent intent = item.getIntent();
					intent.setAction(Intent.ACTION_VIEW);

					ShortcutInfoCompat.Builder shortcutInfoBuilder = new ShortcutInfoCompat.Builder(AbstractApplication.get(), item.name());
					shortcutInfoBuilder.setShortLabel(LocalizationUtils.getString(item.getNameResource()));
					shortcutInfoBuilder.setLongLabel(LocalizationUtils.getString(item.getNameResource()));
					shortcutInfoBuilder.setIcon(IconCompat.createWithResource(AbstractApplication.get(), item.getIconResource()));
					shortcutInfoBuilder.setIntent(intent);

					AppShortcutsHelper.requestPinShortcut(shortcutInfoBuilder.build());
				}
			}
		});

		findView(R.id.requestPinShortcutWithoutCallback).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppShortcutsHelper.isRequestPinShortcutSupported()) {
					HomeItem item = HomeItem.APP_SHORTCUTS;
					Intent intent = item.getIntent();
					intent.setAction(Intent.ACTION_VIEW);

					ShortcutInfoCompat.Builder shortcutInfoBuilder = new ShortcutInfoCompat.Builder(AbstractApplication.get(), item.name());
					shortcutInfoBuilder.setShortLabel(LocalizationUtils.getString(item.getNameResource()));
					shortcutInfoBuilder.setLongLabel(LocalizationUtils.getString(item.getNameResource()));
					shortcutInfoBuilder.setIcon(IconCompat.createWithResource(AbstractApplication.get(), item.getIconResource()));
					shortcutInfoBuilder.setIntent(intent);

					AppShortcutsHelper.requestPinShortcut(shortcutInfoBuilder.build(), null);
				}
			}
		});
	}
}
