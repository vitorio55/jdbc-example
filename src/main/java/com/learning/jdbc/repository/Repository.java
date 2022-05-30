package com.learning.jdbc.repository;

import com.learning.jdbc.connection.ConnectionManager;
import com.learning.jdbc.dao.CategoryDAO;
import com.learning.jdbc.dao.ProductDAO;
import com.learning.jdbc.model.Category;
import com.learning.jdbc.model.Product;

import java.sql.*;
import java.util.List;

public class Repository {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    public Repository() {
        this.categoryDAO = new CategoryDAO();
        this.productDAO = new ProductDAO();
    }

    public List<Product> getProducts() throws SQLException {
        return this.productDAO.getProducts();
    }

    public List<Product> getProductsWithCategory() throws SQLException {
        return this.productDAO.getProductsWithCategory();
    }

    public List<Category> getCategories() throws SQLException {
        return this.categoryDAO.getCategories();
    }

    public void insertCategories(List<Category> categories) {
        this.categoryDAO.insertCategories(categories);
    }

    public void insertProducts(List<Product> products) {
        this.productDAO.insertProducts(products);
    }

    public void insertProductsUsingTransaction(List<Product> products) {
        this.productDAO.insertProductsUsingTransaction(products);
    }

    public void deleteProducts() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             Statement stm = connection.createStatement()) {
            stm.execute("DELETE FROM product");
        }
    }

    public void deleteCategories() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             Statement stm = connection.createStatement()) {
            stm.execute("DELETE FROM category");
        }
    }
}
