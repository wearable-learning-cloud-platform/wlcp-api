package org.wlcp.wlcpapi.security;

public class SecurityConstants {
    public static final String SECRET = "WlcpRocks!";
    public static final long EXPIRATION_TIME = 3600000; // 60 minutes
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/usernameController/registerUser";
}
