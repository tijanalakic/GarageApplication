/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garageapplication;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import vozilo.Automobil;
import vozilo.Kombi;
import vozilo.Motocikl;
import vozilo.Vozilo;

/**
 *
 * @author Tijana Lakic
 */
public class FXMLAddVoziloController implements Initializable {

    @FXML
    private Button addVoziloSubmitButton;

    @FXML
    private VBox parentVBox;

    private File voziloImage = new File("");

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        switch (GarageApplication.getExchanger().getVoziloInput()) {

            case "Automobil": {
                TextField brojVrataField = new TextField();
                brojVrataField.setPromptText("Broj vrata");
                parentVBox.getChildren().add(brojVrataField);
                break;
            }
            case "Kombi": {
                TextField nosivostField = new TextField();
                nosivostField.setPromptText("Nosivost");
                parentVBox.getChildren().add(nosivostField);
                break;
            }
        }

        Button dodajFotografijuButton = new Button("Dodaj fotografiju");
        dodajFotografijuButton.setPrefWidth(580);
        parentVBox.getChildren().add(dodajFotografijuButton);

        ImageView voziloImageView = new ImageView();
        voziloImageView.setFitHeight(200);
        voziloImageView.setFitWidth(380);
        parentVBox.getChildren().add(voziloImageView);

        dodajFotografijuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();

                FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("*.jpg", "*.JPG", "*.gif", "*.GIF", "*.png", "*.PNG");
                fileChooser.getExtensionFilters().addAll(imageFilter);

                fileChooser.setTitle("Dodaj fotografiju vozila");
                File file = fileChooser.showOpenDialog((Stage) (dodajFotografijuButton.getScene().getWindow()));
                voziloImage = file;
                voziloImageView.setImage(new Image(voziloImage.toURI().toString()));

            }
        });
    }

    @FXML
    private void addVoziloSubmitButtonAction() {

        String naziv = ((TextField) (parentVBox.getChildren().get(1))).getText();
        String brojSasije = ((TextField) (parentVBox.getChildren().get(2))).getText();
        String brojMotora = ((TextField) (parentVBox.getChildren().get(3))).getText();
        String registarskiBroj = ((TextField) (parentVBox.getChildren().get(4))).getText();

        switch (GarageApplication.getExchanger().getVoziloInput()) {
            case "Automobil": {

                String brojVrata = ((TextField) (parentVBox.getChildren().get(5))).getText();
                Automobil auto = new Automobil(naziv, brojSasije, brojMotora, voziloImage, registarskiBroj, Integer.parseInt(brojVrata));
                auto.setTrenutniNivo(GarageApplication.getExchanger().getNivo());
                //proba
                GarageApplication.getExchanger().VOZILO_KRETANJE = auto;

                GarageApplication.getExchanger().getVozila().add(auto);

                break;
            }
            case "Kombi": {
                String nosivost = ((TextField) (parentVBox.getChildren().get(5))).getText();
                Kombi kombi = new Kombi(naziv, brojSasije, brojMotora, voziloImage, registarskiBroj, Double.parseDouble(nosivost));
                kombi.setTrenutniNivo(GarageApplication.getExchanger().getNivo());
                
                GarageApplication.getExchanger().VOZILO_KRETANJE = kombi;

                GarageApplication.getExchanger().getVozila().add(kombi);

                break;
            }
            case "Motocikl": {

                Motocikl motocikl = new Motocikl(naziv, brojSasije, brojMotora, voziloImage, registarskiBroj);
                motocikl.setTrenutniNivo(GarageApplication.getExchanger().getNivo());

                GarageApplication.getExchanger().VOZILO_KRETANJE = motocikl;

                GarageApplication.getExchanger().getVozila().add(motocikl);
                break;
            }

        }
        GarageApplication.getExchanger().refreshVozilaTable();

        ((Stage) addVoziloSubmitButton.getScene().getWindow()).close();
    }

}
