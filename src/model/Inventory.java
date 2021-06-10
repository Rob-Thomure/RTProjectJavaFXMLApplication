/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author robertthomure
 */
public class Inventory {
    
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<Part> allFilteredParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allFilteredProducts = FXCollections.observableArrayList();
    private static ObservableList<Product> tempProduct = FXCollections.observableArrayList();
    private static int autoPartId = 5;

    public static void addPart(Part newPart) {
        allParts.add(newPart);        
    }
    
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    public static void addFilteredPart(Part part) {
        allFilteredParts.add(part);
    }
    
    public static void addFilteredProduct(Product product) {
        allFilteredProducts.add(product);
    }

    public static Part lookupPart(int partId) { 
        Part matchingPart = null;        
        for(Part part: allParts) {
            if(part.getId() == partId) {
                matchingPart = part;
            }
        }       
        return matchingPart;
    }
    
    public static Product lookupProduct(int productId) {
        Product matchingProduct = null;
        for(Product product: allProducts) {
            if(product.getId() == productId) {
                matchingProduct = product;
            }
        }
        return matchingProduct;
    }
    
    public static ObservableList<Part> lookupPart(String partName) {        
        for(Part part: getAllParts()) {
            if(part.getName().contains(partName)) {
                allFilteredParts.add(part);
            }
        }
        return allFilteredParts;
    }

    public static ObservableList<Product> lookupProduct(String productName) {        
        for(Product product: getAllProducts()) {
            if(product.getName().contains(productName)) {
                allFilteredProducts.add(product);
            }
        }
        return allFilteredProducts;
    }
    
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }
    
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }
    
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }
    
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }
    
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    
    public static ObservableList<Part> getAllFilteredParts() {
        return allFilteredParts;
    }
    
    public static ObservableList<Product> getAllFilteredProducts() {
        return allFilteredProducts;
    }
    
    public static int getAutoPartId() {
        return autoPartId++;
    }
    
    public static void addTempProduct(Product newProduct) {
        tempProduct.add(newProduct);
    }
    
    public static ObservableList<Product> getTempProduct() {
        return tempProduct;
    }
}