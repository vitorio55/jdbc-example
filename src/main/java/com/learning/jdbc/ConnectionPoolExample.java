package com.learning.jdbc;

import com.learning.jdbc.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolExample {
    public static void main(String[] args) throws SQLException {
        System.out.println("Testing limiting the number of connections...");

        // Change the maximumPoolSize in the configuration class and check the result here
        // If the maximumPoolSize configured is less than the number of iterations in the loop
        // we can see the fetching of the connection blocking until the timeout is reached

        for (int i = 0; i < 20; i++) {
            System.out.printf("Getting connection %s%n", i);
            Connection c = ConnectionManager.getConnection();
        }
        System.out.println("Finished");
    }
}
