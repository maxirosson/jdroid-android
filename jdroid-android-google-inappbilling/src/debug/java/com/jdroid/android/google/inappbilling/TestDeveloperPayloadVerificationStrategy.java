package com.jdroid.android.google.inappbilling;

import com.jdroid.android.google.inappbilling.client.DeveloperPayloadVerificationStrategy;
import com.jdroid.android.google.inappbilling.client.Product;

public class TestDeveloperPayloadVerificationStrategy implements DeveloperPayloadVerificationStrategy {
	
	public static final String VALID_TEST_VERIFICATION_ENABLED = "validTestVerificationEnabled";
	
	@Override
	public Boolean verify(Product product) {
		return true;
	}
}
