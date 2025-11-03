package io.exterminator3618.server.utils;

public class UsernameRequirementsCheck {

    public static final String ALLOWED_SPECIAL_CHARACTERS = "_-.";

    public static boolean check(String username) {
        if (username == null) {
            return false;
        }
        if (username.length() < 3 || username.length() > 20) {
            return false;
        }
        if (!Character.isLetter(username.charAt(0))) {
            return false;
        }
        for (char c : username.toCharArray()) {
            // only allow ascii letters, digits, and specific special characters
            if (c > 127) {
                return false;
            }
            if (!Character.isLetterOrDigit(c) && ALLOWED_SPECIAL_CHARACTERS.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

}
