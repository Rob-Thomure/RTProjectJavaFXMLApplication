/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtprojectjavafxmlapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

/**
 *
 * @author robertthomure
 */
public class RTProjectJavaFXMLApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InHouse part1 = new InHouse(1, "Hard Drive", 45.16, 5, 1, 14, 4506);
        InHouse part2 = new InHouse(2, "Sound Card", 76.12, 3, 1, 12, 4507);
        InHouse part3 = new InHouse(3, "Mother Board", 274.16, 4, 1, 11, 4509);
        Outsourced part4 = new Outsourced(4, "Video Card", 124.14, 3, 1, 5, "Best");
        
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);
        
        Product product1 = new Product(101, "Computer", 345.75, 7, 2, 9);
        Product product2 = new Product(102, "Laptop", 546.32, 3, 1, 10);
        
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        
        product1.addAssociatedPart(part4);
        product1.addAssociatedPart(part3);
        product2.addAssociatedPart(part1);
        
        launch(args);
    }    
}