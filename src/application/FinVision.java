package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.HomeController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;


/**
 * @author milan
 *Initial Launch Window
 */
public class FinVision extends Application {
	 private static Stage pStage;
	 private static Scene pScene;
	
	@Override
	public void start(Stage primaryStage) throws IOException, SQLException {
		//Load fxml file and prepare for application launch
		FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/Home.fxml"));
		loader.setController(new HomeController() );
		VBox root= (VBox)loader.load();
		HomeController homeController= loader.getController();
		setPrimaryStage(primaryStage);
		primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));

		//Set preferred dimensions for application home
		homeController.start(primaryStage);
		Scene scene=new Scene(root, 800, 600);
		setPrimaryScene(scene);
		primaryStage.setMinHeight(700);
		primaryStage.setMinWidth(830);
		primaryStage.setScene(scene);
		primaryStage.setTitle("FinVision");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	 //Getter and Setters for the primary stage of the application
	 public static Stage getPrimaryStage() {
	        return pStage;
	    }

	 private void setPrimaryStage(Stage pStage) {
	        FinVision.pStage = pStage;
	    }
	    
	 //Getter and Setters for the primary scene of the application
	 public static Scene getPrimaryScene() {
	       return pScene;
	    }

	private void setPrimaryScene(Scene pScene) {
		     FinVision.pScene = pScene;
		    }
}
