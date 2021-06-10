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
import javafx.stage.Stage;
import model.Inventory;
import model.Outsourced;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class AddPartOutSourcedController implements Initializable {    
    Stage stage;
    Parent scene;    
    @FXML
    private RadioButton partInHouseRBtn;
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
    private TextField companyNameTxt;
    @FXML
    private TextField maxTxt;
    @FXML
    private TextField minTxt;
    
    @FXML
    void onActionAddPartInHouse(ActionEvent event) throws IOException {
        stage = (Stage)((RadioButton)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartInHouse."
                + "fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {        
        if (!partNameTxt.getText().isEmpty() && !companyNameTxt.getText().isEmpty()) {        
            try {
                // collects typed text from addPartOutSourced.fxml screen and saves
                String name = partNameTxt.getText();
                double price = Double.parseDouble(priceTxt.getText());
                int stock = Integer.parseInt(invTxt.getText());
                int min = Integer.parseInt(minTxt.getText());
                int max = Integer.parseInt(maxTxt.getText());
                String companyName = companyNameTxt.getText();            
                try {
                    if (stock < min || stock > max) {
                        throw new IllegalArgumentException("stock must be within min and max values");
                    }
                    //  saves the new part to Outsourced data
                    Inventory.addPart(new Outsourced(Inventory.getAutoPartId(), name, price, stock, min, max, companyName));
                    //  takes you back to the main menu        
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }            
                catch(IllegalArgumentException e) {
                    System.out.println("out of range");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Please enter a inventory value within range of min and max");
                    alert.showAndWait();
                }
            }
            catch(NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a valid value for the text field");
                alert.showAndWait();
            }
        }
        else {            
            if(partNameTxt.getText().isEmpty()) {      
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please enter a Part Name");
                alert.showAndWait();
            }
            else if(companyNameTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Please enter a Company Name");
                alert.showAndWait();   
            }
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
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
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