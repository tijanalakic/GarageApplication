/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garageapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Tijana Lakic
 */
public class GarageApplication extends Application {

    private static final RootExchanger EXCHANGER = new RootExchanger();

    static public RootExchanger getExchanger() {
        return EXCHANGER;
    }

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);

        stage.setMinHeight(800);
        stage.setMinWidth(500);

        stage.setScene(scene);
        stage.setTitle("Administratorski dio");
        stage.show();

    }
}
