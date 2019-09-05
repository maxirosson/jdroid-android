package com.jdroid.android.google.inappbilling

import android.preference.CheckBoxPreference
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceGroup
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.android.debug.PreferencesAppender
import com.jdroid.android.google.inappbilling.client.SimpleDeveloperPayloadVerificationStrategy

class InAppBillingDebugPrefsAppender : PreferencesAppender() {

    override fun getNameResId(): Int {
        return R.string.jdroid_inAppBillingSettings
    }

    override fun initPreferences(activity: AppCompatActivity, preferenceGroup: PreferenceGroup) {

        var checkBoxPreference = CheckBoxPreference(activity)
        checkBoxPreference.key = InAppBillingContext.STATIC_RESPONSES_ENABLED
        checkBoxPreference.setTitle(R.string.jdroid_inAppBillingStaticResponsesTitle)
        checkBoxPreference.setSummary(R.string.jdroid_inAppBillingStaticResponsesDescription)
        preferenceGroup.addPreference(checkBoxPreference)

        checkBoxPreference = CheckBoxPreference(activity)
        checkBoxPreference.key = TestDeveloperPayloadVerificationStrategy.VALID_TEST_VERIFICATION_ENABLED
        checkBoxPreference.setTitle(R.string.jdroid_inAppBillingTestVerificationTitle)
        checkBoxPreference.setSummary(R.string.jdroid_inAppBillingTestVerificationDescription)
        checkBoxPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                InAppBillingAppModule.get().developerPayloadVerificationStrategy = TestDeveloperPayloadVerificationStrategy()
            } else {
                InAppBillingAppModule.get().developerPayloadVerificationStrategy = SimpleDeveloperPayloadVerificationStrategy()
            }
            true
        }
        preferenceGroup.addPreference(checkBoxPreference)

        // Purchased products
        val purchasedProductTypes = InAppBillingAppModule.get().inAppBillingContext!!.purchasedProductTypes
        if (purchasedProductTypes.isNotEmpty()) {
            val preference = ListPreference(activity)
            preference.setTitle(R.string.jdroid_inAppBillingPurchasedProductTypeTitle)
            preference.setDialogTitle(R.string.jdroid_inAppBillingPurchasedProductTypeTitle)
            preference.setSummary(R.string.jdroid_inAppBillingPurchasedProductTypeTitle)
            val entries = mutableListOf<CharSequence>()
            for (each in InAppBillingAppModule.get().inAppBillingContext!!.purchasedProductTypes) {
                entries.add(each.getProductId())
            }
            preference.entries = entries.toTypedArray()
            preference.entryValues = entries.toTypedArray()
            preferenceGroup.addPreference(preference)
        }
    }

    override fun isEnabled(): Boolean {
        return InAppBillingAppModule.get() != null && (InAppBillingAppModule.get().inAppBillingContext!!.managedProductTypes.isNotEmpty() || !InAppBillingAppModule.get().inAppBillingContext!!.subscriptionsProductTypes.isEmpty())
    }

    override fun getSharedPreferencesName(): String? {
        return InAppBillingContext.SHARED_PREFERENCES_NAME
    }
}
