package team19.java.application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import team19.java.core.Model;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;

/**
 * Entry Class
 * @author Chu Wu
 *
 */
public class Main extends Application {
	
	@Override
	public void start(Stage stage) {
		try {
			Application.setUserAgentStylesheet(STYLESHEET_MODENA);
			HBox root = (HBox)FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			stage.setTitle("Face Recognition System");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	         @Override
	         public void handle(WindowEvent event) {
	        	 Model.getInstance().save();
	        	 System.exit(0);
	         }
	         
	     });
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
