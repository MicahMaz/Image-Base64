/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image.base64;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JFileChooser;
import sun.misc.BASE64Decoder;

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
    ScrollPane spImage;
  @FXML
  TextArea teBase64Text;
  
  @FXML
  TextField tfBrowseLocation;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        chooser = new JFileChooser();
        tfBrowseLocation.setText(new java.io.File("").getAbsolutePath());
        
        onActionEvent();
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
    
    @FXML
    private void mnuAbout(Event e) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLAbout.fxml"));
                Parent rootAbout = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(rootAbout));  
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("About Image Base64");
                stage.setResizable(false);
                stage.show();
          }

    private void onActionEvent() {
      spImage.setOnDragOver(event->{
       Dragboard db = event.getDragboard();
            if (db.hasImage() || db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
      });
      
      spImage.setOnDragDropped(event->{
                     Dragboard db = event.getDragboard();
            if (db.hasImage()) {
//                ImageView imageView = new ImageView(db.getImage());
//                imageView.setFitHeight(IMAGE_SIZE);
//                imageView.setFitWidth(IMAGE_SIZE);
//                imageView.setPreserveRatio(true);
//                imagePane.getChildren().add(imageView);
                imgDisplayImage.setImage(db.getImage());
                event.setDropCompleted(true);
            } else if (db.hasFiles()) {
                db.getFiles().forEach(file -> {
                    try {
                        Image i = new Image(file.toURI().toString());
                        
                        Image image = new Image(file.toURI().toString(), i.getWidth(), i.getHeight(), true, true);
                           imgDisplayImage.setImage(image);
                           teBase64Text.setText(encodeFileToBase64Binary(file));
//                        ImageView imageView = new ImageView(image);
//                        imagePane.getChildren().add(imageView);
                            teBase64Text.setWrapText(true);
                        tfBrowseLocation.setText(file.toURI().toString());
                    } catch (Exception exc) {
                        System.out.println("Could not load image "+file);
                    }
      });
    }
      });
      
      teBase64Text.textProperty().addListener((observable, oldValue, newValue) -> {
          try {
              // System.out.println("textfield changed from " + oldValue + " to " + newValue);

              getImageFromBase64String(newValue);
               teBase64Text.setWrapText(true);
          } catch (IOException ex) {
              Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
          }
});
    }    

    private void getImageFromBase64String(String newValue) throws IOException {
       BASE64Decoder base64Decoder = new BASE64Decoder();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64Decoder.decodeBuffer(newValue));
        Image img = new Image(inputStream);
       imgDisplayImage.setImage(img);
    }
}
