

package imagine.scenes.characterbio.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Character {

	public Info getInfo() {
		return info;
	}

	public Physical getPhysical() {
		return physical;
	}

	public Personality getPersonality() {
		return personality;
	}

	public History getHistory() {
		return history;
	}

	public Relationships getRelationships() {
		return relationships;
	}

	public Skills getSkills() {
		return skills;
	}

	private final Info info = new Info();
	private final Physical physical = new Physical();
	private final Personality personality = new Personality();
	private final History history = new History();
	private final Relationships relationships = new Relationships();
	private final Skills skills = new Skills();

	public @NotNull String getNickname() {
		return nickname;
	}

	@NotNull
	String nickname;

	public Character(@NotNull String nickname) {
		this.nickname = nickname;
	}

	public @NotNull Character setNickname(@NotNull String nickname) {
		this.nickname = nickname;
		return this;
	}

	public static class Info {


		@Nullable
		String title,
				forename,
				middlename,
				surname,
				gender,
				sex,
				sexuality,

				age;

		public @Nullable String getAge() {
			return age;
		}

		public @NotNull Info setAge(String age) {
			this.age = age;
			return this;
		}

		public @Nullable String getTitle() {
			return title;
		}

		public @NotNull Info setTitle(String title) {
			this.title = title;
			return this;
		}

		public @Nullable String getForename() {
			return forename;
		}

		public @NotNull Info setForename(String forename) {
			this.forename = forename;
			return this;
		}

		public @Nullable String getMiddlename() {
			return middlename;
		}

		public @NotNull Info setMiddlename(String middlename) {
			this.middlename = middlename;
			return this;
		}

		public @Nullable String getSurname() {
			return surname;
		}

		public @NotNull Info setSurname(String surname) {
			this.surname = surname;
			return this;
		}

		public @Nullable String getGender() {
			return gender;
		}

		public @NotNull Info setGender(String gender) {
			this.gender = gender;
			return this;
		}

		public @Nullable String getSex() {
			return sex;
		}

		public @NotNull Info setSex(String sex) {
			this.sex = sex;
			return this;
		}

		public @Nullable String getSexuality() {
			return sexuality;
		}

		public void setSexuality(@Nullable String sexuality) {
			this.sexuality = sexuality;
		}
	}

	public static class Physical {

		public @Nullable String getWeight() {
			return weight;
		}

		public Physical setWeight(String weight) {
			this.weight = weight;
			return this;
		}

		public @Nullable String getHeight() {
			return height;
		}

		public Physical setHeight(String height) {
			this.height = height;
			return this;
		}

		public @Nullable String getPhysique() {
			return physique;
		}

		public Physical setPhysique(String physique) {
			this.physique = physique;
			return this;
		}

		public @Nullable String getHaircolour() {
			return haircolour;
		}

		public Physical setHaircolour(String haircolour) {
			this.haircolour = haircolour;
			return this;
		}

		public @Nullable String getSkincolour() {
			return skincolour;
		}

		public Physical setSkincolour(String skincolour) {
			this.skincolour = skincolour;
			return this;
		}

		public @Nullable String getEyecolour() {
			return eyecolour;
		}

		public Physical setEyecolour(String eyecolour) {
			this.eyecolour = eyecolour;
			return this;
		}

		public @Nullable String getRace() {
			return race;
		}

		public Physical setRace(String race) {
			this.race = race;
			return this;
		}

		public @Nullable String getDisabilities() {
			return disabilities;
		}

		public Physical setDisabilities(String disabilities) {
			this.disabilities = disabilities;
			return this;
		}

		public @Nullable String getMarkings() {
			return markings;
		}

		public Physical setMarkings(String markings) {
			this.markings = markings;
			return this;
		}

		public @Nullable String getGait() {
			return gait;
		}

		public Physical setGait(String gait) {
			this.gait = gait;
			return this;
		}

		public @Nullable String getVoice() {
			return voice;
		}

		public void setVoice(@Nullable String voice) {
			this.voice = voice;
		}

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

	public static class Personality {
		@Nullable
		String alignment, //
				drive, //
				fear, //
				education, //
				openness, //
				conscientiousness, //
				extroversion, //
				agreebleness, //
				neuroticism; //
		@Nullable
		String theology, //
				honor, //
				honesty, //
				generosity, //
				kindness, //
				pleasures,
				dislikes,
				clothingstyle,
				mannerisms,
				intelligence;

		public @Nullable String getAlignment() {
			return alignment;
		}

		public @NotNull Personality setAlignment(String alignment) {
			this.alignment = alignment;
			return this;
		}

		public @Nullable String getDrive() {
			return drive;
		}

		public @NotNull Personality setDrive(String drive) {
			this.drive = drive;
			return this;
		}

		public @NotNull Personality setFear(String fear) {
			this.fear = fear;
			return this;
		}

		public @Nullable String getEducation() {
			return education;
		}

		public @NotNull Personality setEducation(String education) {
			this.education = education;
			return this;
		}

		public @Nullable String getOpenness() {
			return openness;
		}

		public @NotNull Personality setOpenness(String openness) {
			this.openness = openness;
			return this;
		}

		public @Nullable String getConscientiousness() {
			return conscientiousness;
		}

		public @NotNull Personality setConscientiousness(String conscientiousness) {
			this.conscientiousness = conscientiousness;
			return this;
		}

		public @Nullable String getExtroversion() {
			return extroversion;
		}

		public @NotNull Personality setExtraversion(String extraversion) {
			this.extroversion = extraversion;
			return this;
		}

		public @Nullable String getAgreebleness() {
			return agreebleness;
		}

		public @NotNull Personality setAgreebleness(String agreebleness) {
			this.agreebleness = agreebleness;
			return this;
		}

		public @Nullable String getNeuroticism() {
			return neuroticism;
		}

		public @NotNull Personality setNeuroticism(String neuroticism) {
			this.neuroticism = neuroticism;
			return this;
		}

		public @Nullable String getTheology() {
			return theology;
		}

		public @NotNull Personality setTheology(String theology) {
			this.theology = theology;
			return this;
		}

		public @Nullable String getHonor() {
			return honor;
		}

		public @NotNull Personality setHonor(String honor) {
			this.honor = honor;
			return this;
		}

		public @Nullable String getHonesty() {
			return honesty;
		}

		public @NotNull Personality setHonesty(String honesty) {
			this.honesty = honesty;
			return this;
		}

		public @Nullable String getGenerosity() {
			return generosity;
		}

		public @NotNull Personality setGenerosity(String generosity) {
			this.generosity = generosity;
			return this;
		}

		public @Nullable String getKindness() {
			return kindness;
		}

		public @NotNull Personality setKindness(String kindness) {
			this.kindness = kindness;
			return this;
		}

		public @Nullable String getLikes() {
			return pleasures;
		}

		public @NotNull Personality setLikes(String pleasures) {
			this.pleasures = pleasures;
			return this;
		}

		public @Nullable String getDislikes() {
			return dislikes;
		}

		public @NotNull Personality setDislikes(String dislikes) {
			this.dislikes = dislikes;
			return this;
		}

		public @Nullable String getClothingstyle() {
			return clothingstyle;
		}

		public @NotNull Personality setClothingstyle(String clothingstyle) {
			this.clothingstyle = clothingstyle;
			return this;
		}

		public @Nullable String getMannerisms() {
			return mannerisms;
		}

		public @NotNull Personality setMannerisms(String mannerisms) {
			this.mannerisms = mannerisms;
			return this;
		}

		public @Nullable String getIntelligence() {
			return intelligence;
		}

		public void setIntelligence(@Nullable String intelligence){
			this.intelligence = intelligence;
		}
	}

	public static class History {
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

		public @Nullable String getHomecountry() {
			return homecountry;
		}

		public void setHomecountry(@Nullable String homecountry) {
			this.homecountry = homecountry;
		}

		public @Nullable String getHomeregion() {
			return homeregion;
		}

		public @NotNull History setHomeregion(String homeregion) {
			this.homeregion = homeregion;
			return this;
		}

		public @Nullable String getHometown() {
			return hometown;
		}

		public @NotNull History setHometown(String hometown) {
			this.hometown = hometown;
			return this;
		}

		public @Nullable String getBriefhistory() {
			return briefhistory;
		}

		public @NotNull History setBriefhistory(String briefhistory) {
			this.briefhistory = briefhistory;
			return this;
		}

		public @Nullable String getLonghistory() {
			return longhistory;
		}

		public @NotNull History setLonghistory(String longhistory) {
			this.longhistory = longhistory;
			return this;
		}

	}

	public static class Relationships {
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

		public @Nullable String getClan() {
			return clan;
		}

		public @NotNull Relationships setClan(String clan) {
			this.clan = clan;
			return this;
		}

		public @Nullable String getFaction() {
			return faction;
		}

		public @NotNull Relationships setFaction(String faction) {
			this.faction = faction;
			return this;
		}

		public @Nullable List<Character> getFamily() {
			return family;
		}

		public @NotNull Relationships setFamily(List<Character> family) {
			this.family = family;
			return this;
		}

		public @Nullable List<Character> getFriends() {
			return friends;
		}

		public @NotNull Relationships setFriends(List<Character> friends) {
			this.friends = friends;
			return this;
		}

		public @Nullable List<Character> getEnemies() {
			return enemies;
		}

		public @NotNull Relationships setEnemies(List<Character> enemies) {
			this.enemies = enemies;
			return this;
		}

		public @Nullable List<Character> getAllies() {
			return allies;
		}

		public @NotNull Relationships setAllies(List<Character> allies) {
			this.allies = allies;
			return this;
		}

		public @Nullable List<Character> getLovers() {
			return lovers;
		}

		public @NotNull Relationships setLovers(List<Character> lovers) {
			this.lovers = lovers;
			return this;
		}
	}

	public static class Skills {

		@Nullable
		String majorclass,
				minorclass,
				physicalskills,
				mysticalskills,
				dexterityskills,
				thinkingskills;

		public @Nullable String getMajorclass() {
			return majorclass;
		}

		public @NotNull Skills setMajorclass(String majorclass) {
			this.majorclass = majorclass;
			return this;
		}

		public @Nullable String getMinorclass() {
			return minorclass;
		}

		public @NotNull Skills setMinorclass(String minorclass) {
			this.minorclass = minorclass;
			return this;
		}

		public @Nullable String getPhysicalskills() {
			return physicalskills;
		}

		public @NotNull Skills setPhysicalskills(String physicalskills) {
			this.physicalskills = physicalskills;
			return this;
		}

		public @Nullable String getMysticalskills() {
			return mysticalskills;
		}

		public @NotNull Skills setMysticalskills(String mysticalskills) {
			this.mysticalskills = mysticalskills;
			return this;
		}

		public @Nullable String getDexterityskills() {
			return dexterityskills;
		}

		public @NotNull Skills setDexterityskills(String dexterityskills) {
			this.dexterityskills = dexterityskills;
			return this;
		}

		public @Nullable String getThinkingskills() {
			return thinkingskills;
		}

		public @NotNull Skills setThinkingskills(String thinkingskills) {
			this.thinkingskills = thinkingskills;
			return this;
		}
	}


}
