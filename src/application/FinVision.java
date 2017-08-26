package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.HomeController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

import org.rosuda.JRI.*;


/**
 * @author milan
 *Initial Launch Window
 */
public class FinVision extends Application {
	 private static Stage pStage;
	
	@Override
	public void start(Stage primaryStage) throws IOException, SQLException {
		FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/Home.fxml"));
		loader.setController(new HomeController() );
		VBox root= (VBox)loader.load();
		HomeController homeController= loader.getController();
		setPrimaryStage(primaryStage);
		primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));

		
		homeController.start(primaryStage);
		Scene scene=new Scene(root, 800, 600);
		primaryStage.setMinHeight(700);
		primaryStage.setMinWidth(830);
		primaryStage.setScene(scene);
		primaryStage.setTitle("FinVision");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	 public static Stage getPrimaryStage() {
	        return pStage;
	    }

	    private void setPrimaryStage(Stage pStage) {
	        this.pStage = pStage;
	    }
}
