package fr.bts.iris.slam.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String testEmail = "kris@dreemurr.net";
        String testPassword = "securePassword123";

        // Act
        LoginRequest request = new LoginRequest(testEmail, testPassword);

        // Assert
        assertEquals(testEmail, request.getEmail(), "The email getter should return the value passed in the constructor");
        assertEquals(testPassword, request.getPassword(), "The password getter should return the value passed in the constructor");
    }
}