package fr.bts.iris.slam.dao;

import fr.bts.iris.slam.model.Layer;
import fr.bts.iris.slam.model.Project;

import java.sql.*;
import java.util.ArrayList;

public class ProjectDAO {

    private final Connection connection;

    public ProjectDAO() throws SQLException {
        this.connection = ConnectionManager.getConnection();
    }

    public ArrayList<Project> getAll(int user_id) {
        ArrayList<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM project WHERE user_id = ? ORDER BY id";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                projects.add(new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("user_id"),
                        rs.getInt("challenge_id"),
                        rs.getString("challenge_name")
                ));
            }
            return projects;
        } catch (SQLException e) {
            throw new RuntimeException("Get projects failed", e);
        }
    }

    public Project getLast(int user_id) {
        Project project;
        String sql = "SELECT * FROM project WHERE user_id = ? ORDER BY id DESC LIMIT 1";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
                project = new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("user_id"),
                        rs.getInt("challenge_id"),
                        rs.getString("challenge_name")
                );
            return project;
        } catch (SQLException e) {
            throw new RuntimeException("Get last project failed", e);
        }
    }

    public int getLastId(int user_id) {
        String sql = "SELECT id FROM project WHERE user_id = ? ORDER BY id DESC LIMIT 1";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException("Get last project id failed", e);
        }
    }

    public void insert(Project project) {
        String sql = "INSERT INTO project (name, user_id, challenge_id, challenge_name) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, project.getName());
            pstmt.setInt(2, project.getUser_id());
            pstmt.setInt(3, project.getChallenge_id());
            pstmt.setString(4, project.getChallenge_name());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    project.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert project: " + e.getMessage(), e);
        }
    }

    private void update(Project project) {
        String sql = "UPDATE project SET name = ? user_id = ? challenge_id = ? challenge_name = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, project.getName());
            pstmt.setInt(2, project.getUser_id());
            pstmt.setInt(3, project.getChallenge_id());
            pstmt.setString(4, project.getChallenge_name());

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Project not found for id: " + project.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Update failed", e);
        }
    }

    public void save(Project project) throws SQLException {
        if (project.getId() < 0) {
            update(project);
        } else {
            insert(project);
        }
        LayerDAO layerDAO = new LayerDAO();
        layerDAO.deleteByProjectId(project.getId());
        for (int i = 0; i < project.getLayers().size(); i++) {
            layerDAO.save(project.getLayers().get(i), project.getId());
        }
    }

    public boolean deteteById(int id) {
        ArrayList<Project> projects = new ArrayList<>();
        String sql = "DELETE FROM project WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete project failed", e);
        }
    }

}
