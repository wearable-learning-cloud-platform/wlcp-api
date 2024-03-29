package org.wlcp.wlcpapi.security;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 86400000L; // 24 hours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/usernameController/registerUser";
}
