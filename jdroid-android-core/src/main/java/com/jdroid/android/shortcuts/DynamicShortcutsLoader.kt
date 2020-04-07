package com.jdroid.android.shortcuts

import android.content.pm.ShortcutInfo

import androidx.annotation.WorkerThread

interface DynamicShortcutsLoader {

    @WorkerThread
    fun loadDynamicShortcuts(): List<ShortcutInfo>
}
