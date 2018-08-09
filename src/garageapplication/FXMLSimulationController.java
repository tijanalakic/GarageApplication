/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garageapplication;

import garaza.Platforma;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import specijalnovozilo.InstitutionalInterface;
import vozilo.Vozilo;

/**
 *
 * @author Tijana Lakic
 */
public class FXMLSimulationController implements Initializable {

    @FXML
    private ComboBox platformaComboBox;
    @FXML
    private TextArea simulationTextArea;
    @FXML
    private Button addVoziloButton;
    @FXML
    private ComboBox addVoziloComboBox;
    @FXML
    private Label addVoziloWarningLabel;

    public ExecutorService executor = Executors.newFixedThreadPool(100);
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (int i = 0; i < Integer.parseInt(utils.Utils.PROPERTIES.getProperty("BROJ_PLATFORMI")); i++) {

            platformaComboBox.getItems().add(i);

        }
        platformaComboBox.getSelectionModel().selectFirst();

        addVoziloComboBox.getItems().addAll("Automobil", "Kombi", "Motocikl");

        //simulationTextArea.setText(FXMLUserApplicationController.garaza.getPlatforme().get());
        GarageApplication.getExchanger().setSimulacijaMatrica(simulationTextArea);
        GarageApplication.getExchanger().refreshSimulacijaMatrica();
        // simulationTextArea.setStyle("-fx-font-family: monospace");
        simulationTextArea.setStyle("-fx-font: 58 monospace");


        for (int k = 0; k < GarageApplication.getExchanger().getGaraza().getBrojPlatformi(); k++) {
        
            int brojVozilaKojaSeIsparkiravaju =(int) Math.round(GarageApplication.getExchanger().getBrojVozila(k) * 0.15);

            for (int i = 0; i < brojVozilaKojaSeIsparkiravaju; i++) {
                Boolean isparkiraj = false;
                while (!isparkiraj) {

                    int x = new Random().nextInt(4);
                    int y = new Random().nextInt(8);

                    Object vozilo = GarageApplication.getExchanger().getGaraza().getPlatforme().get(k).getElement(Platforma.Xparking[x],
                            Platforma.Yparking[y]);
                   
                    if (vozilo instanceof Vozilo && !(vozilo instanceof InstitutionalInterface) &&
                            !((Vozilo) vozilo).isAlive()) {
                        
                        executor.submit((Vozilo) vozilo);
                        isparkiraj = true;

                    }
                }
            }
        }

    }

    @FXML
    public void addVoziloButtonAction() throws IOException {

        if (addVoziloComboBox.getSelectionModel().isEmpty()) {
            addVoziloWarningLabel.setText("Odaberite tip vozila!");
            return;
        }
        addVoziloWarningLabel.setText("");

        String vozilo = addVoziloComboBox.getSelectionModel().getSelectedItem().toString();
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
        newStage.showAndWait();

        RootExchanger.VOZILO_KRETANJE.setParkiran(false);
        RootExchanger.VOZILO_KRETANJE.start();

    }

    @FXML
    private void izborPlatformeOnAction(ActionEvent event) {

        int nivo = Integer.parseInt(platformaComboBox.getSelectionModel().getSelectedItem().toString());
        GarageApplication.getExchanger().setNivoPrikaz(nivo);

        simulationTextArea.clear();
        simulationTextArea.setText(FXMLUserApplicationController.garaza.getPlatforme().
                get(nivo).toString());

    }

    @FXML
    private void HandleButtonOnClose(ActionEvent event) {

        try (ObjectOutputStream ser = new ObjectOutputStream(new FileOutputStream(new File("garaza.ser")))) {
            ser.writeObject(GarageApplication.getExchanger().getGaraza());
        } catch (IOException e) {
            System.out.println("SERIJALIZACIJA ERROR" + e);
        }

    }
}
