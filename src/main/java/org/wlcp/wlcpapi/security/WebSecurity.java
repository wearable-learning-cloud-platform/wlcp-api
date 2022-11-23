package org.wlcp.wlcpapi.security;

import static org.wlcp.wlcpapi.security.SecurityConstants.SIGN_UP_URL;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.wlcp.wlcpapi.exception.GlobalFilterExceptionHandler;
import org.wlcp.wlcpapi.service.UsernameService;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private UsernameService usernameService;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${security.jwt-secret}")
	private String jwtSecret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.logout()
    	.logoutSuccessHandler(new LogoutSuccessHandler() {
    	     
    	    @Override
    	    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
    	                Authentication authentication)
    	            throws IOException, ServletException {
    	    	
    	    	Cookie cookie = new Cookie("wlcp.userSession", null);
    			cookie.setMaxAge(0);
    			cookie.setPath("/");
    			cookie.setHttpOnly(false);
    			response.addCookie(cookie);
    	    }
    	});
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.GET, "/logout").permitAll()
                .antMatchers(HttpMethod.GET, "/objectStoreController/files/**").permitAll()
                .antMatchers(HttpMethod.GET, "/import.html").permitAll()
                .antMatchers(HttpMethod.POST, "/gameController/importJSONGame").permitAll()
                .antMatchers(HttpMethod.GET, "/gameController/exportJSONGame").permitAll()
                // Disabling mail endpoint .antMatchers(HttpMethod.POST, "/mailController/postMail").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new GlobalFilterExceptionHandler(), JWTAuthenticationFilter.class)
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtSecret))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtSecret))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usernameService).passwordEncoder(bCryptPasswordEncoder);
    }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }
}
