package com.jdroid.javaweb.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.spi.BadRequestException;
import com.jdroid.java.exception.BusinessException;
import com.jdroid.javaweb.exception.CommonErrorCode;
import com.jdroid.javaweb.exception.InvalidAuthenticationException;

/**
 * Class that handles exceptions.
 * 
 */
@Provider
public class ExceptionHandler implements ExceptionMapper<Throwable> {
	
	private static final Log LOG = LogFactory.getLog(ExceptionHandler.class);
	
	public static final String STATUS_CODE_HEADER = "status-code";
	public static final String OK_STATUS_CODE_HEADER_VALUE = "200";
	
	/**
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
	public Response toResponse(Throwable throwable) {
		
		Response response;
		if (throwable instanceof BusinessException) {
			response = handleException((BusinessException)throwable);
		} else if (throwable instanceof BadRequestException) {
			response = handleException((BadRequestException)throwable);
		} else if (throwable instanceof InvalidAuthenticationException) {
			response = handleException((InvalidAuthenticationException)throwable);
		} else {
			response = handleException(throwable);
		}
		return response;
	}
	
	private Response handleException(BusinessException businessException) {
		ResponseBuilder responseBuilder = Response.ok();
		responseBuilder.header(STATUS_CODE_HEADER, businessException.getErrorCode().getStatusCode());
		LOG.info("Server Status code: " + businessException.getErrorCode().getStatusCode());
		return responseBuilder.build();
	}
	
	private Response handleException(BadRequestException badRequestException) {
		ResponseBuilder responseBuilder = Response.status(Status.BAD_REQUEST);
		responseBuilder.header(STATUS_CODE_HEADER, CommonErrorCode.BAD_REQUEST.getStatusCode());
		LOG.warn("Bad Request", badRequestException);
		return responseBuilder.build();
	}
	
	private Response handleException(InvalidAuthenticationException invalidAuthentificationException) {
		ResponseBuilder responseBuilder = Response.status(Status.UNAUTHORIZED);
		responseBuilder.header(STATUS_CODE_HEADER, invalidAuthentificationException.getErrorCode().getStatusCode());
		LOG.warn("User NOT authenticated.");
		return responseBuilder.build();
	}
	
	private Response handleException(Throwable throwable) {
		ResponseBuilder responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
		LOG.error("Unexpected error", throwable);
		return responseBuilder.build();
	}
}
