package com.whitneygoodey.inventoryManager;

import com.whitneygoodey.inventoryManager.partsAndProducts.Inventory;
import com.whitneygoodey.inventoryManager.partsAndProducts.Part;
import com.whitneygoodey.inventoryManager.partsAndProducts.Product;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * The ProductFormController class provides the code for the product form window.
 * Creating products and modifying products are implemented here.
 * @author Whitney Goodey
 * @version 1.0
 */
public class ProductFormController {

    @FXML
    private TextField textFieldPartId;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldInv;
    @FXML
    private TextField textFieldPrice;
    @FXML
    private TextField textFieldMax;
    @FXML
    private TextField textFieldMin;
    @FXML
    private TextField partSearchBar;
    @FXML
    private TableView<Part> allPartsTable;
    @FXML
    private TableColumn<Part, Integer> partIDCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInventoryCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    @FXML
    private TableView<Part> associatedPartsTable;
    @FXML
    private TableColumn<Part, Integer> associatedPartIDCol;
    @FXML
    private TableColumn<Part, String> associatedPartNameCol;
    @FXML
    private TableColumn<Part, Integer> associatedPartInventoryCol;
    @FXML
    private TableColumn<Part, Double> associatedPartPriceCol;

    boolean modifyProduct = false;
    private Product tempProduct = new Product();

    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    /**
     * Initializes the product form. Parts and Products tableViews are set up and populated.
     * Listeners are defined for the search bars which will filter tables.
     */
    public void initialize() {
        MainWindowController.loadPartsTable(allPartsTable, partIDCol, partNameCol, partInventoryCol, partPriceCol);

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
        sortedParts.comparatorProperty().bind(allPartsTable.comparatorProperty());
        //add data to the TableView
        allPartsTable.setItems(sortedParts);

        //set associated products table
        loadAssociatedPartsTable();

    }

    /**
     * Initializes the product form in the case that a product is being modified.
     * @param product the product used to fill in the form with existing data
     */
    public void initialize(Product product) {
        modifyProduct = true;
        tempProduct = new Product(product);

        //load data into textFields
        textFieldPartId.setText(String.valueOf(tempProduct.getId()));
        textFieldName.setText(String.valueOf(tempProduct.getName()));
        textFieldInv.setText(String.valueOf(tempProduct.getStock()));
        textFieldPrice.setText(String.valueOf(tempProduct.getPrice()));
        textFieldMax.setText(String.valueOf(tempProduct.getMax()));
        textFieldMin.setText(String.valueOf(tempProduct.getMin()));

        initialize();

    }

    /**
     * Handler manages clicks for buttons on the product window.
     * <p>
     * Adding a part to the associated list was crashing my program. It turned
     * out that when the form was initially loaded there wasn't an associated
     * parts list to add to, so NullPointerExceptions were being thrown. <br>
     *     
     * To get around this problem I added a temporary part that is initialized
     * with the form so there is always a place for associated parts to be stored.
     * </p>
     * @param event button clicked on part window
     */
    @FXML
    private void handleButtonClick(ActionEvent event) {

        if(event.getSource().equals(addButton)) {
            Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();
            tempProduct.addAssociatedPart(selectedPart);

        } else if(event.getSource().equals(removeButton)) {
            Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove associated part");
            alert.setHeaderText("Remove " + selectedPart.getName() + " from associated parts list?");
            alert.setContentText("Press Okay to remove or Cancel to keep.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                tempProduct.deleteAssociatedPart(selectedPart);
            }

        } else if(event.getSource().equals(saveButton)) {
            boolean flag = false;
            try {
                int min = Integer.parseInt(textFieldMin.getText());
                int max = Integer.parseInt(textFieldMax.getText());
                int inv = Integer.parseInt(textFieldInv.getText());

                flag = PartFormController.checkNumbers(min, max, inv);
                if (flag) {
                    PartFormController.showInventoryWarning(min, max, inv);
                }
            } catch (NumberFormatException e) {
//            e.printStackTrace();
            }

            if (!flag) {
                try {
                    saveData();
                    //close window
                    ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error saving product");
                    alert.setHeaderText("Invalid data entered");
                    alert.setContentText("One or more fields contains invalid data. " +
                            "Please check fields and continue.");
                    alert.showAndWait();
//                e.printStackTrace();
                }
            }

        } else if (event.getSource().equals(cancelButton)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modify product");
            alert.setHeaderText("Discard modifications to product?");
            alert.setContentText("Press Okay to discard changes or Cancel to continue.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                //close window
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            }
        }
    }

    /**
     * Saves new Product or changes made to existing product into the allProducts inventory list
     */
    private void saveData() {
        tempProduct.setName(textFieldName.getText());
        tempProduct.setStock(Integer.parseInt(textFieldInv.getText()));
        tempProduct.setMax(Integer.parseInt(textFieldMax.getText()));
        tempProduct.setMin(Integer.parseInt(textFieldMin.getText()));
        tempProduct.setPrice(Double.parseDouble(textFieldPrice.getText()));

        //for new product generate ID and save
        if(!modifyProduct) {
            tempProduct.setId(MainWindowController.generateProductID());
            Inventory.addProduct(tempProduct);
        } else {
            Inventory.updateProduct(Inventory.getProductIndex(tempProduct.getId()), tempProduct);
        }

    }

    /**
     * loads associated parts list into the associatedParts tableView.
     */
    private void loadAssociatedPartsTable() {
        associatedPartsTable.setItems(tempProduct.getAllAssociatedParts());
        associatedPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


}
