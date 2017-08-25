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

import org.rosuda.JRI.*;

/**
 * @author milan
 *Initial Launch Window
 */
public class FinVision extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/Home.fxml"));
		loader.setController(new HomeController() );
		VBox root= (VBox)loader.load();
		HomeController homeController= loader.getController();
		primaryStage.getIcons().add(new Image("file:resources/images/icon.png"));

		
		homeController.start(primaryStage);
		Scene scene=new Scene(root, 800, 600);
		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("FinVision");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
