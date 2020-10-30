package com.whitneygoodey.inventoryManager;

import com.whitneygoodey.inventoryManager.partsAndProducts.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * The Controller class provides the code for the main window of the inventory manager.
 * @author Whitney Goodey
 * @version 1.0
 */
public class MainWindowController {

    @FXML
    private TextField partSearchBar;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> partIDCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInventoryCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TextField productSearchBar;
    @FXML
    private TableView productsTable;
    @FXML
    private TableColumn<Product, Integer> productIDCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Integer> productInventoryCol;
    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private Button addPartButton;
    @FXML
    private Button modifyPartButton;
    @FXML
    private Button deletePartButton;
    @FXML
    private Button addProductButton;
    @FXML
    private Button modifyProductButton;
    @FXML
    private Button deleteProductButton;
    @FXML
    private Button exitButton;


    /**
     * Loads parts and products tableViews into the main window.
     */
    public void initialize() {
        //set tableView for all parts in inventory
        loadPartsTable(partsTable, partIDCol, partNameCol, partInventoryCol, partPriceCol);

        //wrap observable list in a filtered list
        FilteredList<Part> filteredParts = new FilteredList<>(Inventory.getAllParts(), p -> true);

        //configure listener for parts searchbar
        partSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredParts.setPredicate(part -> {
                if((newValue == null) || (newValue.isEmpty())) {
                    return true;
                }
                String lowerCase = newValue.toLowerCase();

                if(part.getName().toLowerCase().contains(lowerCase)) {
                    return true;
                } else if (String.valueOf((part.getId())).contains(lowerCase)) {
                    return true;
                }
                return false;
            });
        });

        //wrap the filtered list in a sorted list
        SortedList<Part> sortedParts = new SortedList(filteredParts);
        //bind sortedParts comparator to partsTable comparator
        sortedParts.comparatorProperty().bind(partsTable.comparatorProperty());
        //add data to the TableView
        partsTable.setItems(sortedParts);

        //set tableView for all products in inventory
        loadProductsTable(productsTable,productIDCol,productNameCol,productInventoryCol,productPriceCol);

        //wrap observable list in a filtered list
        FilteredList<Product> filteredProducts = new FilteredList<>(Inventory.getAllProducts(), p -> true);

        //configure listener for products searchbar
        productSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredProducts.setPredicate(product -> {
                if((newValue == null) || (newValue.isEmpty())) {
                    return true;
                }
                String lowerCase = newValue.toLowerCase();

                if(product.getName().toLowerCase().contains(lowerCase)) {
                    return true;
                } else if (String.valueOf((product.getId())).contains(lowerCase)) {
                    return true;
                }
                return false;
            });
        });

        //wrap the filtered list in a sorted list
        SortedList<Product> sortedProducts = new SortedList(filteredProducts);
        //bind sortedProducts comparator to productsTable comparator
        sortedProducts.comparatorProperty().bind(productsTable.comparatorProperty());
        //add data to the TableView
        productsTable.setItems(sortedProducts);


    }


    /**
     * Handler manages clicks for all buttons on main window.
     * @param event button clicked on mainWindow
     */
    @FXML
    private void handleButtonClick(ActionEvent event) {
        Object clickedButton = event.getSource();

        //if exit or an add button pressed
        if(clickedButton.equals(exitButton)) {
            Platform.exit();
        } else if(clickedButton.equals(addPartButton)) {
            openPartForm();
        } else if(clickedButton.equals(addProductButton)) {
                openProductForm();
        }

        //record selected part
        if (partsTable.getSelectionModel().getSelectedItem() != null) {
            Part selectedPart = partsTable.getSelectionModel().getSelectedItem();

            //if modify part or delete part is pressed
            if (clickedButton.equals(modifyPartButton)) {
                openPartForm(selectedPart);

            } else if (clickedButton.equals(deletePartButton)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete part");
                alert.setHeaderText("Delete " + selectedPart.getName());
                alert.setContentText("Press Okay to confirm or Cancel to abort.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && (result.get() == ButtonType.OK)) {
                    Inventory.deletePart(selectedPart);
                }
            }
        }

        //record selected product
        if (productsTable.getSelectionModel().getSelectedItem() != null) {
            Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();

            //if modify product or delete product is pressed
            if (clickedButton.equals(modifyProductButton)) {
                openProductForm(selectedProduct);
            } else if (clickedButton.equals(deleteProductButton)) {
                if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Delete product");
                    alert.setHeaderText(selectedProduct.getName() + " still has associated parts.");
                    alert.setContentText("Remove all associated parts before deleting.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete product");
                    alert.setHeaderText("Delete " + selectedProduct.getName());
                    alert.setContentText("Press Okay to confirm or Cancel to abort.");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && (result.get() == ButtonType.OK)) {
                        Inventory.deleteProduct(selectedProduct);
                    }
                }
            }
        }
    }


    /**
     * open new window to add a new part to the inventory.
     */
    public void openPartForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("partForm.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Part");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * open new window to modify an existing part to the inventory.
     * @param selectedPart the part used to fill in data
     */
    private void openPartForm(Part selectedPart) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("partForm.fxml"));
            Parent root = fxmlLoader.load();
            PartFormController controller = fxmlLoader.getController();
            controller.initialize(selectedPart);
            Stage stage = new Stage();
            stage.setTitle("Modify Part");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Opens new window to add a new product to the inventory.
     */
    public void openProductForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productForm.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Product");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens new window to modify an existing product in the inventory.
     * @param selectedProduct the product used to fill in data
     */
    public void openProductForm(Product selectedProduct) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productForm.fxml"));
            Parent root = fxmlLoader.load();
            ProductFormController controller = fxmlLoader.getController();
            controller.initialize(selectedProduct);
            Stage stage = new Stage();
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();    
        }
    }


    /**
     * Loads parts tableView.
     * @param partsTable the table to set
     * @param partIDCol the ID column to set
     * @param partNameCol the name column to set
     * @param partInventoryCol the inventory column to set
     * @param partPriceCol the price column to set
     */
    public static void loadPartsTable(TableView<Part> partsTable,
                               TableColumn<Part, Integer> partIDCol,
                               TableColumn<Part, String> partNameCol,
                               TableColumn<Part, Integer> partInventoryCol,
                               TableColumn<Part, Double> partPriceCol) {
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setPlaceholder(new Label("No parts found."));
    }


    /**
     * Loads products tableView.
     * @param productsTable the table to set
     * @param productIDCol the ID column to set
     * @param productNameCol the name column to set
     * @param productInventoryCol the inventory column to set
     * @param productPriceCol the price column to set
     */
    static void loadProductsTable(TableView<Product> productsTable,
                                  TableColumn<Product, Integer> productIDCol,
                                  TableColumn<Product, String> productNameCol,
                                  TableColumn<Product, Integer> productInventoryCol,
                                  TableColumn<Product, Double> productPriceCol) {
        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsTable.setPlaceholder(new Label("No products found."));
    }

    /**
     * Generates a part ID for new parts
     * @return the generated ID
     */
    public static int generatePartID() {
        //get id from last element in allParts list
        ObservableList<Part> parts = Inventory.getAllParts();
        if (parts.isEmpty()) {
            return 1;
        }

        Part part = parts.get(parts.size()-1);

        return part.getId()+1;
    }
    /**
     * Generates a part ID for new products
     * @return the generated ID
     */
    public static int generateProductID() {
        //get id from last element in allParts list
        ObservableList<Product> products = Inventory.getAllProducts();
        if (products.isEmpty()) {
            return 1;
        } else {
            Product product = products.get(products.size()-1);
            return product.getId()+1;
        }

    }

}