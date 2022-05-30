package com.learning.jdbc.dao;

import com.learning.jdbc.connection.ConnectionManager;
import com.learning.jdbc.model.Category;
import com.learning.jdbc.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getProducts() throws SQLException {
        List<Product> products = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             Statement stm = connection.createStatement()) {
            connection.setAutoCommit(false);

            stm.execute("SELECT id, name, description FROM product");

            try (ResultSet rst = stm.getResultSet()) {
                boolean hadResults = false;

                while (rst.next()) {
                    hadResults = true;
                    products.add(
                        new Product(
                            rst.getInt("id"),
                            rst.getString("name"),
                            rst.getString("description"),
                            null
                        )
                    );
                }

                if (!hadResults) {
                    System.out.println("No products found");
                }
            }
        }
        return products;
    }

    public List<Product> getProductsWithCategory() throws SQLException {
        List<Product> products = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             Statement stm = connection.createStatement()) {
            connection.setAutoCommit(false);

            stm.execute("SELECT " +
                "p.id," +
                "p.name," +
                "p.description," +
                "c.id AS category_id," +
                "c.name AS category_name " +
                "FROM product p " +
                "JOIN category c " +
                "ON p.category_id = c.id");

            try (ResultSet rst = stm.getResultSet()) {
                boolean hadResults = false;

                while (rst.next()) {
                    hadResults = true;
                    products.add(
                        new Product(
                            rst.getInt("id"),
                            rst.getString("name"),
                            rst.getString("description"),
                            new Category(
                                rst.getInt("category_id"),
                                rst.getString("category_name"))
                        )
                    );
                }

                if (!hadResults) {
                    System.out.println("No products found");
                }
            }
        }
        return products;
    }

    public List<Integer> insertProducts(List<Product> products) {
        List<Integer> insertedKeys = new ArrayList<>();
        if (products == null || products.isEmpty()) {
            return insertedKeys;
        }

        System.out.println("\nInserting products using single SQL query");

        try (Connection connection = ConnectionManager.getConnection()) {
            connection.setAutoCommit(true);
            try {
                StringBuilder query = new StringBuilder("INSERT INTO product (name, description, category_id) VALUES ");
                var it = products.iterator();
                int entries = 0;
                while (it.hasNext()) {
                    query.append("(?, ?, ?),");
                    entries++;
                    it.next();
                }
                query.delete(query.length() - 1, query.length());
                try (PreparedStatement stm = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS)) {
                    int prodIndex = 0;
                    for (int i = 1; i < entries * 3; i += 3) {
                        stm.setString(i, products.get(prodIndex).getName());
                        stm.setString(i + 1, products.get(prodIndex).getDescription());
                        stm.setInt(i + 2, products.get(prodIndex).getCategory().getId());
                        prodIndex++;
                    }
                    stm.execute();
                    try (ResultSet rs = stm.getGeneratedKeys()) {
                        while (rs.next()) {
                            insertedKeys.add(rs.getInt(1));
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("> Exception received. Rolling back transaction.");
                connection.rollback();
            }
        } catch (Exception ex) {
            System.out.println("> Some exception happened.");
        }
        System.out.println();
        return insertedKeys;
    }

    public void insertProductsUsingTransaction(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return;
        }

        System.out.println("\nInserting products using transaction");

        try (Connection connection = ConnectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try {
                for (Product p : products) {
                    int key = insertProduct(p.getName(), p.getDescription(), connection);
                    p.setId(key);
                    System.out.println("> Inserted product into DB: " + p);
                }
            } catch (Exception ex) {
                System.out.println("> Exception received. Rolling back transaction.");
                connection.rollback();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (Exception ex) {
            System.out.println("> Some exception happened.");
        }
    }

    public int insertProduct(String name, String description, Connection conn) throws SQLException {
        String query = "INSERT INTO product (name, description) VALUES (?, ?)";
        int key = 0;

        try (PreparedStatement stm = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, name);
            stm.setString(2, description);
            stm.execute();
            try (ResultSet rs = stm.getGeneratedKeys()) {
                rs.next();
                key = rs.getInt(1);
            }
        }
        System.out.printf(">> Prepared statement to insert (%s/%s)%n", name, description);
        return key;
    }
}
