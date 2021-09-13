

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine;

import imagine.scenes.core.Window;
import imagine.scenes.worldatlus.Map;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static lib.math.voronoi.algorithm.Voronoi.Resolution;

public class Main extends Application {
    public static Window window;
    @FXML
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setTitle("Imagine");
        primaryStage.setMaximized(true);
        window = new Window();
        Scene scene = new Scene(window);
        primaryStage.setScene(scene);
        show();
        runDebug();
    }

    private void runDebug() {
        Resolution r = Resolution.TESTXXXL;
        Map m = new Map(r.width, r.height, null).populateWithColor();
        m.saveImage();
    }

    void show() {
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
