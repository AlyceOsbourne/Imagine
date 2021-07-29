/*
 * © Owned by Alyce Kat Osbourne, AKA Alycrafticus, All Rights Reserved.
 */

package imagine.scenes.characterbio;

import com.google.gson.annotations.Expose;

public class CharacterBioData {
	@Expose private final String nickname;
	@Expose private final String title;
	@Expose private final String forename;
	@Expose private final String middlename;
	@Expose private final String surname;
	@Expose private final int age;
	@Expose private final String gender;
	@Expose private final String sex;
	@Expose private final String sexuality;
	@Expose private final String race;
	@Expose private final String bio;
	@Expose private final String alignment;
	@Expose private final String faction;




	CharacterBioData(String nickname,
	                 String title,
	                 String forename,
	                 String middlename,
	                 String surname,
	                 int age,
	                 String gender,
	                 String sex,
	                 String sexuality,
	                 String race,
	                 String bio,
	                 String alignment,
	                 String faction) {
		this.nickname = nickname;
		this.title = title;
		this.forename = forename;
		this.middlename = middlename;
		this.surname = surname;
		this.age = age;
		this.gender = gender;
		this.sex = sex;
		this.sexuality = sexuality;
		this.race = race;
		this.bio = bio;
		this.alignment = alignment;
		this.faction = faction;
	}
}
