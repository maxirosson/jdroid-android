package com.jdroid.java.collections;

import java.util.Collection;

/**
 * A set of useful functions used against a Collection
 */
public abstract class CollectionUtils {
	
	protected CollectionUtils() {
		// Helper class
	}
	
	/**
	 * Null-safe check if the specified collection is empty.
	 * <p>
	 * Null returns true.
	 * 
	 * @param coll the collection to check, may be null
	 * @return true if empty or null
	 */
	public static boolean isEmpty(Collection<?> coll) {
		return (coll == null) || coll.isEmpty();
	}
	
	public static boolean isEmptyCollection(Object object) {
		if (object instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>)object;
			return isEmpty(collection);
		} else {
			return false;
		}
	}
	
	/**
	 * Null-safe check if the specified collection is not empty.
	 * <p>
	 * Null returns false.
	 * 
	 * @param coll the collection to check, may be null
	 * @return true if non-null and non-empty
	 */
	public static boolean isNotEmpty(Collection<?> coll) {
		return !CollectionUtils.isEmpty(coll);
	}
}
