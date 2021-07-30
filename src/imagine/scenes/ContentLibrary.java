/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes;

import imagine.scenes.characterbio.CreateCharacter;
import imagine.scenes.characterbio.CharacterScreen;
import imagine.scenes.core.Window;
import imagine.scenes.menus.MainMenu;
import javafx.scene.Parent;

public class ContentLibrary {

	public static final Window window = new Window();

	public enum Menus{
		MainMenu(new MainMenu());
		public Parent content;
		Menus(Parent parent){
			content = parent;
		}
	}

	public enum CharacterBiograpies{

		CharacterScreen(new CharacterScreen()),
		CreateCharacter(new CreateCharacter());

		public Parent content;
		CharacterBiograpies(Parent parent){
			content = parent;
		}
	}

}
