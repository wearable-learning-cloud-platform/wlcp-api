package org.wlcp.wlcpapi.exception;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.wlcp.wlcpapi.response.ErrorResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GlobalFilterExceptionHandler extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception ex) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setContentType("application/json");
			response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getCause().getMessage(), ex)));
		}

	}

}
