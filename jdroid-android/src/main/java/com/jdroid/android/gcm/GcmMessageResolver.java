package com.jdroid.android.gcm;

import android.content.Intent;

/**
 * 
 * @author Maxi Rosson
 */
public interface GcmMessageResolver {
	
	public GcmMessage resolve(Intent intent);
	
}
