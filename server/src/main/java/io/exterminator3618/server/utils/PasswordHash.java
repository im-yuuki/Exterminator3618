package io.exterminator3618.server.utils;

public class PasswordHash {

    public static String hashPassword(String password) {
        // Simple hashing example (not secure, use a proper library like BCrypt in production)
        return Integer.toHexString(password.hashCode());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        } else if (password == null || password.isEmpty()) {
            return false;
        }
        return hashPassword(password).equals(hashedPassword);
    }

}
