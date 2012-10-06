package com.jdroid.javaweb;

import java.util.Collection;
import java.util.List;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Matcher that verify that two collections have the same elements in no particular order.
 * 
 * @param <T> The collection type
 */
public class EqCollectionMatcher<T> extends ArgumentMatcher<List<T>> {
	
	private final Collection<T> expected;
	
	/**
	 * @param expected list of arguments to match.
	 */
	public EqCollectionMatcher(Collection<T> expected) {
		this.expected = expected;
	}
	
	/**
	 * @see org.mockito.ArgumentMatcher#describeTo(org.hamcrest.Description)
	 */
	@Override
	public void describeTo(Description description) {
		description.appendText(expected.toString());
	}
	
	/**
	 * @see org.mockito.ArgumentMatcher#matches(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(Object actual) {
		
		List<T> actualList = (List<T>)actual;
		if (Iterables.size(expected) == Iterables.size(actualList)) {
			// Create a list based on the expected results.
			List<?> expectedList = Lists.newArrayList(expected);
			
			// Iterate over the obtained results and check if the expected list
			// contains the item. If the item is contained within the expected list
			// then it is removed from it so that repeated items can be checked.
			for (Object o : actualList) {
				if (expectedList.contains(o)) {
					expectedList.remove(o);
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
