/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monocholor;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.test.TestInterface;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 *
 * @author Oliver
 */
public class FXMLDocumentController implements Initializable {
    
    String apiKey = "ec7e3362915164ccab3a61299422bd7b";
    String sharedSecret = "07b05df1b1c75797";
    
    PhotosInterface iface;
    PhotoList photos;
    
    int clickedPhoto;
    
    @FXML
    FlowPane flowPane;
    
    @FXML
    BorderPane borderPane;
    
    @FXML
    public void handleImageClicked(MouseEvent event){
        System.out.println("Image clicked");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
        try {
            Flickr f = new Flickr(apiKey, sharedSecret, new REST());
            TestInterface testInterface = f.getTestInterface();
            Collection results = testInterface.echo(Collections.EMPTY_MAP);
        
            iface = f.getPhotosInterface();
            photos = iface.getRecent(null,10,0);
        } catch (FlickrException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File file = new File(url.getFile());
        String fxml = file.getName();
        if(fxml.equals("FXMLDocument.fxml")){
            try {
                this.initPrevPics();
            } catch (FlickrException ex) {
             Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                Image editImg = SwingFXUtils.toFXImage(iface.getImage((Photo) photos.get(clickedPhoto), Size.SMALL),null);
            } catch (FlickrException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            ImageView editImgView = new ImageView();
            //borderPane.getChildren().add(editImgView);
            //borderPane.setCenter(editImgView);
            System.out.println("so weit so gut" + borderPane.toString());
        }
    }    
    
    private void initPrevPics() throws FlickrException{
        
        for(int i=0; i<photos.size(); i++){
            
            VBox vb = new VBox();
            vb.setPadding(new Insets(20, 30, 20, 30));
            
            Image tempImg = SwingFXUtils.toFXImage(iface.getImage((Photo) photos.get(i), Size.SMALL),null);
            ImageView imgView = new ImageView(tempImg);
            imgView.setId(""+i);
            imgView.setStyle("-fx-margin: 50px; ");
            
            imgView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    Stage aktStage = (Stage)imgView.getScene().getWindow();
                    
                    try {
                        clickedPhoto = Integer.parseInt(imgView.getId());
                        Parent root = FXMLLoader.load(getClass().getResource("FXMLEditPhoto.fxml"));
                        Scene editScene = new Scene(root);
                        aktStage.setScene(editScene);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            vb.getChildren().add(imgView);
            flowPane.getChildren().add(vb);
        }
        
        
    }
    
}
