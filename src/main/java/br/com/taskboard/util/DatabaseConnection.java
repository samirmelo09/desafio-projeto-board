package br.com.taskboard.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url == null || user == null || password == null) {
            throw new RuntimeException("Variáveis de ambiente do banco não configuradas!");
        }

        return DriverManager.getConnection(url, user, password);
    }
}

