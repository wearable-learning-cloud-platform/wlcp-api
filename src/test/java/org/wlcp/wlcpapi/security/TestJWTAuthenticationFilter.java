package org.wlcp.wlcpapi.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wlcp.wlcpapi.datamodel.master.Username;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestJWTAuthenticationFilter {
	
	@Mock
	private AuthenticationManager authenticationManager;
	
	
	@Test
	public void testAttemptAuthenticationSuccess() throws IOException, ServletException {
		JWTAuthenticationFilter filterUnderTest = new JWTAuthenticationFilter(authenticationManager);
	    MockFilterChain chain = new MockFilterChain();
	    MockHttpServletRequest req = new MockHttpServletRequest();
	    req.setContent(new ObjectMapper().writeValueAsBytes(new Username("", "", "", "", "")));
	    MockHttpServletResponse res = new MockHttpServletResponse();
	    
	    when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(new TestAuthentication());

	    Authentication auth = filterUnderTest.attemptAuthentication(req, res);
	    
	    filterUnderTest.successfulAuthentication(req, res, chain, auth);
	}
	
	@Test
	public void testAttemptAuthenticationFailure() throws IOException, ServletException {
		JWTAuthenticationFilter filterUnderTest = new JWTAuthenticationFilter(authenticationManager);
	    MockFilterChain chain = new MockFilterChain();
	    MockHttpServletRequest req = new MockHttpServletRequest();
	    req.setContent(new ObjectMapper().writeValueAsBytes(new Username("", "", "", "", "")));
	    MockHttpServletResponse res = new MockHttpServletResponse();
	    
	    when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(new TestAuthentication());

	    Authentication auth = filterUnderTest.attemptAuthentication(req, res);
	    
	    Assertions.assertThrows(TestAuthenticationException.class, () -> {
	    	filterUnderTest.unsuccessfulAuthentication(req, res, new TestAuthenticationException("Message"));
	    });
       
	}

}

class TestAuthentication implements Authentication {

	private static final long serialVersionUID = 1L;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return new User("username", "password", new ArrayList<GrantedAuthority>());
		//return null;
	}

	@Override
	public boolean isAuthenticated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}
	
}

class TestAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public TestAuthenticationException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
}
