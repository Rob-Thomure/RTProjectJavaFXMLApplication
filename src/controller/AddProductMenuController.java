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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class AddProductMenuController implements Initializable {    
    Stage stage;
    Parent scene;    

    @FXML
    private TextField searchPartTxt;

    @FXML
    private TextField ProductIdTxt;

    @FXML
    private TextField NameTxt;

    @FXML
    private TextField invTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField minTxt;
    
    @FXML
    private TableView<Part> addPartsTableView;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInventoryLevelCol;
    
    @FXML
    private TableColumn<Part, Double> partPricePerUnitCol;

    @FXML
    private TableView<Part> associatedPartTableView;

    @FXML
    private TableColumn<Part, Integer> associatedPartIdCol;

    @FXML
    private TableColumn<Part, String> associatedPartNameCol;
    
    @FXML
    private TableColumn<Part, Integer> associatedInventoryLevelCol;
    
    @FXML
    private TableColumn<Part, Double> associatedPricePerUnitCol;
        
    @FXML
    void onActionAddPart(ActionEvent event) {        
        if (addPartsTableView.getSelectionModel().getSelectedItem() != null) {
            int id = Product.getAutoProductId();
            Product product = Inventory.lookupProduct(id);
            product.addAssociatedPart(addPartsTableView.getSelectionModel().getSelectedItem());
            associatedPartTableView.setItems(product.getAllAssociatedParts());
            associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            associatedInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            associatedPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price")); 
        }
    }

    @FXML
    void onActionDeletePart(ActionEvent event) {        
        Part selectedPart = associatedPartTableView.getSelectionModel().getSelectedItem();
        if(selectedPart != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            try {
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    throw new IllegalArgumentException("verify deletion");
                }     
            }
            catch(IllegalArgumentException e) {
                int id = Product.getAutoProductId();
                Product product = Inventory.lookupProduct(id);
                product.deleteAssociatedPart(associatedPartTableView.getSelectionModel().getSelectedItem());
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a part from the associated parts tableview");
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
            for (Product product : Inventory.getAllProducts()) {
                if(product.getId() == Product.getAutoProductId()) {
                    Inventory.deleteProduct(product);
                    Product.cancelAutoProductId();
                    break;
                }
            }
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu."
                    + "fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    void onActionSaveProduct(ActionEvent event) throws IOException {        
        if (!NameTxt.getText().isEmpty()) {      
            try {
                for (Product product : Inventory.getAllProducts()) {
                    if(product.getId() == Product.getAutoProductId()) {
                        int id = Product.getAutoProductId();
                        String name = NameTxt.getText();
                        double price = Double.parseDouble(priceTxt.getText());
                        int stock = Integer.parseInt(invTxt.getText());
                        int min = Integer.parseInt(minTxt.getText());
                        int max = Integer.parseInt(maxTxt.getText());                        
                        try {
                            if (stock < min || stock > max) {
                                throw new IllegalArgumentException("stock must be within min and max values");
                            }
                            product.setId(id);
                            product.setName(name);
                            product.setPrice(price);
                            product.setStock(stock);
                            product.setMin(min);
                            product.setMax(max);
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
            alert.setContentText("Please enter a Product Name");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionSearchPart(ActionEvent event) {
        if (searchPartTxt.getText().isEmpty()) {
            addPartsTableView.setItems(Inventory.getAllParts());
        }        
        else {
            if(!(Inventory.getAllFilteredParts().isEmpty())) {
                Inventory.getAllFilteredParts().clear();
            }            
            try {
                int partIdSearch = Integer.parseInt(searchPartTxt.getText());           
                Inventory.addFilteredPart(Inventory.lookupPart(partIdSearch));                
                addPartsTableView.setItems(Inventory.getAllFilteredParts());                       
            }            
            catch (NumberFormatException e) {               
                String partNameSearch = searchPartTxt.getText();                
                addPartsTableView.setItems(Inventory.lookupPart(partNameSearch));               
            }            
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //  puts the parts data into the parts table
        addPartsTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));       
    }    
}