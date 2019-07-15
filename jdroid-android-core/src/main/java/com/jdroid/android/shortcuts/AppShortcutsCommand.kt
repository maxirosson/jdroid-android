package com.jdroid.android.shortcuts

import android.content.Context
import android.os.Bundle

import com.jdroid.android.firebase.jobdispatcher.ServiceCommand

class AppShortcutsCommand : ServiceCommand() {

    override fun start() {
        if (AppShortcutsHelper.isDynamicAppShortcutsSupported() && AppShortcutsHelper.getDynamicShortcutsLoader() != null) {
            super.start()
        }
    }

    override fun execute(context: Context, bundle: Bundle): Boolean {
        val dynamicShortcutsLoader = AppShortcutsHelper.getDynamicShortcutsLoader()
        AppShortcutsHelper.setDynamicShortcuts(dynamicShortcutsLoader.loadDynamicShortcuts())
        return false
    }
}
