package view;


import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import application.DBOperations;
import application.FinVision;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
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
	
	
	//Static Classes
	/**
	 * @author milan
	 *Allows callback for ListView Cell Factory
	 */
	class CustomCell extends ListCell<String>{
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
			
			if(result.get()==ButtonType.OK){
				DBOperations Ops=new DBOperations();
				Boolean delete=Ops.deletePortfolio(deletePortfolio);
				
				//Refresh window if successful delete
				if(delete){
					FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/Home.fxml"));
					loader.setController(new HomeController());
					VBox root= (VBox)loader.load();
					HomeController homeController= loader.getController();
					Stage primaryStage = FinVision.getPrimaryStage();
					primaryStage.close();
					
					homeController.start(primaryStage);
					Scene scene=new Scene(root, 800, 600);
					primaryStage.setMinHeight(700);
					primaryStage.setMinWidth(830);
					primaryStage.setScene(scene);
					primaryStage.setTitle("FinVision");
					primaryStage.show();		
				}
				//Handle if unsuccessful delete
				else{
				}
				
			}
			else{
			}

			
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
			createStage.setMinHeight(430);
			createStage.setMinWidth(600);
			createStage.setScene(stageScene);
			
			//Override method for closing application
			createStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent event) {

			        // consume event
			        event.consume();
			        createStage.close();
			        try {
			        	FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/Home.fxml"));
						loader.setController(new HomeController());
						VBox root= (VBox)loader.load();
						HomeController homeController= loader.getController();
						Stage primaryStage = FinVision.getPrimaryStage();
						primaryStage.close();
						
						homeController.start(primaryStage);
						Scene scene=new Scene(root, 800, 600);
						primaryStage.setMinHeight(700);
						primaryStage.setMinWidth(830);
						primaryStage.setScene(scene);
						primaryStage.setTitle("FinVision");
						primaryStage.show();	
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
			        
			        
			    }
			});
			
			//Extract portfolio name from cell
			Button B=(Button)E.getSource();
			HBox Htemp=(HBox) B.getParent();
			Label tempLabel=(Label) Htemp.getChildren().get(0);
			String editPortfolio=tempLabel.getText();
			createStage.setTitle(editPortfolio);
			List<String> stockNames=new ArrayList<String>();
			
			//Connect to database
			DBOperations Ops=new DBOperations();
			stockNames=Ops.retrieveStocks(editPortfolio);
			
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
							}
			   			
			   			}
			        	   
			           }
			        }
			    }
			});
			
			//Show edit screen
			createStage.show();
			
			
			
		}
		
		private FXMLLoader mLLoader;
		
		@Override
		protected void updateItem(String item, boolean empty){
			super.updateItem(item, empty);
			if(empty || item==null){
				setText(null);
				setGraphic(null);
			}
			else {
	            if (mLLoader == null) {
	                mLLoader = new FXMLLoader(getClass().getResource("/view/CustomCell.fxml"));
	                mLLoader.setController(this);

	                try {
	                    mLLoader.load();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }

	            }
	            
	            PName.setText(item);
				setText(null);
				setGraphic(container);
			}
			
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
		createStage.setMinHeight(300);
		createStage.setMinWidth(600);
		createStage.setScene(stageScene);
		
		//Override method for closing application
		createStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {

		        // consume event
		        event.consume();
		        createStage.close();
		        try {
					restart();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
		        
		        
		    }
		});
		
		//Show Creation window
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
	 */
	@FXML
	private void FinishCreate(ActionEvent E) throws IOException{
		Button b=(Button) E.getSource();
		Stage stage=(Stage)b.getScene().getWindow();
		stage.close();
		restart();
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
			 ObservableList<String> selectedItems =  PortfolioList.getSelectionModel().getSelectedItems();
			 for(int i=0; i<selectedItems.size(); i++){
				 String Portfolio=selectedItems.get(i);
				 List<String> stockNames=new ArrayList<String>();
					
				 //Connect to database
				 DBOperations Ops=new DBOperations();
				 stockNames=Ops.retrieveStocks(Portfolio);
			    
	
				Rengine re=new Rengine(null, false, null);
				
				// Recommended, though not needed as such.
				if (!Rengine.versionCheck()) {System.exit(0);}
					
				// This just reports whether R was running and we connected to it, or whether we started it.
				if (re.isStandAlone()) System.out.println("R initialised by java");
					
				// We're going to use R to read the file but we'll get all the data back out from it 
				// into java, including the predicted points from our linear regression.
					
				// Original data.
				double[] x1 = null;
				double[] y1 = null;
				// Prediction data.
				double[] x2 = null;
				double[] y2 = null;
					
					
				try {
				// The easiest way to deal with R is to use the Rengine eval method. This takes in standard R commands as Strings.
				// Note in the file string that the original text, which needed escaping, is now within a larger String, meaning all the 
				// escape characters need escaping themselves. Easier to use forward-slashes, but I've used back-slashes here to show the point.
				REXP a = re.eval("data1 <- read.csv(\"C://Users/milan/Desktop/data.tab.txt\", header = TRUE)");
						
				// The eval method returns a very useful object of REXP class. This can be converted to 
				// various things to extract data. Here were just convert it to a String, more than anything so you can see it working.
				System.out.println("1: " + a.toString());
						
				a = re.eval("attach(data1)");
						
				// The problem with toString is that it can give you a whole complicated object.
				// The nice thing about the following command is that, while it doesn't have any output in R, in 
				// rJava it sets up the REXP with a copy of the data asked for. 
				a = re.eval("data1$Age");
				
				// We can then use the following method to get the data as a double array.
				x1 = a.asDoubleArray();
				for (int i1 = 0; i1 < x1.length; i1++) System.out.println(x1[i1]); 

					a = re.eval("data1$Desperation");
					y1 = a.asDoubleArray();
					for (int i1 = 0; i1 < y1.length; i1++) System.out.println(y1[i1]); 
						
				// That's got our two original data sets out. Now we plot that data.
				// Note that this opens up the R plot window. This will hang until the 
				// application is terminated, which you can do on the Eclipse output window by 
				// pushing the square red icon. This causes some issues, as we'll see.
				a = re.eval("plot(Age, Desperation, main=\"Age vs. Desperation\")");

				// Do the regression.
				a = re.eval("lineeq <- lm(Desperation ~ Age, data=data1)");
				a = re.eval("x <- seq(min(Age), max(Age), by=10.0)");
						
				// Get the novel x-axis data.
				x2 = a.asDoubleArray();
				for (int i1 = 0; i1 < x2.length; i1++) System.out.println(x2[i1]);
						
				a = re.eval("newData <- data.frame(Age = x)");
				a = re.eval("predictions <- predict(lineeq, newdata = newData)");

				// Get the predicted y-axis data.
				y2 = a.asDoubleArray();
				for (int i1 = 0; i1 < y2.length; i1++) System.out.println(y2[i1]); 

				// Add to the plot. Or so we'd think. 
				a = re.eval("lines(Age, predictions)");
				a = re.eval("title(main=\"Autos\", col.main=\"red\", font.main=4)");

				// Clean up.
				a = re.eval("detach(data1)");
				a = re.eval("rm (data1, lineeq, newData, predictions, x)");

				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
			 
			    }
				 
				 
			 }
		
		
		}
		
		
	
		
	
	/**
	 * @throws IOException
	 * Restarts Home View and updates list of portfolios
	 */
	public void restart() throws IOException{
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
	
	
	//Start
	/**
	 * @param primaryStage
	 * Start method for home menu
	 * @throws SQLException 
	 */
	public void start(Stage primaryStage) throws SQLException {
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
            	
                return new CustomCell();
                
            }
        }
    );
	
		PortfolioList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
	}

}
