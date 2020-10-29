package org.wlcp.wlcpapi.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.wlcp.wlcpapi.security.SecurityConstants.EXPIRATION_TIME;
import static org.wlcp.wlcpapi.security.SecurityConstants.HEADER_STRING;
import static org.wlcp.wlcpapi.security.SecurityConstants.SECRET;
import static org.wlcp.wlcpapi.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.wlcp.wlcpapi.datamodel.master.Username;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            Username username = new ObjectMapper()
                    .readValue(req.getInputStream(), Username.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                    		username.getUsernameId(),
                    		username.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        
    	Cookie cookie = new Cookie("wlcp.userSession", token);
		cookie.setMaxAge((int)EXPIRATION_TIME / 1000);
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		res.addCookie(cookie);
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
    	throw failed;
	}
}
