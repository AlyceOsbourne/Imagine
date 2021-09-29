package lib.javafx;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.List;

import static java.lang.System.Logger;
import static java.lang.System.getLogger;

/**
 * a class designed to act as the core of a standardized app format
 **/
public abstract class App extends Application {


	static final Logger LOGGER = getLogger("AppLogger");
	static final FileChooser CHOOSER = new FileChooser();
	static final Clipboard CLIPBOARD = Clipboard.getSystemClipboard();
	public static Scene scene;
	public static Stage stage;
	public static boolean debug = false;
	String title = title();
	Window window;

	protected abstract String title();

	@Override
	public final void start(Stage primaryStage) {
		stage = primaryStage;
		changeWindow(startingWindow());
		stage.setScene(scene);
		stage.setTitle(title);
		stage.setMaximized(true);
		stage.show();
	}

	void changeWindow(Window window) {
		if (window == null) this.window = new WindowNotFound();
		else this.window = window;
		if (scene == null) scene = new Scene(this.window);
		else scene.setRoot(this.window);
	}

	protected abstract Window startingWindow();

	static class WindowNotFound extends Window {
		@Override
		protected List<MenuItem> fileMenu() {
			return null;
		}

		@Override
		protected MenuItem newFile() {
			return null;
		}

		@Override
		protected MenuItem saveFile() {
			return null;
		}

		@Override
		protected MenuItem loadFile() {
			return null;
		}

		@Override
		protected List<MenuItem> editMenu() {
			return null;
		}

		@Override
		protected MenuItem undo() {
			return null;
		}

		@Override
		protected MenuItem redo() {
			return null;
		}

		@Override
		protected MenuItem copy() {
			return null;
		}

		@Override
		protected MenuItem cut() {
			return null;
		}

		@Override
		protected MenuItem paste() {
			return null;
		}

		@Override
		protected List<MenuItem> toolsMenu() {
			return null;
		}

		@Override
		protected List<MenuItem> helpMenu() {
			return null;
		}

		@Override
		protected MenuItem help() {
			return null;
		}

		@Override
		protected List<MenuItem> aboutMenu() {
			return null;
		}

		@Override
		protected MenuItem aboutTheDev() {
			return null;
		}

		@Override
		protected Node content() {
			TextArea output = new TextArea("Window not found.");
			output.setEditable(false);
			output.autosize();
			return output;
		}

		@Override
		protected Node rightBar() {
			return null;
		}

		@Override
		protected Node leftBar() {
			return null;
		}

		@Override
		protected Node bottomBar() {
			return null;
		}
	}
}

