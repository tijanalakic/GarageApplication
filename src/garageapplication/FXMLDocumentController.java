/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garageapplication;

import garaza.Garaza;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import vozilo.Vozilo;

/**
 *
 * @author Tijana Lakic
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TableView<Vozilo> vozilaTableView;
    @FXML
    TableColumn<Vozilo, String> nazivCol;
    @FXML
    TableColumn<Vozilo, String> brojSasijeCol;
    @FXML
    TableColumn<Vozilo, String> brojMotoraCol;
    @FXML
    TableColumn<Vozilo, String> registarskiBrojCol;
    @FXML
    TableColumn<Vozilo, String> tipVozilaCol;
    @FXML
    private ComboBox addVoziloComboBox;
    @FXML
    private Label addVoziloWarningLabel;
    @FXML
    private ComboBox<String> platformaComboBox;
    @FXML
    private Label brojPlatformeLabel;
    @FXML
    private Button deleteVoziloButton;
     @FXML
    private Button editVoziloButton;
    @FXML
    private Button startUserAppButton;
    
    public static ObservableList<Vozilo> listaVozila = FXCollections.observableArrayList();
    public static String ROOT_RESOURCE = "./src/resources/"; // staviti u property

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
        brojPlatformeLabel.setText("Odaberi platformu");
        brojPlatformeLabel.setAlignment(Pos.CENTER); //ne radii!!
        vozilaTableView.setItems(listaVozila);
        
        tipVozilaCol.setCellValueFactory(new PropertyValueFactory<>("tip"));
        nazivCol.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        brojSasijeCol.setCellValueFactory(new PropertyValueFactory<>("brojSasije"));
        brojMotoraCol.setCellValueFactory(new PropertyValueFactory<>("brojMotora"));
        registarskiBrojCol.setCellValueFactory(new PropertyValueFactory<>("registarskiBroj"));
   
        for(int i=0; i<GarageApplication.getExchanger().getGaraza().getBrojPlatformi(); i++){
            platformaComboBox.getItems().add(""+i);
        }
        
        platformaComboBox.getSelectionModel().selectFirst(); 

        addVoziloComboBox.setItems(FXCollections.observableArrayList("Automobil", "Kombi", "Motocikl"));

        
        vozilaTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (vozilaTableView.getSelectionModel().getSelectedItem() != null) {
                    editVoziloButton.setDisable(false);
                    deleteVoziloButton.setDisable(false);
                }
            }
        });
    }
    
    
    @FXML
    public void addVoziloButtonAction() throws IOException {
        
        if (addVoziloComboBox.getSelectionModel().isEmpty()) {
            addVoziloWarningLabel.setText("Odaberite tip vozila!");
            return;
        }
        addVoziloWarningLabel.setText("");

        String vozilo =  addVoziloComboBox.getSelectionModel().getSelectedItem().toString();
        GarageApplication.getExchanger().setVoziloInput(vozilo);

        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLAddVozilo.fxml"));
        Scene stageScene = new Scene(root, 400, 500);
        newStage.setScene(stageScene);
        
        switch (vozilo) {
            case "Automobil": {
                newStage.setHeight(680);
                break;
            }
            case "Kombi": {
                newStage.setHeight(680);
                break;
            }
            
            case "Motocikl": {
                newStage.setHeight(640);
                break;
            }
        }
        
        newStage.show();
    }

    @FXML
    public void startUserAppButtonAction() throws IOException {

        try (ObjectOutputStream ser = new ObjectOutputStream(new FileOutputStream(new File("garaza.ser")))) {
            ser.writeObject(GarageApplication.getExchanger().getGaraza());
        } catch (IOException e) {
            System.out.println("SERIJALIZACIJA ERROR" + e);
        }

        Stage newStage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("FXMLUserApplication.fxml"));

        Scene stageScene = new Scene(root, 400, 400);
        newStage.setScene(stageScene);
        newStage.setTitle("Korisnicki dio");
      // newStage.getIcons().add(new Image("parking.png"));

        newStage.show();
        // newStage.setResizable(false);
        
        ((Stage)startUserAppButton.getScene().getWindow()).close();
    }

   

    @FXML
    private void editVoziloButtonAction() throws IOException {
       
        Vozilo odabranoVozilo = vozilaTableView.getSelectionModel().getSelectedItem();
        GarageApplication.getExchanger().setEditVozilo(odabranoVozilo);  
        
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLEditVozilo.fxml"));
        Scene stageScene = new Scene(root, 400, 500);  
        newStage.setScene(stageScene);

        switch (odabranoVozilo.getClass().getSimpleName()) {
            case "Automobil": {
                newStage.setHeight(640);
                break;
            }
            case "Kombi": {
                newStage.setHeight(640);
                break;
            }
            case "Motocikl": {
                newStage.setHeight(600);
                break;
            }
        }

        newStage.show();
    }


    @FXML
    public void deleteVoziloButtonAction() {

        Vozilo vozilo = vozilaTableView.getSelectionModel().getSelectedItem();
        GarageApplication.getExchanger().getVozila().remove(vozilo);
        GarageApplication.getExchanger().refreshVozilaTable();
        
        editVoziloButton.setDisable(true);
        deleteVoziloButton.setDisable(true);
    }

    

    @FXML
    private void izborPlatformeOnAction(ActionEvent event) {
        GarageApplication.getExchanger().setNivo(Integer.parseInt(platformaComboBox.getSelectionModel().getSelectedItem()));
        GarageApplication.getExchanger().refreshVozilaTable();
    }

}
