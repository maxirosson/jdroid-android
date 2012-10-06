package com.jdroid.javaweb.guava.function;

import java.util.Iterator;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.google.common.collect.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.javaweb.guava.function.NestedPropertyFunction;
import com.jdroid.javaweb.guava.function.PropertyFunction;

/**
 * Tests the {@link PropertyFunction} class
 * 
 */
public class NestedPropertyFunctionTest {
	
	/**
	 * @return The different cases
	 */
	@DataProvider
	public Iterator<Object[]> getApply() {
		List<Object[]> cases = Lists.newArrayList();
		TestObject object1 = new TestObject();
		TestObject object2 = new TestObject("SomeValue");
		TestObject parentObject1 = new TestObject(object1);
		TestObject parentObject2 = new TestObject(object2);
		cases.add(new Object[] { parentObject1, "nested", object1 });
		cases.add(new Object[] { parentObject1, "value", null });
		cases.add(new Object[] { parentObject1, "nested.value", null });
		cases.add(new Object[] { parentObject1, "nested.nested", null });
		cases.add(new Object[] { parentObject1, "nested.nested.value", null });
		
		cases.add(new Object[] { parentObject2, "nested", object2 });
		cases.add(new Object[] { parentObject2, "value", null });
		cases.add(new Object[] { parentObject2, "nested.value", "SomeValue" });
		cases.add(new Object[] { parentObject2, "nested.nested", null });
		cases.add(new Object[] { parentObject2, "nested.nested.value", null });
		
		return cases.iterator();
	}
	
	/**
	 * Test the {@link PropertyFunction#apply(Object)} method
	 * 
	 * @param testObject The object to test
	 * @param property The property to look for
	 * @param expectedResponse The expected response
	 */
	@Test(dataProvider = "getApply")
	public void apply(TestObject testObject, String property, Object expectedResponse) {
		NestedPropertyFunction<TestObject, Object> function = new NestedPropertyFunction<TestObject, Object>(property);
		Assert.assertEquals(function.apply(testObject), expectedResponse);
	}
	
	/**
	 * @return The different cases
	 */
	@DataProvider
	public Iterator<Object[]> getApplyNegative() {
		List<Object[]> cases = Lists.newArrayList();
		TestObject object = new TestObject();
		TestObject parentObject = new TestObject(object);
		cases.add(new Object[] { parentObject, "nested.anInexistentProperty" });
		cases.add(new Object[] { parentObject, "anInexistentProperty" });
		return cases.iterator();
	}
	
	/**
	 * Test the negative {@link PropertyFunction#apply(Object)} method
	 * 
	 * @param testObject The object to test
	 * @param property The property to look for
	 */
	@Test(dataProvider = "getApplyNegative", expectedExceptions = UnexpectedException.class)
	public void applyNegative(TestObject testObject, String property) {
		NestedPropertyFunction<TestObject, Object> function = new NestedPropertyFunction<TestObject, Object>(property);
		function.apply(testObject);
	}
	
	/**
	 * Test object
	 * 
	 */
	public class TestObject {
		
		private TestObject nested;
		private Object value;
		
		/**
		 * Default constructor
		 */
		public TestObject() {
			this(null);
		}
		
		/**
		 * @param nested The nested value
		 */
		public TestObject(TestObject nested) {
			this.nested = nested;
		}
		
		/**
		 * @param value The value
		 */
		public TestObject(Object value) {
			this.value = value;
		}
		
		/**
		 * @return The nested value
		 */
		public TestObject getNested() {
			return nested;
		}
		
		/**
		 * @return The value
		 */
		public Object getValue() {
			return value;
		}
	}
}
