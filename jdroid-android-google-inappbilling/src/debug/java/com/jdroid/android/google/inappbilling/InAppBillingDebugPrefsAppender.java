package com.jdroid.android.google.inappbilling;

import android.app.Activity;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceGroup;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.google.inappbilling.client.ProductType;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class InAppBillingDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_inAppBillingSettings;
	}

	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {

		CheckBoxPreference checkBoxPreference = new CheckBoxPreference(activity);
		checkBoxPreference.setKey(InAppBillingContext.STATIC_RESPONSES_ENABLED);
		checkBoxPreference.setTitle(R.string.jdroid_inAppBillingStaticResponsesTitle);
		checkBoxPreference.setSummary(R.string.jdroid_inAppBillingStaticResponsesDescription);
		preferenceGroup.addPreference(checkBoxPreference);

		// Purchased products
		List<ProductType> purchasedProductTypes = InAppBillingAppModule.get().getInAppBillingContext().getPurchasedProductTypes();
		if (!purchasedProductTypes.isEmpty()) {
			ListPreference preference = new ListPreference(activity);
			preference.setTitle(R.string.jdroid_inAppBillingPurchasedProductTypeTitle);
			preference.setDialogTitle(R.string.jdroid_inAppBillingPurchasedProductTypeTitle);
			preference.setSummary(R.string.jdroid_inAppBillingPurchasedProductTypeTitle);
			List<CharSequence> entries = Lists.newArrayList();
			for (ProductType each : InAppBillingAppModule.get().getInAppBillingContext().getPurchasedProductTypes()) {
				entries.add(each.getProductId());
			}
			preference.setEntries(entries.toArray(new CharSequence[0]));
			preference.setEntryValues(entries.toArray(new CharSequence[0]));
			preferenceGroup.addPreference(preference);
		}
	}

	@Override
	public Boolean isEnabled() {
		return InAppBillingAppModule.get() != null && (!InAppBillingAppModule.get().getInAppBillingContext().getManagedProductTypes().isEmpty()
			|| !InAppBillingAppModule.get().getInAppBillingContext().getSubscriptionsProductTypes().isEmpty());
	}

	@Override
	public String getSharedPreferencesName() {
		return InAppBillingContext.SHARED_PREFERENCES_NAME;
	}
}
