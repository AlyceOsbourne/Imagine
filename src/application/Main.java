/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package application;

import application.windows.MainMenu;
import javafx.scene.image.ImageView;
import lib.javafx.App;
import lib.javafx.Window;
import lib.javafx.fxml.LazierWindow;
import lib.math.noise.Noise;

public class Main extends App {
	@Override
	protected String title() {
		return null;
	}

	@Override
	protected Window startingWindow() {
		if (debug) {
			Noise n = new Noise(1000, 700, 1, 1, 1, 1, 100, 0, 0);
			ImageView view = new ImageView(n.i);
			view.setFitHeight(stage.getHeight());
			view.setFitWidth(stage.getWidth());
			return new LazierWindow() {
			}.withContent(view);
		} else return new MainMenu();

	}


}
