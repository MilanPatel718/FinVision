package view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.DBOperations;
import application.FinVision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import view.HomeController.CustomCell;
import javafx.scene.control.Alert.AlertType;

public class EditController {
	@FXML TextField editStock;
	@FXML TextField editTicker;
	@FXML TextField editName;
	@FXML Button stockAdd;
	@FXML Button Rename;
	@FXML Button FinishEdit;
	@FXML HBox editBox;
	@FXML ListView<String> stockList;
	
	/**
	 * @param E
	 * @throws IOException
	 * @throws SQLException
	 * Allows User to add new stocks to existing portfolios
	 */
	@FXML
	private void addStock(ActionEvent E) throws IOException, SQLException{
		
		//Extract Relevant fields
		String ticker=editTicker.getText();
		String stockName=editStock.getText();
		Button b=(Button)E.getSource();
		Stage stage=(Stage)b.getScene().getWindow();
		String portfolioName=stage.getTitle();
		
		//Make sure Ticker and Stock Name are filled out
		if(ticker.isEmpty() || stockName.isEmpty()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("WARNING");
			alert.setContentText("Please ensure Ticker and Stock Name fields are both nonempty before clicking add");
			alert.showAndWait();
		}
		
		//Connect to database
		DBOperations ops=new DBOperations();
		Boolean check = null;
		
		try {
			check = ops.addStock(ticker,stockName, portfolioName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Handle Success
		if(check){
			stockList.getItems().add(ticker+" "+stockName);
			stockList.refresh();
			editTicker.clear();
			editStock.clear();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Success");
			alert.setContentText("Portfolio Successfully Created");
			alert.showAndWait();
		}
		
		//Handle Failure
		else{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Stock could not be added, make sure ticker is unique");
			alert.showAndWait();
		}
		
	}
	
	/**
	 * @param Original
	 * @throws IOException
	 * @throws SQLException
	 * Allows user tor rename their portfolio, given the new name is different from the original
	 */
	@FXML 
	private void renamePortfolio(ActionEvent E) throws IOException, SQLException{
		Button b=(Button)E.getSource();
		Stage stage=(Stage)b.getScene().getWindow();
		String Original=stage.getTitle();
		String rename=editName.getText();
		if(rename.isEmpty()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("WARNING");
			alert.setContentText("Please fill out the new name field before pressing rename ");
			alert.showAndWait();
			
		}
		else if(rename.equals(Original)){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("WARNING");
			alert.setContentText("New name is identical to the current name");
			alert.showAndWait();
		}
		else{
			
			//Connect to Database
			DBOperations ops=new DBOperations();
			Boolean check=ops.renamePortfolio(Original, rename);
			
			//Handle Success
			if(check){
				stage.setTitle(rename);
				editName.clear();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText("Success");
				alert.setContentText("Portfolio Successfully Renamed");
				alert.showAndWait();
				
			}
			
		}
	}
	
	/**
	 * @param E
	 * @throws IOException
	 * @throws SQLException
	 * Closes edit window
	 */
	@FXML 
	private void finishEdit(ActionEvent E)throws IOException, SQLException{
		Button b=(Button) E.getSource();
		Stage stage=(Stage)b.getScene().getWindow();
		stage.close();
		FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/Home.fxml"));
		loader.setController(new HomeController());
		VBox root = null;
		try {
			root = (VBox)loader.load();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HomeController homeController= loader.getController();
		Stage primaryStage = FinVision.getPrimaryStage();
		primaryStage.close();
		
		try {
			homeController.start(primaryStage);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene scene=new Scene(root, 800, 600);
		primaryStage.setMinHeight(700);
		primaryStage.setMinWidth(830);
		primaryStage.setScene(scene);
		primaryStage.setTitle("FinVision");
		primaryStage.show();
		
	}
	

}
