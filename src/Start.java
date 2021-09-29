import application.Main;
import javafx.application.Application;
import lib.javafx.App;

public class Start {
	static Class<? extends App> app = Main.class;

	public static void main(String[] args) {
		Application.launch(app, args);
	}
}

