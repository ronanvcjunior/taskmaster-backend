package com.ronanvcjunior.taskmaster.constants;

import java.util.List;

public class SecurityConstants {
    public static final Integer STRENGTH = 12;
    public static final int NINETY_DAYS = 90;

    public static final String AUTHORITIES = "authorities";
    public static final String API_SYSTEM = "TASK_MASTER";
    public static final String EMPTY_VALUE = "empty";
    public static final String SAME_SITE = "SameSite";

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
    public static final String PATH_LOGIN = "/user/login";


    public static final String [] HTTP_DOMAINS = {
            "http://taskmaster.com",
            "http://localhost:4200",
            "http://localhost:3000"
    };

}
