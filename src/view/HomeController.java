package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import application.DBOperations;
import application.FinVision;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import org.rosuda.JRI.*;


/**
 * @author milan
 * Controls application Home page
 */
public class HomeController {
	
	//FXML declarations
	@FXML Button Create;
	@FXML Button Visualize;
	@FXML ListView<String> PortfolioList;
	@FXML SplitPane Split;
	@FXML GridPane Grid;
	@FXML TextField Pname;
	@FXML TextField Ticker;
	@FXML TextField StockName;
	@FXML Button Submit;
	@FXML Button Add;
	@FXML Button Finish;
	@FXML Pane TopPane;
	@FXML Pane BottomPane;
	
	public String start;
	public String end;
	public int movingAverage;
	
	
	//Inner class for custom ListView Cell
	/**
	 * @author milan
	 *Allows callback for ListView Cell Factory
	 */
	public class CustomCell extends ListCell<String>{
		@FXML private Label PName;
		@FXML private HBox container;
		@FXML private Button Edit;
		@FXML private Button Delete;
		@FXML ListView<String> stockList;
		@FXML TextField editStock;
		@FXML TextField editTicker;
		@FXML TextField editName;
		@FXML Button stockAdd;
		@FXML Button Rename;
		@FXML Button FinishEdit;
		
		public CustomCell(){
			FXMLLoader mLoader = new FXMLLoader(getClass().getResource("/view/CustomCell.fxml"));
			mLoader.setController(this);
            try {
                mLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {              
                    Edit.setVisible(true);
                    Delete.setVisible(true);
                    
                }
            }); 
            PortfolioList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
            	Edit.setVisible(false);
            	Delete.setVisible(false);
            });
		}
		
		@Override
		protected void updateItem(String item, boolean empty){
			super.updateItem(item, empty);
			if(empty || item==null){
				setText(null);
				setGraphic(null);
			}
			else {     
				this.setGraphic(this);
	           	PName.setText(item);
				setText(null);
				setGraphic(container);
			}
			
		}
		
		/**
		 * @param E
		 * @throws IOException
		 * Prompts confirmation for portfolio deletion, makes delete command to DB if confirmed
		 * @throws SQLException 
		 */
		@FXML
		private void deletePortfolio(ActionEvent E) throws IOException, SQLException{
			//Extract portfolio name from cell
			Button B=(Button)E.getSource();
			HBox Htemp=(HBox) B.getParent();
			Label tempLabel=(Label) Htemp.getChildren().get(0);
			String deletePortfolio=tempLabel.getText();
			
			//Request delete confirmation from user
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText(deletePortfolio + " will be deleted.");
			alert.setContentText("Are you sure you want to delete this portfolio?");
			Optional<ButtonType> result = alert.showAndWait();
			
			//Delete the item
			if(result.get()==ButtonType.OK){
				DBOperations Ops=new DBOperations();
				Boolean delete=Ops.deletePortfolio(deletePortfolio);
				
				//Refresh window if successful delete
				if(delete){
					int index = PortfolioList.getSelectionModel().getSelectedIndex();
					PortfolioList.getItems().remove(index);
					if (PortfolioList.getItems().isEmpty()){
						PortfolioList.getSelectionModel().clearSelection();
					}
				}
				//Handle if unsuccessful delete
				else{
				}
				
			}
			else{}
			
		}
	
		
		/**
		 * @param E
		 * @throws IOException
		 * @throws SQLException
		 * Opens view to edit a portfolio, either deleting stocks or adding new ones
		 */
		@SuppressWarnings("unchecked")
		@FXML 
		private void editPortfolio(ActionEvent E) throws IOException, SQLException{
			
			//Create modal stage as pop up for edit screen
			Stage createStage = new Stage();
			createStage.initModality(Modality.APPLICATION_MODAL);
			
			//Switch controller to EditController for edit screen methods
			FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/editPortfolio.fxml"));
			loader.setController(new EditController());
			HBox comp=(HBox) loader.load();
			stockList=(ListView<String>) comp.getChildren().get(0);
			Scene stageScene = new Scene(comp, 600, 400);
			
			String css = getClass().getResource("/view/dark.css").toExternalForm();
			stageScene.getStylesheets().clear();
			stageScene.getStylesheets().add(css);
			
			createStage.setMinHeight(430);
			createStage.setMinWidth(600);
			createStage.setScene(stageScene);
			
			//Extract portfolio name from cell
			Button B=(Button)E.getSource();
			HBox Htemp=(HBox) B.getParent();
			Label tempLabel=(Label) Htemp.getChildren().get(0);
			String editPortfolio=tempLabel.getText();
			createStage.setTitle(editPortfolio);
			List<String> stockNames=new ArrayList<String>();
			
			//Connect to database
			DBOperations Ops=new DBOperations();
			stockNames=Ops.retrieveStocks(editPortfolio, false);
			
			//Create ListView for list of stock names
			ObservableList<String> Stocknames =FXCollections.observableArrayList(stockNames);
			stockList.setItems(Stocknames);
			
			//Handle double click event for deletion of stocks from list
			stockList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			    @Override
			    public void handle(MouseEvent click) {

			        if (click.getClickCount() == 2) {
			           //Use ListView's getSelected Item
			           String currentItemSelected = stockList.getSelectionModel()
			                                                    .getSelectedItem();
			           int currIndex=stockList.getSelectionModel().getSelectedIndex();
			           if(currentItemSelected==null){}
			           else{
			        	Alert alert = new Alert(AlertType.CONFIRMATION);
			   			alert.setTitle("Confirmation Dialog");
			   			alert.setHeaderText(currentItemSelected+ " will be deleted.");
			   			alert.setContentText("Are you sure you want to delete this stock?");
			   			Optional<ButtonType> result = alert.showAndWait();
			   			
			   			if(result.get()==ButtonType.OK){
			   				String [] split=currentItemSelected.split(" ");
			   				String Ticker=split[0];
			   				
			   				DBOperations ops=new DBOperations();
			   				try {
								boolean check=ops.deleteStock(editPortfolio, Ticker);
								if(check){
									stockList.getItems().remove(currIndex);
									stockList.refresh();
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			   			
			   			}
			        	   
			           }
			        }
			    }
			});
			
			//Show edit screen
			createStage.getIcons().add(new Image("file:resources/images/icon.png"));
			createStage.show();
		}
	}
	
	//Controller Methods
	
	/**
	 * @param E
	 * @throws IOException
	 * On click method for "Create" button, opens pop up window for portfolio creation
	 */
	@FXML 
	private void openCreateView(ActionEvent E) throws IOException{
		//Create Modal Popup window for Portfolio Creation Page
		Stage createStage = new Stage();
		createStage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/createPortfolio.fxml"));
		loader.setController(new HomeController());
		VBox comp=(VBox) loader.load();
		Scene stageScene = new Scene(comp, 600, 217);
		
		String css = getClass().getResource("/view/dark.css").toExternalForm();
		stageScene.getStylesheets().clear();
		stageScene.getStylesheets().add(css);
		
		createStage.setMinHeight(300);
		createStage.setMinWidth(600);
		createStage.setScene(stageScene);

		
		//Show Creation window
		createStage.getIcons().add(new Image("file:resources/images/icon.png"));
		createStage.show();
		
		
		
	}
	/**
	 * @param E
	 * @throws IOException
	 * On click method for creating portfolio and corresponding table in database
	 * Checks uniqueness before allowing table creation
	 * @throws SQLException 
	 */
	@FXML
	private void submitPortfolioName(ActionEvent E) throws IOException, SQLException{
		//Extract TextField and make sure it is nonempty
		String pName=Pname.getText();
		if(pName.isEmpty()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("WARNING");
			alert.setContentText("Please enter a nonempty Portfolio name");
			alert.showAndWait();
		}
		
		//Connect to database
		DBOperations ops=new DBOperations();
		Boolean check=ops.createPortfolio(pName);
		
		//Handle Success
		if(check){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Success");
			alert.setContentText("Portfolio Successfully Created");
			TopPane.setDisable(true);
			BottomPane.setDisable(false);
			alert.showAndWait();
			
		}
		
		//Handle Failure
		else{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Portfolio could not be created, make sure name is unique");
			alert.showAndWait();
			
		}
		
		
	}
	
	/**
	 * @param E
	 * @throws IOException
	 * Takes Ticker and Stock Name and inserts entry into appropriate portfolio/table
	 * Both fields must be nonempty, and Ticker uniqueness is enforced via primary key
	 * @throws SQLException 
	 */
	@FXML
	private void addStock(ActionEvent E) throws IOException, SQLException{
		//Extract Relevant fields
		String ticker=Ticker.getText();
		String stockName=StockName.getText();
		String portfolioName=Pname.getText();
		
		//Ensure both Ticker and stockName are filled out
		if(ticker.isEmpty() || stockName.isEmpty()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("WARNING");
			alert.setContentText("Please ensure Ticker and Stock Name fields are both nonempty before clicking add");
			alert.showAndWait();
		}
		
		//Connect to database
		DBOperations ops=new DBOperations();
		Boolean check=ops.addStock(ticker,stockName, portfolioName);
		
		//Handle Success
		if(check){
			Ticker.clear();
			StockName.clear();
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
			TopPane.setDisable(true);
			BottomPane.setDisable(false);
			alert.showAndWait();
		}
		
	}
	
	/**
	 * @param E
	 * @throws IOException
	 * Closes Portfolio creation window and returns user to home screen
	 * @throws SQLException 
	 */
	@FXML
	private void FinishCreate(ActionEvent E) throws IOException, SQLException{
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
	
	/**
	 * @param E
	 * @throws IOException
	 * On click method for "Visualize" button, takes user selected portfolios and creates data visualizations
	 * @throws SQLException 
	 */
	@FXML 
	private void visualizeData(ActionEvent E) throws IOException, SQLException{
		if(PortfolioList.getItems().isEmpty()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("Portfolio List is Empty");
			alert.showAndWait();
		}
		else if(PortfolioList.getSelectionModel().getSelectedItems().isEmpty()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Error");
			alert.setContentText("No Portfolio selected");
			alert.showAndWait();
			
		}
		else{
		    
			Dialog<Pair<String, String>> dialog = new Dialog<>();
			dialog.setTitle("Date Specification");
			dialog.setHeaderText("Input start/end date: \n 1) Ensure start date is before end date \n 2) Select Moving Average value to plot (Select 0 for no MA) \n 3) "
					+ "Number of weekdays between start and end must be atleast equal to selected n value");
			
			start=null;
			end=null;
			ButtonType dateButton = new ButtonType("Enter", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(dateButton, ButtonType.CANCEL);
			
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));
			
			DatePicker startDate = new DatePicker();
			DatePicker endDate = new DatePicker();
			
			ObservableList<Integer> options = FXCollections.observableArrayList(0, 10, 15, 30, 50, 100, 200);
			final ComboBox<Integer> comboBox = new ComboBox<Integer>(options);
			
			grid.add(new Label("Start Date:"), 0, 0);
			grid.add(startDate, 1, 0);
			grid.add(new Label("End Date:"), 0, 1);
			grid.add(endDate, 1, 1);
			grid.add(new Label("Moving Average n value: "), 2, 0);
			grid.add(comboBox, 3, 0);
			
			dialog.getDialogPane().setContent(grid);
			
			

			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == dateButton) {
			    	if(startDate.getValue()==null || endDate.getValue()==null || comboBox.getValue()==null){
			    		Alert alert = new Alert(AlertType.ERROR);
			    		alert.setTitle("Error Dialog");
			    		alert.setHeaderText("Incorrect Entry");
			    		alert.setContentText("Please make sure fields aren't blank");
			    		alert.showAndWait();
			    		dialog.showAndWait();
			    		
			    	}
			    	if(startDate.getValue().compareTo(endDate.getValue()) < 0){
			    		java.util.Date sDate = java.sql.Date.valueOf(startDate.getValue());
			    		java.util.Date eDate = java.sql.Date.valueOf(endDate.getValue());
			    		int tradingDays = HomeController.getTradingDays(sDate,eDate);
			    		if(tradingDays < comboBox.getValue()){
				    		Alert alert = new Alert(AlertType.ERROR);
				    		alert.setTitle("Error Dialog");
				    		alert.setHeaderText("Incorrect Entry");
				    		alert.setContentText("Number of observations must be at least equal to selected moving average value n");
				    		alert.showAndWait();
				    		dialog.showAndWait();
			    			
			    		}
			    		movingAverage = comboBox.getValue();
			    		DateTimeFormatter s = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			    		start = startDate.getValue().format(s);
			    		end = endDate.getValue().format(s);
			    		return new Pair<>(start, end);
			    		
			    	}
			    	else{
			    		Alert alert = new Alert(AlertType.ERROR);
			    		alert.setTitle("Error Dialog");
			    		alert.setHeaderText("Incorrect Entry");
			    		alert.setContentText("Please enter dates in correct order");
			    		
			    		alert.showAndWait();
			    		return null;
			    	}
			    }
				return null;
			} );
			
			dialog.showAndWait();
			if(start == null || end == null){
				return;
			}
			
			ObservableList<String> selectedItems =  PortfolioList.getSelectionModel().getSelectedItems();
			Rengine re = Rengine.getMainEngine();
			 if (re==null){
				re = new Rengine(null, false, null);
			 }
				
			 // Recommended, though not needed as such.
			 if (!Rengine.versionCheck()) {System.exit(0);}
			 List<String> stockNames=new ArrayList<String>();
					
			 //Connect to database
			 DBOperations Ops=new DBOperations();
			 stockNames=Ops.retrieveStocks(selectedItems.get(0), true);
				 
			 int Size= stockNames.size();

				 
			 String combinePlot = "";
			 String vector = "c(";
			 for(int i=0; i<stockNames.size(); i++){
				 String ticker=stockNames.get(i).toString();
				 if(i==stockNames.size()-1){
					 vector = vector + "\"" + ticker + "\")";
					 combinePlot= combinePlot+ ticker +"="+ ticker+"[, \""+ ticker+".Close\"]";
				 }
						 
				 else{
					 vector = vector + "\"" + ticker + "\", ";
					 combinePlot= combinePlot+ ticker +"= "+ ticker+"[, \""+ ticker+".Close\"], ";
				 }
					 

				 	}

				 @SuppressWarnings("unused")
				 REXP rLink;
				 String sourceFile ="\"" + re.eval("getwd()").asString() + "/FinVision.R" +"\"";
				 String sourceCommand = "source(" + sourceFile + ")";
				 rLink = re.eval(sourceCommand);
				 rLink = re.eval("Apple <-" + "singlePortfolio(" + vector + ", " + Size + "," + "\"" + start + "\"" +  "," + "\"" + end + "\"" + "," + movingAverage + ")");
				
			}
	}
		
	
	
	//Start
	/**
	 * @param primaryStage
	 * @throws SQLException
	 * @throws IOException
	 * Start method for home menu 
	 */
	public void start(Stage primaryStage) throws SQLException, IOException {
		//Fix SplitPane Divider in middle position
		Split.setDividerPositions(0.50);
		PortfolioList.maxWidthProperty().bind(Split.widthProperty().multiply(.50));
		Grid.maxWidthProperty().bind(Split.widthProperty().multiply(.50));
		
		//Upon Start, populate ListView with Portfolios saved in Database (if any)
		List<String> portfolioNames=new ArrayList<String>();
		DBOperations Ops=new DBOperations();
		portfolioNames=Ops.retrieveNames();
		ObservableList<String> names=FXCollections.observableArrayList(portfolioNames);
		PortfolioList.setItems(names);
		

		//Create custom ListView Cells for each Portfolio
		PortfolioList.setCellFactory(new Callback<ListView<String>, 
	            ListCell<String>>() {
            @Override 
            public ListCell<String> call(ListView<String> PortfolioList) {
            ListCell<String>  Cell = new CustomCell();
            return Cell;
                
            }
        }
    );
	
		
	}
	
	public static int getTradingDays(Date startDate, Date endDate) {
	    Calendar startCal = Calendar.getInstance();
	    startCal.setTime(startDate);        

	    Calendar endCal = Calendar.getInstance();
	    endCal.setTime(endDate);

	    int workDays = 0;

	    while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()){
	        if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
	 	        ++workDays;
	 	     }
	        	 startCal.add(Calendar.DAY_OF_MONTH, 1);
	        }
	    return workDays;
	    	
	    }

}
