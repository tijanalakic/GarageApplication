/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garageapplication;

import garaza.Garaza;
import garaza.Platforma;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import specijalnovozilo.PolicijskiAutomobil;
import specijalnovozilo.PolicijskiKombi;
import specijalnovozilo.PolicijskiMotocikl;
import specijalnovozilo.SanitetskiAutomobil;
import specijalnovozilo.SanitetskiKombi;
import specijalnovozilo.VatrogasniKombi;
import vozilo.Automobil;
import vozilo.Kombi;
import vozilo.Motocikl;
import vozilo.Vozilo;

/**
 *
 * @author Tijana Lakic
 */
public class FXMLUserApplicationController implements Initializable {

    public static Garaza garaza = GarageApplication.getExchanger().getGaraza();

    @FXML
    private TextField brojVozilaTextField;

    @FXML
    private AnchorPane applicationMainAnchorPane;
    @FXML
    private AnchorPane applicationMenuAnchorPane;
    @FXML
    private VBox formVBox;

    @FXML
    private Button userAppStartCompetitionButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void userAppStartCompetitionButtonAction() throws IOException {

        String[] nizSpecijalnihVozila = {"policijski automobil", "policijski kombi",
            "policijski motocikl", "sanitarni automobil", "sanitarni kombi",
            "vatrogasni kombi"};
        String[] nizObicnihVozila = {"automobil", "motocikl", "kombi"};

        Random rn = new Random();

        int brojSpecijalnihVozila = (int) Math.round(Integer.parseInt(brojVozilaTextField.getText()) * 0.1);

        for (int j = 0; j < GarageApplication.getExchanger().getGaraza().getBrojPlatformi(); j++) {

            int razlika = (int) (Integer.parseInt(brojVozilaTextField.getText()) - GarageApplication.getExchanger().getBrojVozila(j));

            for (int i = 0; i < brojSpecijalnihVozila; i++) {
                String tipSpecijalnogVozila = nizSpecijalnihVozila[rn.nextInt(6)];

                Vozilo vozilo = null;
                switch (tipSpecijalnogVozila) {

                    case "policijski automobil":
                        vozilo = new PolicijskiAutomobil();
                        break;
                    case "policijski kombi":
                        vozilo = new PolicijskiKombi();

                        break;
                    case "policijski motocikl":
                        vozilo = new PolicijskiMotocikl();

                        break;
                    case "sanitarni automobil":
                        vozilo = new SanitetskiAutomobil();

                        break;
                    case "sanitarni kombi":
                        vozilo = new SanitetskiKombi();

                        break;
                    case "vatrogasni kombi":
                        vozilo = new VatrogasniKombi();

                        break;
                }
                vozilo.setTrenutniNivo(j);
                garaza.getPlatforme().get(j).setElement(vozilo);
            }
            for (int k = 0; k < razlika - brojSpecijalnihVozila; k++) {

                String tipVozila = nizObicnihVozila[rn.nextInt(3)];
                Vozilo vozilo = null;

                switch (tipVozila) {
                    case "automobil":
                        vozilo = new Automobil();
                        break;
                    case "kombi":
                        vozilo = new Kombi();
                        break;
                    case "motocikl":
                        vozilo = new Motocikl();
                        break;
                }
                vozilo.setTrenutniNivo(j); //zar ne bi trebalo na pocetku setovati prije svica
                garaza.getPlatforme().get(j).setElement(vozilo);

            }
        }

        for (int i = 0; i < garaza.brojPlatformi; i++) {

            garaza.getPlatforme().get(i).setMatrica();

        }
        String ispis = "";
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 8; j++) {
                if (garaza.getPlatforme().get(0).getMatrica()[j][i] == null) {
                    ispis += "   ";

                } else if (garaza.getPlatforme().get(0).getMatrica()[j][i] instanceof Vozilo) {
                    ispis += " (" + ((Vozilo) garaza.getPlatforme().get(0).getMatrica()[j][i]).getX() + ", "
                            + ((Vozilo) garaza.getPlatforme().get(0).getMatrica()[j][i]).getY();
                }
            }
            ispis += "\n";
        }
        System.out.println("aa" + ispis);

        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLSimulation.fxml"));

        Scene stageScene = new Scene(root, 800, 500);

        newStage.setScene(stageScene);
        newStage.setHeight(1000);
        newStage.setWidth(1500);
        newStage.setTitle("Korisnicki dio");
        newStage.show();

        ((Stage) userAppStartCompetitionButton.getScene().getWindow()).close();

    }

}
