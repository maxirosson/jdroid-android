package com.jdroid.javaweb;

import java.util.Arrays;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

/**
 * Matcher that verify that two arrays have the same elements in no particular order.
 * 
 * @param <T> The collection type
 */
public class EqArrayMatcher<T> extends ArgumentMatcher<T[]> {
	
	private EqCollectionMatcher<T> eqCollectionMatcher;
	
	/**
	 * @param expected list of arguments to match.
	 */
	public EqArrayMatcher(T[] expected) {
		eqCollectionMatcher = new EqCollectionMatcher<T>(Arrays.asList(expected));
	}
	
	/**
	 * @see org.mockito.ArgumentMatcher#describeTo(org.hamcrest.Description)
	 */
	@Override
	public void describeTo(Description description) {
		eqCollectionMatcher.describeTo(description);
	}
	
	/**
	 * @see org.mockito.ArgumentMatcher#matches(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean matches(Object actual) {
		return eqCollectionMatcher.matches(Arrays.asList((T[])actual));
	}
}
