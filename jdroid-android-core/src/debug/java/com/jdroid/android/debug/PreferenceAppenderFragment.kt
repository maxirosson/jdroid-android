package com.jdroid.android.debug

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.jdroid.android.R
import com.jdroid.android.fragment.AbstractPreferenceFragment
import com.jdroid.android.permission.PermissionHelper

class PreferenceAppenderFragment : AbstractPreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.jdroid_debug_preferences)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferencesAppender = getArgument<PreferencesAppender>(PreferenceAppenderActivity.APPENDER_EXTRA)!!
        activity.setTitle(preferencesAppender.getNameResId())

        val sharedPreferencesName = preferencesAppender.getSharedPreferencesName()
        if (sharedPreferencesName != null) {
            preferenceManager.sharedPreferencesName = sharedPreferencesName
        }

        val permissionHelpers = mutableListOf<PermissionHelper>()
        for (each in preferencesAppender.getRequiredPermissions()) {
            permissionHelpers.add(PermissionHelper(activity as FragmentActivity, each, 1))
        }
        preferencesAppender.initPreferences(activity as AppCompatActivity, preferenceScreen)

        for (each in permissionHelpers) {
            each.checkPermission(true)
        }
    }
}
