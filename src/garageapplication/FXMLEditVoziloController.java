/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garageapplication;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class FXMLEditVoziloController implements Initializable {

    private File voziloImage=new File("");
    @FXML
    private Button editVoziloSubmitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Vozilo currentVozilo = GarageApplication.getExchanger().getEditVozilo();

        ((TextField) (parentVBox.getChildren().get(1))).setText(currentVozilo.getNaziv());
        ((TextField) (parentVBox.getChildren().get(4))).setText(currentVozilo.getRegistarskiBroj());
        ((TextField) (parentVBox.getChildren().get(2))).setText(currentVozilo.getBrojSasije());
        ((TextField) (parentVBox.getChildren().get(3))).setText(currentVozilo.getBrojMotora());
//currentVozilo.getClass().getSimpleName()
        switch (currentVozilo.getTip()) {
            case AUTO: {
                Automobil currentAutomobil = (Automobil) currentVozilo;
              
                TextField brojVrataField = new TextField();
                brojVrataField.setText(Integer.toString(currentAutomobil.getBrojVrata()));
                parentVBox.getChildren().add(brojVrataField);
                break;
            }
            case KOMBI: {
                Kombi currentKombi = (Kombi) currentVozilo;
                

                TextField nosivostField = new TextField();
                nosivostField.setText(Double.toString(currentKombi.getNosivost()));
                nosivostField.setMaxHeight(120);
                parentVBox.getChildren().add(nosivostField);

                break;
            }
         
        }

        Button dodajFotoButton = new Button("Izmjeni fotografiju");
        dodajFotoButton.setPrefWidth(580);
        parentVBox.getChildren().add(dodajFotoButton);

        ImageView fotkaImageView = new ImageView();
        fotkaImageView.setFitHeight(200);
        fotkaImageView.setFitWidth(380);
        
        Image image = new Image(currentVozilo.getFoto().toURI().toString());
        fotkaImageView.setImage(image);
        
        parentVBox.getChildren().add(fotkaImageView);

        dodajFotoButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();

                FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("*.jpg", "*.JPG",
                        "*.gif", "*.GIF", "*.png", "*.PNG");
                fileChooser.getExtensionFilters().addAll(imageFilter);

                fileChooser.setTitle("Dodaj fotografiju");
                File file = fileChooser.showOpenDialog((Stage) (dodajFotoButton.getScene().getWindow()));
                voziloImage = file;
                fotkaImageView.setImage(new Image(voziloImage.toURI().toString()));
               

            }
        });
    }

    @FXML
    public void editVoziloSubmitButtonAction() {

        Vozilo currentVozilo = GarageApplication.getExchanger().getEditVozilo();

        currentVozilo.setNaziv(((TextField) (parentVBox.getChildren().get(1))).getText());
        currentVozilo.setRegistarskiBroj(((TextField) (parentVBox.getChildren().get(4))).getText());
        currentVozilo.setBrojMotora(((TextField) (parentVBox.getChildren().get(3))).getText());
        currentVozilo.setBrojSasije(((TextField) (parentVBox.getChildren().get(2))).getText());
        currentVozilo.setFoto(voziloImage);
        
        switch (currentVozilo.getTip()) {
            case AUTO: {
                Automobil currentAutomobil = (Automobil) currentVozilo;
               
                currentAutomobil.setBrojVrata(Integer.parseInt(((TextField) (parentVBox.getChildren().get(5))).getText()));

                GarageApplication.getExchanger().refreshVozilaTable();
                break;
            }
            case KOMBI: {
                Kombi currentKombi = (Kombi) currentVozilo;
                
                currentKombi.setNosivost(Double.parseDouble(((TextField) (parentVBox.getChildren().get(5))).getText()));

                GarageApplication.getExchanger().refreshVozilaTable();
                break;
            }
            case MOTOR: {
                Motocikl currentMotocikl = (Motocikl) currentVozilo;
               

                GarageApplication.getExchanger().refreshVozilaTable();
                break;
            }

        }
        ((Stage) editVoziloSubmitButton.getScene().getWindow()).close();
    }

    @FXML
    private VBox parentVBox;

}
