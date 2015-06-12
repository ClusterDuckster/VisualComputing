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
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;


/**
 *
 * @author Oliver
 */
public class FXMLDocumentController implements Initializable {
    
    String apiKey = "ec7e3362915164ccab3a61299422bd7b";
    String sharedSecret = "07b05df1b1c75797";
    
    @FXML
    FlowPane flowPane;
    
    @FXML
    public void handleImageClicked(MouseEvent event){
        System.out.println("Image clicked");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        try {
            this.initPrevPics();
        } catch (FlickrException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void initPrevPics() throws FlickrException{
        
        Flickr f = new Flickr(apiKey, sharedSecret, new REST());
        TestInterface testInterface = f.getTestInterface();
        Collection results = testInterface.echo(Collections.EMPTY_MAP);
        
        PhotosInterface iface = f.getPhotosInterface();
        PhotoList photos = iface.getRecent(null,10,0);
        
        for(int i=0; i<photos.size(); i++){
            
            VBox vb = new VBox();
            vb.setPadding(new Insets(20, 30, 20, 30));
            
            Image tempImg = SwingFXUtils.toFXImage(iface.getImage((Photo) photos.get(i), Size.SMALL),null);
            ImageView imgView = new ImageView(tempImg);
            imgView.setId("imgView"+i);
            imgView.setStyle("-fx-margin: 50px; ");
            
            vb.getChildren().add(imgView);
            flowPane.getChildren().add(vb);
        }
        
        
    }
    
}
