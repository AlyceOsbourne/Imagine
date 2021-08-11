/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */


package imagine;

import imagine.scenes.core.Window;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lib.general.Screen;

public class Main extends Application implements Screen{

    public static final Window window = new Window();

    @FXML
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setTitle("Imagine");
        primaryStage.setMaximized(true);
        Scene scene = new Scene(window);
        scene.setRoot(window);
        primaryStage.setScene(scene);
        show();
    }

    void show(){stage.show();}

    void kill() throws Exception {stage.close(); stop();}

    public static void main(String[] args) {
        launch(args);
    }


}
