package com.jdroid.java.json;

import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.java.marshaller.MarshallerProvider;
import com.jdroid.java.utils.CollectionUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class JsonMap extends LinkedHashMap<String, Object> {
	
	private MarshallerMode mode;
	private Map<String, String> extras;
	
	public JsonMap() {
		this(MarshallerMode.COMPLETE);
	}
	
	public JsonMap(MarshallerMode mode) {
		this(mode, null);
	}
	
	public JsonMap(MarshallerMode mode, Map<String, String> extras) {
		this.mode = mode;
		this.extras = extras;
	}
	
	public Object put(String key, Object value, MarshallerMode mode) {
		Object marshalledValue = MarshallerProvider.get().marshall(value, mode, extras);
		if ((marshalledValue != null) && !CollectionUtils.isEmptyCollection(marshalledValue)) {
			super.put(key, marshalledValue);
		}
		return null;
	}
	
	/**
	 * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object put(String key, Object value) {
		return put(key, value, mode);
	}
	
	/**
	 * @see java.util.AbstractMap#toString()
	 */
	@Override
	public String toString() {
		return new JSONObject(this).toString();
	}
}
