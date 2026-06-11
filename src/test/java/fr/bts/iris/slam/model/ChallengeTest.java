package fr.bts.iris.slam.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChallengeTest {

    @Test
    public void testChallengeGettersAndSetters() {
        // Arrange
        Challenge challenge = new Challenge();

        // Act
        challenge.setId(1);
        challenge.setTitle("UnNonRenard");
        challenge.setDescription("Es-ce un renard ? Faites en sorte que non !");
        challenge.setPhoto_url("/challenges/photoUrl-1774113081488.png");
        challenge.setStart_date("2026-03-21 12:00:00");
        challenge.setEnd_date("2026-03-29 00:00:00");
        challenge.setIs_active(1);
        challenge.setCreator_id(1);

        // Assert
        assertEquals(1, challenge.getId(), "ID should match the set value");
        assertEquals("UnNonRenard", challenge.getTitle(), "Title should match the set value");
        assertEquals("Es-ce un renard ? Faites en sorte que non !", challenge.getDescription(), "Description should match the set value");
        assertEquals("/challenges/photoUrl-1774113081488.png", challenge.getPhoto_url(), "Photo URL should match the set value");
        assertEquals("2026-03-21 12:00:00", challenge.getStart_date(), "Start date should match the set value");
        assertEquals("2026-03-29 00:00:00", challenge.getEnd_date(), "End date should match the set value");
        assertEquals(1, challenge.getIs_active(), "Active status should match the set value");
        assertEquals(1, challenge.getCreator_id(), "Creator ID should match the set value");
    }
}