package com.staff.filter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * アノテーションでバリデーションエラーが発生した場合に、値を集約するクラス
 *
 * @author zhizit
 *
 */
@Provider
public class BeanValidExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	public Response toResponse(ConstraintViolationException e) {
		ConstraintViolation<?> cv = (ConstraintViolation<?>) e.getConstraintViolations().toArray()[0];
		return Response.status(Response.Status.BAD_REQUEST).entity(cv.getMessage()).build();
	}
}