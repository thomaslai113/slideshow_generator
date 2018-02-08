package ssm.model;

import java.awt.Color;
import java.awt.Paint;
import java.io.File;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import static ssm.StartupConstants.DEFAULT_THUMBNAIL_WIDTH;
import static ssm.StartupConstants.PATH_SLIDE_SHOW_IMAGES;
import ssm.error.ErrorHandler;
import static ssm.file.SlideShowFileManager.SLASH;
import ssm.view.SlideShowMakerView;

/**
 * This class manages all the data associated with a slideshow.
 * 
 * @author McKilla Gorilla & _____________
 */
public class SlideShowModel {
    SlideShowMakerView ui;
    String title;
    ObservableList<Slide> slides;
    Slide selectedSlide;
    
    public SlideShowModel(SlideShowMakerView initUI) {
	ui = initUI;
	slides = FXCollections.observableArrayList();
	reset();	
    }

    // ACCESSOR METHODS
    public boolean isSlideSelected() {
	return selectedSlide != null;
    }
    
    public ObservableList<Slide> getSlides() {
	return slides;
    }
    
    public Slide getSelectedSlide() {
	return selectedSlide;
    }

    public String getTitle() { 
	return title; 
    }
    public SlideShowMakerView getUI()
    {
        return ui;
    }
    
    // MUTATOR METHODS
    public void setSelectedSlide(Slide initSelectedSlide) {
	selectedSlide = initSelectedSlide;
    }
    
    public void setTitle(String initTitle) { 
	title = initTitle; 
    }

    // SERVICE METHODS
    
    /**
     * Resets the slide show to have no slides and a default title.
     */
    public void reset() {
	slides.clear();
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	title = props.getProperty(LanguagePropertyType.DEFAULT_SLIDE_SHOW_TITLE);
	selectedSlide = null;
    }

    /**
     * Adds a slide to the slide show with the parameter settings.
     * @param initImageFileName File name of the slide image to add.
     * @param initImagePath File path for the slide image to add.
     */
    public void addSlide(   String initImageFileName,
			    String initImagePath) {
	Slide slideToAdd = new Slide(initImageFileName, initImagePath);
	slides.add(slideToAdd);
	selectedSlide = slideToAdd;
	ui.reloadSlideShowPane(this);
    }
    public void reloadAnyway()
    {
        FlowPane captionCarrier = new FlowPane();
        FlowPane captionCarrier1 = new FlowPane();
        captionCarrier.setStyle("-fx-background-color: rgb(255, 255, 255);");     
        Label captionLab = new Label();
        Label captionLab1 = new Label();
        ui.reloadSlideShowPane(this);
        ImageView star =new ImageView();
        String imagePath = selectedSlide.getImagePath() + SLASH + selectedSlide.getImageFileName();
        File file = new File(imagePath);
        try {
            // GET AND SET THE IMAGE
            URL fileURL = file.toURI().toURL();
            Image slideImage = new Image(fileURL.toExternalForm());
            star.setImage(slideImage);

            // AND RESIZE IT
            double scaledWidth = ui.slideSpace.getWidth();
            double perc = scaledWidth / slideImage.getWidth();
            double scaledHeight = slideImage.getHeight() * perc;
            star.setFitWidth(scaledWidth);
            star.setFitHeight(scaledHeight);
            if(star.getFitHeight()>960)
                star.setFitHeight(960);
        } catch (Exception e) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processErrors(LanguagePropertyType.ERROR_DATA_FILE_LOADING,LanguagePropertyType.ERRORDATA, "Missing Image");
        }
        captionLab.setText(selectedSlide.getCaption());
        captionLab.setId("captiontext");
        captionCarrier.getChildren().add(captionLab);
        captionCarrier1.setStyle("-fx-background-color: rgb(0, 255, 0);");
        ui.slideSpace.getChildren().clear();
        ui.slideSpace.getChildren().add(star);
        ui.slideSpace.getChildren().add(captionCarrier);
        ImageView star1 = new ImageView();
        try {
            // GET AND SET THE IMAGE
            URL fileURL = file.toURI().toURL();
            Image slideImage = new Image(fileURL.toExternalForm());
            star1.setImage(slideImage);

            // AND RESIZE IT
            star1.setFitWidth(ui.presentSpace.getWidth());
            star1.setFitHeight(ui.presentSpace.getHeight());
        } catch (Exception e) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processErrors(LanguagePropertyType.ERROR_DATA_FILE_LOADING,LanguagePropertyType.ERRORDATA, "Missing Image");
        }
        captionLab1.setText(selectedSlide.getCaption());
        captionLab1.setId("captiontext");
        captionCarrier1.getChildren().add(captionLab1);
        star1.setFitWidth(1900);
        star1.setFitHeight(950);
        ui.presentSpace.getChildren().clear();
        ui.presentSpace.getChildren().add(star1);
        ui.presentSpace.getChildren().add(captionCarrier1);
        ui.notSaved();
        setIconImage();
    }
    public void setIconImage()
    {
        String imagePath = PATH_SLIDE_SHOW_IMAGES + SLASH + "ICP.jpg";
        File file = new File(imagePath);
        try {
            // GET AND SET THE IMAGE
            URL fileURL = file.toURI().toURL();
            Image slideImage = new Image(fileURL.toExternalForm());
            ui.getWindow().getIcons().add(slideImage);
        }
        catch(Exception e)
        {System.out.println("Nope");}
    }
}