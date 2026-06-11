package fr.bts.iris.slam.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserGettersAndSetters() {
        // Arrange
        User user = new User();

        // Act
        user.setId(3);
        user.setEmail("kris@dreemurr.net");
        user.setPassword("securePassword123");
        user.setLast_name("Dreemurr");
        user.setFirst_name("Kris");
        user.setIs_admin(true);

        // Assert
        assertEquals(3, user.getId(), "ID should match the set value");
        assertEquals("kris@dreemurr.net", user.getEmail(), "Email should match the set value");
        assertEquals("securePassword123", user.getPassword(), "Password should match the set value");
        assertEquals("Dreemurr", user.getLast_name(), "Last name should match the set value");
        assertEquals("Kris", user.getFirst_name(), "First name should match the set value");
        assertTrue(user.isIs_admin(), "Admin status should be true as set");
    }

    @Test
    public void testUserDefaultValues() {
        // Arrange
        User user = new User();

        // Assert
        // Testing that default values are properly handled before setters are called
        assertEquals(0, user.getId(), "Default ID for int should be 0");
        assertNull(user.getEmail(), "Default value for String should be null");
        assertNull(user.getPassword(), "Default value for String should be null");
        assertNull(user.getLast_name(), "Default value for String should be null");
        assertNull(user.getFirst_name(), "Default value for String should be null");
        assertFalse(user.isIs_admin(), "Default value for boolean should be false");
    }
}