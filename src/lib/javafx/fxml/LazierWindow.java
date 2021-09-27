/*
 * Do what the F**k you want
 */

package lib.javafx.fxml;

import javafx.scene.Node;

/**
 * will essentially be a Window Factory,
 * should allow end user to throw a fully
 * functioning window together in minutes
 **/

public abstract class LazierWindow extends LazyWindow {
	public LazierWindow withContent(Node content) {
		if (content instanceof LoadsFXML) ((LoadsFXML) content).loadFXML();
		setCenter(content);
		return this;
	}

	LazierWindow withLeftBar(Node content) {
		if (content instanceof LoadsFXML) ((LoadsFXML) content).loadFXML();
		setLeft(content);
		return this;
	}

	LazierWindow witRightBar(Node content) {
		if (content instanceof LoadsFXML) ((LoadsFXML) content).loadFXML();
		setRight(content);
		return this;
	}

	LazierWindow withBottomBar(Node content) {
		if (content instanceof LoadsFXML) ((LoadsFXML) content).loadFXML();
		setBottom(content);
		return this;
	}

}
