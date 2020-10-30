package com.whitneygoodey.inventoryManager;

import com.whitneygoodey.inventoryManager.partsAndProducts.InHouse;
import com.whitneygoodey.inventoryManager.partsAndProducts.Inventory;
import com.whitneygoodey.inventoryManager.partsAndProducts.Outsourced;
import com.whitneygoodey.inventoryManager.partsAndProducts.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Whitney Goodey
 * @version 1.0
 * <p>
 * Potential feature extensions for the next version: <br>
 * Add a dropdown menu at the top of the main window that would allow the user to
 * switch between multiple locations or add a new location. The main window part
 * and product tables would populate with the inventory of the selected location.
 * </p>
 * Currently if a product should need more than one of a particular part it can be
 * associated multiple times. In the next version, alter the associated parts list
 * on the product form to allow for adding each unique part only a single time,
 * defining the quantity needed.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Inventory Manager");
        primaryStage.setScene(new Scene(root, 800, 375));
        primaryStage.show();
    }


    public static void main(String[] args) {
//        //test data
//        InHouse part1 = new InHouse(1,"Brakes",12.99,15,2,15,1);
//        Outsourced part2 = new Outsourced(2,"Premium Brakes",19.99,10,2,20,"Company");
//        InHouse part3 = new InHouse(3,"Wheel",10.99,15,2,15,2);
//        Outsourced part4 = new Outsourced(4,"Premium Wheel",15.99,10,2,20,"Tangent");
//        InHouse part5 = new InHouse(5,"Seat",6.99,15,2,15,3);
//        Outsourced part6 = new Outsourced(6,"Premium Seat",9.99,10,2,20,"Shorts");
//        InHouse part7 = new InHouse(25,"fork",6.99,15,2,15,3);
//        Inventory.addPart(part1);
//        Inventory.addPart(part2);
//        Inventory.addPart(part3);
//        Inventory.addPart(part4);
//        Inventory.addPart(part5);
//        Inventory.addPart(part6);
//        Inventory.addPart(part7);
//
//        Product product1 = new Product(1,"Bike",60.99,2,1,3);
//        product1.addAssociatedPart(part1);
//        product1.addAssociatedPart(part3);
//        Inventory.addProduct(product1);

        launch(args);
    }
}
