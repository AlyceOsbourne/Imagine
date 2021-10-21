package application.data;


public class Character {

	Personality personality;
	Physicality physicality;
	Aesthetics aesthetics;

	Character() {
	}

}

class Personality {
	static class Extraversion {
		double friendliness, gregariousness, assertiveness, activityLevel, excitementSeeking, cheerfulness;

		Extraversion() {
		}
	}

	static class Agreeableness {
		double trust, morality, altruism, cooperation, modesty, sympathy;

		Agreeableness() {
		}
	}

	static class Conscientiousness {
		double selfEfficacy, orderliness, dutifulness, achievementStriving, selfDiscipline, cautiousness;

		Conscientiousness() {
		}
	}

	static class Neuroticism {
		double anxiety, anger, depression, self_consciousness, immoderation, vulnerability;

		Neuroticism() {
		}
	}

	static class Openness {
		double imagination, artistic_interests, emotionality, adventurousness, intellect, liberalism;

		Openness() {
		}
	}
}

class Physicality {

	//currently only includes standard human traits, will include more fantasy traits as requested

	double height, weight;

	EyeColour eyeColour;
	HairColour hairColour;
	SkinColour skinColour;

	enum EyeColour {
		Amber,

		Aquamarine,

		ArcticBlue,

		AshGray,

		Azure,

		Brandy,

		Cerulean,

		Chestnut,

		ChocolateBrown,

		Chrome,

		Coal,

		CocoaBrown,

		CoffeeBrown,

		Cognac,

		ConcreteGray,

		Copper,

		CornflowerBlue,

		CrystalBlue,

		DenimBlue,

		DoveGray,

		ElectricBlue,

		Emerald,

		FogGray,

		GlacialBlue,

		GunmetalGray,

		Hazel,

		Honey,

		IceBlue,

		LakeBlue,

		LeafGreen,

		Mahogany,

		Midnight,

		MinkBrown,

		Mocha,

		MossGreen,

		Obsidian,

		Olive,

		Onyx,

		Pewter,

		Platinum,

		Raven,

		Sepia,

		SharkGray,

		SiennaBrown,

		Silver,

		SkyBlue,

		SlateBlue,

		SmokyGray,

		SteelBlue,

		StormGray,

		SunnyBlue,

		Tawny,
		Topaz,

		Turquoise,

		Whiskey

	}

	enum HairColour {
		AshBrown,

		Auburn,

		Black,

		Bleached,

		Blonde,

		BlueBlack,

		Brown,

		Brunette,

		Butterscotch,

		Caramel,

		CharcoalGray,

		ChocolateBrown,

		CoffeeBrown,

		Copper,

		Ebony,

		FairHaired,

		Flaxen,

		Ginger,

		Golden,

		Honey,

		InkyBlack,

		JetBlack,

		Midnight,

		NutBrown,

		Platinum,

		Raven,

		Red,

		Sable,

		SaltAndPepper,

		SandyBlond,

		Silver,

		SnowWhite,

		SteelGray,

		StrawberryBlonde,

		TawnyBrown,

		TitianHaired,

		ToffeeBrown,

		Wheat,

		White

	}

	enum SkinColour {
		Alabaster,

		Amber,

		Bisque,

		Bronze,

		Chalky,

		Cinnamon,

		Copper,

		Cream,

		DarkBrown,

		DeepBrown,

		Ebony,

		Fair,

		Fawn,

		Florid,

		Golden,

		Honey,

		Ivory,

		Light,

		Milk,

		Olive,

		Onyx,

		Pale,

		Pallid,

		Pasty,

		Peach,

		Porcelain,

		Rose,

		Ruddy,

		Russet,

		Sallow,
		Tawny

	}

}

class Aesthetics {
}

class Relationships {
	class Relationship {
		String name;

		enum RelationshipType {
			Grandparent,
			Parent,
			Guardian,
			Child,
			Sibling,
			ExtendedFamily,
			InLaw,
			Clan,
			Friend,
			Lover,
			Enemy,
			Rival,
			Confidant,
			Advisor,
			Kin,
			GuildPeer,
			Peer,
			Neighbour

		}
	}
}