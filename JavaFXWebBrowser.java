package javafxwebbrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Delayed;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;



import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.net.CookieManager;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.security.SecureRandom;



/**
 *
 * @author artur
 */
public class JavaFXWebBrowser extends Application 
{
	
	private WebView webView;
	private WebEngine browser;
	private JavaFXCharts charsScreen;
	
	private ObservableList<String> historyOptions ;
	private ComboBox historyListCombo;
	
	private ComboBox addressesList;
	private TextField addressTextField;
	
	private static int screenWidth;
	private static int screenHeight;
	private Button go_Load_Page_Button;
	
	private static CookieManager cookieManager;
	private CookieStore cookieStore;
	private static CookieHandler cookieHandler;
	
	//effects
	private DropShadow shadow = new DropShadow();
	private ColorAdjust colorAdjust = new ColorAdjust();		 
	private Blend blend = new Blend();
	
	private Button refreshButton;
	private ToggleButton start_Demo_Mode_ToggleButton;
	
	private final Label cookiesInstalledLabel = new Label("0");
	private String selectedCookiePolicy = "ACCEPT_ORIGINAL_SERVER";
	
	private TextField enterRefreshFrequencyTextField;	
	private boolean isAutoPageRefreshRunning = false;
	private int refreshingFequency_seconds = 5;
	
	private RefreshTask repeat_page_Refresh_task;

	private Timer sceenRefreshTimer;
	private ActionListener screenRefreshListener;
	
	private ArrayList<String> listOfRandom_Web_Addresses = new ArrayList<String> ();
	private ArrayList<String> listOf_Favourites_Web_Addresses = new ArrayList<String> ();
	
	private Timer demoMode_Timer;
	private boolean isDemo_Running = false;
	private ActionListener demoMode_Listener;
	private SecureRandom secRand;
	//private Toolkit tk;
	
    @SuppressWarnings("unchecked")
	@Override
    public void start(final Stage primaryStage)     
    {
        try 
        {
            LookAndFeelInfo[] lookandfeelinfo =  UIManager.getInstalledLookAndFeels();
            for ( LookAndFeelInfo laf : lookandfeelinfo)
             System.out.println("\t" +laf.getClassName());
            /*
            javax.swing.plaf.metal.MetalLookAndFeel
            javax.swing.plaf.nimbus.NimbusLookAndFeel
            com.sun.java.swing.plaf.motif.MotifLookAndFeel
            com.sun.java.swing.plaf.windows.WindowsLookAndFeel
            com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel
            */
            
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } 
        catch (Exception e) 
        {
        }
        System.out.println();
        
        
        
        Scene scene = new Scene(new Group(), 1400, 800);
        final GridPane root = new GridPane();
        
        TabPane tabpane = new TabPane();
        //tabpane
        secRand = new SecureRandom();
        
        final GridPane addressGrid = new GridPane();
        
        final ScrollPane scrollPane = new ScrollPane();
        
        webView = new WebView();
        //webView.setContextMenuEnabled(true);
        browser = webView.getEngine();  
        
        DatePicker checkInDatePicker = new DatePicker(LocalDate.now());
        checkInDatePicker.setMaxSize(120, 20);
        checkInDatePicker.setPrefSize(120, 20);
        checkInDatePicker.setMinSize(120, 20);
        
        checkInDatePicker.setOnAction(new EventHandler() 
        {
            public void handle(Event t) 
            {
                LocalDate date = checkInDatePicker.getValue();
                System.err.println("Selected date: " + date.getDayOfMonth() +" " +date.getMonth() +" " +date.getYear());
            }
        });

        
        
        //Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/605.1 (KHTML, like Gecko) JavaFX/10 Safari/605.1
        browser.setUserAgent("Home grown stuff/1.0 (Lynx XT 776.81; x256; x256) AppleWebKit/605.1 (KHTML, like Gecko) XTFX/100 Safari/605.1");
        
        browser.setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() 
        {
        	@Override
			public WebEngine call(PopupFeatures param) 
        	{	
        		WebView newView = new WebView();        		
        		WebEngine newWebEngine = newView.getEngine();    
        		boolean javascriptEnabled = browser.isJavaScriptEnabled();
        		//JavaFXWebNewWindow newWindow = new JavaFXWebNewWindow(newView, newWebEngine, historyOptions , javascriptEnabled );       		
        		
        		startNew_BrowserInstance( newView, newWebEngine, historyOptions , javascriptEnabled );
				return newWebEngine;
			}            
        });
        
        final ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0.0);
        progressBar.setMinSize( 80, 20);
        progressBar.setPrefSize( 80, 20);
        progressBar.setMaxSize( 80, 20);
        
        final Label progressIndicatorLabel = new Label("Hello...");
        progressIndicatorLabel.setPrefSize(60, 20);
        progressIndicatorLabel.setMaxSize( 60, 20);
        progressIndicatorLabel.setMinSize( 60, 20);
        
        //final Label addressTextField = new Label("");
        addressTextField = new TextField("");
        addressTextField.setMaxSize(400, 25);
        addressTextField.setPrefSize(400, 25);
        addressTextField.setMinSize(400,  25);
        
        
        enterRefreshFrequencyTextField = new TextField("5");
        enterRefreshFrequencyTextField.setMaxSize( 60, 20);
        enterRefreshFrequencyTextField.setPrefSize( 60, 20);
        enterRefreshFrequencyTextField.setTooltip(new Tooltip("Page refresh frequency (sec) 10,800 max"));
              
        ToggleButton schedulePageRefreshToggleButton = new ToggleButton("Keep refreshing");
        schedulePageRefreshToggleButton.setMaxSize(120, 20);
        schedulePageRefreshToggleButton.setPrefSize(120, 20);
          
        start_Demo_Mode_ToggleButton = new ToggleButton("Demo Mode");
        start_Demo_Mode_ToggleButton.setMaxSize(100, 20);
        start_Demo_Mode_ToggleButton.setPrefSize(100, 20);
        start_Demo_Mode_ToggleButton.setTooltip(new Tooltip("Demo mode"));
        
        cookiesInstalledLabel.setMaxSize(60, 20);
        cookiesInstalledLabel.setPrefSize(60, 20);
        cookiesInstalledLabel.setMinSize(60,  20);
        
        refreshButton = new Button("Refresh");
        
        Button fullScreenButton = new Button("Full Screen (F11)");
        
        Button disableJavaScriptButton = new Button("Disable JS");
        disableJavaScriptButton.setMaxSize(100, 25);
        disableJavaScriptButton.setMinSize(100, 25);
        disableJavaScriptButton.setPrefSize(100, 25);
        disableJavaScriptButton.setTooltip(new Tooltip("Disable JavaScript"));
        
        Button showChartsButton = new Button("Charts");
        showChartsButton.setTooltip(new Tooltip(showChartsButton.getText()));
        showChartsButton.setPrefSize(100, 20);
        showChartsButton.setMaxSize(100, 20);
        
        go_Load_Page_Button = new Button("Go");        
        go_Load_Page_Button.setPrefSize(100, 20);
        go_Load_Page_Button.setMaxSize(100, 20);
        
        colorAdjust.setContrast(0.1);
   	 	colorAdjust.setHue(0.95);
   	 	colorAdjust.setBrightness(0.1);
   	 	colorAdjust.setSaturation(0.2);
   	 
   	 	//blend.setMode(BlendMode.SCREEN);
   	 
        Button eraseCookiesButton = new Button("Delete cookies");
        eraseCookiesButton.setTooltip(new Tooltip("Delete all cookies..."));
        eraseCookiesButton.setPrefSize(100, 20);
        eraseCookiesButton.setMaxSize(100, 20);
        
        Button setCookies_PolicyButton = new Button("Original Server");
        setCookies_PolicyButton.setTooltip(new Tooltip("Change cookies policy"));
        setCookies_PolicyButton.setPrefSize(100, 20);
        setCookies_PolicyButton.setMaxSize(100, 20);
        setCookies_PolicyButton.setBackground(  new Background( new BackgroundFill(Color.YELLOW, null, null))  );
        
        
        setCookies_PolicyButton.setOnAction(new EventHandler<ActionEvent>() 
        {            
            @Override
            public void handle(ActionEvent event) 
            {	            	
            	if (selectedCookiePolicy.equalsIgnoreCase( "ACCEPT_ORIGINAL_SERVER" ))
            	{
            		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_NONE);
            		setCookies_PolicyButton.setText("None");
            		setCookies_PolicyButton.setBackground(  new Background( new BackgroundFill(Color.RED, null, null))  );
            		selectedCookiePolicy = "ACCEPT_NONE";            		            		
            	}
            	else if (selectedCookiePolicy.equalsIgnoreCase("ACCEPT_NONE"))
            	{
            		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            		setCookies_PolicyButton.setText("All");
            		setCookies_PolicyButton.setBackground(  new Background( new BackgroundFill(Color.LAWNGREEN, null, null))  );
            		selectedCookiePolicy = "ACCEPT_ALL";            		
            	}
            	else if (selectedCookiePolicy.equalsIgnoreCase("ACCEPT_ALL"))
            	{
            		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            		setCookies_PolicyButton.setText("Original server");
            		setCookies_PolicyButton.setBackground(  new Background( new BackgroundFill(Color.YELLOW, null, null))  );
            		selectedCookiePolicy = "ACCEPT_ORIGINAL_SERVER";
            		
            	}
            	
            	Task eraseCookiesTask = new Task()
                {
                    @Override
                    protected Object call() throws Exception 
                    {                  	
                        deleteAllCookies();
                        return null;
                    }                    
                };
                
            	eraseCookiesTask.run();
                                
            }   
         });
        
        
        
        ObservableList<String> favouritesaddressOptions;
        
        load_Favourites_from_File();
        
        if  (  listOf_Favourites_Web_Addresses.size() > 1  )
        	favouritesaddressOptions = 
                FXCollections.observableArrayList( listOf_Favourites_Web_Addresses);
        else   //in case file do not exist or empty
        	favouritesaddressOptions = 
                    FXCollections.observableArrayList(
                            "https://www.duckduckgo.com",
                            "https://www.shodan.io",
                            "https://www.swisscows.com",
                            "https://en.wikipedia.org",
                            "https://en.wikichip.org",
                            "https://www.bing.com",
                            "https://www.ebay.ie",
                            "https://www.techpowerup.com/",
                            "https://www.openstreetmap.org",
                            "https://www.worldatlas.com",                            
                            "https://www.cpubenchmark.net",
                            "https://www.stackoverflow.com",
                            "https://www.w3schools.com",
                            "https://www.w3schools.com/python/default.asp",
                            "https://www.oracle.com",
                            "https://www3.nhk.or.jp/nhkworld/en/tv/sumo",
                            ""
                            );
       
        addressesList = new ComboBox(favouritesaddressOptions);        
        
        addressesList.setPromptText("Favourites...");
        //addressesList.setEditable(true);  
        addressesList.setMaxWidth(400);
        addressesList.setPrefWidth(400);
        addressesList.setMinWidth(400);
        
        addressesList.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent t) 
            {            	
                browser.load(  addressesList.getSelectionModel().getSelectedItem().toString() );
                addressTextField.setText(browser.getLocation()  );
                addressTextField.setTooltip(new Tooltip(browser.getLocation()));
              //addressesList.getSelectionModel().clearSelection();
                
            }
            
        });

        
        addressTextField.setOnAction(new EventHandler<ActionEvent>()
        {
			@Override
			public void handle(ActionEvent event) 
			{
				browser.load(  addressTextField.getText().toString() );
                addressTextField.setText(browser.getLocation()  );
                addressTextField.setTooltip(new Tooltip(browser.getLocation()));
				
			}
        	
        });
        
        go_Load_Page_Button.setOnAction(new EventHandler<ActionEvent>()
        {
			@Override
			public void handle(ActionEvent event) 
			{
				if ( !browser.getLoadWorker().isRunning() )
					addressTextField.fireEvent( new ActionEvent() );
				else
				{
					browser.getLoadWorker().cancel();
					go_Load_Page_Button.setEffect(null);
				}
				
				//browser.load(  addressTextField.getText().toString() );
                //addressTextField.setText(browser.getLocation()  );
                //addressTextField.setTooltip(new Tooltip(browser.getLocation()));
				
			}
        	
        });
        
        schedulePageRefreshToggleButton.setOnAction(new EventHandler<ActionEvent>() 
        {            
            @Override
            public void handle(ActionEvent event) 
            {	
            	if ( isAutoPageRefreshRunning )
            	{
            		isAutoPageRefreshRunning = false;
            		schedulePageRefreshToggleButton.setEffect(null);
            		
            		
            		System.out.println(" Swithing automatic page refresh OFF ....");            		
            		enableDisable_UI_Components(false);        
            		start_Demo_Mode_ToggleButton.setDisable(false);
            		
            		try 
            		{
            			stopPageRefresh();
            		}
            		catch (Exception e) 
            		{
            			e.printStackTrace();
            		}
            	}
            	else
            	{
            		isAutoPageRefreshRunning = true;
            		schedulePageRefreshToggleButton.setEffect(shadow);            		
            		
            		System.out.println("Swithing automatic page refresh On ... ");
            		enableDisable_UI_Components(true); 
            		start_Demo_Mode_ToggleButton.setDisable(true);
            		
            		setRefreshTiming();
            		
            		try 
            		{
            			startPageRefresh();
            		}
            		catch (Exception e) 
            		{
            			e.printStackTrace();
            		}            		
            	}               
            }   
         });
        
        
        start_Demo_Mode_ToggleButton.setOnAction(new EventHandler<ActionEvent>() 
        {            
            @Override
            public void handle(ActionEvent event) 
            {	
            	if ( listOfRandom_Web_Addresses.size()  == 0 )
        			loadWebAddresses_from_File();
            	
            	if ( listOfRandom_Web_Addresses.size() >0 )
            	{
	            	if ( isDemo_Running )
	            	{
	            		isDemo_Running = false;
	            		start_Demo_Mode_ToggleButton.setEffect(null);	            		
	            		
	            		System.out.println(" Swithing demo mode OFF ....");            		
	            		enableDisable_UI_Components(false);   
	            		schedulePageRefreshToggleButton.setDisable(false);
	            		
	            		try 
	            		{
	            			stopDemoMode();
	            		}
	            		catch (Exception e) 
	            		{
	            			e.printStackTrace();
	            		}
	            	}
	            	else 
	            	{	
	            		isDemo_Running = true;
	            		start_Demo_Mode_ToggleButton.setEffect(shadow);            		
	            		
	            		System.out.println("Swithing demo mode On ... ");
	            		enableDisable_UI_Components(true); 
	            		schedulePageRefreshToggleButton.setDisable(true);
	            		
	            		try 
	            		{
	            			startDemoMode();
	            		}
	            		catch (Exception e) 
	            		{
	            			e.printStackTrace();
	            		}            		
	            	}
            	}
            	else
            		System.out.println("No web addresses found in a file.");
            }   
         });
        
        
        
        
        eraseCookiesButton.setOnAction(new EventHandler<ActionEvent>() 
        {            
            @Override
            public void handle(ActionEvent event) 
            {	
                Task eraseCookiesTask = new Task()
                {
                    @Override
                    protected Object call() throws Exception 
                    {                  	
                        deleteAllCookies();
                        return null;
                    }                    
                };
                eraseCookiesTask.run();                
            }   
         });
        
        historyOptions =  FXCollections.observableArrayList( );
        
        
        historyListCombo = new ComboBox(historyOptions);
        historyListCombo.setEditable(false);
        historyListCombo.setPrefWidth(300);
        historyListCombo.setMaxWidth(300);
        historyListCombo.setMinWidth(300);
        
        historyListCombo.setPromptText("History... ");
        
        historyListCombo.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent t) 
            {
                browser.load( historyListCombo.getSelectionModel().getSelectedItem().toString()  ); 
                addressTextField.setText(browser.getLocation()  );                
                addressTextField.setTooltip(new Tooltip(browser.getLocation()));
            }            
        });
        
        //webView.setZoom(2);
        
        browser.getLoadWorker().progressProperty().addListener(new ChangeListener<Number>()
        		{
					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) 
					{
						String progressvalue = String.format("%2.2f", newValue.doubleValue() *100  ) +" %";
						progressIndicatorLabel.setText( progressvalue );
						
					}
        		});
        		
        
        browser.getLoadWorker().stateProperty().addListener( new ChangeListener<State>() 
        {
        	@Override
                    public void changed(ObservableValue ov, State oldState, State newState)                     
                    {
		        		if (newState == State.SCHEDULED) 
		                {
		        			if ( !browser.getLocation().equalsIgnoreCase("about:blank") )
		        			{
		        				if ( !historyOptions.contains(browser.getLocation())   )
		        				historyOptions.add(0,browser.getLocation());
		        			}
		        				
	                        
                        	//historyOptions.sort(null);
		        			addressTextField.setText(browser.getLocation()  );
		        			
		        			
		        			
		        			
		        			//go_Load_Page_Button.setBorder(default_go_Load_Page_Button_border);
		        			
		        			go_Load_Page_Button.setEffect(colorAdjust);
		        			//.setBackground(  new Background( new BackgroundFill(Color.ORANGE, null, null))  );
		        			//.setBackground(  new Background( new BackgroundFill(Color.YELLOW, null, null))  );
		        			
		        			
		        			
		        			
		        			
		                }
                        if (newState == State.SUCCEEDED) 
                        {
                        	try
                        	{
	                        	if (browser != null)
	                        	{
		                        	
		                        	System.out.println( "Page stats :\n");
		                        	System.out.println( "Document     : " +browser.getDocument() );
		                            System.out.println( "Location     : " +browser.getLocation());
		                            System.out.println( "Title        : " +browser.getTitle());
		                            System.out.println( "User agent   : " +browser.getUserAgent());
		                            System.out.println( "User Stsh lo : " +browser.getUserStyleSheetLocation() );
		                            System.out.println( "Base URI     : " +browser.getDocument().getBaseURI() );
		                            System.out.println( "Document URI : " +browser.getDocument().getDocumentURI() );
		                            System.out.println( "XML version  : " + browser.getDocument().getXmlVersion() );
		                            System.out.println( "History obj  : " +browser.getHistory().toString() );
		                            
		                            ObservableList<WebHistory.Entry> addressHistory =  browser.getHistory().getEntries();
		                                    
		                            System.out.println( "\nHistory max size : " +browser.getHistory().getMaxSize()  );
		                            System.out.println( "History :");
		                            for ( WebHistory.Entry whe : addressHistory)
		                            	System.out.println( whe.getUrl() + "  " +whe.getLastVisitedDate().toString());
		                            
		                            
		                            System.out.println();
		                                 
		                       
	                        	}
                        	}	
                        	catch (java.lang.NullPointerException exc)
                        	{
                        		
                        	}
                        	cookiesInstalledLabel.setText(String.valueOf(cookieManager.getCookieStore().getCookies().size() )  );
                        	go_Load_Page_Button.setEffect(null);
                        }
                    }
                    
					

					
                });
        
        progressBar.progressProperty().bind(browser.getLoadWorker().progressProperty());
        
        //scrollPane.getStyleClass().add("noborder-scroll-pane");
       // scrollPane.setStyle("-fx-background-color: white");
        
       
        
        ///*****
        scrollPane.setContent(webView);
        
        scrollPane.setPrefSize(2560, 1280);
        scrollPane.setMaxSize(2560, 1280);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        disableJavaScriptButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            
            @Override
            public void handle(ActionEvent event) 
            {
                if ( browser.isJavaScriptEnabled() )
                {
                	
                	disableJavaScriptButton.setEffect(shadow);
                	
                	//disableJavaScriptButton.setRotate(90);
                	browser.setJavaScriptEnabled(false);
                	disableJavaScriptButton.setText("Enable JS");
                	disableJavaScriptButton.setBackground(  new Background( new BackgroundFill(Color.ORANGERED, new CornerRadii(1),  new Insets(1,1,1,1)))  );
                }
                else
                {
                	disableJavaScriptButton.setEffect(null);
                	browser.setJavaScriptEnabled(true);
                	disableJavaScriptButton.setText("Disable JS");
                	disableJavaScriptButton.setBackground(  new Background( new BackgroundFill(Color.LIGHTGREY, null, null))  );
                }
                        
                        
                
            }   });
        
        fullScreenButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            
            @Override
            public void handle(ActionEvent event) 
            {
                if ( primaryStage.isFullScreen() )
                {
                    primaryStage.setFullScreen(false);
                    root.add(addressGrid, 0, 0);
                    root.add(scrollPane, 0, 1);
                }
                else
                {
                    primaryStage.setFullScreen(true);
                    root.getChildren().removeAll(root.getChildren());
                    root.add(scrollPane, 0, 1);
                }
                        
                        
                
            }   });
                
        
        
        refreshButton.setOnAction(new EventHandler<ActionEvent>() 
        {            
            @Override
            public void handle(ActionEvent event) 
            {  
                browser.reload();
                addressTextField.setText(browser.getLocation()  );
                addressTextField.setTooltip(new Tooltip(browser.getLocation()));
                getCookiesInfo();
                Task loadProgressUpdateTask = new Task()
                {
                    @Override
                    protected Object call() throws Exception 
                    {                                              
                        System.out.print(  browser.getLoadWorker().getProgress()  );
                        System.out.println( "  "  +progressBar.progressProperty().get()  );                        
                        return null;
                    }                    
                };
                loadProgressUpdateTask.run();                
            }   
         });
        
        showChartsButton.setOnAction(new EventHandler<ActionEvent>() 
        {
        	@Override
            public void handle(ActionEvent event) 
            {
        		if ( charsScreen == null)
        			charsScreen = new JavaFXCharts();
        		else
        			charsScreen.show();
            }
        });
        
        
        addressGrid.setPadding(  new Insets(2,10,3,10)   );
        //addressGrid.setGridLinesVisible(true);
        
        //addressGrid.setConstraints(refreshButton, 350,50);
        addressGrid.setHgap(10);
        addressGrid.setVgap(5);
        
        addressGrid.add(refreshButton, 0,0);
        addressGrid.add(fullScreenButton, 0,1);
        
        addressGrid.add(addressesList, 1,0);
        addressGrid.add(addressTextField, 1,1);
        
        addressGrid.add(progressBar, 2,0);
        addressGrid.add(go_Load_Page_Button, 2,1);
        
        addressGrid.add(progressIndicatorLabel, 3,0);
        addressGrid.add(enterRefreshFrequencyTextField, 3,1);
        
        addressGrid.add(historyListCombo, 4,0);
        addressGrid.add(schedulePageRefreshToggleButton, 4,1);
        
        addressGrid.add(disableJavaScriptButton, 6,1);
        addressGrid.add(checkInDatePicker, 7,0);
        addressGrid.add(eraseCookiesButton, 7, 1);
        
        
        addressGrid.add(showChartsButton, 8,0);
        addressGrid.add(cookiesInstalledLabel, 8,1);
        
        
        addressGrid.add(start_Demo_Mode_ToggleButton, 9 , 0);
        addressGrid.add(setCookies_PolicyButton, 9 , 1);
        
        //addressGrid.add(pieChart, 8,1);
        
        
        //addressGrid.getChildren().addAll(progressBar, addressesList)   ;     
                
        
        root.setPadding(  new Insets(5, 5, 5, 5 )   );
        root.setHgap(10);
        root.setVgap(1);
        
        
        root.add(addressGrid, 0, 0);
        root.add(scrollPane, 0, 1);
        
        //KeyEvent ke;
        scene.setOnKeyReleased(  new EventHandler<KeyEvent>  ()
        {
            @Override
            public void handle(KeyEvent t) 
            {
                if (t.getCode().toString().equalsIgnoreCase("ESCAPE")  )
                {
                    System.out.println("ESCAPE pressed");
                    root.getChildren().removeAll(root.getChildren());
                    root.add(addressGrid, 0, 0);
                    root.add(scrollPane, 0, 1);
                }
                if (t.getCode().toString().equalsIgnoreCase("F11"))
                {
                	if ( primaryStage.isFullScreen() )
                    {
                        primaryStage.setFullScreen(false);
                        root.getChildren().removeAll(root.getChildren());
                        root.add(addressGrid, 0, 0);
                        root.add(scrollPane, 0, 1);
                    }
                    else
                    {
                        primaryStage.setFullScreen(true);
                        root.getChildren().removeAll(root.getChildren());
                        root.add(scrollPane, 0, 1);
                    }
                }
                System.out.println(t.getCode().toString()  +" pressed");
            }
        });
        
        primaryStage.setOnCloseRequest( new EventHandler<WindowEvent>()
        {

			@Override
			public void handle(WindowEvent event) 
			{
				System.out.println("Exit requested...");
				if (charsScreen != null)
					charsScreen.close();
				System.exit(0);
				
			}
        	
        });
        
        
        scene.setRoot(root);
        primaryStage.setTitle("Denise");//Java FX Web Browser");
        try
        {
        	primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("D.jpg")));        	
        }
        catch(Exception e) {}
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void startNew_BrowserInstance(  WebView webViewNew, WebEngine browserNew, ObservableList<String> newhistoryOptions, boolean javascriptEnabledorNot  )
    {
    	try
		{    		
			javafx.application.Platform.runLater(  new Runnable()
			{
				 public void run()
				 { 
					 try 
					 {
						 JavaFXWebNewWindow newWindow = new JavaFXWebNewWindow(webViewNew, browserNew, newhistoryOptions , javascriptEnabledorNot );      					 
					 } 
					 catch (Exception e) 
					 {
						e.printStackTrace();
					 }  
					 System.out.println("System time milisec : "  +System.currentTimeMillis()  );
				 }    				 
			});
		} 
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
    }
    
    private void enableDisable_UI_Components( boolean enable_Disable_Components)
    {
    	refreshButton.setDisable(enable_Disable_Components);
    	enterRefreshFrequencyTextField.setDisable(enable_Disable_Components);
    	addressesList.setDisable(enable_Disable_Components);
        addressTextField.setDisable(enable_Disable_Components);
        historyListCombo.setDisable(enable_Disable_Components);
        go_Load_Page_Button.setDisable(enable_Disable_Components);
        
    }
    
    private void setRefreshTiming()
    {
    	if (enterRefreshFrequencyTextField.getText().length() >0 && enterRefreshFrequencyTextField.getText().length() <6 )
    	{
    		System.out.println("User input length correct...");
    		boolean isUserInputCorrect = true;
    		for (char singlechar : enterRefreshFrequencyTextField.getText().toCharArray()  )
    		{
    			if ( ! Character.isDigit(singlechar)  ) 
    			{   
    				isUserInputCorrect = false;
    				System.out.println("Invalid character detected...");
    				break;
    			}
    		}
    		if (isUserInputCorrect )
			{
    			System.out.println("User input is correct - all digits...");
			}
    		else
    		{
    			enterRefreshFrequencyTextField.setText("5");
    			System.out.println("User input incorrect - non digits");
    		}
    	}
    	else            		
    	{
    		System.out.println("Too long or too short...");
    		enterRefreshFrequencyTextField.setText("5");
    	}
    	
    	int frequencyValue = Integer.valueOf(enterRefreshFrequencyTextField.getText() );
    	
    	if ( frequencyValue >= 1 && frequencyValue <= 10800 )
    		refreshingFequency_seconds = frequencyValue;
    	else
    	{
    		enterRefreshFrequencyTextField.setText("5");
    		refreshingFequency_seconds = 5;
    	}
    		
    	
    	System.out.println("\n\tRefresh frequency = "  +refreshingFequency_seconds);
    	System.out.println();
    }
    
    
    private void deleteAllCookies()
    {    	
    	cookieStore = cookieManager.getCookieStore();
    	
    	List<HttpCookie> allCookies = cookieStore.getCookies();
    	
    	int numberOfCookies = allCookies.size();
    	if (numberOfCookies >0)
    		System.out.println("Erasing all " +numberOfCookies +" cookies...");   
    	
    	for ( HttpCookie singleCookie : allCookies )
    	{
    		singleCookie.setMaxAge(0);
    		singleCookie.setDiscard(true);    		
    	}
    	try 
    	{
			Thread.sleep(100);
		} 
    	catch (InterruptedException e) 
    	{		
			e.printStackTrace();
		}
    	if (allCookies.size() == 0 && numberOfCookies > 0 )
    		System.out.println("Deleted " +numberOfCookies +" cookies.");
    	else if (allCookies.size() > 0 )
    		System.out.println(+numberOfCookies +" cookies left...will be removed in a second...");
    	System.out.println(+numberOfCookies +" cookies left.");
    	Runtime.getRuntime().gc();
    	cookiesInstalledLabel.setText(String.valueOf(cookieManager.getCookieStore().getCookies().size() )  );
    }
    
    private class RefreshTask extends Task
    {
        @Override
        protected Object call() throws Exception 
        { 
    		try
    		{
    			//this is not working correctly - incorrect thread when using timer - must be run on javafx thread
    			//browser.reload();    			
    		}	
        	catch (Exception e) 
    		{
    			e.printStackTrace();
    		}
    		System.out.println("Refreshing page usint RefreshTask.. " );
    		
        	return null;
        }                    
    }
    
    private void startPageRefresh()
	{
    	if ( screenRefreshListener == null)
    		screenRefreshListener = new TimerListenerforPageRefresh( );
		if ( sceenRefreshTimer == null) 
		{
			sceenRefreshTimer = new Timer( refreshingFequency_seconds * 1000 , screenRefreshListener);
			sceenRefreshTimer.setInitialDelay(10);
		}
		
		sceenRefreshTimer.setDelay( refreshingFequency_seconds * 1000 );
		//sceenRefreshTimer.setRepeats(false);
		sceenRefreshTimer.start();
	}
    
    private void stopPageRefresh()
    {
    	sceenRefreshTimer.stop();    	
    }
    
    
    private class TimerListenerforPageRefresh implements ActionListener
	{
    	public void actionPerformed (java.awt.event.ActionEvent event)
		{		
			try
			{
				javafx.application.Platform.runLater(  new Runnable()
    			{
    				 public void run()
    				 { 
    					 try 
    					 {
    						 System.out.println("Started java FX Thread ...refreshing page... ");
    						 browser.reload();      					 
						 } 
    					 catch (Exception e) 
    					 {
							e.printStackTrace();
						 }  
    					 System.out.println("System time milisec : "  +System.currentTimeMillis()  );
    				 }    				 
    			});			
				//browser.reload();
				//refreshButton.fire();  
			} 
			catch(Exception exc)
			{
				exc.printStackTrace();
			}			
		}
	}

    private void startDemoMode()
	{
    	if ( demoMode_Listener == null)
    		demoMode_Listener = new Timer_Listener_for_DemoMode( );
		if ( demoMode_Timer == null) 
		{
			demoMode_Timer = new Timer( refreshingFequency_seconds * 1000 , demoMode_Listener);
			demoMode_Timer.setInitialDelay(10);
		}
		
		demoMode_Timer.setDelay( 2 * 1000 );
		demoMode_Timer.start();
	}
    
    private void stopDemoMode()
    {
    	demoMode_Timer.stop();    	
    }
    
    private class Timer_Listener_for_DemoMode implements ActionListener
   	{
       	public void actionPerformed (java.awt.event.ActionEvent event)
   		{		
   			try
   			{
   				int nextRun = demoMode_Timer.getDelay() / 1000;
   				refreshingFequency_seconds = secRand.nextInt(300)+1;  
   				demoMode_Timer.setDelay( refreshingFequency_seconds * 1000 );  
   				
   				javafx.application.Platform.runLater(  new Runnable()
       			{
       				 public void run()
       				 { 
       					 try 
       					 {
       						 System.out.println("Started java FX Thread ...loading random page...next load in " +nextRun +" seconds, then in " +(refreshingFequency_seconds ) +" seconds...");
       						
       						 browser.load(  listOfRandom_Web_Addresses.get( secRand.nextInt( listOfRandom_Web_Addresses.size() ) )  );      					 
   						 } 
       					 catch (Exception e) 
       					 {
   							e.printStackTrace();
   						 }  
       					 System.out.println("System time milisec : "  +System.currentTimeMillis()  );
       				 }    				 
       			});			
   				//browser.reload();
   				//refreshButton.fire();  
   			} 
   			catch(Exception exc)
   			{
   				exc.printStackTrace();
   			}			
   		}
   	}
    
    private void getCookiesInfo()
    {
  /*  	if (  cookieManager == null  )
    		cookieManager = new CookieManager();
    	
    	cookieHandler.setDefault(cookieManager);
    	
    	cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    	*/
    	cookieStore = cookieManager.getCookieStore();
    	
    	List<HttpCookie> allCookies = cookieStore.getCookies();
    	System.out.println ("\n\tCookies info : ");
    	int numberOfCookies = allCookies.size();
    	for ( HttpCookie singleCookie : allCookies )
    	{
    		System.out.println ("Domain : " +singleCookie.getDomain()   );
    		System.out.println ("Name : " +singleCookie.getName()   );
    		System.out.println ("Value : " +singleCookie.getValue()   );
    		System.out.println ("Comment : " +singleCookie.getComment()   );
    		System.out.println ("Max age : " +singleCookie.getMaxAge()  );
    		System.out.println ("Path : " +singleCookie.getPath()   );
    		System.out.println ("Portlist : " +singleCookie.getPortlist()   );
    		System.out.println ("Secure : " +singleCookie.getSecure() );
    		System.out.println ("Version : " +singleCookie.getVersion()   );
    		System.out.println("------------------------------------------------------------------------------------------");
    	}
    	
    	//CookieHandler defualtCookieHandler = CookieHandler.getDefault();
    	//defualtCookieHandler.
    	System.out.println(+numberOfCookies +" cookies installed...");
    	System.out.println("\n\t");
    	//HttpCookie cookie = new HttpCookie(null, null);
    	//cookie.getMaxAge();
    }
    
    private void loadWebAddresses_from_File()
    {    	
    	//String filename = "C:/Users/artur/EclipseWorkspace/Java FX WebBrowser/randomwebpages.txt";
    	filename = "./randomwebpages.txt";
    	//String fileOut = "C:/Users/artur/EclipseWorkspace/Java FX WebBrowser/randomwebpages22.txt";
    	File fileWith_WebAddresses = new File( filename );    	     	
    	
        if (fileWith_WebAddresses.exists() && fileWith_WebAddresses.isFile() )
        {	        	
        	listOfRandom_Web_Addresses = getAddressesfromFile( fileWith_WebAddresses );           		
        }	
        //saveList_toFile(  listOfRandom_Web_Addresses, new File ( fileOut )  ) ;
    }
    
    
    private void load_Favourites_from_File()
    {    	
    	//String filename = "C:/Users/artur/EclipseWorkspace/Java FX WebBrowser/favouritewebpages.txt";
    	filename = "./favouritewebpages.txt";
    	//String fileOut = "C:/Users/artur/EclipseWorkspace/Java FX WebBrowser/randomwebpages22.txt";
    	File fileWith_Fav_WebAddresses = new File( filename );    	     	
    	
        if (fileWith_Fav_WebAddresses.exists() && fileWith_Fav_WebAddresses.isFile() )
        {	        	
        	listOf_Favourites_Web_Addresses = getAddressesfromFile( fileWith_Fav_WebAddresses );           		
        }	
        //saveList_toFile(  listOfRandom_Web_Addresses, new File ( fileOut )  ) ;
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	try
    	{
    	cookieManager = new CookieManager();    	
    	cookieHandler.setDefault(cookieManager);    	
    	cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    	
    	getScreenCoordinates();
    	Locale.setDefault(Locale.US);
    	
    	new Thread( new Runnable()
    			{			
    				public void run() 
    				{
    					JFrame exitFrame = new JFrame();
    					JButton exitButon = new JButton("Force Exit");    
    					
    					try
    			        {
    						exitFrame.setIconImage(  ImageIO.read(getClass().getResourceAsStream("ForceExit.jpg")));    			        	       	
    			        }
    			        catch(Exception e) {}
    					exitButon.addActionListener( new ActionListener() 
    				    {    						
							@Override
							public void actionPerformed(java.awt.event.ActionEvent event) 
							{
								if (event.getSource() == exitButon )
    					        {
    				        		System.exit(0);
    					        }								
							} 
						});
    					
    					JPanel mainpanel = new JPanel();    					
    					//mainpanel.setBorder (new TitledBorder(new EtchedBorder(), ""));
    					mainpanel.setLayout( new GridLayout( 3, 3, 0, 0 ) );
    					mainpanel.setBackground(  new  java.awt.Color (11,222,11)  );
    					
    					exitButon.setSize( new Dimension (280, 100));
    					exitButon.setPreferredSize(new Dimension (280, 100));    					
    					exitButon.setBackground( new  java.awt.Color (255,0,0) );
    					exitButon.setForeground( new  java.awt.Color (255,255,255) );
    					exitButon.setFont(new Font("Serif",Font.BOLD, 22));
    					
    					for (int i=0;i<4;i++)
    						mainpanel.add(new JLabel(""));
    					mainpanel.add( exitButon );
    					for (int i=0;i<4;i++)
    						mainpanel.add(new JLabel(""));
    					
    					
    					exitFrame.setSize(200, 110);
    					exitFrame.setTitle(" Force Exit ");
    					exitFrame.add(exitButon, BorderLayout.CENTER);
    					
    					exitFrame.setLocation(screenWidth-230, 20);     //.setLocationRelativeTo(null);  //centre frame
    					exitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    					exitFrame.setVisible(true);
    				}
    			}
    			).start();
        	
    	launch(args);
        //getSystemEnvInfo();
        
    	}
    	catch (Exception exc)
    	{
    		exc.printStackTrace();
    	}
        
    }
 
    
    private void saveList_toFile(ArrayList<String> addresses, File saveAsfile) 
	{	
    	StringBuilder strBuilder = new StringBuilder();		
		
		for(int e=0; e <addresses.size() ; e++)
		{
			strBuilder.append(addresses.get(e) +" \n" );			
		}		
		
		//System.out.print(strBuilder);
		//System.exit(0);
		try
		{
			FileWriter fw;
			if ( saveAsfile.exists() )
			{
				fw = new FileWriter(saveAsfile, true);
			}
			else
			{
				fw = new FileWriter(saveAsfile);
			}
			
			PrintWriter out = new PrintWriter(new BufferedWriter(fw));
			
			
			try
			{			
			    out.write(strBuilder.toString()); 
			}			
			finally
			{
				out.flush();
				out.close();
			}
		}
		catch(IOException ex)
		{
			System.out.println("Unable to write to file:\n\t" +saveAsfile 
					+"\n\nFile can not be created, can not be opened or user has no write privilege.\n\n" 
								+ex.getMessage() +"\n");
			ex.printStackTrace();
			//System.exit(0);
		}
			
	}
    
    private static ArrayList<String> getAddressesfromFile(File filein) 
	{		
    	ArrayList<String> addresses = new ArrayList<String>();	
    	ArrayList<String> addresses_To_Avoid = new ArrayList<String>();	
    	Scanner scin = null;
    	String outputLine = null;
    	
    	addresses_To_Avoid.add( "bnia.cn");
		addresses_To_Avoid.add( "numaga");
		addresses_To_Avoid.add( "163.com");  //nothing wrong with this one - but too many appear
		addresses_To_Avoid.add( "http%3A%2F%2F"); 
		addresses_To_Avoid.add("https%3A%2F%2F"); 
		addresses_To_Avoid.add("gpo.gov");
		addresses_To_Avoid.add("%253A%252F%252F");
		addresses_To_Avoid.add("fb.com");
		addresses_To_Avoid.add("facebook");
		addresses_To_Avoid.add("porn");
		addresses_To_Avoid.add("nude");
		
		try
		{
			BufferedReader streamIn = new BufferedReader ( new FileReader (filein) );
			try
			{				
				scin = new Scanner(streamIn);				
				scin.useDelimiter(" ");
				Pattern url_pattern = Pattern.compile("http[.]*\\p{Print}*");				
				Matcher url_matcher;
				
				while (scin.hasNext())  //continue until end of file
				{					
					outputLine = scin.next();  //read one chunk at a time
					//System.out.println(outputLine +"");
					url_matcher = url_pattern.matcher(outputLine);
					
					
					if (url_matcher.find())
					{		
						String adres_ = url_matcher.group();//.substring( 6, url_matcher.group().indexOf(" ")-1);
						
						//make sure no addresses from prohibited list are visited
						for (int index=0; index< addresses_To_Avoid.size(); index++)
						{
							if ( adres_.contains(addresses_To_Avoid.get(index) ) )
							{
								//System.out.println("\t\tAvoiding address ... " + adres_);
								adres_ = "https://en.wikipedia.org";
								break;
							}
						}						
						//System.out.println(  adres_  );
						if (! addresses.contains(adres_)  )
							addresses.add(adres_);			
						
			/*			else
						{
							String alreadyinList = addresses.get(addresses.indexOf(adres_));
							System.out.println("Duplicate found ...original : " +alreadyinList +"\t\t\t diplicate : " +adres_ );
						}
			*/			
					}
				}
			}
			finally
			{
				scin.close();		
				streamIn.close();
			}	
		}
		catch(FileNotFoundException ex)
		{ex.printStackTrace();}
		catch(IOException ex)
		{ex.printStackTrace();}
		catch(Exception ex)
		{ex.printStackTrace();}		
		
		System.out.println("Finished loading file... total : " +addresses.size() +" addresses loaded.\n\n");
		return addresses;
			
	}
    
    
    private static void getSystemEnvInfo()
    {
    	Map<String, String> systemenvir  = System.getenv();
		Set<String> sysEnvir_setkeys = systemenvir.keySet();
		
		System.out.println(System.getenv(  "NUMBER_OF_PROCESSORS"));
		System.out.println(System.getenv(  "PROCESSOR_ARCHITECTURE"));
		
		
		System.out.println("\n");
		
		for (String key : sysEnvir_setkeys )
		{
			System.out.println(key +" \t\t\t "  +systemenvir.get(key)   );
		}

		System.out.println("\n\n**************************    **************************\n\n");
		
	   
		
		Properties prop = System.getProperties();
		Set<Object> setkeys = prop.keySet();
		
		for (Object key : setkeys )
		{
			System.out.println(key.toString() +" \t\t\t "  +prop.get(key)   );
		}
		
		System.out.println("\n\n**************************    **************************\n\n");
		
		Runtime runtime = Runtime.getRuntime();

		System.out.println("Processors #: " +runtime.availableProcessors() );
		 
		
    }

	private static void getScreenCoordinates()
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();		
		GraphicsDevice defaultDevice = ge.getDefaultScreenDevice();
		
		//System.out.println( defaultDevice.getDisplayMode().getWidth());
		//System.out.println(defaultDevice.getDisplayMode().getHeight()  );
		//System.out.println(defaultDevice.getDisplayMode().getBitDepth()  );
		//System.out.println(defaultDevice.getDisplayMode().getRefreshRate() );
		screenWidth = defaultDevice.getDisplayMode().getWidth();
		screenHeight = defaultDevice.getDisplayMode().getHeight();
	}
	
}
