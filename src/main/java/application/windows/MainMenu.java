/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package application.windows;

import application.Imagine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lib.javafx.App;
import lib.javafx.windows.LazyWindow;

import java.io.IOException;
import java.util.Objects;

public class MainMenu extends LazyWindow {
	@Override
	protected Node content() throws IOException {
		VBox buttonBox = new VBox();
		Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("title.png")));
		ImageView title = new ImageView(image);
		title.setPreserveRatio(true);
		int box = 600;
		title.setFitWidth(box);
		title.setFitHeight(box);
		buttonBox.setPadding(new Insets(100.0D));
		buttonBox.setSpacing(30);
		buttonBox.setAlignment(Pos.CENTER);
		Button atlas, characters, encyclopaedia, options, quit;

		atlas = new Button("World Atlas");
		atlas.setOnAction(event -> Imagine.scene.setRoot(new Atlas()));
		atlas.setMaxSize(600, 600);
		atlas.setFont(Font.font(25));

		characters = new Button("Character Dossiers");
		characters.setOnAction(event -> Imagine.scene.setRoot(new Dossier()));
		characters.setMaxSize(600, 600);
		characters.setFont(Font.font(25));

		encyclopaedia = new Button("Encyclopaedia");
		encyclopaedia.setOnAction(event -> Imagine.scene.setRoot(new Encyclopaedia()));
		encyclopaedia.setMaxSize(600, 600);
		encyclopaedia.setFont(Font.font(25));

		options = new Button("Options");
		options.setOnAction(event -> Imagine.scene.setRoot(new Encyclopaedia()));
		options.setMaxSize(600, 600);
		options.setFont(Font.font(25));

		quit = new Button("Quit");
		quit.setOnAction(event -> App.stage.close());
		quit.setMaxSize(600, 600);
		quit.setFont(Font.font(25));


		buttonBox.getChildren().addAll(title, atlas, characters, encyclopaedia, options, quit);
		buttonBox.setFillWidth(true);
		buttonBox.setAlignment(Pos.CENTER);

		App.stage.setMaximized(false);
		return buttonBox;
	}
}
