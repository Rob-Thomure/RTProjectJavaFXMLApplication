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
public class ModifyPartOutSourcedController implements Initializable {    
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
    private TextField companyNameTxt;
    @FXML
    private TextField maxTxt;
    @FXML
    private TextField minTxt;
    
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {
        if (!PartNameTxt.getText().isEmpty() && !companyNameTxt.getText().isEmpty()) {
            try {
                int index = -1;
                for (Part outSourced : Inventory.getAllParts()) {
                    index ++;
                    if(outSourced.getId() == parseInt(partIdTxt.getText())) {
                        int Id = Integer.parseInt(partIdTxt.getText());
                        String name = PartNameTxt.getText();
                        double price = Double.parseDouble(priceTxt.getText());
                        int stock = Integer.parseInt(invTxt.getText());
                        int min = Integer.parseInt(minTxt.getText());
                        int max = Integer.parseInt(maxTxt.getText());
                        String companyName = companyNameTxt.getText();
                        try {
                            if (stock < min || stock > max) {
                                throw new IllegalArgumentException("stock must be within min and max values");
                            }
                            Inventory.updatePart(index, new Outsourced(Id, name, price, stock, min, max, companyName));
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
            if(PartNameTxt.getText().isEmpty()) {      
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
    void onActionModifyPartInHouse(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPartInHouse.fxml"));
        loader.load();
        ModifyPartInHouseController MPIHController = loader.getController();
        int Id = Integer.parseInt(partIdTxt.getText());
        MPIHController.sendPart(Inventory.lookupPart(Id));
        stage = (Stage)((RadioButton)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
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
    
    public void sendPart(Part outSourced) {
        partIdTxt.setText(String.valueOf(outSourced.getId()));
        PartNameTxt.setText(outSourced.getName());
        invTxt.setText(String.valueOf(outSourced.getStock()));
        priceTxt.setText(String.valueOf(outSourced.getPrice()));
        maxTxt.setText(String.valueOf(outSourced.getMax()));
        minTxt.setText(String.valueOf(outSourced.getMin()));        
        if (!(outSourced instanceof InHouse)) {
            companyNameTxt.setText(((Outsourced) outSourced).getCompanyName());
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