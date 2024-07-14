package com.eliferden.expensetracker.util;

public class ValidationUtil {
    public static void validateUserId (Long id) {
        if (id == 0 || id <= 0) throw new IllegalArgumentException("Invalid User ID: " + id);

    }
    public static void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password must not be null or empty");
        }
    }

    public static void validateCategoryId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid category ID");
        }
    }

    public static void validateBudgetId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid budget ID");
        }
    }

    public static void validateExpenseId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid budget ID");
        }
    }
}
