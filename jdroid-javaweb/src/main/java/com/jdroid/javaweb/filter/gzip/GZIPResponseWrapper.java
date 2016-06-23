package com.jdroid.javaweb.filter.gzip;

import com.jdroid.java.utils.EncodingUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GZIPResponseWrapper extends HttpServletResponseWrapper {

	private static final Logger LOGGER = LoggerUtils.getLogger(GZIPResponseWrapper.class);
	
	protected HttpServletResponse wrappedResponse;
	protected ServletOutputStream stream;
	protected PrintWriter writer;
	
	public GZIPResponseWrapper(HttpServletResponse wrappedResponse) {
		super(wrappedResponse);
		this.wrappedResponse = wrappedResponse;
	}
	
	public void close() {
		try {
			if (writer != null) {
				writer.close();
			} else {
				if (stream != null) {
					stream.close();
				}
			}
		} catch (IOException e) {
			LOGGER.error("IOException when closing GZIPResponseWrapper", e);
		}
	}
	
	/**
	 * @see javax.servlet.ServletResponseWrapper#flushBuffer()
	 */
	@Override
	public void flushBuffer() throws IOException {
		stream.flush();
	}
	
	/**
	 * @see javax.servlet.ServletResponseWrapper#getOutputStream()
	 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called!");
		}
		
		if (stream == null) {
			stream = createOutputStream();
		}
		return stream;
	}
	
	/**
	 * @see javax.servlet.ServletResponseWrapper#getWriter()
	 */
	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer != null) {
			return writer;
		}
		
		if (stream != null) {
			throw new IllegalStateException("getOutputStream() has already been called!");
		}
		
		stream = createOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(stream, EncodingUtils.UTF8));
		return writer;
	}
	
	/**
	 * @see javax.servlet.ServletResponseWrapper#setContentLength(int)
	 */
	@Override
	public void setContentLength(int length) {
	}
	
	private ServletOutputStream createOutputStream() throws IOException {
		return new GZIPResponseStream(wrappedResponse);
	}
}
