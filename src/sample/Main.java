package sample;

import javafx.application.Application;
import javafx.stage.Stage;;
import view.*;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        View view = new View();
        view.startView(primaryStage);

    }



    public static void main(String[] args) {
        launch(args);
    }
}
