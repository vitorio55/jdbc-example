package com.learning.jdbc.connection;

import java.sql.*;

public class ConnectionManager {

    public static final String CONNECTION_URL = "jdbc:mysql://localhost:3306";
    public static final String CONNECTION_USER = "root";
    public static final String CONNECTION_PASSWORD = "password";
    public static final String DATABASE = "virtual_store";

    public static Connection getConnection() throws SQLException {
        var conn = getConnectionUsingConnectionPool();
        // Creating database and tables if they don't exist whenever we want to access db is not optimized at all,
        // but it's just so we don't need to manually set everything up when running from scratch
        createDatabaseIfNotExists(conn);
        createCategoryTableIfNotExists(conn);
        createProductsTableIfNotExists(conn);
        return conn;
    }

    private static void createCategoryTableIfNotExists(Connection conn) throws SQLException {
        try (Statement stm = conn.createStatement()) {
            try {
                String query = "CREATE TABLE IF NOT EXISTS category " +
                    "(" +
                        "id INT AUTO_INCREMENT," +
                        "name VARCHAR(50) NOT NULL," +
                        "PRIMARY KEY(id)" +
                    ") Engine = InnoDB";
                stm.execute(query);
            } catch (SQLSyntaxErrorException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void createProductsTableIfNotExists(Connection conn) throws SQLException {
        try (Statement stm = conn.createStatement()) {
            try {
                String query = "CREATE TABLE IF NOT EXISTS product " +
                    "(" +
                        "id INT AUTO_INCREMENT," +
                        "name VARCHAR(50) NOT NULL," +
                        "description VARCHAR(255)," +
                        "category_id INT," +
                        "FOREIGN KEY (category_id) REFERENCES category(id)," +
                        "PRIMARY KEY(id)" +
                    ") Engine = InnoDB";
                stm.execute(query);
            } catch (SQLSyntaxErrorException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void createDatabaseIfNotExists(Connection conn) throws SQLException {
        try (Statement stm = conn.createStatement()) {
            try {
                stm.execute("USE " + DATABASE);
            } catch (SQLSyntaxErrorException ex) {
                if (ex.getMessage().toLowerCase().contains("unknown database")) {
                    stm.execute("CREATE DATABASE " + DATABASE);
                }
            }
        }
    }

    private static Connection getConnectionNotUsingConnectionPool() throws SQLException {
        System.out.println("(getting connection not using Connection Pool)");
        return DriverManager.getConnection(CONNECTION_URL, CONNECTION_USER, CONNECTION_PASSWORD);
    }

    private static Connection getConnectionUsingConnectionPool() throws SQLException {
        System.out.println("(getting connection using Hikari Connection Pool)");
        return HikariCPDataSource.getConnection();
    }
}
