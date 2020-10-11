package org.wlcp.wlcpapi.exception;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.wlcp.wlcpapi.response.ErrorResponse;
import org.wlcp.wlcpapi.response.SubErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = RuntimeException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> runtimeExceptionErrorHandler(HttpServletRequest req, RuntimeException ex) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCause().getMessage(), ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> onConstraintValidationException(ConstraintViolationException ex) {
	    return null;
    }
	
	@Override
	@ResponseBody
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "There was an error validating one or more fields", ex);
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			errorResponse.getSubErrors().add(new SubErrorResponse(fieldError.getField() + " " + fieldError.getDefaultMessage()));
		}
		return new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
