package org.wlcp.wlcpapi.security;

import static org.wlcp.wlcpapi.security.SecurityConstants.SIGN_UP_URL;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.wlcp.wlcpapi.exception.GlobalFilterExceptionHandler;
import org.wlcp.wlcpapi.service.UsernameService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Autowired
    private UsernameService usernameService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Value("${security.jwt-secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

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

        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                .requestMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                                .requestMatchers(HttpMethod.GET, "/logout").permitAll()
                                .requestMatchers(HttpMethod.GET, "/objectStoreController/files/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/import.html").permitAll()
                                .requestMatchers(HttpMethod.POST, "/gameController/importJSONGame").permitAll()
                                .requestMatchers(HttpMethod.GET, "/gameController/exportJSONGame").permitAll()
                                // // Disabling mail endpoint .antMatchers(HttpMethod.POST,
                                // "/mailController/postMail").permitAll()
                                .anyRequest().authenticated())

                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new GlobalFilterExceptionHandler(), JWTAuthenticationFilter.class)
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtSecret))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtSecret));

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usernameService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider::authenticate;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
