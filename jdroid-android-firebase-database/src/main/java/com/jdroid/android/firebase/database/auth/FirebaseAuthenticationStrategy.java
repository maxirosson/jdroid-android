package com.jdroid.android.firebase.database.auth;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.jdroid.android.firebase.database.FirebaseDatabaseRepository;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.concurrent.CountDownLatch;

public abstract class FirebaseAuthenticationStrategy implements FirebaseAuth.AuthStateListener, OnFailureListener {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseDatabaseRepository.class);

	private CountDownLatch countDownLatch;

	public final void authenticate(DatabaseReference databaseReference) {
		countDownLatch = new CountDownLatch(1);
		doAuthenticate(databaseReference);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			throw new UnexpectedException(e);
		}
	}

	protected abstract void doAuthenticate(DatabaseReference databaseReference);

	@Override
	public void onFailure(@NonNull Exception e) {
		getCountDownLatch().countDown();
		doOnAuthenticationError(e);
	}

	protected void doOnAuthenticationError(Exception e) {
		throw new UnexpectedException(e);
	}

	@Override
	public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
		if (firebaseAuth.getCurrentUser() != null) {
			LOGGER.debug("AuthStateChanged: User is signed in with uid: " + firebaseAuth.getCurrentUser().getUid());
			getCountDownLatch().countDown();
			doOnAuthenticated(firebaseAuth);
		} else {
			LOGGER.info("AuthStateChanged: No user is signed in.");
		}
	}

	protected void doOnAuthenticated(FirebaseAuth firebaseAuth) {
		// Do nothing
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}
}
