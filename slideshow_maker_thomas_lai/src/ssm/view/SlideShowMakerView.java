package ssm.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.*;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.net.URL;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import static ssm.LanguagePropertyType.*;
import static ssm.StartupConstants.*;
import static ssm.StartupConstants.PATH_ICONS;
import static ssm.StartupConstants.STYLE_SHEET_UI;
import ssm.controller.FileController;
import ssm.controller.SlideShowEditController;
import ssm.model.Slide;
import ssm.model.SlideShowModel;
import ssm.error.ErrorHandler;
import ssm.file.SlideShowFileManager;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import ssm.SlideShowMaker;
import static ssm.file.SlideShowFileManager.SLASH;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import javafx.stage.*;

/**
 * This class provides the User Interface for this application,
 * providing controls and the entry points for creating, loading, 
 * saving, editing, and viewing slide shows.
 * 
 * @author McKilla Gorilla & _____________
 */
public class SlideShowMakerView {

    // THIS IS THE MAIN APPLICATION UI WINDOW AND ITS SCENE GRAPH
    Stage primaryStage;
    Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION GUI
    BorderPane ssmPane;

    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    FlowPane fileToolbarPane;
    Button newSlideShowButton;
    Button loadSlideShowButton;
    Button saveSlideShowButton;
    Button viewSlideShowButton;
    Button exitButton;
    // WORKSPACE
    HBox workspace;
    HBox btn;
    // THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
    VBox slideEditToolbar;
    public VBox slideSpace;
    public VBox presentSpace;
    Button addSlideButton;
    Button slideShowUp;
    Button slideShowDown;
    public Button slideShowUp1;
    public Button slideShowDown1;
    Button slideShowRemove;
    // AND THIS WILL GO IN THE CENTER
    ScrollPane slidesEditorScrollPane;
    public VBox slidesEditorPane;
    Scene questionScene;
    FlowPane questionPane;
    public Stage questionStage;
    public Scene webScene;
    boolean canc;
    boolean language;
    // THIS IS THE SLIDE SHOW WE'RE WORKING WITH
    SlideShowModel slideShow;
    
    // THIS IS FOR SAVING AND LOADING SLIDE SHOWS
    SlideShowFileManager fileManager;

    // THIS CLASS WILL HANDLE ALL ERRORS FOR THIS PROGRAM
    private ErrorHandler errorHandler;

    // THIS CONTROLLER WILL ROUTE THE PROPER RESPONSES
    // ASSOCIATED WITH THE FILE TOOLBAR
    private FileController fileController;
    
    // THIS CONTROLLER RESPONDS TO SLIDE SHOW EDIT BUTTONS
    private SlideShowEditController editController;
    
    ObservableList<String> choiceList;
    
    ComboBox choice;
    Button choiceSelect;
    PropertiesManager p = PropertiesManager.getPropertiesManager();
    Label saveChoice =  new Label(p.getProperty(TOOLTIP_SAVE_SLIDE_SHOW));
    Stage uiStage;
    Scene uiScene;
    FlowPane uiPane;
    Button ya;
    Button nah;
    Button cancel;
    int decision;
    
    Label titleQuest = new Label(p.getProperty(TITLETOP));
    Stage titleStage;
    Scene titleScene;
    FlowPane titlePane;
    Button confirm;
    Button cancel1;
    TextField titled;
    Label titleOverall = new Label(p.getProperty(TITLE_WINDOW));
    
    Label errors = new Label(p.getProperty(ERROR_DATA_FILE_LOADING));
    Stage errorStage;
    Scene errorScene;
    HBox errorPane;
    Button dismiss;
    public Button changeTitle;
    /**
     * Default constructor, it initializes the GUI for use, but does not yet
     * load all the language-dependent controls, that needs to be done via the
     * startUI method after the user has selected a language.
     */
    public SlideShowMakerView(SlideShowFileManager initFileManager) {
	// FIRST HOLD ONTO THE FILE MANAGER
	fileManager = initFileManager;
	
	// MAKE THE DATA MANAGING MODEL
	slideShow = new SlideShowModel(this);

	// WE'LL USE THIS ERROR HANDLER WHEN SOMETHING GOES WRONG
	errorHandler = new ErrorHandler(this);
        
    }
    public void recieveLanguage()
    {
        language = true;
        choiceList = FXCollections.observableArrayList(
        "English",
        "Espanol"
        );
        choice = new ComboBox(choiceList);
        questionPane = new FlowPane();
        questionPane.getChildren().add(new Label("Select Language (Defaults to English)"));
        questionPane.getChildren().add(choice);
        choiceSelect= new Button("Confirm");
        choiceSelect.setOnAction(e -> {questionAns();});
        questionPane.getChildren().add(choiceSelect);
        questionScene = new Scene(questionPane);
        questionStage = new Stage();
        questionStage.setScene(questionScene);
        questionStage.showAndWait();
    }
    // ACCESSOR METHODS
    public SlideShowModel getSlideShow() {
	return slideShow;
    }

    public Stage getWindow() {
	return primaryStage;
    }

    public ErrorHandler getErrorHandler() {
	return errorHandler;
    }

    /**
     * Initializes the UI controls and gets it rolling.
     * 
     * @param initPrimaryStage The window for this application.
     * 
     * @param windowTitle The title for this window.
     */
    public void startUI(Stage initPrimaryStage, String windowTitle) {
       
	// THE TOOLBAR ALONG THE NORTH
	initFileToolbar();

        // INIT THE CENTER WORKSPACE CONTROLS BUT DON'T ADD THEM
	// TO THE WINDOW YET
	initWorkspace();

	// NOW SETUP THE EVENT HANDLERS
	initEventHandlers();

	// AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
	// KEEP THE WINDOW FOR LATER
	primaryStage = initPrimaryStage;
         
        slideShow.setIconImage();
	initWindow(windowTitle);
        PropertiesManager p = PropertiesManager.getPropertiesManager();
    }

    // UI SETUP HELPER METHODS
    private void initWorkspace() {
	// FIRST THE WORKSPACE ITSELF, WHICH WILL CONTAIN TWO REGIONS
	workspace = new HBox(5);
	
	// THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
	slideEditToolbar = new VBox(60);
        btn = new HBox();
	slideEditToolbar.getStyleClass().add(CSS_CLASS_SLIDE_SHOW_EDIT_VBOX);
	addSlideButton = this.initChildButton(slideEditToolbar,		ICON_ADD_SLIDE,	    TOOLTIP_ADD_SLIDE,	    CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
        slideShowRemove = this.initChildButton(slideEditToolbar,        ICON_REMOVE_SLIDE,   TOOLTIP_REMOVE_SLIDE,    CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
	slideShowUp = this.initChildButton(slideEditToolbar,        ICON_MOVE_UP,	    TOOLTIP_MOVE_UP,	    CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
        slideShowDown = this.initChildButton(slideEditToolbar,        ICON_MOVE_DOWN,	    TOOLTIP_MOVE_DOWN,	    CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
        slideShowUp1 = this.initChildButton(btn,        ICON_MOVE_UP,	    TOOLTIP_MOVE_UP,	    CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
        slideShowDown1 = this.initChildButton(btn,        ICON_MOVE_DOWN,	    TOOLTIP_MOVE_DOWN,	    CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
        
	// AND THIS WILL GO IN THE CENTER
	slidesEditorPane = new VBox();
	slidesEditorScrollPane = new ScrollPane(slidesEditorPane);
	
        slideSpace = new VBox();
        slideSpace.setPrefWidth(1200);
        slideSpace.setPrefHeight(1000);
        presentSpace = new VBox();
        slideSpace.getStyleClass().add(HEY);
        workspace.getStyleClass().add(HEY);
	// NOW PUT THESE TWO IN THE WORKSPACE
	workspace.getChildren().add(slideEditToolbar);
	workspace.getChildren().add(slidesEditorScrollPane);
    }

    private void initEventHandlers() {
	// FIRST THE FILE CONTROLS
	fileController = new FileController(this, fileManager);
	newSlideShowButton.setOnAction(e -> {
	    fileController.handleNewSlideShowRequest();
	});
	loadSlideShowButton.setOnAction(e -> {
	    fileController.handleLoadSlideShowRequest();
	});
	saveSlideShowButton.setOnAction(e -> {
	    fileController.handleSaveSlideShowRequest();
	});
        viewSlideShowButton.setOnAction(e -> {
            viewSlideShowRequest();
        });
	exitButton.setOnAction(e -> {
	    fileController.handleExitRequest();
	});
	
	// THEN THE SLIDE SHOW EDIT CONTROLS
	editController = new SlideShowEditController(this);
	addSlideButton.setOnAction(e -> {
	    editController.processAddSlideRequest();
	});
        slideShowUp.setOnAction(e -> {
            editController.slideShowSwapUp();
        });
        slideShowDown.setOnAction(e -> {
            editController.slideShowSwapDown();
        });
         slideShowUp1.setOnAction(e -> {
            editController.up();
        });
        slideShowDown1.setOnAction(e -> {
            editController.down();
        });
        slideShowRemove.setOnAction(e -> {
            editController.removeSlide();
        });
    }

    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
	fileToolbarPane = new FlowPane();
        
        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
	// START AS ENABLED (false), WHILE OTHERS DISABLED (true)
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	newSlideShowButton = initChildButton(fileToolbarPane, ICON_NEW_SLIDE_SHOW,	TOOLTIP_NEW_SLIDE_SHOW,	    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
	loadSlideShowButton = initChildButton(fileToolbarPane, ICON_LOAD_SLIDE_SHOW,	TOOLTIP_LOAD_SLIDE_SHOW,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
	saveSlideShowButton = initChildButton(fileToolbarPane, ICON_SAVE_SLIDE_SHOW,	TOOLTIP_SAVE_SLIDE_SHOW,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, true);
	viewSlideShowButton = initChildButton(fileToolbarPane, ICON_VIEW_SLIDE_SHOW,	TOOLTIP_VIEW_SLIDE_SHOW,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, true);
	exitButton = initChildButton(fileToolbarPane, ICON_EXIT, TOOLTIP_EXIT, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        changeTitle  = new Button(props.getProperty(LanguagePropertyType.TITLETOP));
        changeTitle.setOnAction(e->{promptTitle();});
        changeTitle.setDisable(true);
        fileToolbarPane.getChildren().add(changeTitle);
        fileToolbarPane.getChildren().add(titleOverall);
        fileToolbarPane.getStyleClass().add(CSS_CLASS_STYLE_ONE);
    }

    private void initWindow(String windowTitle) {
	// SET THE WINDOW TITLE
	primaryStage.setTitle(windowTitle);

	// GET THE SIZE OF THE SCREEN
	Screen screen = Screen.getPrimary();
	Rectangle2D bounds = screen.getVisualBounds();

	// AND USE IT TO SIZE THE WINDOW
	primaryStage.setX(bounds.getMinX());
	primaryStage.setY(bounds.getMinY());
	primaryStage.setWidth(bounds.getWidth());
	primaryStage.setHeight(bounds.getHeight());

        // SETUP THE UI, NOTE WE'LL ADD THE WORKSPACE LATER
	ssmPane = new BorderPane();
	ssmPane.setTop(fileToolbarPane);
        ssmPane.setRight(slideSpace);
        ssmPane.getStyleClass().add(HEY);
	primaryScene = new Scene(ssmPane);
	
        // NOW TIE THE SCENE TO THE WINDOW, SELECT THE STYLESHEET
	// WE'LL USE TO STYLIZE OUR GUI CONTROLS, AND OPEN THE WINDOW
	primaryScene.getStylesheets().add(STYLE_SHEET_UI);
	primaryStage.setScene(primaryScene);
        primaryStage.show();
    }
    
    /**
     * This helps initialize buttons in a toolbar, constructing a custom button
     * with a customly provided icon and tooltip, adding it to the provided
     * toolbar pane, and then returning it.
     */
    public Button initChildButton(
	    Pane toolbar, 
	    String iconFileName, 
	    LanguagePropertyType tooltip, 
	    String cssClass,
	    boolean disabled) {
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	String imagePath = "file:" + PATH_ICONS + iconFileName;
	Image buttonImage = new Image(imagePath);
	Button button = new Button();
	button.getStyleClass().add(cssClass);
	button.setDisable(disabled);
	button.setGraphic(new ImageView(buttonImage));
	Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip.toString()));
	button.setTooltip(buttonTooltip);
	toolbar.getChildren().add(button);
	return button;
    }
    
    /**
     * Updates the enabled/disabled status of all toolbar
     * buttons.
     * 
     * @param saved 
     */
    public void updateToolbarControls(boolean saved) {
	// FIRST MAKE SURE THE WORKSPACE IS THERE
	ssmPane.setCenter(workspace);
	
	// NEXT ENABLE/DISABLE BUTTONS AS NEEDED IN THE FILE TOOLBAR
	saveSlideShowButton.setDisable(saved);
	viewSlideShowButton.setDisable(false);
	
	// AND THE SLIDESHOW EDIT TOOLBAR
	addSlideButton.setDisable(false);
        slideShowDown.setDisable(true);
        slideShowUp.setDisable(true);
        slideShowDown1.setDisable(true);
        slideShowUp1.setDisable(true);
        slideShowRemove.setDisable(true);
    }

    /**
     * Uses the slide show data to reload all the components for
     * slide editing.
     * 
     * @param slideShowToLoad SLide show being reloaded.
     */
    public void reloadSlideShowPane(SlideShowModel slideShowToLoad) {
	slidesEditorPane.getChildren().clear();
        ObservableList<Slide> ss = slideShowToLoad.getSlides();
        notSaved();
	for (Slide slide : slideShowToLoad.getSlides()) {
	    SlideEditView slideEditor = new SlideEditView(slide);
            slideEditor.setSlides(slideShow);
            if(!(slide.equals(slideShowToLoad.getSelectedSlide())))
                slideEditor.getStyleClass().add(CSS_UNSELECT);
            else
                slideEditor.getStyleClass().add(CSS_SELECT);
                slideShowUp.setDisable(false);
                slideShowDown.setDisable(false);
                slideShowRemove.setDisable(false);
                slideShowUp1.setDisable(false);
                slideShowDown1.setDisable(false);
            if(slideShowToLoad.getSelectedSlide() == null){}
            else{
            if(ss.get(0) == slideShowToLoad.getSelectedSlide())
            {
                slideShowUp.setDisable(true);
                slideShowUp1.setDisable(true);
            }
            if(ss.get(ss.size()-1) == slideShowToLoad.getSelectedSlide())
            {
                slideShowDown.setDisable(true);
                   slideShowDown1.setDisable(true);
            }
                    }
	    slidesEditorPane.getChildren().add(slideEditor);
	}
        titleOverall.setText(slideShow.getTitle());
    }
    public void viewSlideShowRequest()
    {
     /*   Stage display = new Stage();
        display.setMinWidth(1850);
        display.setMinHeight(1100);
        
      */  
       
        /*
        display.setScene(paned);
        display.showAndWait();*/
        //document.getElementById("image").src = "smiley.gif";(Change img)
        //
        WebView xm = new WebView();
        WebEngine xxm = xm.getEngine();
        File xc = new File("data/slide_shows/"+slideShow.getTitle()+".json");
        String xa = "";
        try{
        xa = xc.toURI().toURL().toString();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        File dir = new File("data/slide_shows/"+slideShow.getTitle());
        File jsDir = new File("data/slide_shows/"+slideShow.getTitle()+"/js");
        File cssDir = new File("data/slide_shows/"+slideShow.getTitle()+"/css");
        File imgDir = new File("data/slide_shows/"+slideShow.getTitle()+"/img");
        if(imgDir.exists() == false)
            imgDir.mkdirs();
        if(jsDir.exists() == false)
            jsDir.mkdirs();
        if(cssDir.exists() == false)
            cssDir.mkdirs();
        ObservableList<Slide> ss = slideShow.getSlides();
        for(int i = 0;i<ss.size();i++)
        {
            File tempo = new File(ss.get(i).getImagePath(),ss.get(i).getImageFileName());
            if(tempo.exists() == true)
            {
             try{
            Files.copy(tempo.toPath(), new File(imgDir,ss.get(i).getImageFileName()).toPath(),REPLACE_EXISTING);
             }
             catch(Exception e){System.out.println(e.getMessage());}
            }
            else{System.out.println("wow");}
        }
        File tempo2 = new File(ss.get(0).getImagePath(),"play.jpg");
        try{
            Files.copy(tempo2.toPath(), new File(imgDir,"play.jpg").toPath(),REPLACE_EXISTING);
             }catch(Exception e){System.out.println(e.getMessage());}
         tempo2 = new File(ss.get(0).getImagePath(),"pause.jpg");
        try{
            Files.copy(tempo2.toPath(), new File(imgDir,"pause.jpg").toPath(),REPLACE_EXISTING);
             }catch(Exception e){System.out.println(e.getMessage());}
        xa = imgDir.getPath();
        File tester = new File(dir,slideShow.getTitle()+".html");
        File csser = new File(cssDir,slideShow.getTitle()+".css");
        File messer = new File(jsDir,slideShow.getTitle()+".js");
        if(dir.exists() == true)
        {
            try{
                BufferedWriter pen = new BufferedWriter(new FileWriter(tester));
            pen.write(htmlMaker());
            pen.close();
            if(cssDir.exists() != false)
                cssDir.mkdirs();
             BufferedWriter penc = new BufferedWriter(new FileWriter(csser));
            penc.write(cssStyler());
            penc.close();
            if(jsDir.exists()!= true)
                jsDir.mkdirs();
            BufferedWriter pen1 = new BufferedWriter(new FileWriter(messer));
            pen1.write(stayCurrent(xa));
            pen1.close();
            }
            catch(Exception e)
            {}
        }
        else{
        dir.mkdirs();
        
        try{
            BufferedWriter pen = new BufferedWriter(new FileWriter(tester));
            pen.write(htmlMaker());
            pen.close();
            if(cssDir.exists() != false)
                cssDir.mkdirs();
             BufferedWriter penc = new BufferedWriter(new FileWriter(csser));
            penc.write(cssStyler());
            penc.close();
            if(jsDir.exists()!= true)
                jsDir.mkdirs();
            BufferedWriter pen1 = new BufferedWriter(new FileWriter(messer));
           pen1.write(stayCurrent(xa));
            pen1.close();
        tester.createNewFile();
        }
        catch(Exception e)
        {}
        }
        xxm.setJavaScriptEnabled(true);
        try{
        xm.getEngine().load(tester.toURI().toURL().toString());
        }
        catch (Exception e){System.out.println("issue");}
        webScene = new Scene(xm,1000,1000);
        /*presentSpace.setPrefWidth(webScene.getWidth()-200);
        presentSpace.setPrefHeight(webScene.getHeight()-200);
        ObservableList<Slide> ss1 = slideShow.getSlides();
        slideShow.setSelectedSlide(ss1.get(0));
        slideShow.reloadAnyway();
        BorderPane frame = new BorderPane();
        frame.setCenter(presentSpace);
        BorderPane tkbar =new BorderPane();
        tkbar.setCenter(btn);
        frame.setBottom(tkbar);
        Scene paned = new Scene(frame);
        */
        Stage webStage = new Stage();
        webStage.setScene(webScene);
        
        webStage.show();
        
    }
    
    public void questionAns()
    {
        
        String ans = choice.getValue().toString();
        if(ans.equals("English"))
            language = true;
        else
            language = false;
        questionStage.close();
    }
    public boolean getLanguage()
    {
        return language;
    }
    public int justAsk()
    {
        titleQuest = new Label(p.getProperty(TITLETOP));
        decision = 3;
        uiStage = new Stage();
        uiPane = new FlowPane();
        uiScene = new Scene(uiPane);
        uiPane.getChildren().add(saveChoice);
        ya = new Button("Yes/Si");
        nah = new Button("No/No");
        cancel = new Button("Cancel");
        ya.setOnAction(e ->{uiStage.close(); decision = 1;});
        nah.setOnAction(e ->{uiStage.close(); decision = 2;});
        cancel.setOnAction(e ->{uiStage.close(); decision = 3;});
        uiPane.getChildren().add(ya);
                uiPane.getChildren().add(nah);
                        uiPane.getChildren().add(cancel);
        uiStage.setScene(uiScene);
        uiStage.showAndWait();
        return decision;
    }
 public void notSaved()
 {
     fileController.markFileAsNotSaved();
     saveSlideShowButton.setDisable(fileController.isSaved());
 }
 public void promptTitle()
 {
     canc = false;
     titleStage = new Stage();
     titlePane = new FlowPane();
     titled = new TextField();
     Label shock = new Label(titleQuest.getText());
     titlePane.getChildren().add(shock);
     titlePane.getChildren().add(titled);
     confirm = new Button("Confirm");
     confirm.setOnAction(e-> {getTitleFromUser();} );
     cancel1 =new Button("Cancel");
     cancel1.setOnAction(e -> {titleStage.close(); canc = true;});
     titled.setOnAction(e -> {getTitleFromUser();});
     titlePane.getChildren().add(confirm);
     titlePane.getChildren().add(cancel1);
     titleScene = new Scene(titlePane);
     titleStage.setScene(titleScene);
     titleStage.showAndWait();
     if(fileController != null)
         notSaved();
 }
 public void getTitleFromUser()
 {
     PropertiesManager pd = PropertiesManager.getPropertiesManager();
     if(titled.getText() == null)
         promptError(pd.getProperty(LanguagePropertyType.ERRORTTL));
     
     else if(titled.getText().trim().equals(""))
         promptError(pd.getProperty(LanguagePropertyType.ERRORTTL));
     else
     {
         slideShow.setTitle(titled.getText().trim());
         titleOverall.setText(slideShow.getTitle());
         titleStage.close();
     }
 }
 public boolean getCanc()
 {
     return canc;
 }
 public void promptError(String error)
 {
     errorStage = new Stage();
     errorPane = new HBox();
     if(error != null)
         if(error != "")
             errors.setText(error);
     errorPane.getChildren().add(errors);
     dismiss = new Button("K");
     dismiss.setOnAction(e -> {errorStage.close();});
     errorPane.getChildren().add(dismiss);
     errorScene = new Scene(errorPane);
     errorStage.setScene(errorScene);
     errorStage.showAndWait();
 }
 public String tempHtml()
 {
     return "\n<html>\n"+
             "<head>\n"+
                     "Awesome"+
                     "</head>";
 }
 public String htmlMaker()
 {
     return "<!DOCTYPE html>\n<html>\n"+
   
"    <head id = \"sus\">\n"+

"<h1>"+slideShow.getTitle()+"</h1>\n"+             
"        <title id = \"h1\"></title>\n"+
             "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/"+slideShow.getTitle()+".css\">\n"+
"    </head>\n"+
"    <body>\n"+

"\n"+
"        <div><img id = \"img\" src = \"img/Icon-Presentation.jpg\" height = \"600\" width = \"650\"></div>\n"+
"        <p id = \"p1\">p1</p>\n"+
"        <p id = \"p3\"><button type = \"button\" id = \"butt\" onclick = \"prevSlide()\">Left</button>\n<img id = \"imgButt\" src = \"img/play.jpg\" height = \"50\" width = \"50\" onclick=\"react()\">\n<button type = \"button\" id = \"butt2\" onclick = \"nextSlide()\">Right</button></p>\n"+
              "         <script src = \"js/"+slideShow.getTitle()+".js\"></script>\n"+
"    </body>\n"+

"    \n\n"+
"</html>\n";
 }
 public String stayCurrent(String x)
    {
        ObservableList<Slide> ss = slideShow.getSlides();
        
        
        return "document.getElementById(\"sus\").innerHTML = \""+slideShow.getTitle() +"\";\n" +
"var firstOne = createNodes(\""+ss.get(0).getImageFileName()+"\",\""+ss.get(0).getCaption()+"\");\n" +
"var nodetemp = firstOne;\n" +
loops()+
"\n" +
"document.getElementById(\"butt\").disabled = true;\n" +
"var currentNode = firstOne;\nvar nodular;\nvar timer=null;\n" +
"document.getElementById(\"img\").src = \"img/\"+currentNode.image;\n" +
"document.getElementById(\"p1\").innerHTML = currentNode.caption;\n" +
"function createNodes(im,ca){\n" +
"        var nodex = {\n" +
"        prev: null,\n" +
"                image: im,\n" +
"                caption: ca,\n" +
"                next: null\n" +
"        };\n" +
"    return nodex;\n" +
"  }\n" +
"function setNext(first, second) {\n" +
"    first.next = second;\n" +
"    second.prev = first;\n" +
"}\n" +
"function nextSlide() {\n" +
"    currentNode = currentNode.next;\n" +
"    if (currentNode.next === null)\n" +
"        document.getElementById(\"butt2\").disabled = true;\n" +
"    else\n" +
"        document.getElementById(\"butt2\").disabled = false;\n" +
"    document.getElementById(\"butt\").disabled = false;\n" +
"    document.getElementById(\"img\").src = \"img/\" +currentNode.image;\n" +
"    document.getElementById(\"p1\").innerHTML = currentNode.caption;\n" +
"}\n" +
"function react() {\n" +
"    var imgHolder = document.getElementById(\"imgButt\");\n" + 
"        if(imgHolder.src.match(\"play\")){\n" +
"               imgHolder.src = \"img/pause.jpg\"\n" +
"               timer = setInterval(nextSlide,5500);\n}\n" +
"        else{\n" +
"              imgHolder.src = \"img/play.jpg\";\n" +
"              clearInterval(timer);\n} \n" +
"    " +
"}\n" +
"function prevSlide() {\n" +
"    currentNode = currentNode.prev;\n" +
"    if (currentNode.prev === null)\n" +
"        document.getElementById(\"butt\").disabled = true;\n" +
"    else\n" +
"        document.getElementById(\"butt\").disabled = false;\n" +
"    document.getElementById(\"butt2\").disabled = false;\n" +
"    document.getElementById(\"img\").src = \"img/\" +currentNode.image;\n" +
"    document.getElementById(\"p1\").innerHTML = currentNode.caption;\n" +
"}";
 }
 public String loops()
 {
     String hole = "";
     ObservableList<Slide> ss = slideShow.getSlides();
     for(int i = 0; i<ss.size();i++)
     {
         hole+="        nodular = createNodes(\""+ss.get(i).getImageFileName()+"\",\""+ss.get(i).getCaption()+"\");\n" + "    setNext(nodetemp, nodular);\n" + "    nodetemp = nodular;\n";
         
     }
     return hole;
 }
 public String cssStyler()
 {
     return "h1{\n" +
"    text-align: left;\n" +
"    font-family: \"Times New Roman\", Times, serif;\n" +
"    font-size: 40px;\n" +
"}\n" +
"body{\n" +
"    text-align: center;\n" +
"    font-family: \"Times New Roman\", Times, serif;\n" +
"    font-size: 25px;\n" +
"}\n" +
"p1{\n" +
"    text-align: center;\n" +
"    font-family: \"Times New Roman\", Times, serif;\n" +
"    font-size: 25px;\n" +
"}\n" +
"p3{\n" +
"    text-align: center;\n" +
"    font-family: \"Times New Roman\", Times, serif;\n" +
"    font-size: 25px;\n" +
"    background-color: chocolate;\n" +
"}\n" +
"";
 }
}

