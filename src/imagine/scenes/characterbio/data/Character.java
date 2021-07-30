/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.characterbio.data;

import com.google.gson.annotations.Expose;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Character {
	@NotNull
	String nickname;

	@Expose
	@Nullable
	private final Info info;
	@Expose
	@Nullable
	private final Physical physical;
	@Expose
	@Nullable
	private final Personality personality;
	@Expose
	@Nullable
	private final History history;
	@Expose
	@Nullable
	private final Relationships relationships;
	@Expose
	@Nullable
	private final Skills skills;

	Character(String nickname, Info info, Physical physical, Personality personality, History history, Relationships relationships, Skills skills){
		this.nickname = nickname;
		this.info = info;
		this.physical = physical;
		this.personality = personality;
		this.history = history;
		this.relationships = relationships;
		this.skills = skills;
	}

	class Info{

		@Expose
		@Nullable
		Integer age;
		@Expose
		@Nullable
		String title,forename,middlename,surname,gender,sex,sexuality;



	}
	class Physical{
		@Expose
		@Nullable
		String weight,height,musclature,haircolour,skincolour,eyecolour,race,disabilities,markings,gait,voice;
	}

	class Personality{
		@Expose
		@Nullable
		String alignment,drive,fear,education,openness,conscientiousness,extraversion,agreebleness,neuroticism;
		@Expose
		@Nullable
		String theology,honor,honesty,generosity,kindness,pleasures,dislikes,clothingstyle,mannerisms;
	}
	class History{
		@Expose
		@Nullable
		String placeofbirth,homecountry,homeregion,hometown,briefhistory,longhistory,fondmemories,aversememories,definingmoments;
	}
	class Relationships{
		@Expose
		@Nullable
		String clan,faction;
		@Expose
		@Nullable
		List<Character> family;
		@Expose
		@Nullable
		List<Character> friends;
		@Expose
		@Nullable
		List<Character> enemies;
		@Expose
		@Nullable
		List<Character> allies;
		@Expose
		@Nullable
		List<Character> lovers;
	}
	class Skills{
		@Expose
		@Nullable
		String majorclass,minorclass,physicalskills,mysticalskills,dexterityskills,thinkingskills;
	}


}
