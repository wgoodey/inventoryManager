package com.whitneygoodey.inventoryManager.partsAndProducts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Inventory class is a utility class that simulates an inventory of Parts and Products.
 * @author Whitney Goodey
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * Adds a Part to the allParts list.
     * @param newPart the part to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a Product to the allProducts list.
     * @param newProduct the product to add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Looks up the index in the allParts list of the part id queried.
     * @param id the id of the part whose index is being searched.
     * @return the index of the part or -1 if not found.
     */
    public static int getPartIndex(int id) {
        int index;
        for(index = 0; index<Inventory.getAllParts().size(); index++) {
            if(id == Inventory.allParts.get(index).getId()) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Looks up the index in the allProducts list of the product id queried.
     * @param id the id of the product whose index is being searched.
     * @return the index of the product or -1 if not found.
     */
    public static int getProductIndex(int id) {
        int index;
        for(index = 0; index<Inventory.getAllProducts().size(); index++) {
            if(id == Inventory.allProducts.get(index).getId()) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Looks up the index in the allParts list of the part id queried.
     * @param partId the id of the product whose index is being searched.
     * @return the Part or null if not found.
     */
    public static Part lookupPart(int partId) {
        //find part in allParts and return it
        for(Part part : allParts) {
            if(part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * Iterates through the parts list and returns a filtered list of all parts that match the search string
     * @param name the name of the part to lookup
     * @return a filtered list of Parts
     */
    public static ObservableList<Part> lookupPart(String name) {
        ObservableList<Part> filteredList = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().contains(name)) {
                filteredList.add(part);
            }
        }
        return filteredList;
    }

    /**
     * Looks up the index in the allProducts list of the product id queried.
     * @param productId the id of the product whose index is being searched.
     * @return the Product or null if not found.
     */
    public static Product lookupProduct(int productId) {
        //find product in allProducts and return it
        for(Product product : allProducts) {
            if(product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Iterates through the product list and returns a filtered list of all products that match the search string
     * @param name the name of the product to lookup
     * @return a filtered list of products
     */
    public static ObservableList<Product> lookupProduct(String name) {
        ObservableList<Product> filteredList = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().contains(name)) {
                filteredList.add(product);
            }
        }
        return filteredList;
    }

    /**
     * Updates the data of an existing part in the list.
     * @param index the index of selectedPart in allParts
     * @param selectedPart the part to update
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates the data of an existing product in the list.
     * @param index the index of selectedProduct in allProducts
     * @param selectedProduct the product to update
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Deletes a part from the Parts list.
     * @param selectedPart the part to delete
     * @return
     */
    public static boolean deletePart(Part selectedPart) {
        for(Part part : allParts) {
            if (part == selectedPart) {
                allParts.remove(selectedPart);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a product from the Products list.
     * @param selectedProduct the product to delete
     * @return
     */
    public static boolean deleteProduct(Product selectedProduct) {
        for(Product product : allProducts) {
            if(product == selectedProduct) {
                allProducts.remove(selectedProduct);
                return true;
            }
        }
        return false;
    }

    /**
     * @return the allParts ObservableList
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return the allProducts ObservableList
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}