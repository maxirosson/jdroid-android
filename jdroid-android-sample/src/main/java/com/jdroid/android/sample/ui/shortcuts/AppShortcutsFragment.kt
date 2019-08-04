package com.jdroid.android.sample.ui.shortcuts

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.graphics.drawable.IconCompat
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.sample.ui.home.HomeItem
import com.jdroid.android.shortcuts.AppShortcutsHelper
import com.jdroid.android.utils.LocalizationUtils

class AppShortcutsFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.app_shortcuts_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.requestPinShortcutWithCallback).setOnClickListener {
            if (AppShortcutsHelper.isRequestPinShortcutSupported()) {
                val item = HomeItem.APP_SHORTCUTS
                val intent = item.getIntent()
                intent.action = Intent.ACTION_VIEW

                val shortcutInfoBuilder = ShortcutInfoCompat.Builder(AbstractApplication.get(), item.name)
                shortcutInfoBuilder.setShortLabel(LocalizationUtils.getString(item.getNameResource())!!)
                shortcutInfoBuilder.setLongLabel(LocalizationUtils.getString(item.getNameResource())!!)
                shortcutInfoBuilder.setIcon(IconCompat.createWithResource(AbstractApplication.get(), item.getIconResource()))
                shortcutInfoBuilder.setIntent(intent)

                AppShortcutsHelper.requestPinShortcut(shortcutInfoBuilder.build())
            }
        }

        findView<View>(R.id.requestPinShortcutWithoutCallback).setOnClickListener {
            if (AppShortcutsHelper.isRequestPinShortcutSupported()) {
                val item = HomeItem.APP_SHORTCUTS
                val intent = item.getIntent()
                intent.action = Intent.ACTION_VIEW

                val shortcutInfoBuilder = ShortcutInfoCompat.Builder(AbstractApplication.get(), item.name)
                shortcutInfoBuilder.setShortLabel(LocalizationUtils.getString(item.getNameResource())!!)
                shortcutInfoBuilder.setLongLabel(LocalizationUtils.getString(item.getNameResource())!!)
                shortcutInfoBuilder.setIcon(IconCompat.createWithResource(AbstractApplication.get(), item.getIconResource()))
                shortcutInfoBuilder.setIntent(intent)

                AppShortcutsHelper.requestPinShortcut(shortcutInfoBuilder.build(), null)
            }
        }
    }
}
