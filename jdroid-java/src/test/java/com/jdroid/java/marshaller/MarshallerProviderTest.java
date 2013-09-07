package com.jdroid.java.marshaller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.json.JsonMap;

/**
 * 
 * @author Maxi Rosson
 */
public class MarshallerProviderTest {
	
	private MarshallerProvider marshallerProvider;
	
	public MarshallerProviderTest() {
		marshallerProvider = MarshallerProvider.get();
		marshallerProvider.addMarshaller(DummyClass.class, new DummyClassMarshaller());
	}
	
	/**
	 * @return The different cases
	 */
	@DataProvider
	public Iterator<Object[]> masrsallDataProvider() {
		List<Object[]> cases = Lists.newArrayList();
		cases.add(new Object[] { 1, "1" });
		cases.add(new Object[] { "1", "1" });
		cases.add(new Object[] { Lists.newArrayList(1, 2, 3), "[1,2,3]" });
		cases.add(new Object[] { Lists.newArrayList("1", "2", "3"), "[\"1\",\"2\",\"3\"]" });
		
		DummyClass dummyClass = new DummyClass("1", 2L, Lists.newArrayList("3", "4"), Lists.newArrayList(5L, 6L));
		String dummyJson = "{\"stringProperty\":\"1\",\"longProperty\":2,\"listStringProperty\":[\"3\",\"4\"],\"listLongProperty\":[5,6]}";
		cases.add(new Object[] { dummyClass, dummyJson });
		cases.add(new Object[] { Lists.newArrayList(dummyClass, dummyClass), "[" + dummyJson + "," + dummyJson + "]" });
		
		cases.add(new Object[] { new DummyClass(), "{}" });
		
		return cases.iterator();
	}
	
	@Test(dataProvider = "masrsallDataProvider")
	public void masrsall(Object data, String expectedJson) {
		String result = marshallerProvider.marshall(data, null, null).toString();
		Assert.assertEquals(result.replace(" ", ""), expectedJson);
	}
	
	public static class DummyClass {
		
		private String stringProperty;
		private Long longProperty;
		private List<String> listStringProperty;
		private List<Long> listLongProperty;
		
		public DummyClass() {
		}
		
		public DummyClass(String stringProperty, Long longProperty, List<String> listStringProperty,
				List<Long> listLongProperty) {
			this.stringProperty = stringProperty;
			this.longProperty = longProperty;
			this.listStringProperty = listStringProperty;
			this.listLongProperty = listLongProperty;
		}
	}
	
	public class DummyClassMarshaller implements Marshaller<DummyClass, JsonMap> {
		
		@Override
		public JsonMap marshall(DummyClass dummyClass, MarshallerMode mode, Map<String, String> extras) {
			JsonMap map = new JsonMap(mode, extras);
			map.put("stringProperty", dummyClass.stringProperty);
			map.put("longProperty", dummyClass.longProperty);
			map.put("listStringProperty", dummyClass.listStringProperty);
			map.put("listLongProperty", dummyClass.listLongProperty);
			return map;
		}
	}
}
