package ssm.controller;

import properties_manager.PropertiesManager;
import static ssm.LanguagePropertyType.DEFAULT_IMAGE_CAPTION;
import static ssm.StartupConstants.DEFAULT_SLIDE_IMAGE;
import static ssm.StartupConstants.PATH_SLIDE_SHOW_IMAGES;
import ssm.model.SlideShowModel;
import ssm.view.SlideShowMakerView;
import javafx.collections.ObservableList;
import ssm.model.Slide;
import ssm.view.SlideEditView;

/**
 * This controller provides responses for the slideshow edit toolbar,
 * which allows the user to add, remove, and reorder slides.
 * 
 * @author McKilla Gorilla & _____________
 */
public class SlideShowEditController {
    // APP UI
    private SlideShowMakerView ui;
    
    /**
     * This constructor keeps the UI for later.
     */
    public SlideShowEditController(SlideShowMakerView initUI) {
	ui = initUI;
    }
    
    /**
     * Provides a response for when the user wishes to add a new
     * slide to the slide show.
     */
    public void processAddSlideRequest() {
	SlideShowModel slideShow = ui.getSlideShow();
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	slideShow.addSlide(DEFAULT_SLIDE_IMAGE, PATH_SLIDE_SHOW_IMAGES);
    }
    public void slideShowSwapUp()
    {
        SlideShowModel slideShow = ui.getSlideShow();
        ObservableList<Slide> ss = slideShow.getSlides();
        if(ss.get(0)==slideShow.getSelectedSlide())
            return;
        int temp = 0;
        for(int i=1; i<ss.size();i++)
        {
            if(ss.get(i)==slideShow.getSelectedSlide())
            {
            temp = i;
            i += ss.size();
            }
        }
            Slide ntemp = new Slide(ss.get(temp).getImageFileName(), ss.get(temp).getImagePath());
            ntemp.updateCaption(ss.get(temp).getCaption());
            ss.set(temp,ss.get(temp-1));
            ss.set(temp-1,ntemp);
            slideShow.setSelectedSlide(ntemp);
            ui.reloadSlideShowPane(slideShow);
        
        
    }
    public void slideShowSwapDown()
    {
        SlideShowModel slideShow = ui.getSlideShow();
        ObservableList<Slide> ss = slideShow.getSlides();
        if(ss.get(ss.size()-1)==slideShow.getSelectedSlide())
            return;
        int temp = 0;
        for(int i=ss.size()-1; i>=0;i--)
        {
            if(ss.get(i)==slideShow.getSelectedSlide())
            {
            temp = i;
            i -= ss.size();
            }
        }
            Slide ntemp = new Slide(ss.get(temp).getImageFileName(), ss.get(temp).getImagePath());
            ntemp.updateCaption(ss.get(temp).getCaption());
            ss.set(temp,ss.get(temp+1));
            ss.set(temp+1,ntemp);
            slideShow.setSelectedSlide(ntemp);
            ui.reloadSlideShowPane(slideShow);
        
    }
    public void up()
    {
        int g =0;
        SlideShowModel slideShow = ui.getSlideShow();
        ObservableList<Slide> ss = slideShow.getSlides();
        if(slideShow.getSelectedSlide()==ss.get(0))
            ui.slideShowUp1.setDisable(true);
        for(int i = 0; i < ss.size();i++)
        {
            if(ss.get(i)==slideShow.getSelectedSlide())
                g = i;
        }
        slideShow.setSelectedSlide(ss.get(g-1));
        slideShow.reloadAnyway();
        if(g-1 == 0)
            ui.slideShowUp1.setDisable(true);
        else
            ui.slideShowUp1.setDisable(false);
    }
    public void down()
    {
        int g =0;
        SlideShowModel slideShow = ui.getSlideShow();
        ObservableList<Slide> ss = slideShow.getSlides();
        if(slideShow.getSelectedSlide()==ss.get(ss.size()-1))
            ui.slideShowDown1.setDisable(true);
        for(int i = ss.size()-1; i >= 0; i--)
        {
            if(ss.get(i)==slideShow.getSelectedSlide())
                g = i;
        }
        slideShow.setSelectedSlide(ss.get(g+1));
        slideShow.reloadAnyway();
        if(g+1 == ss.size()-1)
            ui.slideShowDown1.setDisable(true);
        else
            ui.slideShowDown1.setDisable(false);
    }
    public void removeSlide()
    {
        SlideShowModel slideShow = ui.getSlideShow();
        ObservableList<Slide> ss = slideShow.getSlides();
        int temp=0;
        for(int i=0; i< ss.size();i++)
        {
            if(ss.get(i)==slideShow.getSelectedSlide())
            {
            temp = i;
            i += ss.size();
            }
        }
        ss.remove(temp);
        slideShow.setSelectedSlide(null);
        ui.reloadSlideShowPane(slideShow);
    }
}
