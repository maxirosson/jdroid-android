package com.jdroid.java.api;

import java.util.List;
import java.util.Map;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MultipartWebService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.mock.AbstractMockWebService;
import com.jdroid.java.http.post.EntityEnclosingWebService;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.java.marshaller.MarshallerProvider;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractApiService {
	
	protected WebService newGetService(Object... urlSegments) {
		return newGetService(false, urlSegments);
	}
	
	protected WebService newGetService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newGetServiceImpl(getServer(), Lists.newArrayList(urlSegments),
				toArray(getHttpWebServiceProcessors()));
		}
	}
	
	protected abstract WebService newGetServiceImpl(Server server, List<Object> urlSegments,
			HttpWebServiceProcessor... httpWebServiceProcessors);
	
	protected EntityEnclosingWebService newPostService(Object... urlSegments) {
		return newPostService(false, urlSegments);
	}
	
	protected EntityEnclosingWebService newPostService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newPostServiceImpl(getServer(), Lists.newArrayList(urlSegments),
				toArray(getHttpWebServiceProcessors()));
		}
	}
	
	protected abstract EntityEnclosingWebService newPostServiceImpl(Server server, List<Object> urlSegments,
			HttpWebServiceProcessor... httpWebServiceProcessors);
	
	protected EntityEnclosingWebService newPutService(Object... urlSegments) {
		return newPutService(false, urlSegments);
	}
	
	protected EntityEnclosingWebService newPutService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newPutServiceImpl(getServer(), Lists.newArrayList(urlSegments),
				toArray(getHttpWebServiceProcessors()));
		}
	}
	
	protected abstract EntityEnclosingWebService newPutServiceImpl(Server server, List<Object> urlSegments,
			HttpWebServiceProcessor... httpWebServiceProcessors);
	
	protected MultipartWebService newMultipartPutService(Object... urlSegments) {
		return newMultipartPutService(false, urlSegments);
	}
	
	protected MultipartWebService newMultipartPutService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newMultipartPutServiceImpl(getServer(), Lists.newArrayList(urlSegments),
				toArray(getHttpWebServiceProcessors()));
		}
	}
	
	protected abstract MultipartWebService newMultipartPutServiceImpl(Server server, List<Object> urlSegments,
			HttpWebServiceProcessor... httpWebServiceProcessors);
	
	protected WebService newDeleteService(Object... urlSegments) {
		return newDeleteService(false, urlSegments);
	}
	
	protected WebService newDeleteService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newDeleteServiceImpl(getServer(), Lists.newArrayList(urlSegments),
				toArray(getHttpWebServiceProcessors()));
		}
	}
	
	protected abstract WebService newDeleteServiceImpl(Server server, List<Object> urlSegments,
			HttpWebServiceProcessor... httpWebServiceProcessors);
	
	protected EntityEnclosingWebService newFormPostService(Object... urlSegments) {
		return newFormPostService(false, urlSegments);
	}
	
	protected EntityEnclosingWebService newFormPostService(Boolean mocked, Object... urlSegments) {
		if (isHttpMockEnabled() || mocked) {
			return getAbstractMockWebServiceInstance(urlSegments);
		} else {
			return newFormPostServiceImpl(getServer(), Lists.newArrayList(urlSegments),
				toArray(getHttpWebServiceProcessors()));
		}
	}
	
	protected abstract EntityEnclosingWebService newFormPostServiceImpl(Server server, List<Object> urlSegments,
			HttpWebServiceProcessor... httpWebServiceProcessors);
	
	protected HttpWebServiceProcessor[] toArray(List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		if (httpWebServiceProcessors != null) {
			return httpWebServiceProcessors.toArray(new HttpWebServiceProcessor[] {});
		} else {
			return new HttpWebServiceProcessor[] {};
		}
	}
	
	protected abstract Server getServer();
	
	protected abstract List<HttpWebServiceProcessor> getHttpWebServiceProcessors();
	
	protected abstract AbstractMockWebService getAbstractMockWebServiceInstance(Object... urlSegments);
	
	protected abstract Boolean isHttpMockEnabled();
	
	public void marshallSimple(EntityEnclosingWebService webservice, Object object) {
		marshall(webservice, object, MarshallerMode.SIMPLE);
	}
	
	public void marshall(EntityEnclosingWebService webservice, Object object) {
		marshall(webservice, object, MarshallerMode.COMPLETE);
	}
	
	public void marshall(EntityEnclosingWebService webservice, Object object, MarshallerMode mode) {
		marshall(webservice, object, mode, null);
	}
	
	public void marshall(EntityEnclosingWebService webservice, Object object, Map<String, String> extras) {
		marshall(webservice, object, MarshallerMode.COMPLETE, extras);
	}
	
	public void marshall(EntityEnclosingWebService webservice, Object object, MarshallerMode mode,
			Map<String, String> extras) {
		webservice.setEntity(MarshallerProvider.get().marshall(object, mode, extras).toString());
	}
}
