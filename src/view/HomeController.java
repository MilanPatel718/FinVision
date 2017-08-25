package view;

import javafx.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * @author milan
 * Controls application Home page
 */
public class HomeController {
	//FXML declarations
	@FXML Button Create;
	@FXML Button Visualize;
	@FXML ListView PortfolioList;
	
	
	//Controller Methods
	
	/**
	 * @param E
	 * @throws IOException
	 * On click method for "Create" button, opens pop up window for portfolio creation
	 */
	@FXML 
	private void openCreateView(ActionEvent E) throws IOException{
		
	}
	
	/**
	 * @param E
	 * @throws IOException
	 * On click method for "Visualize" button, takes user selected portfolios and creates data visualizations
	 */
	@FXML 
	private void visualizeData(ActionEvent E) throws IOException{
		
	}
	
	
	//Start
	/**
	 * @param primaryStage
	 * Start method for home menu
	 */
	public void start(Stage primaryStage) {
		
		//Upon Start, populate ListView with Portfolios saved in Database (if any)
		
		
	}

}
