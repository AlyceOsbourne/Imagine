

package imagine.scenes.characterbio;

import imagine.Main;
import imagine.scenes.core.data.SaveData;
import imagine.scenes.characterbio.data.Character;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import lib.fxml.LoadsFXML;

public class CreateCharacter extends BorderPane implements LoadsFXML {

	FXMLLoader loader = loadFXML();
	Character character;


	@FXML
	private TextField race;
	@FXML
	private TextField weight;
	@FXML
	private TextField height;
	@FXML
	private TextField physique;
	@FXML
	private TextField skincolour;
	@FXML
	private TextField haircolour;
	@FXML
	private TextField eyecolour;
	@FXML
	private TextField disabilities;
	@FXML
	private TextField gait;
	@FXML
	private TextField voice;
	@FXML
	private TextField drives;
	@FXML
	private TextField alignment;
	@FXML
	private TextField fears;
	@FXML
	private TextField education;
	@FXML
	private TextField theology;
	@FXML
	private TextField openness;
	@FXML
	private TextField conscientiousness;
	@FXML
	private TextField extroversion;
	@FXML
	private TextField agreeableness;
	@FXML
	private TextField neuroticism;
	@FXML
	private TextField honor;
	@FXML
	private TextField honesty;
	@FXML
	private TextField generosity;
	@FXML
	private TextField kindness;
	@FXML
	private TextField intelligence;
	@FXML
	private TextArea likes;
	@FXML
	private TextArea dislikes;
	@FXML
	private TextArea mannerisms;
	@FXML
	private TextArea clothingstyle;
	@FXML
	private TextField hometown;
	@FXML
	private TextField homeregion;
	@FXML
	private TextField homecountry;
	@FXML
	private TextArea briefhistory;
	@FXML
	private TextArea longhistory;
	@FXML
	private TextField markings;
	@FXML
	private TextField title;
	@FXML
	private TextField forename;
	@FXML
	private TextField middlename;
	@FXML
	private TextField surname;
	@FXML
	private TextField gender;
	@FXML
	private TextField age;
	@FXML
	private TextField sex;
	@FXML
	private TextField sexuality;
	@FXML
	private TextField nickname;

	/**
	 * Load controls.
	 */
	@Override
	public void loadControls() {
		Main.window.getEditMenu().getItems().clear();
		Main.window.getEditMenu().setVisible(true);
		MenuItem saveChar = new MenuItem("Save Character and return to Character Biographies");
		saveChar.setOnAction(e -> {
			saveCharacter(e);
			Main.window.changeContent(new CharacterScreen());
		});
		Main.window.getEditMenu().getItems().add(saveChar);
	}




	private void saveCharacter(ActionEvent event) {
		String nick = nickname.getText();
		character = new Character(nick);

		{
			character.getInfo()
					.setTitle(title.getText())
					.setForename(forename.getText())
					.setMiddlename(middlename.getText())
					.setSurname(surname.getText())
					.setAge(age.getText())
					.setGender(gender.getText())
					.setSex(sex.getText())
					.setSexuality(sexuality.getText());
		}

		{
			character.getPhysical()
					.setHeight(height.getText())
					.setWeight(weight.getText())
					.setPhysique(physique.getText())
					.setHaircolour(haircolour.getText())
					.setEyecolour(eyecolour.getText())
					.setSkincolour(skincolour.getText())
					.setRace(race.getText())
					.setDisabilities(disabilities.getText())
					.setMarkings(markings.getText())
					.setGait(gait.getText())
					.setVoice(voice.getText());
		}

		{
			character.getPersonality()
					.setAgreebleness(agreeableness.getText())
					.setAlignment(alignment.getText())
					.setClothingstyle(clothingstyle.getText())
					.setConscientiousness(conscientiousness.getText())
					.setDislikes(dislikes.getText())
					.setDrive(drives.getText())
					.setEducation(education.getText())
					.setExtraversion(extroversion.getText())
					.setFear(fears.getText())
					.setGenerosity(generosity.getText())
					.setHonesty(honesty.getText())
					.setHonor(honor.getText())
					.setKindness(kindness.getText())
					.setMannerisms(mannerisms.getText())
					.setNeuroticism(neuroticism.getText())
					.setOpenness(openness.getText())
					.setLikes(likes.getText())
					.setTheology(theology.getText())
					.setIntelligence(intelligence.getText());

		}

		{
			character.getHistory()
					.setBriefhistory(briefhistory.getText())
					.setLonghistory(longhistory.getText())
					.setHometown(hometown.getText())
					.setHomeregion(homeregion.getText())
					.setHomecountry(homecountry.getText());
		}

		{
			//noinspection ResultOfMethodCallIgnored
			character.getRelationships();
			//todo fill
		}

		{
			//noinspection ResultOfMethodCallIgnored
			character.getSkills();
			//todo fill
		}

		SaveData.data.getCharacters().put(nick, character);
	}

	CreateCharacter loadCharacter(Character characterIn){
		character = characterIn;
		nickname.setText(character.getNickname());
		{
			Character.Info info = character.getInfo();
			title.setText(info.getTitle());
			forename.setText(info.getForename());
			middlename.setText(info.getMiddlename());
			surname.setText(info.getSurname());
			age.setText(info.getAge());
			gender.setText(info.getGender());
			sex.setText(info.getSex());
			sexuality.setText(info.getSexuality());
		}//character info
		{
			Character.Skills skills = character.getSkills();
			//todo fill
		}//character skills
		{
			Character.Relationships relationships = character.getRelationships();
			//todo fill
		}//character relationships
		{
			Character.Physical physical = character.getPhysical();
			race.setText(physical.getRace());
			weight.setText(physical.getWeight());
			height.setText(physical.getHeight());
			physique.setText(physical.getPhysique());
			skincolour.setText(physical.getSkincolour());
			eyecolour.setText(physical.getEyecolour());
			haircolour.setText(physical.getHaircolour());
			disabilities.setText(physical.getDisabilities());
			gait.setText(physical.getGait());
			markings.setText(physical.getMarkings());
			voice.setText(physical.getVoice());
		}//character physical
		{
			Character.History history = character.getHistory();
			hometown.setText(history.getHometown());
			homeregion.setText(history.getHomeregion());
			homecountry.setText(history.getHomecountry());
			longhistory.setText(history.getLonghistory());
			briefhistory.setText(history.getBriefhistory());
		}//character history
		{
			Character.Personality personality = character.getPersonality();
			//todo fill
			alignment.setText(personality.getAlignment());
			drives.setText(personality.getDrive());
			fears.setText(personality.getEducation());
			theology.setText(personality.getTheology());
			openness.setText(personality.getOpenness());
			conscientiousness.setText(personality.getConscientiousness());
			extroversion.setText(personality.getExtroversion());
			agreeableness.setText(personality.getAgreebleness());
			neuroticism.setText(personality.getNeuroticism());
			honor.setText(personality.getHonor());
			honesty.setText(personality.getHonesty());
			generosity.setText(personality.getGenerosity());
			kindness.setText(personality.getKindness());
			intelligence.setText(personality.getIntelligence());
			likes.setText(personality.getLikes());
			dislikes.setText(personality.getDislikes());
			mannerisms.setText(personality.getMannerisms());
			education.setText(personality.getEducation());
			clothingstyle.setText(personality.getClothingstyle());
		}//character personality
		return this;
	}
}
