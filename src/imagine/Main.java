/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine;

import imagine.scenes.core.Window;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        scene.setRoot(window);
        primaryStage.setScene(scene);
        show();
    }

    void show(){stage.show();}

    public static void main(String[] args) {
        launch(args);
    }


}
