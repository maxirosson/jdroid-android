package com.jdroid.javaweb.search;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.java.search.PagedResult;
import com.jdroid.java.utils.CollectionUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class PagedResultJsonMarshaller implements Marshaller<PagedResult<?>, JsonMap> {
	
	private static final String LAST_PAGE = "lastPage";
	private static final String LIST = "list";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(PagedResult<?> pagedResult, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		if (CollectionUtils.isNotEmpty(pagedResult.getResults())) {
			map.put(LAST_PAGE, pagedResult.isLastPage());
			map.put(LIST, pagedResult.getResults());
		}
		return map;
	}
	
}
