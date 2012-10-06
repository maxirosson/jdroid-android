package com.jdroid.javaweb;

import java.util.Collection;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.ArgumentMatcher;
import com.jdroid.javaweb.utils.ReflectionUtils;

/**
 * Matcher that verify that the nested collection and the expected collection have the same elements in no particular
 * order.
 * 
 * @param <T> The type of argument
 * @param <S> The nested collection type
 */
public class EqNestedCollectionMatcher<T, S> extends ArgumentMatcher<T> {
	
	private Matcher<List<S>> matcher;
	private String propertyName;
	
	/**
	 * @param propertyName The nested property name to obtain the collection used to match
	 * @param expected The list of arguments to match.
	 */
	public EqNestedCollectionMatcher(String propertyName, Collection<S> expected) {
		this.matcher = new EqCollectionMatcher<S>(expected);
		this.propertyName = propertyName;
	}
	
	/**
	 * @see org.mockito.ArgumentMatcher#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(Object argument) {
		return matcher.matches(ReflectionUtils.getPropertyValue(argument, propertyName));
	}
}
