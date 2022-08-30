package javafxwebbrowser; 

import java.security.SecureRandom;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class JavaFXCharts extends Stage  
{
	private SimpleDoubleProperty grapes = new SimpleDoubleProperty(1); 
	private SimpleDoubleProperty oranges = new SimpleDoubleProperty(2);
	private SimpleDoubleProperty plums  = new SimpleDoubleProperty(2);    
    
    private PieChart.Data grapesData;
    private PieChart.Data orangesData;
    private PieChart.Data plumsData;    
    
    private ObservableList<PieChart.Data> pieChartData;
    private final PieChart pieChart ;
   
    private Button animateChartButton;
    private Button resetChartButton;
    private SecureRandom sr;
    
    
    
    //constructor
    public JavaFXCharts()
    {
    	
    	grapesData = new PieChart.Data("Grapes", 0.0);
        orangesData = new PieChart.Data("Oranges", 0.0);
        plumsData = new PieChart.Data("Plums", 0.0);
        
    	pieChartData = FXCollections.observableArrayList(
        			grapesData,
        			orangesData,
        			plumsData,
        			new PieChart.Data("Pears", 4),
        			new PieChart.Data("Apples", 7));
    	
    	pieChart = new PieChart(pieChartData);
    	
    	animateChartButton = new Button("Animate Chart");
    	animateChartButton.setPrefSize(100, 20);
        animateChartButton.setMaxSize(100, 20);
        
        resetChartButton = new Button("Reset Chart");
        resetChartButton.setPrefSize(100, 20);
        resetChartButton.setMaxSize(100, 20);
        
        sr = new SecureRandom();
        
    	start();
    }
	    
    

    public void start()//final Stage chartsStage)     
    {
    	
    	grapes.setValue(0.1);
        oranges.setValue(1);
        plums.setValue(1);
        
    	grapesData.pieValueProperty().bind(grapes);
        orangesData.pieValueProperty().bind(oranges);
        plumsData.pieValueProperty().bind(plums);
        
        
    	 ///pieChart.setTitle("Imported Fruits");
        pieChart.setLabelsVisible(true);
        pieChart.setAnimated(true);
        pieChart.setLabelLineLength(10);
        //pieChart.setLegendSide(Side.LEFT);
        
        
        
        
        animateChartButton.setOnAction(new EventHandler<ActionEvent>() 
        {
        	@Override
            public void handle(ActionEvent event) 
            {
            	Runnable modifyPieChartRinnableIncrease = new Runnable()
        		{
					@Override
					public void run() 
					{
						for (int i=0; i<100; i++)
						{
							System.out.println("\tIncreasing : \t" +grapes.get() +"\t" +oranges.get() +"\t" +plums.get() +" \t" +Thread.currentThread().getId());
							try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();}
							grapes.setValue(grapes.get()+sr.nextInt(3));
			                oranges.setValue(oranges.get()+sr.nextInt(2));
			                plums.setValue(plums.get()+sr.nextInt(2));
			                
			                if (grapes.get() > 20 && grapes.get()<22  )
			                {
			                	Runnable modifyPieChartRinnableDecrease = new Runnable()
		                		{
									@Override
									public void run() 
									{
										for (int i=0; i<100; i++)
										{
											System.out.println("\t\tDecreasing : \t" +grapes.get() +"\t" +oranges.get() +"\t" +plums.get() +" \t" +Thread.currentThread().getId());
											try { Thread.sleep(100); } catch (InterruptedException e) {e.printStackTrace();}
											grapes.setValue(grapes.get()-sr.nextInt(3));
							                oranges.setValue(oranges.get()-sr.nextInt(2));
							                plums.setValue(plums.get()-sr.nextInt(2));
										}
									}		                		
		                		};
		                		new Thread(modifyPieChartRinnableDecrease).start();
			                }
						}						
					}        		
        		};
        		new Thread(modifyPieChartRinnableIncrease).start();
        		
            }   
        });
        
        
        resetChartButton.setOnAction(new EventHandler<ActionEvent>() 
        {
        	@Override
            public void handle(ActionEvent event) 
            {
        		grapes.setValue(1);
                oranges.setValue(1);
                plums.setValue(1);
            }   
        });
        
        
        
    	Scene scene = new Scene(new Group(), 900, 600);
        final GridPane root = new GridPane();
    	root.setPadding(  new Insets(5, 5, 5, 5 )   );
        root.setHgap(10);
        root.setVgap(1);
        
        root.add(animateChartButton, 0, 0);
        root.add(resetChartButton, 3, 0);
        root.add(pieChart, 0, 1);
        
        
        
    	scene.setRoot(root);
    	
    	this.setTitle("Denise - Charts");//Java FX Web Browser");
    	this.sizeToScene();
    	this.setScene(scene);
    	this.show();
    }
    
   

	
}
