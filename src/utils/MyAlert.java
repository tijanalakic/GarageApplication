package utils;

//import static garageapplication.FXMLDocumentController;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MyAlert{
    
    public static void display(String title,String message,String type){
        Alert alert;
        if(type.equals("error")){
          alert=new Alert(Alert.AlertType.ERROR);
        }
        else{
            alert=new Alert(Alert.AlertType.INFORMATION);
        }
        
        alert.setTitle(title);
        alert.setHeaderText(message);
       
        alert.showAndWait();
    }
}
