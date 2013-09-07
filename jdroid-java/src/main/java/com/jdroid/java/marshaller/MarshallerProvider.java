package com.jdroid.java.marshaller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.parser.json.JsonArrayWrapper;

/**
 * 
 * @author Maxi Rosson
 */
public class MarshallerProvider {
	
	private Map<Class<?>, Marshaller<Object, Object>> marshallers = Maps.newHashMap();
	private Marshaller<Object, Object> defaultMarshaller = new DefaultMarshaller();
	private MarshallerTypeEvaluator marshallerTypeEvaluator = new DefaultMarshallerTypeEvaluator();
	
	private static final MarshallerProvider INSTANCE = new MarshallerProvider();
	
	private MarshallerProvider() {
		// singleton
	}
	
	/**
	 * @return The instance of this provider
	 */
	public static MarshallerProvider get() {
		return MarshallerProvider.INSTANCE;
	}
	
	public Object marshall(Object object, MarshallerMode mode, Map<String, String> extras) {
		if (object != null) {
			if (object instanceof Collection<?>) {
				Collection<?> collection = (Collection<?>)object;
				List<Object> list = Lists.newArrayList();
				
				for (Object each : collection) {
					list.add(innerMarshall(each, mode, extras));
				}
				return new JsonArrayWrapper(list);
			} else {
				return innerMarshall(object, mode, extras);
			}
		} else {
			return null;
		}
	}
	
	public Object innerMarshall(Object object, MarshallerMode mode, Map<String, String> extras) {
		if (object != null) {
			if (object instanceof Collection<?>) {
				Collection<?> collection = (Collection<?>)object;
				List<Object> list = Lists.newArrayList();
				
				for (Object each : collection) {
					list.add(innerMarshall(each, mode, extras));
				}
				return list;
			} else {
				return getMarshaller(object).marshall(object, mode, extras);
			}
		} else {
			return null;
		}
	}
	
	public Marshaller<Object, Object> getMarshaller(Object marshallerType) {
		Class<?> clazz = marshallerTypeEvaluator.evaluate(marshallerType);
		return marshallers.containsKey(clazz) ? marshallers.get(clazz) : defaultMarshaller;
	}
	
	public void setMarshallers(Map<Class<?>, Marshaller<Object, Object>> marshallers) {
		this.marshallers = marshallers;
	}
	
	@SuppressWarnings("unchecked")
	public void addMarshaller(Class<?> clazz, Marshaller<? extends Object, ? extends Object> marshaller) {
		marshallers.put(clazz, (Marshaller<Object, Object>)marshaller);
	}
	
	public static interface MarshallerTypeEvaluator {
		
		public Class<?> evaluate(Object marshallerType);
	}
	
	private class DefaultMarshallerTypeEvaluator implements MarshallerTypeEvaluator {
		
		@Override
		public Class<?> evaluate(Object marshallerType) {
			return marshallerType.getClass();
		};
	}
	
	public void setMarshallerTypeEvaluator(MarshallerTypeEvaluator marshallerTypeEvaluator) {
		this.marshallerTypeEvaluator = marshallerTypeEvaluator;
	}
	
	public void setDefaultMarshaller(Marshaller<Object, Object> defaultMarshaller) {
		this.defaultMarshaller = defaultMarshaller;
	}
}
