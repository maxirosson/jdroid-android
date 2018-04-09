package com.jdroid.android.sqlite.repository;

import com.jdroid.java.domain.Entity;

public interface EntityChildrenListener<T extends Entity> {
	/**
	 * Called before an entity is stored, allows to store/update entity children.
	 *
	 * @param item stored entity.
	 */
	void onPreStored(T item);

	/**
	 * Called after an entity is stored, allows to store entity children.
	 *
	 * @param item stored entity.
	 */
	void onStored(T item);

	/**
	 * Called after an entity is updated, allows to store/update entity children.
	 *
	 * @param item stored entity.
	 */
	void onUpdated(T item);

	/**
	 * Called after an entity is loaded. It allows to populate entity children.
	 *
	 * @param item loaded entity.
	 */
	void onLoaded(T item);

	/**
	 * Called after an entity is removed. It allows to remove entity children.
	 *
	 * @param item removed entity.
	 */
	void onRemoved(T item);
}
