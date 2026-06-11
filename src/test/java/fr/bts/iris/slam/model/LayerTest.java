package fr.bts.iris.slam.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LayerTest {

    @Test
    public void testLayerConstructorWithoutId() {
        // Arrange & Act
        Layer layer = new Layer("Challenge Image", 1);

        // Assert
        assertEquals(-1, layer.getId(), "Default ID should be -1 when initialized without ID");
        assertEquals("Challenge Image", layer.getName(), "Name should match the constructor parameter");
        assertEquals(1, layer.getProjectId(), "Project ID should match the constructor parameter");
    }

    @Test
    public void testLayerConstructorWithId() {
        // Arrange & Act
        Layer layer = new Layer(4, "Challenge Image", 2);

        // Assert
        assertEquals(4, layer.getId(), "ID should match the constructor parameter");
        assertEquals("Challenge Image", layer.getName(), "Name should match the constructor parameter");
        assertEquals(2, layer.getProjectId(), "Project ID should match the constructor parameter");
    }

    @Test
    public void testLayerSetters() {
        // Arrange
        Layer layer = new Layer("Layer", 0);

        // Act
        layer.setId(10);
        layer.setName("Second Layer");
        layer.setProjectId(3);

        // Assert
        assertEquals(10, layer.getId(), "ID should be updated correctly");
        assertEquals("Second Layer", layer.getName(), "Name should be updated correctly");
        assertEquals(3, layer.getProjectId(), "Project ID should be updated correctly");
    }
}