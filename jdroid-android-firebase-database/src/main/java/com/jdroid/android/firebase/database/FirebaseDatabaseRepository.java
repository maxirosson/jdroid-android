package com.jdroid.android.firebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jdroid.android.firebase.database.auth.FirebaseAuthenticationStrategy;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.domain.Entity;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FirebaseDatabaseRepository<T extends Entity> implements Repository<T> {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseDatabaseRepository.class);

	private FirebaseAuthenticationStrategy firebaseAuthenticationStrategy;

	public FirebaseDatabaseRepository() {
		firebaseAuthenticationStrategy = createFirebaseAuthenticationStrategy();
	}

	protected FirebaseAuthenticationStrategy createFirebaseAuthenticationStrategy() {
		return null;
	}

	protected abstract String getPath();

	protected abstract Class<T> getEntityClass();
	
	protected String getDatabaseUrl() {
		return null;
	}

	protected DatabaseReference createDatabaseReference() {
		FirebaseDatabase firebaseDatabase = null;
		if (getDatabaseUrl() != null) {
			firebaseDatabase = FirebaseDatabase.getInstance(getDatabaseUrl());
		} else {
			firebaseDatabase = FirebaseDatabase.getInstance();
		}
		DatabaseReference databaseReference = firebaseDatabase.getReference();
		if (firebaseAuthenticationStrategy != null) {
			firebaseAuthenticationStrategy.authenticate(databaseReference);
		}
		databaseReference = databaseReference.child(getPath());
		return databaseReference;
	}

	@Override
	public T get(String id) {
		DatabaseReference databaseReference = createDatabaseReference();
		databaseReference = databaseReference.child(id);
		FirebaseDatabaseValueEventListener listener = new FirebaseDatabaseValueEventListener();
		databaseReference.addListenerForSingleValueEvent(listener);
		listener.waitOperation();
		T result = listener.getDataSnapshot().getValue(getEntityClass());
		LOGGER.info("Retrieved object from database of path: " + getPath() + ". [ " + result + " ]");
		return result;
	}

	@Override
	public void add(T item) {
		DatabaseReference databaseReference = createDatabaseReference();
		if (item.getId() != null) {
			databaseReference = databaseReference.child(item.getId());
		} else {
			databaseReference = databaseReference.push();
		}

		FirebaseDatabaseCompletionListener listener = new FirebaseDatabaseCompletionListener();
		databaseReference.setValue(item, listener);

		listener.waitOperation();
		if (item.getId() == null) {
			// Add the id field
			addIdField(databaseReference.getKey());
		}
		item.setId(databaseReference.getKey());
		LOGGER.info("Stored object in database: " + item);
	}

	private void addIdField(String id) {
		DatabaseReference databaseReference = createDatabaseReference();
		databaseReference = databaseReference.child(id);

		Map<String, Object> map = new HashMap<>();
		map.put("id", id);

		FirebaseDatabaseCompletionListener listener = new FirebaseDatabaseCompletionListener();
		databaseReference.updateChildren(map, listener);
		listener.waitOperation();
	}

	@Override
	public void addAll(Collection<T> items) {
		for(T each : items) {
			add(each);
		}
	}

	@Override
	public void update(T item) {
		if (item.getId() == null) {
			throw new UnexpectedException("Item with null id can not be updated");
		}
		add(item);
	}

	@Override
	public List<T> getByField(String fieldName, Object... values) {
		DatabaseReference databaseReference = createDatabaseReference();
		Query query = databaseReference.orderByChild(fieldName);

		if (values == null) {
			throw new UnexpectedException("Null value type not supported");
		} else if (values.length > 1) {
			throw new UnexpectedException("Just one value is supported");
		}
		Object value = values[0];
		if (value instanceof String) {
			query = query.equalTo((String)value);
		} else if (value instanceof Long) {
			query = query.equalTo((Long)value);
		} else if (value instanceof Double) {
			query = query.equalTo((Double)value);
		} else if (value instanceof Integer) {
			query = query.equalTo((Integer)value);
		} else if (value instanceof Boolean) {
			query = query.equalTo((Boolean)value);
		} else {
			throw new UnexpectedException("Value type not supported");
		}

		FirebaseDatabaseValueEventListener listener = new FirebaseDatabaseValueEventListener();
		query.addListenerForSingleValueEvent(listener);
		listener.waitOperation();
		List<T> results = Lists.newArrayList();
		for (DataSnapshot eachSnapshot: listener.getDataSnapshot().getChildren()) {
			results.add(eachSnapshot.getValue(getEntityClass()));
		}
		LOGGER.info("Retrieved objects [" + results.size() + "] from database of path: " + getPath() + " field: " + fieldName);
		return results;
	}
	
	@Override
	public T getItemByField(String fieldName, Object... values) {
		List<T> items = getByField(fieldName, values);
		if (!items.isEmpty()) {
			return items.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<T> getAll() {
		DatabaseReference databaseReference = createDatabaseReference();
		FirebaseDatabaseValueEventListener listener = new FirebaseDatabaseValueEventListener();
		databaseReference.addListenerForSingleValueEvent(listener);
		listener.waitOperation();
		List<T> results = Lists.newArrayList();
		for (DataSnapshot eachSnapshot: listener.getDataSnapshot().getChildren()) {
			results.add(eachSnapshot.getValue(getEntityClass()));
		}
		LOGGER.info("Retrieved all objects [" + results.size() + "] from path: " + getPath());
		return results;
	}

	@Override
	public List<T> getByIds(List<String> ids) {
		DatabaseReference databaseReference = createDatabaseReference();
		FirebaseDatabaseValueEventListener listener = new FirebaseDatabaseValueEventListener();
		databaseReference.addListenerForSingleValueEvent(listener);
		listener.waitOperation();
		List<T> results = Lists.newArrayList();
		for (DataSnapshot eachSnapshot: listener.getDataSnapshot().getChildren()) {
			T each = eachSnapshot.getValue(getEntityClass());
			if (ids.contains(each.getId())) {
				results.add(each);
			}
		}
		LOGGER.info("Retrieved all objects [" + results.size() + "] from path: " + getPath() + " and ids: " + ids);
		return results;
	}


	@Override
	public void remove(T item) {
		remove(item.getId());
	}

	@Override
	public void removeAll() {
		innerRemove(null);
	}

	@Override
	public void removeAll(Collection<T> items) {
		for(T each : items) {
			remove(each);
		}
	}

	@Override
	public void remove(String id) {
		if (id != null) {
			innerRemove(id);
		}
	}

	private void innerRemove(String id) {
		DatabaseReference databaseReference = createDatabaseReference();
		if (id != null) {
			databaseReference = databaseReference.child(id);
		}

		FirebaseDatabaseCompletionListener listener = new FirebaseDatabaseCompletionListener();
		databaseReference.removeValue(listener);
		listener.waitOperation();
		LOGGER.trace("Deleted object in database: with id: " + id);
	}

	@Override
	public Boolean isEmpty() {
		return getSize() == 0;
	}

	@Override
	public Long getSize() {
		DatabaseReference databaseReference = createDatabaseReference();
		FirebaseDatabaseValueEventListener listener = new FirebaseDatabaseValueEventListener();
		databaseReference.addListenerForSingleValueEvent(listener);
		listener.waitOperation();
		return listener.getDataSnapshot().getChildrenCount();
	}

	@Override
	public void replaceAll(Collection<T> items) {
		for(T each : items) {
			update(each);
		}
	}

	@Override
	public T getUniqueInstance() {
		List<T> results = getAll();
		if (!results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}
}
