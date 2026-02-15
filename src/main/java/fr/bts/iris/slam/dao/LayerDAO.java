package fr.bts.iris.slam.dao;

import fr.bts.iris.slam.model.Layer;

import java.sql.*;
import java.util.ArrayList;

public class LayerDAO {

    private final Connection connection;

    public LayerDAO() throws SQLException {
        this.connection = ConnectionManager.getConnection();
    }

    public ArrayList<Layer> getByProjectId(int project_id) {
        ArrayList<Layer> projects = new ArrayList<>();
        String sql = "SELECT * FROM layer WHERE project_id = ? ORDER BY id";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, project_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                projects.add(new Layer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("project_id")
                ));
            }
            return projects;
        } catch (SQLException e) {
            throw new RuntimeException("Get projects failed", e);
        }
    }

    public void save(Layer layer, int project_id) {
        String sql = "INSERT INTO layer (name, project_id) VALUES (?, ?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, layer.getName());
            pstmt.setInt(2, project_id);
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    layer.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert layer: " + e.getMessage(), e);
        }
    }

    public boolean deteteById(int id) {
        ArrayList<Layer> projects = new ArrayList<>();
        String sql = "DELETE FROM layer WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete layer failed", e);
        }
    }

    public boolean deleteByProjectId(int project_id) {
        ArrayList<Layer> projects = new ArrayList<>();
        String sql = "DELETE FROM layer WHERE project_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, project_id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete layer failed", e);
        }
    }

}
