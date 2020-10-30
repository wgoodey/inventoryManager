package com.whitneygoodey.inventoryManager;

import com.whitneygoodey.inventoryManager.partsAndProducts.InHouse;
import com.whitneygoodey.inventoryManager.partsAndProducts.Inventory;
import com.whitneygoodey.inventoryManager.partsAndProducts.Outsourced;
import com.whitneygoodey.inventoryManager.partsAndProducts.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * The PartFormController class provides the code for the part form window.
 * Creating parts and modifying parts are implemented here.
 * @author Whitney Goodey
 * @version 1.0
 */
public class PartFormController {

    @FXML
    private RadioButton inHouse;
    @FXML
    private RadioButton outsourced;
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
    private Label macManId;
    @FXML
    private TextField textFieldMacManId;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    private boolean modifyPart = false;

    /**
     * Initializes the part form with data from existing part to be modified
     * @param part the part used to fill in the form with existing data
     */
    public void initialize(Part part) {
        modifyPart = true;

        textFieldPartId.setText(String.valueOf(part.getId()));
        textFieldName.setText(part.getName());
        textFieldInv.setText(String.valueOf(part.getStock()));
        textFieldPrice.setText(String.valueOf(part.getPrice()));
        textFieldMax.setText(String.valueOf(part.getMax()));
        textFieldMin.setText(String.valueOf(part.getMin()));
        if(part instanceof Outsourced) {
            outsourced.setSelected(true);
            macManId.setText("Company Name");
            textFieldMacManId.setText(((Outsourced) part).getCompanyName());
        } else {
            textFieldMacManId.setText(String.valueOf(((InHouse) part).getMachineId()));
        }

    }

    /**
     * Switches between Machine ID and Company Name label on form.
     */
    @FXML
    private void handleRadios() {
        if(inHouse.isSelected()) {
            macManId.setText("Machine ID");
        } else if(outsourced.isSelected()) {
            macManId.setText("Company Name");
        }
    }

    /**
     * Handler manages clicks for buttons on part window.
     * @param event button clicked on part window
     */
    @FXML
    private void handleButtonClick(ActionEvent event) {
        if(event.getSource().equals(saveButton)) {
            boolean flag = false;
            try {
                int min = Integer.parseInt(textFieldMin.getText());
                int max = Integer.parseInt(textFieldMax.getText());
                int inv = Integer.parseInt(textFieldInv.getText());

                flag = PartFormController.checkNumbers(min, max, inv);
                if (flag) {
                    showInventoryWarning(min, max, inv);
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
                    alert.setTitle("Error saving part");
                    alert.setHeaderText("Invalid data entered");
                    alert.setContentText("One or more fields contains invalid data. " +
                                            "Please check fields and continue.");
                    alert.showAndWait();
                }
            }

        } else if (event.getSource().equals(cancelButton)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modify Product");
            alert.setHeaderText("Discard modifications to part?");
            alert.setContentText("Press Okay to discard changes or Cancel to continue.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                //close window
                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            }
        }
    }

    /**
     * Saves new Part or changes made to existing part into the allParts inventory list
     */
    private void saveData() {
        int id;
        String name = textFieldName.getText().trim();
        double price = Double.parseDouble(textFieldPrice.getText());
        int stock = Integer.parseInt(textFieldInv.getText());
        int min = Integer.parseInt(textFieldMin.getText());
        int max = Integer.parseInt(textFieldMax.getText());
        if (!modifyPart) {
            id = MainWindowController.generatePartID();
        } else {
            id = Integer.parseInt(textFieldPartId.getText());
        }

        if(inHouse.isSelected()) {
            int machineId = Integer.parseInt(textFieldMacManId.getText());
            if(modifyPart) {
                //update part in list
                Inventory.updatePart(Inventory.getPartIndex(Integer.parseInt(textFieldPartId.getText())),
                                    new InHouse(id,name,price,stock,min,max,machineId));
            } else {
                //add new part to the allParts List
                Inventory.addPart(new InHouse(id,name,price,stock,min,max,machineId));
            }

        } else if (outsourced.isSelected()) {
            String companyName = textFieldMacManId.getText().trim();
            if(modifyPart) {
                //update part in list
                Inventory.updatePart(Inventory.getPartIndex(Integer.parseInt(textFieldPartId.getText())),
                        new Outsourced(id,name,price,stock,min,max,companyName));
            } else {
                //add new part to the allParts List
                Inventory.addPart(new Outsourced(id,name,price,stock,min,max,companyName));
            }

        }
    }

    /**
     * Checks if minimum is less than zero or greater than maximum and if inventory is out of range.
     * @param min the minimum value to validate
     * @param max the maximum value to validate
     * @param inv the inventory value to validate
     * @return true if numbers are invalid
     */
    public static boolean checkNumbers(int min, int max, int inv) {
        if ((min < 0) || (min > max)) {
            return true;
        } else if ((inv < min) || (inv > max)) {
            return true;
        }
        return false;
    }

    /**
     * Displays a warning if minimum is less than zero or greater than maximum
     * or if inventory is out of range.
     * @param min the minimum value to validate
     * @param max the maximum value to validate
     * @param inv the inventory value to validate
     */
    public static void showInventoryWarning(int min, int max, int inv) {
        String minMaxWarning = "";
        String inventoryWarning = "";
        if ((min < 0) || (min > max)) {
            minMaxWarning = "Minimum cannot be less than 0 or greater than maximum.";

        }
        if ((inv < min) || (inv > max)) {
            inventoryWarning = "Inventory must be between minimum and maximum.";
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Check your numbers");
        alert.setContentText(minMaxWarning + "\n" + inventoryWarning + "\n");
        alert.showAndWait();
    }

}