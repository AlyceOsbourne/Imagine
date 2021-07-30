/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


package imagine;

import imagine.scenes.ContentLibrary;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lib.general.Screen;

public class Main extends Application implements Screen{

    @FXML
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Imagine");
        primaryStage.setMaximized(true);
        Scene scene = new Scene(ContentLibrary.window);
        scene.setRoot(ContentLibrary.window);
        primaryStage.setScene(scene);
        stage = primaryStage;
        show();
    }

    void show(){stage.show();}

    void kill() throws Exception {stage.close(); stop();}

    public static void main(String[] args) {
        launch(args);
    }


}
