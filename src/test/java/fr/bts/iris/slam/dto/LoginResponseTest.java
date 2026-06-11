package fr.bts.iris.slam.dto;

import tools.jackson.databind.ObjectMapper;
import fr.bts.iris.slam.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginResponseTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        LoginResponse response = new LoginResponse();
        User mockUser = new User();
        mockUser.setEmail("kris@dreemurr.net");

        // Act
        response.setSuccess("true");
        response.setMessage("Login successful");
        response.setData(mockUser);

        // Assert
        assertEquals("true", response.getSuccess(), "Success status should match the set value");
        assertEquals("Login successful", response.getMessage(), "Message should match the set value");
        assertNotNull(response.getData(), "Data object should not be null after being set");
        assertEquals("kris@dreemurr.net", response.getData().getEmail(), "The User inside data should maintain its properties");
    }

    @Test
    public void testJsonSerializationIgnoresAbsentFields() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        LoginResponse response = new LoginResponse();

        response.setSuccess("false");
        response.setMessage("Invalid credentials");
        // 'data' as null intentionally

        // Act
        String jsonResult = mapper.writeValueAsString(response);

        // Assert
        assertTrue(jsonResult.contains("\"success\":\"false\""), "JSON should contain the success field");
        assertTrue(jsonResult.contains("\"message\":\"Invalid credentials\""), "JSON should contain the message field");
        assertFalse(jsonResult.contains("\"data\""), "JSON should NOT contain the 'data' field when it is null");
    }
}