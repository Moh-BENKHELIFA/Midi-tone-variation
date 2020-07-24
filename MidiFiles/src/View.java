
import java.io.IOException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class View extends Application {

	private AnchorPane pane ;
	private Controler control;
	
	
	/**
	 * Load FXML file and initiate controller
	 * Show the Stage
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		pane = (AnchorPane) FXMLLoader.load(getClass().getResource("view.fxml"));
		control = new Controler();

		Spinner ton = (Spinner) pane.lookup("#ton");
		TextField textField = ton.getEditor();
		textField.textProperty().addListener(new ChangeListener<String>() {
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		            textField.setText(newValue.replaceAll("[^\\d, ^-]", ""));
		        }
		    }
		});
		
		
		Button parcourir = (Button) pane.lookup("#Load");	
		parcourir.setOnAction( a -> {
			
			((TextField) pane.lookup("#path")).textProperty().setValue(control.fileExplorer(primaryStage));
		});
		
		
		Button valider = (Button) pane.lookup("#DOIT");
		valider.setOnAction( a -> {
			try {
				control.toneShift(((TextField) pane.lookup("#path")).textProperty().getValue(), (int) ton.getValue());
			} catch (Exception e) {
				e.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("ERROR \n" + e.getMessage());
				alert.setHeaderText("Oups");
				alert.showAndWait();
			}
		});
			
		Scene s = new Scene(pane);
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
