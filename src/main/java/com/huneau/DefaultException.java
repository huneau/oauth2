package com.huneau;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DefaultException implements ExceptionMapper<Throwable> {
	@Override
	public Response toResponse(Throwable exception) {
		throw new WebApplicationException(exception.getMessage());
	}
}
