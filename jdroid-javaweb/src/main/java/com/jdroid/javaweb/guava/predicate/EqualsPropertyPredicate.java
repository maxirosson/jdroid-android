package com.jdroid.javaweb.guava.predicate;

import java.io.Serializable;
import com.google.common.base.Predicate;
import com.jdroid.java.utils.ObjectUtils;
import com.jdroid.javaweb.guava.function.PropertyFunction;

/**
 * Predicates that verifies if the value of the propertyName is equals to the given value
 * 
 * @param <T> The object type
 */
public class EqualsPropertyPredicate<T> implements Predicate<T>, Serializable {
	
	private Object equalsValue;
	private PropertyFunction<T, ?> propertyFunction;
	
	/**
	 * @param propertyName The propertyName of the object to compare the value with the equalsValue
	 * @param equalsValue The value to compare with the value of the propertyNames
	 */
	public EqualsPropertyPredicate(String propertyName, Object equalsValue) {
		this.propertyFunction = new PropertyFunction<>(propertyName);
		this.equalsValue = equalsValue;
	}
	
	/**
	 * @see com.google.common.base.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(T input) {
		Object value = this.propertyFunction.apply(input);
		return ObjectUtils.equals(this.equalsValue, value);
	}
}
