
/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package lib.fxml;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Fxml loader interface for, can be implemented, used as functional interface.
 * I usually implement and then call FXMLLoader loader = loadFXML();
 * this gives a constructed loader representative of the implementing class.
 */
public interface LoadsFXML {

	/**
	 * this is the controller that will be returned by this interface, if you choose to return it to anything.
	 */
	FXMLLoader loader = new FXMLLoader();

	/**
	 * this is the basic loader method, will load an FXML file that is in the same location as the controller that shares the same name Example
	 * Window.class = Window.fxml.
	 * can be returned or just called as it handles the loading of the class, assigning the root and then the actual FXMLLoader.load() method.
	 */
	@FXML
	default FXMLLoader loadFXML(){
		setControllerAndRoot();
		setLocation(null);
		loadController();
		return loader;
	}
	/**
	 * this is the secondary loader method, allows you to point towards a specific FXML file,
	 * this can be used to either have things like a FXML folder or have a FXML file with multiple controllers.
	 * can be returned or just called as it handles the loading of the class, assigning the root and then the actual FXMLLoader.load() method.
	*/
	default FXMLLoader loadFXML(String fxmlLocation){
		setControllerAndRoot();
		setLocation(fxmlLocation);
		loadController();
		return loader;
	}


	/**
	 * method that runs FXMLLoader.load(), then calls loadControls().
	 * acts as the classes' exception handler too
	 */
	default void loadController(){
		try {
			loader.load();
			loadControls();
			System.out.print(this.getClass().getSimpleName() + " Successfully loaded.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the controller and root to the implementing class.
	 */
	default void setControllerAndRoot(){
		loader.setController(this);
		loader.setRoot(this);
	}

	/**
	 * this is the method that sets the location of the FXML file. if it receives a null value it will set the location to the classes' location,
	 * else it returns the file located at the location provided
	 */
	default void setLocation(@Nullable String location){
		loader.setLocation(this.getClass().getResource(Objects.requireNonNullElseGet(location, () -> this.getClass().getSimpleName() + ".fxml")));
	}

	void loadControls() throws Exception;
}
