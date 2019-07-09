package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.boss1.Boss1Parts;
import com.mygdx.game.entity.character.AssassinParts;
import com.mygdx.game.entity.character.TankParts;
import com.mygdx.game.entity.rock.RockParts;
import com.mygdx.game.entity.shuriken.ShurikenParts;

import java.util.HashMap;

import static com.mygdx.game.assets.Assets.TextureName.BACKGROUND;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_0;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_1;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_2;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_3;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_4;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_5;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_BLOCK;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_CLEANSE;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_DASH;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_FORTRESS;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_IMPALE;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_SHURIKEN_THROW;
import static com.mygdx.game.assets.Assets.TextureName.FLOOR;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_ASSASSIN;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_BACKGROUND;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_BOSS;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_TANK;
import static com.mygdx.game.assets.Assets.TextureName.STUNNED;
import static com.mygdx.game.assets.Assets.TextureName.WEAK_SPOT;

public class Assets {
	private AssetManager assetManager;

	private HashMap<TextureName, TextureAsset> textures;
	private HashMap<TankAnimationName, AnimationAsset<TankParts>> tankAnimations;
	private HashMap<AssassinAnimationName, AnimationAsset<AssassinParts>> assassinAnimations;
	private HashMap<Boss1AnimationName, AnimationAsset<Boss1Parts>> boss1Animations;
	private HashMap<ShurikenAnimationName, AnimationAsset<ShurikenParts>> shurikenAnimations;
	private HashMap<RockAnimationName, AnimationAsset<RockParts>> rockAnimations;

	public static final HashMap<String, TankParts> TANK_PARTS;
	public static final String TANK_STANDING_PATH = "Entity/Tank/Standing";
	public static final String TANK_WALKING_PATH = "Entity/Tank/Walking";
	public static final String TANK_BLOCK_PATH = "Entity/Tank/Block";
	public static final String TANK_IMPALE_PATH = "Entity/Tank/Impale";
	public static final String TANK_FORTRESS_PATH = "Entity/Tank/Fortress";
	public static final String TANK_FORTRESS_STANDING_PATH = "Entity/Tank/Fortress Standing";
	public static final String TANK_FORTRESS_WALKING_PATH = "Entity/Tank/Fortress Walking";
	public static final String TANK_FORTRESS_BLOCK_PATH = "Entity/Tank/Fortress Block";
	public static final String TANK_FORTRESS_IMAPLE_PATH = "Entity/Tank/Fortress Impale";

	public static final HashMap<String, AssassinParts> ASSASSIN_PARTS;
	public static final String ASSASSIN_STANDING_PATH = "Entity/Assassin/Standing";
	public static final String ASSASSIN_WALKING_PATH = "Entity/Assassin/Walking";
	public static final String ASSASSIN_DASH_PATH = "Entity/Assassin/Dash";
	public static final String ASSASSIN_SHURIKEN_THROW_PATH = "Entity/Assassin/Shuriken Throw";

	public static final HashMap<String, Boss1Parts> BOSS1_PARTS;
	public static final String BOSS1_STANDING_PATH = "Entity/Boss1/Standing";
	public static final String BOSS1_GROUND_SMASH_PATH = "Entity/Boss1/Ground Smash";
	public static final String BOSS1_EARTHQUAKE_PATH = "Entity/Boss1/Earthquake";
	public static final String BOSS1_ROLL_PATH = "Entity/Boss1/Roll";

	public static final HashMap<String, ShurikenParts> SHURIKEN_PARTS;
	public static final String SHURIKEN_FLYING_PATH = "Entity/Shuriken";

	public static final HashMap<String, RockParts> ROCK_PARTS;
	public static final String ROCK_ERUPT_PATH = "Entity/Rock";

	static {
		TANK_PARTS = new HashMap<>();
		TANK_PARTS.put("Body", TankParts.BODY);
		TANK_PARTS.put("Left Arm", TankParts.LEFT_ARM);
		TANK_PARTS.put("Left Leg", TankParts.LEFT_LEG);
		TANK_PARTS.put("Right Arm", TankParts.RIGHT_ARM);
		TANK_PARTS.put("Right Leg", TankParts.RIGHT_LEG);
		TANK_PARTS.put("Shield", TankParts.SHIELD);
		TANK_PARTS.put("Weapon", TankParts.WEAPON);

		ASSASSIN_PARTS = new HashMap<>();
		ASSASSIN_PARTS.put("Body", AssassinParts.BODY);
		ASSASSIN_PARTS.put("Left Arm", AssassinParts.LEFT_ARM);
		ASSASSIN_PARTS.put("Left Leg", AssassinParts.LEFT_LEG);
		ASSASSIN_PARTS.put("Right Arm", AssassinParts.RIGHT_ARM);
		ASSASSIN_PARTS.put("Right Leg", AssassinParts.RIGHT_LEG);

		BOSS1_PARTS = new HashMap<>();
		BOSS1_PARTS.put("Body", Boss1Parts.BODY);
		BOSS1_PARTS.put("Left Arm", Boss1Parts.LEFT_ARM);
		BOSS1_PARTS.put("Left Leg", Boss1Parts.LEFT_LEG);
		BOSS1_PARTS.put("Right Arm", Boss1Parts.RIGHT_ARM);
		BOSS1_PARTS.put("Right Leg", Boss1Parts.RIGHT_LEG);
		BOSS1_PARTS.put("Shockwave", Boss1Parts.SHOCKWAVE);

		SHURIKEN_PARTS = new HashMap<>();
		SHURIKEN_PARTS.put("Body", ShurikenParts.BODY);

		ROCK_PARTS = new HashMap<>();
		ROCK_PARTS.put("Body", RockParts.BODY);
	}

	public enum TextureName {
		BACKGROUND, FLOOR, STUNNED, WEAK_SPOT,
		HEALTH_BAR_BACKGROUND, HEALTH_BAR_ASSASSIN, HEALTH_BAR_BOSS, HEALTH_BAR_TANK,
		COOLDOWN_0, COOLDOWN_1, COOLDOWN_2, COOLDOWN_3, COOLDOWN_4, COOLDOWN_5,
		COOLDOWN_BLOCK, COOLDOWN_IMPALE, COOLDOWN_FORTRESS,
		COOLDOWN_DASH, COOLDOWN_SHURIKEN_THROW, COOLDOWN_CLEANSE
	}

	private class TextureAsset {
		private String path;
		private Texture texture;

		private TextureAsset(String path) {
			this.path = path;
		}
	}

	public enum TankAnimationName {
		STANDING, WALKING, BLOCK, IMPALE,
		FORTRESS, FORTRESS_STANDING, FORTRESS_WALKING, FORTRESS_BLOCK, FORTRESS_IMPALE
	}

	public enum AssassinAnimationName {
		STANDING, WALKING, DASH, SHURIKEN_THROW, CLEANSE
	}

	public enum Boss1AnimationName {
		STANDING, WALKING, GROUND_SMASH, EARTHQUAKE, ROLL
	}

	public enum ShurikenAnimationName {
		FLYING
	}

	public enum RockAnimationName {
		ERUPT
	}

	private class AnimationAsset<P extends Enum> {
		private String path;
		private Animation<P> animation;

		private AnimationAsset(String path) {
			this.path = path;
		}
	}

	public Assets() {
		assetManager = new AssetManager();
		textures = new HashMap<>();
		tankAnimations = new HashMap<>();
		assassinAnimations = new HashMap<>();
		boss1Animations = new HashMap<>();
		shurikenAnimations = new HashMap<>();
		rockAnimations = new HashMap<>();

		defineTexture(BACKGROUND, "Background.png");
		defineTexture(FLOOR, "Floor.png");
		defineTexture(STUNNED, "Stunned.png");
		defineTexture(WEAK_SPOT, "Weak Spot.png");

		defineTexture(HEALTH_BAR_BACKGROUND, "HealthBar/Background.png");
		defineTexture(HEALTH_BAR_ASSASSIN, "HealthBar/AssassinBar.png");
		defineTexture(HEALTH_BAR_BOSS, "HealthBar/BossBar.png");
		defineTexture(HEALTH_BAR_TANK, "HealthBar/TankBar.png");

		defineTexture(COOLDOWN_0, "Cooldowns/0.png");
		defineTexture(COOLDOWN_1, "Cooldowns/1.png");
		defineTexture(COOLDOWN_2, "Cooldowns/2.png");
		defineTexture(COOLDOWN_3, "Cooldowns/3.png");
		defineTexture(COOLDOWN_4, "Cooldowns/4.png");
		defineTexture(COOLDOWN_5, "Cooldowns/5.png");

		defineTexture(COOLDOWN_BLOCK, "Cooldowns/Block Ability.png");
		defineTexture(COOLDOWN_IMPALE, "Cooldowns/Impale Ability.png");
		defineTexture(COOLDOWN_FORTRESS, "Cooldowns/Fortress Ability.png");
		defineTexture(COOLDOWN_DASH, "Cooldowns/Dash Ability.png");
		defineTexture(COOLDOWN_SHURIKEN_THROW, "Cooldowns/Shuriken Ability.png");
		defineTexture(COOLDOWN_CLEANSE, "Cooldowns/Cleanse Ability.png");

		defineTankAnimation(TankAnimationName.STANDING, TANK_STANDING_PATH);
		defineTankAnimation(TankAnimationName.WALKING, TANK_WALKING_PATH);
		defineTankAnimation(TankAnimationName.BLOCK, TANK_BLOCK_PATH);
		defineTankAnimation(TankAnimationName.IMPALE, TANK_IMPALE_PATH);
		defineTankAnimation(TankAnimationName.FORTRESS, TANK_FORTRESS_PATH);
		defineTankAnimation(TankAnimationName.FORTRESS_STANDING, TANK_FORTRESS_STANDING_PATH);
		defineTankAnimation(TankAnimationName.FORTRESS_WALKING, TANK_FORTRESS_WALKING_PATH);
		defineTankAnimation(TankAnimationName.FORTRESS_BLOCK, TANK_FORTRESS_BLOCK_PATH);
		defineTankAnimation(TankAnimationName.FORTRESS_IMPALE, TANK_FORTRESS_IMAPLE_PATH);

		defineAssassinAnimation(AssassinAnimationName.STANDING, ASSASSIN_STANDING_PATH);
		defineAssassinAnimation(AssassinAnimationName.WALKING, ASSASSIN_WALKING_PATH);
		defineAssassinAnimation(AssassinAnimationName.DASH, ASSASSIN_DASH_PATH);
		defineAssassinAnimation(AssassinAnimationName.SHURIKEN_THROW, ASSASSIN_SHURIKEN_THROW_PATH);

		defineBoss1Animation(Boss1AnimationName.STANDING, BOSS1_STANDING_PATH);
		defineBoss1Animation(Boss1AnimationName.GROUND_SMASH, BOSS1_GROUND_SMASH_PATH);
		defineBoss1Animation(Boss1AnimationName.EARTHQUAKE, BOSS1_EARTHQUAKE_PATH);
		defineBoss1Animation(Boss1AnimationName.ROLL, BOSS1_ROLL_PATH);

		defineShurikenAnimation(ShurikenAnimationName.FLYING, SHURIKEN_FLYING_PATH);
		defineRockAnimation(RockAnimationName.ERUPT, ROCK_ERUPT_PATH);
	}

	private void defineTexture(TextureName name, String path) {
		textures.put(name, new TextureAsset(path));
	}

	private void defineTankAnimation(TankAnimationName name, String path) {
		tankAnimations.put(name, new AnimationAsset<>(path));
	}

	private void defineAssassinAnimation(AssassinAnimationName name, String path) {
		assassinAnimations.put(name, new AnimationAsset<>(path));
	}

	private void defineBoss1Animation(Boss1AnimationName name, String path) {
		boss1Animations.put(name, new AnimationAsset<>(path));
	}

	private void defineShurikenAnimation(ShurikenAnimationName name, String path) {
		shurikenAnimations.put(name, new AnimationAsset<>(path));
	}

	private void defineRockAnimation(RockAnimationName name, String path) {
		rockAnimations.put(name, new AnimationAsset<>(path));
	}

	public void loadTexture(TextureName name) {
		assetManager.load(textures.get(name).path, Texture.class);
	}

	public void loadTankAnimation(TankAnimationName name) {
		AnimationAsset<TankParts> asset = tankAnimations.get(name);
		FileHandle handle = Gdx.files.internal(asset.path);
		asset.animation = new Animation<>(handle, TANK_PARTS);
	}

	public void loadAssassinAnimation(AssassinAnimationName name) {
		AnimationAsset<AssassinParts> asset = assassinAnimations.get(name);
		FileHandle handle = Gdx.files.internal(asset.path);
		asset.animation = new Animation<>(handle, ASSASSIN_PARTS);
	}

	public void loadBoss1Animation(Boss1AnimationName name) {
		AnimationAsset<Boss1Parts> asset = boss1Animations.get(name);
		FileHandle handle = Gdx.files.internal(asset.path);
		asset.animation = new Animation<>(handle, BOSS1_PARTS);
	}

	public void loadShurikenAnimation(ShurikenAnimationName name) {
		AnimationAsset<ShurikenParts> asset = shurikenAnimations.get(name);
		FileHandle handle = Gdx.files.internal(asset.path);
		asset.animation = new Animation<>(handle, SHURIKEN_PARTS);
	}

	public void loadRockAnimation(RockAnimationName name) {
		AnimationAsset<RockParts> asset = rockAnimations.get(name);
		FileHandle handle = Gdx.files.internal(asset.path);
		asset.animation = new Animation<>(handle, ROCK_PARTS);
	}

	public void load() {
		assetManager.finishLoading();
		for (TextureAsset asset : textures.values()) {
			asset.texture = assetManager.get(asset.path, Texture.class);
		}
	}

	public Texture getTexture(TextureName name) {
		return textures.get(name).texture;
	}

	public Animation<TankParts> getTankAnimation(TankAnimationName name) {
		return new Animation<>(tankAnimations.get(name).animation);
	}

	public Animation<AssassinParts> getAssassinAnimation(AssassinAnimationName name) {
		return new Animation<>(assassinAnimations.get(name).animation);
	}

	public Animation<Boss1Parts> getBoss1Animation(Boss1AnimationName name) {
		return new Animation<>(boss1Animations.get(name).animation);
	}

	public Animation<ShurikenParts> getShurikenAnimation(ShurikenAnimationName name) {
		return new Animation<>(shurikenAnimations.get(name).animation);
	}

	public Animation<RockParts> getRockAnimation(RockAnimationName name) {
		return new Animation<>(rockAnimations.get(name).animation);
	}
}
