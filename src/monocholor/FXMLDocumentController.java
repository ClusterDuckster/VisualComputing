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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;


/**
 *
 * @author Oliver
 */
public class FXMLDocumentController implements Initializable {
    
    String apiKey = "ec7e3362915164ccab3a61299422bd7b";
    String sharedSecret = "07b05df1b1c75797";
    
    static Flickr f;
    static PhotosInterface iface;
    static PhotoList photos;
    
    static Image editImg = null;
    
    static int clickedPhoto;
    
    double redRange;
    double greenRange;
    double blueRange;
    
    ImageView editImgView;
    BufferedImage bimg;
    PixelReader pReader;
    Color c;
    
    @FXML
    FlowPane flowPane;
    
    @FXML
    BorderPane borderPane;
    
    @FXML
    Button backBtn;
    
    @FXML
    TextField searchField;
    
    @FXML
    Rectangle pickedColor;
    
    @FXML
    Slider colWidthSlider;
    @FXML
    Slider redSlider;
    @FXML
    Slider greenSlider;
    @FXML
    Slider blueSlider;
    
    @FXML
    public void handleImageClicked(MouseEvent event){
        System.out.println("Image clicked");
    }
    
    @FXML
    public void handleSetBack(ActionEvent event){
        editImgView.setImage(editImg);
    }
    
    @FXML
    public void handleSearchBtn(ActionEvent event) throws FlickrException{
        Set<String> extras = new HashSet<String>();
        extras.add(searchField.getText());
        flowPane.getChildren().remove(0, 8);
        photos = iface.getRecent(null,8,0);
        initPrevPics();
    }
    
    @FXML
    public void handleRefresh(ActionEvent event) throws FlickrException{
        flowPane.getChildren().remove(0, 8);
        photos = iface.getRecent(null,8,0);
        initPrevPics();
    }
    
    @FXML
    public void handleSaveBtn(ActionEvent event) throws IOException{
        Stage secondStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Speichere Bild");
  
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
              
        //Show save file dialog
        File file = fileChooser.showSaveDialog(secondStage);  
        if (file != null) {
           
                ImageIO.write(bimg, "jpg", file);
                
        }
    }
    
    @FXML
    public void handleLoadBtn(ActionEvent event) throws IOException{
        Stage secondStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Speichere Bild");
  
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPEG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        
        File file = fileChooser.showOpenDialog(secondStage);
        if(file != null){
            
            Stage aktStage = (Stage) flowPane.getScene().getWindow();
            editImg = new Image(file.toURI().toString());
            Parent root = FXMLLoader.load(getClass().getResource("FXMLEditPhoto.fxml"));
            Scene editScene = new Scene(root);
            aktStage.setScene(editScene);
        }
    }
    
    @FXML
    public void handleBackButton(ActionEvent event) throws IOException{
        Stage stage; 
        Parent root;
              
        stage=(Stage) backBtn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void handleAnwenden(ActionEvent event){
        
        refreshImg();
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
        /*
        try {
            f = new Flickr(apiKey, sharedSecret, new REST());
            iface = f.getPhotosInterface();
            photos = iface.getRecent(null,10,0);
        } catch (FlickrException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        File file = new File(url.getFile());
        String fxml = file.getName();
        
        if(fxml.equals("FXMLDocument.fxml")){
            try {
                Set<String> extras = new HashSet<String>();
                extras.add("Landschaft");
                f = new Flickr(apiKey, sharedSecret, new REST());
                iface = f.getPhotosInterface();
                photos = iface.getRecent(extras,8,0);
                this.initPrevPics();
            } catch (FlickrException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            
            pReader = editImg.getPixelReader();

            editImgView = new ImageView(editImg);
            editImgView.setPreserveRatio(true);
            editImgView.setFitHeight(650);
            editImgView.setFitWidth(650);
            
            editImgView.setOnMouseClicked(new EventHandler<MouseEvent>(){

                @Override
                public void handle(MouseEvent event) {
                    //ImageView tempImgView = (ImageView)event.getSource();
                    c = pReader.getColor((int)event.getSceneX(), (int)event.getSceneY());
                    pickedColor.setFill(c);
                    //double randX = editImgView.getFitWidth()-editImg.getWidth();
                    //double randY = editImgView.getFitHeight()-editImg.getHeight();
                    //System.out.println("Width: "+editImg.getWidth()+", Height: "+editImg.getHeight());
                    //System.out.println("X: "+event.getSceneX()+", Y: "+event.getSceneY());
                    //System.out.println("newX: "+randX+", newY: "+randY);
                }
                
            });
            
            colWidthSlider.valueProperty().addListener(new ChangeListener<Number>(){

                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    
                    redSlider.setValue(colWidthSlider.getValue());
                    greenSlider.setValue(colWidthSlider.getValue());
                    blueSlider.setValue(colWidthSlider.getValue());
                    refreshImg();
                    }
                });
            
            redSlider.valueProperty().addListener(new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    redRange = redSlider.getValue();
                    refreshImg();
                }
            });
            greenSlider.valueProperty().addListener(new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    greenRange = greenSlider.getValue();
                    refreshImg();
                }
            });
            blueSlider.valueProperty().addListener(new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    blueRange = blueSlider.getValue();
                    refreshImg();
                }
            });
            
            
            borderPane.setCenter(editImgView);
            //System.out.println("so weit so gut " + clickedPhoto);
        }
    }    
    
    private void initPrevPics() throws FlickrException{
        
        for(int i=0; i<photos.size(); i++){
            
            VBox vb = new VBox();
            vb.setPadding(new Insets(20, 30, 20, 30));
            
            Photo tempPhoto = (Photo)photos.get(i);
            tempPhoto.getId();
            Image tempImg = SwingFXUtils.toFXImage(iface.getImage(tempPhoto, Size.SMALL),null);
            ImageView imgView = new ImageView(tempImg);
            imgView.setId(""+i);
            
            imgView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    Stage aktStage = (Stage)imgView.getScene().getWindow();
                    
                    try {
                        clickedPhoto = Integer.parseInt(imgView.getId());
                        editImg = SwingFXUtils.toFXImage(iface.getImage((Photo) photos.get(clickedPhoto), Size.MEDIUM_800),null);
                        Parent root = FXMLLoader.load(getClass().getResource("FXMLEditPhoto.fxml"));
                        Scene editScene = new Scene(root);
                        aktStage.setScene(editScene);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FlickrException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            vb.getChildren().add(imgView);
            flowPane.getChildren().add(vb);
        }
        
        
    }
    
    private void refreshImg(){
        bimg = SwingFXUtils.fromFXImage(editImg, null);
        for(int x=0; x<bimg.getWidth();x++){
            for(int y=0; y<bimg.getHeight();y++){
                boolean inColor = false;
                
                int rgb = bimg.getRGB(x, y);
                int a = (rgb >> 24) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                
                int desR = (int)c.getRed();
                int desG = (int)c.getBlue();
                int desB = (int)c.getGreen();
                double colRange = colWidthSlider.getValue();
                
                if(r>=desR-redRange&&r<=desR+redRange){
                    if(g>=desG-greenRange&&g<=desG+greenRange){
                        if(b>=desB-blueRange&&b<=desB+blueRange){
                            inColor = true;
                        }
                    }
                }
                
                if(inColor) {
                    
                    int grey = (r+g+b)/3;
                    
                    int newRgb = (a << 24) | (grey << 16) | (grey << 8) | grey;
                    bimg.setRGB(x, y, newRgb);
                } else {
                    bimg.setRGB(x, y, rgb);
                }
            }
        }
        Image newImg = SwingFXUtils.toFXImage(bimg, null);
        editImgView.setImage(newImg);
    }
    
}
