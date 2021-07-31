/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.characterbio.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Character {
	@NotNull
	String nickname;

	@Nullable
	private final Info info;
	@Nullable
	private final Physical physical;
	@Nullable
	private final Personality personality;
	@Nullable
	private final History history;
	@Nullable
	private final Relationships relationships;
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


		@Nullable
		Integer age;

		@Nullable
		String title,
		forename,
		middlename,
		surname,
		gender,
		sex,
		sexuality;



	}

	class Physical{

		@Nullable
		String weight,
		height,
		physique,
		haircolour,
		skincolour,
		eyecolour,
		race,
		disabilities,
		markings,
		gait,
		voice;
	}

	class Personality{

		@Nullable
		String alignment,
		drive,
		fear,
		education,
		openness,
		conscientiousness,
		extraversion,
		agreebleness,
		neuroticism;

		@Nullable
		String theology,
		honor,
		honesty,
		generosity,
		kindness,
		pleasures,
		dislikes,
		clothingstyle,
		mannerisms;
	}

	class History{

		@Nullable
		String placeofbirth,
		homecountry,
		homeregion,
		hometown,
		briefhistory,
		longhistory,
		fondmemories,
		aversememories,
		definingmoments;
	}

	class Relationships{

		@Nullable
		String clan,
		faction;

		@Nullable
		List<Character> family;

		@Nullable
		List<Character> friends;

		@Nullable
		List<Character> enemies;

		@Nullable
		List<Character> allies;

		@Nullable
		List<Character> lovers;
	}

	class Skills{

		@Nullable
		String majorclass,
		minorclass,
		physicalskills,
		mysticalskills,
		dexterityskills,
		thinkingskills;
	}


}
