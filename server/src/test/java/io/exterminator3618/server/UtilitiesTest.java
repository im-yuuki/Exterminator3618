package io.exterminator3618.server;

import io.exterminator3618.server.utils.PasswordHash;
import io.exterminator3618.server.utils.UsernameRequirementsCheck;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtilitiesTest {

    @Test
    public void testUsernameValidation() {
        assertTrue(UsernameRequirementsCheck.check("ValidUser123"));
        assertFalse(UsernameRequirementsCheck.check("ab")); // too short
        assertFalse(UsernameRequirementsCheck.check("ThisUsernameIsWayTooLongToBeValid1231231247126498162")); // too long
        assertFalse(UsernameRequirementsCheck.check("Invalid*User@@@")); // invalid character
        assertFalse(UsernameRequirementsCheck.check("User Name")); // space character
        assertFalse(UsernameRequirementsCheck.check("18127389172391")); // only numbers
    }

    @Test
    public void testUsernameEdgeCases() {
        assertTrue(UsernameRequirementsCheck.check("A1b2C3")); // minimum valid length
        assertTrue(UsernameRequirementsCheck.check("User_Name-123")); // valid special characters
        assertFalse(UsernameRequirementsCheck.check("")); // empty string
        assertFalse(UsernameRequirementsCheck.check("     ")); // only spaces
    }

    @Test
    public void testUsernameWithUnicodeCharacters() {
        assertFalse(UsernameRequirementsCheck.check("用户123")); // non-ASCII characters
        assertFalse(UsernameRequirementsCheck.check("ValidUser😊")); // emoji character
    }

    @Test
    public void testPasswordValidation() {
        String testPasswordHash = PasswordHash.hashPassword("SecureP@ssw0rd!");
        assertTrue(PasswordHash.verifyPassword("SecureP@ssw0rd!", testPasswordHash));
        assertFalse(PasswordHash.verifyPassword("WrongPassword", testPasswordHash));
    }

}
