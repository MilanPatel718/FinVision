package view;

import java.io.IOException;
import java.sql.SQLException;
import application.DBOperations;
import application.FinVision;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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
		
        try {
    		FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/Home.fxml"));
    		loader.setController(new HomeController());
    		Parent root = loader.load();
    		HomeController controller = loader.getController();
    		controller.start(FinVision.getPrimaryStage());
			FinVision.getPrimaryScene().setRoot(root);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
