package fr.bts.iris.slam.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionManager {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:framelab.db");
            initializeTables();
        }
        return connection;
    }

    private static void initializeTables() {
        String[] queries = {
            """
            CREATE TABLE IF NOT EXISTS project (
                id             INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name           TEXT NOT NULL,
                user_id        INTEGER NOT NULL,
                challenge_id   INTEGER NOT NULL,
                challenge_name TEXT NOT NULL
            );
            """,
                    """
            CREATE TABLE IF NOT EXISTS layer (
                id         INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name       TEXT NOT NULL,
                project_id INTEGER NOT NULL,
                FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
            );
            """
        };

        for (String query : queries) {
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create table(s): " + e.getMessage(), e);
            }
        }
    }

}
