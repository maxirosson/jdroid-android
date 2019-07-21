package com.jdroid.android.firebase.fcm

import android.preference.ListPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceChangeListener
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.RemoteMessage
import com.jdroid.android.debug.PreferencesAppender
import com.jdroid.java.collections.Lists
import com.jdroid.java.collections.Maps

class FcmDebugPrefsAppender : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_fcmSettings
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {
        val preference = ListPreference(activity)
        preference.setTitle(R.string.jdroid_emulateFcmMessageTitle)
        preference.setDialogTitle(R.string.jdroid_emulateFcmMessageTitle)
        preference.setSummary(R.string.jdroid_emulateFcmMessageDescription)
        val entries = Lists.newArrayList<CharSequence>()
        for (entry in fcmMessagesMap.keys) {
            entries.add(entry.javaClass.simpleName)
        }
        preference.entries = entries.toTypedArray()
        preference.entryValues = entries.toTypedArray()
        preference.isPersistent = false
        preference.onPreferenceChangeListener = OnPreferenceChangeListener { _, newValue ->
            for ((key, value) in fcmMessagesMap) {
                if (key.javaClass.simpleName == newValue.toString()) {
                    val builder = RemoteMessage.Builder("to")
                    if (value != null) {
                        for ((key1, value1) in value) {
                            builder.addData(key1, value1)
                        }
                    }
                    key.handle(builder.build())
                    break
                }
            }
            false
        }
        preferenceGroup.addPreference(preference)

        val registerDevicePreference = Preference(activity)
        registerDevicePreference.setTitle(R.string.jdroid_registerDeviceTitle)
        registerDevicePreference.setSummary(R.string.jdroid_registerDeviceTitle)
        registerDevicePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            AbstractFcmAppModule.get().startFcmRegistration(true)
            true
        }
        preferenceGroup.addPreference(registerDevicePreference)
    }

    override fun isEnabled(): Boolean {
        return fcmMessagesMap.isNotEmpty()
    }

    companion object {

        private val fcmMessagesMap = Maps.newHashMap<FcmMessage, Map<String, String>>()

        fun addFcmMessage(fcmMessage: FcmMessage, data: Map<String, String> = mapOf()) {
            fcmMessagesMap[fcmMessage] = data
        }
    }
}
