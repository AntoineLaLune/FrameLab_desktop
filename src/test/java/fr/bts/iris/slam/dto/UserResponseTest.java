package fr.bts.iris.slam.dto;

import tools.jackson.databind.ObjectMapper;
import fr.bts.iris.slam.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserResponseTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        UserResponse response = new UserResponse();
        User mockUser = new User();
        mockUser.setFirst_name("Kris");

        // Act
        response.setSuccess("true");
        response.setMessage("User fetched successfully");
        response.setUserData(mockUser);

        // Assert
        assertEquals("true", response.getSuccess(), "Success status should match the set value");
        assertEquals("User fetched successfully", response.getMessage(), "Message should match the set value");
        assertNotNull(response.getUserData(), "UserData object should not be null after being set");
        assertEquals("Kris", response.getUserData().getFirst_name(), "The User inside userData should maintain its properties");
    }

    @Test
    public void testJsonSerializationIgnoresAbsentFields() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        UserResponse response = new UserResponse();

        response.setSuccess("false");
        response.setMessage("Permission denied");
        // We intentionally leave 'userData' as null

        // Act
        String jsonResult = mapper.writeValueAsString(response);

        // Assert
        assertTrue(jsonResult.contains("\"success\":\"false\""), "JSON should contain the success field");
        assertTrue(jsonResult.contains("\"message\":\"Permission denied\""), "JSON should contain the message field");
        assertFalse(jsonResult.contains("\"userData\""), "JSON should NOT contain the 'userData' field when it is null");
    }
}