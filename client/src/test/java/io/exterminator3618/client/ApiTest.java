package io.exterminator3618.client;

import io.exterminator3618.client.api.ApiClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class ApiTest {

    @Test
    @Disabled("No server available for testing in GitHub Actions")
    public void testAccountRegistration() throws IOException, InterruptedException {
        String name = "Test User2";
        String username = "testuser112";
        String password = "password123";
        ApiClient apiClient = ApiClient.register(null, name, username, password);
        assertNotNull(apiClient);
        assertEquals(name, apiClient.getUserInfo().getName());
        assertEquals(username, apiClient.getUserInfo().getUsername());
        assertNotNull(apiClient.exportAuthToken());
        assertNotEquals("", apiClient.exportAuthToken());
    }

}
