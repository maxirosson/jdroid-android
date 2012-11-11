package com.jdroid.javaweb.controller;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.java.marshaller.MarshallerProvider;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractController {
	
	public String marshallSimple(Object object) {
		return marshall(object, MarshallerMode.SIMPLE);
	}
	
	public String marshall(Object object) {
		return marshall(object, MarshallerMode.COMPLETE);
	}
	
	public String marshall(Object object, MarshallerMode mode) {
		return marshall(object, mode, null);
	}
	
	public String marshall(Object object, Map<String, String> extras) {
		return marshall(object, MarshallerMode.COMPLETE, extras);
	}
	
	public String marshall(Object object, MarshallerMode mode, Map<String, String> extras) {
		return object != null ? MarshallerProvider.get().marshall(object, mode, extras).toString() : StringUtils.EMPTY;
	}
}
