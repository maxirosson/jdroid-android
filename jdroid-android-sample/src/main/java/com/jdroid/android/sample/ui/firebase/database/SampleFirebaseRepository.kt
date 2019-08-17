package com.jdroid.android.sample.ui.firebase.database

import com.jdroid.android.firebase.database.FirebaseDatabaseRepository

class SampleFirebaseRepository : FirebaseDatabaseRepository<SampleFirebaseEntity>() {

    // 	@Override
    // 	protected FirebaseAuthenticationStrategy createFirebaseAuthenticationStrategy() {
    // 		return new CustomTokenFirebaseAuthenticationStrategy() {
    // 			@Override
    // 			protected String getAuthToken() {
    // 				return AndroidApplication.get().getAppContext().getFirebaseAuthToken();
    // 			}
    // 		};
    // 	}

    override fun getPath(): String {
        return "samples"
    }

    override fun getEntityClass(): Class<SampleFirebaseEntity> {
        return SampleFirebaseEntity::class.java
    }
}
