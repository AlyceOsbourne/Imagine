/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package application;

import application.windows.MainMenu;
import lib.javafx.App;
import lib.javafx.Window;

public class Imagine extends App {
	static {
		debug = false;
	}

	@Override
	protected String title() {
		return null;
	}

	@Override
	protected Window startingWindow() {
		if (!debug) return new MainMenu();
		else {
			return null;
		}
	}


}
