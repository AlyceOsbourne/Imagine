

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine;

import imagine.scenes.core.Window;
import imagine.scenes.worldatlus.abst.BaseMapAbst;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lib.math.voronoi.algorithm.Voronoi;

public class Main extends Application {
    public static Window window;
    @FXML
    public static Stage stage;

    boolean debug = true;

    @Override
    public void start(Stage primaryStage) {
        if (!debug) {
            stage = primaryStage;
            primaryStage.setTitle("Imagine");
            primaryStage.setMaximized(true);
            window = new Window();
            Scene scene = new Scene(window);
            primaryStage.setScene(scene);
            show();
        } else {
            BaseMapAbst map = new BaseMapAbst(Voronoi.Resolution.MEDIUM.width, Voronoi.Resolution.MEDIUM.height, null) {
            }.startGeneration();
            stage = primaryStage;
            ImageView viewer = new ImageView(map.image);
            AnchorPane pane = new AnchorPane();
            viewer.setX(pane.getWidth());
            viewer.setY(pane.getHeight());
            pane.getChildren().add(viewer);
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.show();
            //map.saveImage();
        }

    }

    void show() {
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
