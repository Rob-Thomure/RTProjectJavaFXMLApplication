/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class AddPartInHouseController implements Initializable {
    
    Stage stage;
    Parent scene;    


    @FXML
    private RadioButton partInHouseRBtn;
    @FXML
    private ToggleGroup radioButton;
    @FXML
    private RadioButton partOutsourcedRBtn;
    @FXML
    private TextField partIdTxt;
    @FXML
    private TextField partNameTxt;
    @FXML
    private TextField invTxt;
    @FXML
    private TextField priceTxt;
    @FXML
    private TextField machineIdTxt;
    @FXML
    private TextField maxTxt;
    @FXML
    private TextField minTxt;
    
    @FXML
    void onActionAddPartOutsourced(ActionEvent event) throws IOException {        
        stage = (Stage)((RadioButton)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartOutsourced.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {        
        if (!partNameTxt.getText().isEmpty()) {
            try {                
                // collects typed text from addPartInHouse.fxml screen and saves
                String name = partNameTxt.getText();
                double price = Double.parseDouble(priceTxt.getText());            
                int stock = Integer.parseInt(invTxt.getText());
                int min = Integer.parseInt(minTxt.getText());
                int max = Integer.parseInt(maxTxt.getText());        
                int machineId = Integer.parseInt(machineIdTxt.getText());            
                // add inventory control levels
                try {
                    if (stock < min || stock > max) {
                        throw new IllegalArgumentException("stock must be within min and max values");
                    }            
                    //  saves the new part to InHouse data
                    Inventory.addPart(new InHouse(Inventory.getAutoPartId(), name, price, stock, min, max, machineId));
                    //  takes you back to the main menu        
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
                catch(IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Please enter a inventory value within range of min and max");
                    alert.showAndWait();
                }
            }
            catch(NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a valid value for the text "
                        + "field");
                alert.showAndWait();
            }            
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please enter a Part Name");
            alert.showAndWait();
        }   
    }

    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to return to Main Menu without saving?");
        Optional<ButtonType> result = alert.showAndWait();
        try {
            if(result.isPresent() && result.get() == ButtonType.OK) {
                throw new IllegalArgumentException("verify cancel");
            }     
        }
        catch(IllegalArgumentException e) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu."
                    + "fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }        
}
