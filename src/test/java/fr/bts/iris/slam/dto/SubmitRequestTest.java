package fr.bts.iris.slam.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubmitRequestTest {

    @Test
    public void testDefaultValues() {
        // Arrange & Act
        SubmitRequest request = new SubmitRequest();

        // Assert
        // There are no setters or constructors, SubmitRequest is not used @TODO
        assertEquals(0, request.getId(), "Default ID should be 0");
        assertNull(request.getName(), "Default name should be null");
        assertNull(request.getImage(), "Default image should be null");
    }
}