package fr.bts.iris.slam.dto;

import tools.jackson.databind.ObjectMapper;
import fr.bts.iris.slam.model.Challenge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChallengeResponseTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        ChallengeResponse response = new ChallengeResponse();
        Challenge mockChallenge = new Challenge();
        mockChallenge.setId(1);
        mockChallenge.setTitle("UnNonRenard");

        // Act
        response.setSuccess("true");
        response.setMessage("Challenge retrieved successfully");
        response.setChallenge(mockChallenge);

        // Assert
        assertEquals("true", response.getSuccess(), "Success status should match the set value");
        assertEquals("Challenge retrieved successfully", response.getMessage(), "Message should match the set value");
        assertNotNull(response.getChallenge(), "Challenge object should not be null after being set");
        assertEquals(1, response.getChallenge().getId(), "The Challenge inside the response should maintain its properties");
    }

    @Test
    public void testJsonSerializationIgnoresAbsentFields() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        ChallengeResponse response = new ChallengeResponse();

        response.setSuccess("false");
        response.setMessage("Challenge not found");
        // 'challenge' as null intentionally

        // Act
        String jsonResult = mapper.writeValueAsString(response);

        // Assert
        assertTrue(jsonResult.contains("\"success\":\"false\""), "JSON should contain the success field");
        assertTrue(jsonResult.contains("\"message\":\"Challenge not found\""), "JSON should contain the message field");
        assertFalse(jsonResult.contains("\"challenge\""), "JSON should NOT contain the 'challenge' field when it is null");
    }
}