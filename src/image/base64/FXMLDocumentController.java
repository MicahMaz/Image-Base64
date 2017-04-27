/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image.base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JFileChooser;

/**
 *
 * @author siddhartha
 */
public class FXMLDocumentController implements Initializable {
    
  private JFileChooser chooser;
  
  @FXML
  Button btnBr;
  @FXML
  ImageView imgDisplayImage;
    
  @FXML
  TextArea teBase64Text;
  
  @FXML
  TextField tfBrowseLocation;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        chooser = new JFileChooser();
        tfBrowseLocation.setText(new java.io.File("").getAbsolutePath());
    }

@FXML
private void btnBrowse(Event e){
     File file = new File(getFilePath());
      Image image = new Image(file.toURI().toString());
      imgDisplayImage.setImage(image);
      imgDisplayImage.setFitHeight(image.getHeight());
        imgDisplayImage.setFitWidth(image.getWidth());
        imgDisplayImage.setPreserveRatio(true);
        
        tfBrowseLocation.setText(file.toURI().toString());
        
        Task task = new Task<Void>() {
    @Override public Void call(){
        teBase64Text.setText(encodeFileToBase64Binary(file));
         teBase64Text.setWrapText(true);
         
         System.out.println(encodeFileToBase64Binary(file));
        return null;
       
    }
};
        
   new Thread(task).start();     
        
}   

 private static String encodeFileToBase64Binary(File file){
            String encodedfile = null;
            try {
                FileInputStream fileInputStreamReader = new FileInputStream(file);
                byte[] bytes = new byte[(int)file.length()];
                fileInputStreamReader.read(bytes);
                encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
                //encodedfile = Base64.getEncoder().encode(bytes).toString();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block

            } catch (IOException e) {
                // TODO Auto-generated catch block

            }

            return encodedfile;
        }

    private String getFilePath() {
       chooser.setCurrentDirectory(new java.io.File("."));
chooser.setDialogTitle("Choose File");
chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
chooser.setAcceptAllFileFilterUsed(false);

if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
  System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
  System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
} else {
  System.out.println("No Selection ");
}

return String.valueOf(chooser.getSelectedFile());
    }
    
    @FXML
    private void mnuClose(Event e){
       Platform.exit();
        System.exit(0);
    }
    
    @FXML
    private void mnuImport(Event e){
        btnBr.fire();
    }
    
    
    
}
