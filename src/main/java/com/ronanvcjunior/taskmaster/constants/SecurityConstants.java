package com.ronanvcjunior.taskmaster.constants;

public class SecurityConstants {
    public static final Integer STRENGTH = 12;
    public static final int NINETY_DAYS = 90;

    public static final String BASE_PATH = "/**";
    public static final String[] PUBLIC_URLS = {
            "/user/register/**",
            "/user/verify/account/**",
            "/user/login/**"
    };
    public static final String [] PUBLIC_ROUTES = {
            "/user/register",
            "/user/verify/account",
            "/user/login"
    };

}
