package com.jdroid.java.marshaller;

import java.util.Date;
import java.util.Map;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.StringUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class DefaultMarshaller implements Marshaller<Object, Object> {
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public Object marshall(Object object, MarshallerMode mode, Map<String, String> extras) {
		Object marshalled = null;
		if (object != null) {
			if (object instanceof Boolean) {
				marshalled = object;
			} else if (object instanceof Date) {
				marshalled = DateUtils.format((Date)object, DateUtils.YYYYMMDDHHMMSSZ_DATE_FORMAT);
			} else if (object instanceof Number) {
				marshalled = object;
			} else {
				marshalled = StringUtils.getNotEmptyString(object.toString());
			}
		}
		return marshalled;
	}
}
