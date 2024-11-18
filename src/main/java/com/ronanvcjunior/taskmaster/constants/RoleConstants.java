package com.ronanvcjunior.taskmaster.constants;

public class RoleConstants {
    public static final String ROLE = "role";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTHORITY_DELIMITER = ",";
    public static final String USER_AUTHORITIES = "task:create,task:read,task:update,task:delete";
    public static final String ADMIN_AUTHORITIES = "user:create,user:read,user:update,user:delete,task:read,task:update,task:delete";
}
