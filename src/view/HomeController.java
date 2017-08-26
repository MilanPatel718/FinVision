package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

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

	//Static Classes
	/**
	 * @author milan
	 *Allows callback for ListView Cell Factory
	 */
	static class CustomCell extends ListCell<String>{
		@FXML
		private Label PName;
		
		@FXML
		private HBox container;
		
		@FXML
		private CheckBox Select;
		
		@FXML 
		private Button Edit;
		
		@FXML 
		private Button Delete;
		
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
				else{
					
				}
				
			}
			else{
				
			}

			
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
		ObservableList<String> names =FXCollections.observableArrayList(portfolioNames);
		PortfolioList.setItems(names);
		PortfolioList.setFocusTraversable(false);
		
		//Create custom ListView Cells for each Portfolio
		PortfolioList.setCellFactory(new Callback<ListView<String>, 
	            ListCell<String>>() {
            @Override 
            public ListCell<String> call(ListView<String> PortfolioList) {
                return new CustomCell();
            }
        }
    );
		
		
	}

}
