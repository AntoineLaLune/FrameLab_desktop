package fr.bts.iris.slam.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParticipationTest {

    @Test
    public void testParticipationConstructor() {
        // Arrange & Act
        Participation participation = new Participation(
                17,
                "/participations/file-1778883501086.png",
                false,
                "2026-06-11 23:27:00",
                1,
                3
        );

        // Assert
        assertEquals(17, participation.getId(), "ID should match the constructor parameter");
        assertEquals("/participations/file-1778883501086.png", participation.getPhoto_url(), "Photo URL should match the constructor parameter");
        assertFalse(participation.getIs_hidden(), "Hidden status should be false as set in constructor");
        assertEquals("2026-06-11 23:27:00", participation.getCreated(), "Creation date should match the constructor parameter");
        assertEquals(1, participation.getChallenge_id(), "Challenge ID should match the constructor parameter");
        assertEquals(3, participation.getUser_id(), "User ID should match the constructor parameter");
    }

    @Test
    public void testParticipationSetters() {
        // Arrange
        Participation participation = new Participation(0, "", true, "", 0, 0);

        // Act
        participation.setId(17);
        participation.setPhoto_url("/participations/file-1778883501086.png");
        participation.setIs_hidden(false);
        participation.setCreated("2026-06-11 23:27:00");
        participation.setChallenge_id(1);
        participation.setUser_id(3);

        // Assert
        assertEquals(17, participation.getId(), "ID should be updated correctly");
        assertEquals("/participations/file-1778883501086.png", participation.getPhoto_url(), "Photo URL should be updated correctly");
        assertFalse(participation.getIs_hidden(), "Hidden status should be updated correctly");
        assertEquals("2026-06-11 23:27:00", participation.getCreated(), "Creation date should be updated correctly");
        assertEquals(1, participation.getChallenge_id(), "Challenge ID should be updated correctly");
        assertEquals(3, participation.getUser_id(), "User ID should be updated correctly");
    }
}