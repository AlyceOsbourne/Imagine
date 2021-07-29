/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


package imagine;

import imagine.scenes.core.Window;
import lib.Screen;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application implements Screen{

    @FXML
    public static Window window;
    @FXML
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Imagine");
        primaryStage.setMaximized(true);
        window = new Window(primaryStage);
        Scene scene = new Scene(window);
        scene.setRoot(window);
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
