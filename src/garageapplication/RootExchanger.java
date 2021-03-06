/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garageapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javafx.stage.Stage;
import vozilo.Vozilo;
import garaza.Garaza;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import specijalnovozilo.InstitutionalInterface;

/**
 *
 * @author Tijana Lakic
 */
public class RootExchanger {

    private Stage parentStage;
    private Stage currentStage;
    private TextArea simulacijaMatrica;

    public static final Object specijalnoVoziloLock = new Object();
    public Garaza garaza;

    public ArrayList<Vozilo> vozila = new ArrayList<>();

    private String voziloInput;

    //Nestaticki blok koji se pokrece prilikom inicajalizacije root exchangera
    {
        File file = new File("garaza.ser");
        if (file.exists()) {
            System.out.println("POSTOJI SERIJALIZOVAN FAJL");
            try (ObjectInputStream ser = new ObjectInputStream(new FileInputStream(file))) {
                garaza = (Garaza) ser.readObject();
            } catch (Exception ex) {
                System.out.println("DESERIJALIZACIJA ERROR" + ex);
            }
        } else {
            System.out.println("NEMA SERIJALIZOVANOG FAJLA");
            garaza = new Garaza();
        }
        refreshVozilaTable();
    }

    public void refreshVozilaTable() {
        FXMLDocumentController.listaVozila.clear();
        ArrayList<Vozilo> listaObicnihVozila= new ArrayList<>();
        getVozila().stream().filter((v) -> (!(v instanceof InstitutionalInterface))).forEachOrdered((v) -> {
            listaObicnihVozila.add(v);
        });
        FXMLDocumentController.listaVozila.setAll(listaObicnihVozila);
    }

    public void setSimulacijaMatrica(TextArea simulacijaMatrica) {
        this.simulacijaMatrica = simulacijaMatrica;
    }

    public Stage getParentStage() {
        return parentStage;
    }

    public void setParentStage(Stage stage) {
        parentStage = stage;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setVoziloInput(String v) {
        this.voziloInput = v;
    }

    public String getVoziloInput() {
        return voziloInput;
    }

    public void setVozila(int nivo, ArrayList<Vozilo> vozila) {
        garaza.getPlatforme().get(nivo).setListaVozila(vozila);

    }

    public ArrayList<Vozilo> getVozila() {
        return garaza.getPlatforme().get(nivo).getListaVozila();

    }

    private Vozilo editVozilo;

    public void setEditVozilo(Vozilo editVozilo) {
        this.editVozilo = editVozilo;
    }

    public Vozilo getEditVozilo() {
        return editVozilo;
    }

    public Garaza getGaraza() {

        return garaza;
    }

    int nivo = 0;

    public void setNivo(int nivo) {
        this.nivo = nivo;
    }

    public int getNivo() {

        return nivo;
    }

    int nivoPrikaz = 0;

    public void setNivoPrikaz(int nivoPrikaz) {
        this.nivoPrikaz = nivoPrikaz;
    }

    public int getNivoPrikaz() {

        return nivoPrikaz;
    }

    public int getBrojVozila() {

        return garaza.getPlatforme().get(nivo).getListaVozila().size();

    }

    public int getBrojVozila(int nivo) {

        return garaza.getPlatforme().get(nivo).getListaVozila().size();

    }

    public synchronized void refreshSimulacijaMatrica() {
        Platform.runLater(() -> {
            try {
                simulacijaMatrica.setText(garaza.getPlatforme().get(nivoPrikaz).toString());
            } catch (Exception ex) {
                Logger.getLogger("error.log").log(Level.SEVERE, null, ex);
            }
        });
    }
    public static Vozilo VOZILO_KRETANJE;
    
    public static HashMap<Vozilo,String> mjestaNesrece = new HashMap<Vozilo, String>();
    public static HashMap<Vozilo,ArrayList<Vozilo>> ucesniciUNesreci = new HashMap<>();
}
