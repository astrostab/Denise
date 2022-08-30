package javafxwebbrowser;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.security.SecureRandom;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;



public class JavaFXWebNewWindow extends Stage  
{
	private WebView newWebView;
	private WebEngine neWBrowser;
    private SecureRandom sr;
   
    private Button refreshButton;
    private Button fullScreenButton;
    private Stage thisStage;
    
    private final ProgressBar progressBar;
    private Button disableJavaScriptButton;    
    
    private DropShadow shadow;
    private final Label progressIndicatorLabel;
    private TextField addressTextField;
    
    private Scene newScene;
    private final GridPane root ;
    private final GridPane topRow ;
    
    private int screenWidth;
    private int screenHeight;
    
    private ObservableList<String> localHistoryOptions ;
    
    //constructor
    public JavaFXWebNewWindow(WebView webViewNew, WebEngine browserNew, ObservableList<String> newhistoryOptions, boolean javascriptEnabled )
    {
    	getScreenCoordinates();
    	
    	this.newWebView = webViewNew;
        this.neWBrowser = browserNew;    
        this.neWBrowser.setJavaScriptEnabled(javascriptEnabled);        
        
        localHistoryOptions =  newhistoryOptions; 
        thisStage = this;
        
        
        
        refreshButton = new Button("Refresh");
        refreshButton.setPrefSize(100, 20);
        refreshButton.setMaxSize(100, 20);
        
        fullScreenButton = new Button("Full screen (F11)");
        fullScreenButton.setPrefSize(100, 20);
        fullScreenButton.setMaxSize(100, 20);
        sr = new SecureRandom();
        
        this.progressBar = new ProgressBar();
        this.progressBar.setProgress(0.0);
        
        this.disableJavaScriptButton = new Button("Disable JS");
        this.disableJavaScriptButton.setMaxSize(100, 20);
        this.disableJavaScriptButton.setPrefSize(100, 20);
        
        enable_Disable_JS_Button();
        
        progressIndicatorLabel = new Label("0.00");
        progressIndicatorLabel.setPrefSize(100, 20);
        progressIndicatorLabel.setMaxSize(100, 20);
        
        addressTextField = new TextField("");
        addressTextField.setMaxSize(400, 25);
        addressTextField.setPrefSize(400, 25);
        addressTextField.setMinSize(400,  25);
        
        
        shadow = new DropShadow();
       
        //this.setTitle("Java FX Web Browser");
        this.setTitle("Denise - "  +neWBrowser.getLocation()  );
        
        
        newScene = new Scene(new Group(), screenWidth-200, screenHeight-200);
        root = new GridPane();
        topRow = new GridPane();
        
        
    	start();
    }
	    
    

    public void start()//final Stage chartsStage)     
    {
    	refreshButton.setOnAction(new EventHandler<ActionEvent>() 
        {
        	@Override
            public void handle(ActionEvent event) 
            {
        		neWBrowser.reload();
        		thisStage.setTitle( neWBrowser.getLocation()  );
        		addressTextField.setText(neWBrowser.getLocation());
        		
            	Runnable modifyPieChartRinnableIncrease = new Runnable()
        		{
					@Override
					public void run() 
					{
						for (int i=0; i<100; i++)
						{
							//System.out.println("\tIncreasing : \t" +);
							try { Thread.sleep(10); } catch (InterruptedException e) {e.printStackTrace();}
							   
			              
						}						
					}        		
        		};
        		new Thread(modifyPieChartRinnableIncrease).start();
        		
            }   
        });
        
        
        
        
        neWBrowser.setUserAgent("Home grown stuff/1.0 (Lynx XT 776.81; x256; x256) AppleWebKit/605.1 (KHTML, like Gecko) XTFX/100 Safari/605.1");
        
        neWBrowser.setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() 
        {
        	@Override
			public WebEngine call(PopupFeatures param) 
        	{	
        		WebView newView = new WebView();        		
        		WebEngine newWebEngine = newView.getEngine();
        		boolean isjavascriptEnabled = neWBrowser.isJavaScriptEnabled();
        		
        		JavaFXWebNewWindow newWindow = new JavaFXWebNewWindow(newView, newWebEngine, localHistoryOptions, isjavascriptEnabled );            		
				return newWebEngine;
			}
            
        });
        
        neWBrowser.getLoadWorker().stateProperty().addListener( new ChangeListener<State>() 
        {
        	@Override
                    public void changed(ObservableValue ov, State oldState, State newState) 
                    
                    {
		        		if (newState == State.SCHEDULED) 
		                {
		        			if ( !neWBrowser.getLocation().equalsIgnoreCase("about:blank") )
		        			{
		        				if ( !localHistoryOptions.contains(neWBrowser.getLocation())   )
		        					localHistoryOptions.add(0,neWBrowser.getLocation());
		        				addressTextField.setText(neWBrowser.getLocation()  );
		        			}
		        				
		                }
                        if (newState == State.SUCCEEDED) 
                        {
                        	try
                        	{
	                        	if (neWBrowser != null)
	                        	{
	                        		thisStage.setTitle( neWBrowser.getLocation()  );
	                        		
		                        	System.out.println( "Page stats :\n");
		                        	System.out.println( "Document     : " +neWBrowser.getDocument() );
		                            System.out.println( "Location     : " +neWBrowser.getLocation());
		                            System.out.println( "Title        : " +neWBrowser.getTitle());
		                            System.out.println( "User agent   : " +neWBrowser.getUserAgent());
		                            System.out.println( "User Stsh lo : " +neWBrowser.getUserStyleSheetLocation() );
		                            System.out.println( "Base URI     : " +neWBrowser.getDocument().getBaseURI() );
		                            System.out.println( "Document URI : " +neWBrowser.getDocument().getDocumentURI() );
		                            System.out.println( "XML version  : " + neWBrowser.getDocument().getXmlVersion() );
		                            System.out.println( "History obj  : " +neWBrowser.getHistory().toString() );
		                            
		                            ObservableList<WebHistory.Entry> addressHistory =  neWBrowser.getHistory().getEntries();
		                                    
		                            System.out.println( "\nHistory max size : " +neWBrowser.getHistory().getMaxSize()  );
		                            System.out.println( "History :");
		                            for ( WebHistory.Entry whe : addressHistory)
		                            	System.out.println( whe.getUrl() + "  " +whe.getLastVisitedDate().toString());
		                            
		                            
		                            System.out.println();
		                                 
		                       
	                        	}
                        	}	
                        	catch (java.lang.NullPointerException exc)
                        	{
                        		
                        	}
                        }
                    }
                    
					

					
                });
        
        neWBrowser.getLoadWorker().progressProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue,
					Number newValue) 
			{
				String progressvalue = String.format("%2.2f", newValue.doubleValue() *100  ) +" %";
				progressIndicatorLabel.setText( progressvalue );
				
			}
		});
        
        
        disableJavaScriptButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            
            @Override
            public void handle(ActionEvent event) 
            {
                if ( neWBrowser.isJavaScriptEnabled() )
                {                	
                	disableJavaScriptButton.setEffect(shadow);
                	
                	//disableJavaScriptButton.setRotate(90);
                	neWBrowser.setJavaScriptEnabled(false);
                	disableJavaScriptButton.setText("Enable JS");
                	disableJavaScriptButton.setBackground(  new Background( new BackgroundFill(Color.ORANGERED, new CornerRadii(1),  new Insets(1,1,1,1)))  );
                }
                else
                {
                	disableJavaScriptButton.setEffect(null);
                	neWBrowser.setJavaScriptEnabled(true);
                	disableJavaScriptButton.setText("Disable JS");
                	disableJavaScriptButton.setBackground(  new Background( new BackgroundFill(Color.LIGHTGREY, null, null))  );
                }
                        
                        
                
            }   });
        
        
        //not used on ui
        fullScreenButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            
            @Override
            public void handle(ActionEvent event) 
            {
                if ( thisStage.isFullScreen() )
                {
                	
                	thisStage.setFullScreen(false);
                	
                	root.add(refreshButton, 0, 0);
                    root.add(fullScreenButton, 1, 0);
                }
                else
                {
                	thisStage.setFullScreen(true);
                    root.getChildren().removeAll(root.getChildren());
                    root.add(newWebView, 0, 1);
                }
                        
                        
                
            }   });
        
        progressBar.progressProperty().bind(neWBrowser.getLoadWorker().progressProperty());        
        
        addressTextField.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent t) 
            {            	
            	neWBrowser.load(  addressTextField.getText().toString() );
                addressTextField.setText(neWBrowser.getLocation()  );
                addressTextField.setTooltip(new Tooltip(neWBrowser.getLocation()));                
            }            
        });
        
        newScene.setOnKeyReleased(  new EventHandler<KeyEvent>  ()
        {
            @Override
            public void handle(KeyEvent t) 
            {
                if (t.getCode().toString().equalsIgnoreCase("ESCAPE")  )
                {                    
                	
                    root.getChildren().removeAll(root.getChildren());
                    root.add(topRow, 0, 0);
                    root.add(newWebView , 0, 1);
                }
                if (t.getCode().toString().equalsIgnoreCase("F11"))
                {
                	if ( thisStage.isFullScreen() )
                    {
                		thisStage.setFullScreen(false);
                        root.getChildren().removeAll(root.getChildren());
                        
                        root.add(topRow, 0, 0);
                        root.add(newWebView , 0, 1);
                    }
                    else
                    {
                    	thisStage.setFullScreen(true);
                        root.getChildren().removeAll(root.getChildren());
                        root.add(newWebView , 0, 0);
                    }
                }
                //System.out.println(t.getCode().toString()  +" pressed");
            }
        });
    	
        
        newWebView.setPrefSize(2560, 1280);
        newWebView.setMaxSize(2560, 1280);
        
        
        
    	root.setPadding(  new Insets(5, 5, 5, 5 )   );
        root.setHgap(10);
        root.setVgap(1);
        
        topRow.setPadding(  new Insets(5, 5, 5, 5 )   );
        topRow.setHgap(10);
        topRow.setVgap(1);
        
        topRow.add(refreshButton, 0, 0);
        topRow.add(progressBar, 1 , 0);
        topRow.add(progressIndicatorLabel, 2 , 0);        
        topRow.add(disableJavaScriptButton, 3 , 0);
        topRow.add(addressTextField, 4 , 0);
        
        
        
        root.add(topRow, 0, 0);
        //root.add(progressBar, 1 , 0);
        
        
        //root.add(fullScreenButton, 1, 0);
        root.add(newWebView , 0, 1);
        
        newScene.setRoot(root);
    	
    	this.setTitle("Denise");//Java FX Web Browser");
    	this.sizeToScene();
    	this.setScene(newScene);
    	this.show();
    }
    
   private void enable_Disable_JS_Button()
   {
	   if ( neWBrowser.isJavaScriptEnabled() )
       {
		   disableJavaScriptButton.setEffect(null);
	       disableJavaScriptButton.setText("Disable JS");
	       disableJavaScriptButton.setBackground(  new Background( new BackgroundFill(Color.LIGHTGREY, null, null))  );
       	}
       else
       {
    	  disableJavaScriptButton.setEffect(shadow);
          disableJavaScriptButton.setText("Enable JS");
          disableJavaScriptButton.setBackground(  new Background( new BackgroundFill(Color.ORANGERED, new CornerRadii(1),  new Insets(1,1,1,1)))  );          
       }
   }
    
    private void getScreenCoordinates()
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		GraphicsDevice defaultDevice = ge.getDefaultScreenDevice();
		
		System.out.println( defaultDevice.getDisplayMode().getWidth());
		System.out.println(defaultDevice.getDisplayMode().getHeight()  );
		System.out.println(defaultDevice.getDisplayMode().getBitDepth()  );
		System.out.println(defaultDevice.getDisplayMode().getRefreshRate() );
		screenWidth = defaultDevice.getDisplayMode().getWidth();
		screenHeight = defaultDevice.getDisplayMode().getHeight();
	}
	
}