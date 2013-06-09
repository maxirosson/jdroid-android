package com.jdroid.java.http.apache.post;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.entity.mime.content.InputStreamBody;

/**
 * 
 * @author Maxi Rosson
 */
public class ByteArrayInputStreamBody extends InputStreamBody {
	
	/**
	 * @param in
	 * @param mimeType
	 * @param filename
	 */
	public ByteArrayInputStreamBody(ByteArrayInputStream in, String mimeType, String filename) {
		super(in, mimeType, filename);
	}
	
	@Override
	public void writeTo(OutputStream out) throws IOException {
		getInputStream().reset();
		super.writeTo(out);
	}
	
	@Override
	public long getContentLength() {
		try {
			return getInputStream().available();
		} catch (IOException e) {
			return -1;
		}
	}
}
