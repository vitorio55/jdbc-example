package com.learning.jdbc;

import com.learning.jdbc.model.Category;
import com.learning.jdbc.repository.Repository;
import com.learning.jdbc.model.Product;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class DatabaseOperationsExample {

    public static void main(String[] args) throws SQLException {
        Repository repo = new Repository();

        repo.deleteProducts();
        repo.deleteCategories();

        Category notebooks = new Category("Notebooks");
        Category smartWatches = new Category("Smart watches");

        List<Category> categories = Arrays.asList(
            notebooks, smartWatches
        );

        repo.insertCategories(categories);
        printDatabaseCategories();

        List<Product> products = Arrays.asList(
            new Product("Notebook #1", "A very fast and expensive notebook", notebooks),
            new Product("Notebook #2", "A slow and cheap notebook", notebooks),
            new Product("Smart watch #1", "Smart watch from a fruit brand", smartWatches),
            new Product("Smart watch #2", "Smart watch from a korean brand", smartWatches)
        );

        repo.insertProducts(products);
        printDatabaseProducts();
    }

    private static void printDatabaseProducts() throws SQLException {
        Repository repo = new Repository();
        List<Product> productsFromDb = repo.getProductsWithCategory();
        if (!productsFromDb.isEmpty()) {
            System.out.println("Retrieved products from Database:");
            productsFromDb.forEach(System.out::println);
        }
        System.out.println();
    }

    private static void printDatabaseCategories() throws SQLException {
        Repository repo = new Repository();
        List<Category> categoriesFromDb = repo.getCategories();
        if (!categoriesFromDb.isEmpty()) {
            System.out.println("Retrieved categories from Database:");
            categoriesFromDb.forEach(System.out::println);
        }
        System.out.println();
    }
}
