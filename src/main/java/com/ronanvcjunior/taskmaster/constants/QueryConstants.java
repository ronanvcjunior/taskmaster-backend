package com.ronanvcjunior.taskmaster.constants;

public class QueryConstants {
    // User
    public final static String SELECT_ALL_USERS = "SELECT u.*, ur.role_id FROM users u " +
            "JOIN user_roles ur ON u.id = ur.user_id " +
            "JOIN roles r ON ur.role_id = r.id " +
            "WHERE u.email <> 'system@gmail.com'";

    // Task
    public final static String CREATE_TASK = "SELECT MAX(task_order) FROM tasks WHERE user_id = :userId";
    public final static String SELECT_TASK = "SELECT * FROM tasks WHERE user_id = :userId and task_id = :taskId";
    public final static String SELECT_TASK_BY_ORDER = "SELECT * FROM tasks WHERE user_id = :userId and task_order = :order";
    public final static String SELECT_TASKS_BY_ORDER_GREATER = "SELECT * FROM tasks WHERE user_id = :userId and task_order > :order";
    public final static String SELECT_ALL_TASKS = "SELECT * FROM tasks WHERE user_id = :userId";
}
