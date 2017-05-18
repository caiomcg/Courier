package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Courrier");
        primaryStage.getIcons().add(new Image("Assets/courier.png"));
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.getScene().getStylesheets().add("Assets/layout.css");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
