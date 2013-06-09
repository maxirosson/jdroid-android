package com.jdroid.java.http.apache.put;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.MultipartWebService;
import com.jdroid.java.http.apache.ApacheHttpWebService;
import com.jdroid.java.http.apache.HttpClientFactory;
import com.jdroid.java.http.apache.post.ByteArrayInputStreamBody;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.java.marshaller.MarshallerProvider;

/**
 * 
 * @author Maxi Rosson
 */
public class ApacheMultipartHttpPutWebService extends ApacheHttpPutWebService implements MultipartWebService {
	
	private MultipartEntity multipartEntity = new MultipartEntity();
	
	public ApacheMultipartHttpPutWebService(HttpClientFactory httpClientFactory, String baseURL,
			HttpWebServiceProcessor... httpWebServiceProcessors) {
		super(httpClientFactory, baseURL, httpWebServiceProcessors);
	}
	
	/**
	 * @see com.jdroid.java.http.apache.post.ApacheHttpPostWebService#addEntity(org.apache.http.client.methods.HttpEntityEnclosingRequestBase)
	 */
	@Override
	protected void addEntity(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
		httpEntityEnclosingRequestBase.setEntity(multipartEntity);
	}
	
	/**
	 * @see com.jdroid.java.http.apache.ApacheHttpWebService#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		// The MultipartEntity will fill the proper content type header. So, we need to avoid the override of it
		if (!name.equals(ApacheHttpWebService.CONTENT_TYPE_HEADER)) {
			super.addHeader(name, value);
		}
	}
	
	/**
	 * @see com.jdroid.java.http.MultipartWebService#addPart(java.lang.String, java.io.ByteArrayInputStream,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void addPart(String name, ByteArrayInputStream in, String mimeType, String filename) {
		multipartEntity.addPart(name, new ByteArrayInputStreamBody(in, mimeType, filename));
	}
	
	/**
	 * @see com.jdroid.java.http.MultipartWebService#addPart(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public void addPart(String name, Object value, String mimeType) {
		if (value != null) {
			try {
				multipartEntity.addPart(name, new StringBody(value.toString(), mimeType, Charset.defaultCharset()));
			} catch (UnsupportedEncodingException e) {
				throw new UnexpectedException(e);
			}
		}
	}
	
	/**
	 * @see com.jdroid.java.http.MultipartWebService#addJsonPart(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addJsonPart(String name, Object value) {
		addPart(name, MarshallerProvider.get().marshall(value, MarshallerMode.COMPLETE, null), MimeType.JSON.toString());
	}
}
