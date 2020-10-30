package com.whitneygoodey.inventoryManager.partsAndProducts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *  The product class simulates a product in the inventory.
 *  It can be composed of individual Parts, which are stored in an ObservableList.
 * @author Whitney Goodey
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private Double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructor to create a new Product
     * @param id the product id
     * @param name the product name
     * @param price the product price
     * @param stock the product inventory level
     * @param min the minimum inventory
     * @param max the maximum inventory
     */
    public Product(int id, String name, Double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Copy constructor
     * @param product is copied into new Product
     */
    public Product(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.min = product.getMin();
        this.max = product.getMax();
        this.associatedParts.addAll(product.getAllAssociatedParts());
    }

    /**
     * Empty constructor
     */
    public Product() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }
    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the min to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @param part the part to add to the associated parts list
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * @param selectedAssociatedPart the part to delete from the associated parts list
     */
    public void deleteAssociatedPart(Part selectedAssociatedPart) {
        associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return the associatedParts list
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
