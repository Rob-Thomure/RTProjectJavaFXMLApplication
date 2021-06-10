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
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;

/**
 *
 * @author robertthomure
 */
public class MainMenuController implements Initializable {    
    Stage stage;
    Parent scene;    
    
    @FXML
    private TextField searchPartsTxt;

    @FXML
    private TextField searchProductsTxt;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInventoryLevelCol;

    @FXML
    private TableColumn<Part, Double> partPricePerUnitCol;

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> ProductIdCol;

    @FXML
    private TableColumn<Product, String> ProductNameCol;

    @FXML
    private TableColumn<Product, Integer> ProductInventoryLevelCol;

    @FXML
    private TableColumn<Product, Double> ProductsPricePerUnitCol;

    @FXML
    void onActionAddParts(ActionEvent event) throws IOException {     
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartInHouse.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }

    @FXML
    void onActionAddProducts(ActionEvent event) throws IOException {        
        int id = Product.generateAutoProductId();
        Inventory.addProduct(new Product(id, null, 0, 0, 0, 0));        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProductMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();        
    }

    @FXML
    void onActionDeleteParts(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        if(selectedPart != null){            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            try {
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    throw new IllegalArgumentException("verify deletion");
                }     
            }
            catch(IllegalArgumentException e) {
                Inventory.deletePart(partsTableView.getSelectionModel().getSelectedItem());
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a part from the parts tableview");
            alert.showAndWait();         
        }
    }

    @FXML
    void onActionDeleteProducts(ActionEvent event) {
        Product selectedPart = productsTableView.getSelectionModel().getSelectedItem();
        if(selectedPart != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();
            try {
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    throw new IllegalArgumentException("verify deletion");
                }     
            }
            catch(IllegalArgumentException e) {
                Inventory.deleteProduct(productsTableView.getSelectionModel().getSelectedItem());
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a product from the products tableview");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionExit(ActionEvent event) {        
        System.exit(0);
    }

    @FXML
    void onActionModifyParts(ActionEvent event) throws IOException {        
        try {
            FXMLLoader loader = new FXMLLoader();
            if(partsTableView.getSelectionModel().getSelectedItem() instanceof InHouse) {
                loader.setLocation(getClass().getResource("/view/ModifyPartInHouse.fxml"));
                loader.load();
                ModifyPartInHouseController MPIHController = loader.getController();
                MPIHController.sendPart(partsTableView.getSelectionModel().getSelectedItem());
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else {
                loader.setLocation(getClass().getResource("/view/ModifyPartOutSourced.fxml"));
                loader.load();
                ModifyPartOutSourcedController MPOSController = loader.getController();
                MPOSController.sendPart(partsTableView.getSelectionModel().getSelectedItem());
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();               
            }
        }
        catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a part from the parts tableview");
            alert.showAndWait();      
        }
    }

    @FXML
    void onActionModifyProducts(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProductMenu.fxml"));
            loader.load();
            ModifyProductMenuController mPMController = loader.getController();            
            Product thisProduct = productsTableView.getSelectionModel().getSelectedItem();
            mPMController.sendProduct(productsTableView.getSelectionModel().getSelectedItem());
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a product from the products tableview");
            alert.showAndWait();      
        }
    }

    @FXML
    void onActionSearchParts(ActionEvent event) {        
        if (searchPartsTxt.getText().isEmpty()) {
            partsTableView.setItems(Inventory.getAllParts());
        }        
        else {
            if(!(Inventory.getAllFilteredParts().isEmpty())) {
                Inventory.getAllFilteredParts().clear();
            }            
            try {
                int partIdSearch = Integer.parseInt(searchPartsTxt.getText());           
                Inventory.addFilteredPart(Inventory.lookupPart(partIdSearch));                
                partsTableView.setItems(Inventory.getAllFilteredParts());                       
            }            
            catch (NumberFormatException e) {               
                String partNameSearch = searchPartsTxt.getText();                
                partsTableView.setItems(Inventory.lookupPart(partNameSearch));               
            }            
        }
    }

    @FXML
    void onActionSearchProducts(ActionEvent event) {
        if (searchProductsTxt.getText().isEmpty()) {
            productsTableView.setItems(Inventory.getAllProducts());
        }
        else {
            if(!(Inventory.getAllFilteredProducts().isEmpty())) {
                Inventory.getAllFilteredProducts().clear();
            }
            try {
                int productIdSearch = Integer.parseInt(searchProductsTxt.getText());
                Inventory.addFilteredProduct(Inventory.lookupProduct(productIdSearch));
                productsTableView.setItems(Inventory.getAllFilteredProducts());
            }
            catch (NumberFormatException e) {
                String productNameSearch = searchProductsTxt.getText();
                productsTableView.setItems(Inventory.lookupProduct(productNameSearch));
            }
        }        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
      
        //  puts the parsts data into the parts table
        partsTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        //  puts the products data into the products table
        productsTableView.setItems(Inventory.getAllProducts());
        ProductIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProductsPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));        
    }    
}