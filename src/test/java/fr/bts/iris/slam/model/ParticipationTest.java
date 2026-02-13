package fr.bts.iris.slam.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipationTest {

    Participation participation = new Participation(1, "photo.png", false, "01012000",1, 1);

    @Test void shouldReturnId() {assertEquals(1, participation.getId());}


}
