package ssm.view;

import java.io.File;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import static ssm.StartupConstants.CSS_CLASS_SLIDE_EDIT_VIEW;
import static ssm.StartupConstants.DEFAULT_THUMBNAIL_WIDTH;
import ssm.controller.ImageSelectionController;
import ssm.model.Slide;
import static ssm.file.SlideShowFileManager.SLASH;
import ssm.model.SlideShowModel;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import ssm.error.ErrorHandler;
import ssm.model.SlideShowModel;

/**
 * This UI component has the controls for editing a single slide in a slide
 * show, including controls for selected the slide image and changing its
 * caption.
 *
 * @author McKilla Gorilla & _____________
 */
public class SlideEditView extends HBox{
    // SLIDE THIS COMPONENT EDITS
    Slide slide;
    SlideShowModel allSlides;
    // DISPLAYS THE IMAGE FOR THIS SLIDE
    ImageView imageSelectionView;
    // CONTROLS FOR EDITING THE CAPTION
    VBox captionVBox;
    Label captionLabel;
    TextField captionTextField;
    String caption;
    // PROVIDES RESPONSES FOR IMAGE SELECTION
    ImageSelectionController imageController;
    /**
     * THis constructor initializes the full UI for this component, using the
     * initSlide data for initializing values./
     *
     * @param initSlide The slide to be edited by this component.
     */
    public SlideEditView(Slide initSlide) {
        // FIRST SELECT THE CSS STYLE CLASS FOR THIS CONTAINER
        this.getStyleClass().add(CSS_CLASS_SLIDE_EDIT_VIEW);

        // KEEP THE SLIDE FOR LATER
        slide = initSlide;
        allSlides = null;
        // MAKE SURE WE ARE DISPLAYING THE PROPER IMAGE
        imageSelectionView = new ImageView();
        updateSlideImage();
        // SETUP THE CAPTION CONTROLS
        captionVBox = new VBox();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        captionLabel = new Label(props.getProperty(LanguagePropertyType.LABEL_CAPTION));
        captionTextField = new TextField();
        TextHandler snow = new TextHandler();
        captionTextField.setOnAction(snow);
        captionVBox.getChildren().add(captionLabel);
        captionVBox.getChildren().add(captionTextField);
        // LAY EVERYTHING OUT INSIDE THIS COMPONENT
        getChildren().add(imageSelectionView);
        getChildren().add(captionVBox);
    
        addEventHandler(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event)
            {
                if(event.equals(MouseEvent.MOUSE_PRESSED))
                   changeUp();
            }
        });
        setOnMousePressed(e->{changeUp();});
        // SETUP THE EVENT HANDLERS
        imageController = new ImageSelectionController();
        imageSelectionView.setOnMousePressed(e -> {imageController.processSelectImage(slide, this);});
    }
    public ImageView getImage()
    {
        return imageSelectionView;
    }
    public void changeUp()
    {
        this.setStyle("-fx-background-color: rgb(90, 255, 90);");
        
        allSlides.setSelectedSlide(slide);
        allSlides.reloadAnyway();
    }
    public void setSlides(SlideShowModel p1)
    {
        allSlides = p1;
    }
    public SlideShowModel getSlidest()
    {
        return allSlides;
    }
    /**
     * This function gets the image for the slide and uses it to update the
     * image displayed.
     */
    public void updateSlideImage() {
        String imagePath = slide.getImagePath() + SLASH + slide.getImageFileName();
        File file = new File(imagePath);
        try {
            // GET AND SET THE IMAGE
            URL fileURL = file.toURI().toURL();
            Image slideImage = new Image(fileURL.toExternalForm());
            imageSelectionView.setImage(slideImage);

            // AND RESIZE IT
            double scaledWidth = DEFAULT_THUMBNAIL_WIDTH;
            double perc = scaledWidth / slideImage.getWidth();
            double scaledHeight = slideImage.getHeight() * perc;
            imageSelectionView.setFitWidth(scaledWidth);
            imageSelectionView.setFitHeight(scaledHeight);
        } catch (Exception e) {
            
            System.out.println("Could not update slide image");
        }
    }
    class TextHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent e)
        {
            caption = captionTextField.getText();
            slide.updateCaption(caption);
            changeUp();
        }
    }
}
