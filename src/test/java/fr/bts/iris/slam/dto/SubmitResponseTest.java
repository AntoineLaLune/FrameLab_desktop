package fr.bts.iris.slam.dto;

import tools.jackson.databind.ObjectMapper;
import fr.bts.iris.slam.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubmitResponseTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        SubmitResponse response = new SubmitResponse();
        User mockUser = new User();
        mockUser.setId(3);
        mockUser.setEmail("kris@dreemurr.net");

        // Act
        response.setSuccess("true");
        response.setMessage("Submission successful");
        response.setData(mockUser);

        // Assert
        assertEquals("true", response.getSuccess(), "Success status should match the set value");
        assertEquals("Submission successful", response.getMessage(), "Message should match the set value");
        assertNotNull(response.getData(), "Data object should not be null after being set");
        assertEquals(3, response.getData().getId(), "The User inside data should maintain its properties");
    }

    @Test
    public void testJsonSerializationIgnoresAbsentFields() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        SubmitResponse response = new SubmitResponse();

        // We set success and message, but we intentionally leave 'data' as null
        response.setSuccess("false");
        response.setMessage("Missing user data");

        // Act
        String jsonResult = mapper.writeValueAsString(response);

        // Assert
        assertTrue(jsonResult.contains("\"success\":\"false\""), "JSON should contain the success field");
        assertTrue(jsonResult.contains("\"message\":\"Missing user data\""), "JSON should contain the message field");

        // Because of @JsonInclude(JsonInclude.Include.NON_ABSENT), 'data' should NOT be in the JSON output
        assertFalse(jsonResult.contains("\"data\""), "JSON should NOT contain the 'data' field when it is null");
    }
}