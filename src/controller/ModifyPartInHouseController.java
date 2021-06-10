/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import static java.lang.Integer.parseInt;
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
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class ModifyPartInHouseController implements Initializable {    
    Stage stage;
    Parent scene;    

    @FXML
    private RadioButton modifyPartInHouseRBtn;
    @FXML
    private RadioButton modifyPartOutsourcedRBtn;
    @FXML
    private TextField partIdTxt;
    @FXML
    private TextField PartNameTxt;
    @FXML
    private TextField invTxt;
    @FXML
    private TextField priceTxt;
    @FXML
    private TextField maxTxt;
    @FXML
    private TextField minTxt;
    @FXML
    private TextField machineIdTxt;
    
    @FXML
    void onActionModifyPartOutsourced(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPartOutSourced.fxml"));
        loader.load();
        ModifyPartOutSourcedController MPOSController = loader.getController();        
        int Id = Integer.parseInt(partIdTxt.getText());
        MPOSController.sendPart(Inventory.lookupPart(Id));
        stage = (Stage)((RadioButton)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {
        if (!PartNameTxt.getText().isEmpty()) {      
            try {
                int index = -1;
                for (Part inHouse : Inventory.getAllParts()) {
                    index ++;
                    if(inHouse.getId() == parseInt(partIdTxt.getText())) {
                        int Id = Integer.parseInt(partIdTxt.getText());
                        String name = PartNameTxt.getText();
                        double price = Double.parseDouble(priceTxt.getText());
                        int stock = Integer.parseInt(invTxt.getText());
                        int min = Integer.parseInt(minTxt.getText());
                        int max = Integer.parseInt(maxTxt.getText());
                        int machineId = Integer.parseInt(machineIdTxt.getText());
                        try {
                            if (stock < min || stock > max) {
                                throw new IllegalArgumentException("stock must be within min and max values");
                            }
                            Inventory.updatePart(index, new InHouse(Id, name, price, stock, min, max, machineId));      
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
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }        
    }    
    public void sendPart(Part inHouse) {
        partIdTxt.setText(String.valueOf(inHouse.getId()));
        PartNameTxt.setText(inHouse.getName());
        invTxt.setText(String.valueOf(inHouse.getStock()));
        priceTxt.setText(String.valueOf(inHouse.getPrice()));
        maxTxt.setText(String.valueOf(inHouse.getMax()));
        minTxt.setText(String.valueOf(inHouse.getMin()));        
        if (!(inHouse instanceof Outsourced)) {
            machineIdTxt.setText(String.valueOf(((InHouse) inHouse).getMachineId()));            
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
