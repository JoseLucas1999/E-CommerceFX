package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            LoginView loginView = new LoginView();
            primaryStage.setTitle("E-commerceFX - Login");
            primaryStage.setScene(loginView.getScene(primaryStage));
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // dispara a aplicação JavaFX
    }
}
