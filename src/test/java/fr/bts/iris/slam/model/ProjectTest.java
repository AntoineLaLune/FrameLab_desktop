package fr.bts.iris.slam.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {

    @Test
    public void testProjectConstructorWithoutIdAndLayers() {
        // Arrange & Act
        Project project = new Project("Test", 3, 1, "UnNonRenard");

        // Assert
        assertEquals(-1, project.getId(), "Default ID should be -1 when initialized without ID");
        assertEquals("Test", project.getName(), "Name should match the constructor parameter");
        assertNotNull(project.getLayers(), "Layers list should be initialized and not null");
        assertTrue(project.getLayers().isEmpty(), "Layers list should be empty initially");
        assertEquals(3, project.getUser_id(), "User ID should match the constructor parameter");
        assertEquals(1, project.getChallenge_id(), "Challenge ID should match the constructor parameter");
        assertEquals("UnNonRenard", project.getChallenge_name(), "Challenge name should match the constructor parameter");
    }

    @Test
    public void testProjectConstructorWithIdWithoutLayers() {
        // Arrange & Act
        Project project = new Project(2, "Test 2", 3, 1, "UnNonRenard");

        // Assert
        assertEquals(2, project.getId(), "ID should match the constructor parameter");
        assertEquals("Test 2", project.getName(), "Name should match the constructor parameter");
        assertNotNull(project.getLayers(), "Layers list should be initialized and not null");
        assertTrue(project.getLayers().isEmpty(), "Layers list should be empty initially");
        assertEquals(3, project.getUser_id(), "User ID should match the constructor parameter");
    }

    @Test
    public void testProjectConstructorWithAllParameters() {
        // Arrange
        ArrayList<Layer> initialLayers = new ArrayList<>();
        initialLayers.add(new Layer("Challenge Image", 3));

        // Act
        Project project = new Project(3, "Test 3", initialLayers, 1, 1, "UnNonRenard");

        // Assert
        assertEquals(3, project.getId());
        assertEquals(1, project.getLayers().size(), "Layers list should contain 1 item");
        assertEquals("Challenge Image", project.getLayers().get(0).getName(), "Layer name should match the added layer");
    }

    @Test
    public void testProjectSettersAndGetters() {
        // Arrange
        Project project = new Project("Temp", 0, 0, "");
        ArrayList<Layer> newLayers = new ArrayList<>();
        newLayers.add(new Layer("Challenge Image", 200));

        // Act
        project.setId(4);
        project.setName("Test 4");
        project.setLayers(newLayers);
        project.setUser_id(3);
        project.setChallenge_id(1);
        project.setChallenge_name("UnNonRenard");

        // Assert
        assertEquals(4, project.getId());
        assertEquals("Test 4", project.getName());
        assertEquals(1, project.getLayers().size());
        assertEquals(3, project.getUser_id());
        assertEquals(1, project.getChallenge_id());
        assertEquals("UnNonRenard", project.getChallenge_name());
    }

    @Test
    public void testAddLayer() {
        // Arrange
        Project project = new Project("Test 5", 1, 1, "UnNonRenard");
        Layer layer1 = new Layer("Challenge Image", 1);
        Layer layer2 = new Layer("Layer", 1);

        // Act
        project.addLayer(layer1);
        project.addLayer(layer2);

        // Assert
        assertEquals(2, project.getLayers().size(), "Layers list should contain exactly 2 layers after adding");
        assertEquals("Challenge Image", project.getLayers().get(0).getName(), "First layer should match the first added item");
        assertEquals("Layer", project.getLayers().get(1).getName(), "Second layer should match the second added item");
    }
}