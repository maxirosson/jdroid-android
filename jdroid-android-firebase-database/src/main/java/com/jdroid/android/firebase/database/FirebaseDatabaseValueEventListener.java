package com.jdroid.android.firebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jdroid.java.exception.UnexpectedException;

public class FirebaseDatabaseValueEventListener implements ValueEventListener {

	private FirebaseDatabaseCountDownLatch done = new FirebaseDatabaseCountDownLatch();

	@Override
	public void onDataChange(DataSnapshot snapshot) {
		done.setDataSnapshot(snapshot);
		done.countDown();
	}

	@Override
	public void onCancelled(DatabaseError databaseError) {
		done.setFirebaseDatabaseException(new FirebaseDatabaseException(databaseError));
		done.countDown();
	}

	public void waitOperation() {
		try {
			done.await();
			if (done.getFirebaseDatabaseException() != null) {
				throw done.getFirebaseDatabaseException();
			}
		} catch (InterruptedException e) {
			throw new UnexpectedException(e);
		}
	}

	public DataSnapshot getDataSnapshot() {
		return done.getDataSnapshot();
	}

}
