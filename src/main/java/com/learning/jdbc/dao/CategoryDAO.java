package com.learning.jdbc.dao;

import com.learning.jdbc.connection.ConnectionManager;
import com.learning.jdbc.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> getCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             Statement stm = connection.createStatement()) {
            connection.setAutoCommit(false);

            stm.execute("SELECT id, name FROM category");

            try (ResultSet rst = stm.getResultSet()) {
                boolean hadResults = false;

                while (rst.next()) {
                    hadResults = true;
                    categories.add(
                        new Category(
                            rst.getInt("id"),
                            rst.getString("name")
                        )
                    );
                }

                if (!hadResults) {
                    System.out.println("No products found");
                }
            }
        }
        return categories;
    }

    public List<Integer> insertCategories(List<Category> categories) {
        List<Integer> insertedKeys = new ArrayList<>();
        if (categories == null || categories.isEmpty()) {
            return insertedKeys;
        }

        System.out.println("\nInserting categories using single SQL query");

        try (Connection connection = ConnectionManager.getConnection()) {
            connection.setAutoCommit(true);
            StringBuilder query = new StringBuilder("INSERT INTO category (name) VALUES ");
            var it = categories.iterator();
            int entries = 0;
            while (it.hasNext()) {
                query.append("(?),");
                entries++;
                it.next();
            }
            query.delete(query.length() - 1, query.length());
            try (PreparedStatement stm = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS)) {
                int categoryIndex = 0;
                for (int i = 1; i <= entries; i++) {
                    stm.setString(i, categories.get(categoryIndex).getName());
                    categoryIndex++;
                }
                stm.execute();
                try (ResultSet rs = stm.getGeneratedKeys()) {
                    categoryIndex = 0;
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        insertedKeys.add(id);
                        categories.get(categoryIndex).setId(id);
                        categoryIndex++;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("> Some exception happened");
        }
        System.out.println();
        return insertedKeys;
    }
}
