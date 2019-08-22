package com.untitled.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.untitled.game.animation.Animation;
import com.untitled.game.boss1.Boss1Parts;
import com.untitled.game.character.AssassinParts;
import com.untitled.game.character.TankParts;
import com.untitled.game.rock.RockParts;
import com.untitled.game.shuriken.ShurikenParts;

import java.util.HashMap;

/**
 * Asset manager for Untitled.
 */
public class Assets {
	private AssetManager assetManager;

	private HashMap<TextureName, TextureAsset> textures;
	private HashMap<FontName, FontAsset> fonts;
	private HashMap<MusicName, MusicAsset> music;
	private HashMap<TankAnimationName, AnimationAsset<TankParts>> tankAnimations;
	private HashMap<AssassinAnimationName, AnimationAsset<AssassinParts>> assassinAnimations;
	private HashMap<Boss1AnimationName, AnimationAsset<Boss1Parts>> boss1Animations;
	private HashMap<ShurikenAnimationName, AnimationAsset<ShurikenParts>> shurikenAnimations;
	private HashMap<RockAnimationName, AnimationAsset<RockParts>> rockAnimations;

	private static final float FONT_LINE_HEIGHT = 1.8f;

	public static final HashMap<String, TankParts> TANK_PARTS;
	public static final String TANK_STANDING_PATH = "Animations/Tank/Standing";
	public static final String TANK_WALKING_PATH = "Animations/Tank/Walking";
	public static final String TANK_BLOCK_PATH = "Animations/Tank/Block";
	public static final String TANK_HAMMER_SWING_PATH = "Animations/Tank/Hammer Swing";
	public static final String TANK_FORTRESS_PATH = "Animations/Tank/Fortress";
	public static final String TANK_FORTRESS_STANDING_PATH = "Animations/Tank/Fortress Standing";
	public static final String TANK_FORTRESS_WALKING_PATH = "Animations/Tank/Fortress Walking";
	public static final String TANK_FORTRESS_BLOCK_PATH = "Animations/Tank/Fortress Block";
	public static final String TANK_FORTRESS_IMPALE_PATH = "Animations/Tank/Fortress Impale";

	public static final HashMap<String, AssassinParts> ASSASSIN_PARTS;
	public static final String ASSASSIN_STANDING_PATH = "Animations/Assassin/Standing";
	public static final String ASSASSIN_WALKING_PATH = "Animations/Assassin/Walking";
	public static final String ASSASSIN_DASH_PATH = "Animations/Assassin/Dash";
	public static final String ASSASSIN_SHURIKEN_THROW_PATH = "Animations/Assassin/Shuriken Throw";
	public static final String ASSASSIN_CLEANSE_PATH = "Animations/Assassin/Cleanse";

	public static final HashMap<String, Boss1Parts> BOSS1_PARTS;
	public static final String BOSS1_STANDING_PATH = "Animations/Boss1/Standing";
	public static final String BOSS1_GROUND_SMASH_PATH = "Animations/Boss1/Ground Smash";
	public static final String BOSS1_EARTHQUAKE_PATH = "Animations/Boss1/Earthquake";
	public static final String BOSS1_ROLL_PATH = "Animations/Boss1/Roll";

	public static final HashMap<String, ShurikenParts> SHURIKEN_PARTS;
	public static final String SHURIKEN_FLYING_PATH = "Animations/Shuriken";

	public static final HashMap<String, RockParts> ROCK_PARTS;
	public static final String ROCK_ERUPT_PATH = "Animations/Rock";

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

	private class TextureAsset {
		private boolean load;
		private String path;
		private Texture texture;

		private TextureAsset(String path) {
			this.path = path;
		}
	}

	private class FontAsset {
		private boolean load;
		private String path;
		private String id;
		private int size;
		private BitmapFont font;

		private FontAsset(String path, int size) {
			this.path = path;
			this.size = size;
			this.id = size + path;
		}
	}

	private class MusicAsset {
		private boolean load;
		private String path;
		private Music music;

		private MusicAsset(String path) {
			this.path = path;
		}
	}

	private class AnimationAsset<P extends Enum> {
		private String path;
		private Animation<P> animation;

		private AnimationAsset(String path) {
			this.path = path;
		}
	}

	/**
	 * Definition of all assets.
	 */
	public Assets() {
		assetManager = new AssetManager();

		// No idea what this does
		// https://stackoverflow.com/questions/46619234/libgdx-asset-manager-load-true-type-font
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		textures = new HashMap<>();
		fonts = new HashMap<>();
		music = new HashMap<>();

		tankAnimations = new HashMap<>();
		assassinAnimations = new HashMap<>();
		boss1Animations = new HashMap<>();
		shurikenAnimations = new HashMap<>();
		rockAnimations = new HashMap<>();

		defineTexture(TextureName.MENU_BACKGROUND, "Textures/Game/Menu Background.png");
		defineTexture(TextureName.GAME_BACKGROUND, "Textures/Game/Background.png");
		defineTexture(TextureName.GAME_FLOOR, "Textures/Game/Floor.png");
		defineTexture(TextureName.GAME_OVERLAY, "Textures/Game/Overlay.png");

		defineTexture(TextureName.BUTTON_NORMAL, "Textures/Button/Normal.png");
		defineTexture(TextureName.BUTTON_HOVER, "Textures/Button/Hover.png");
		defineTexture(TextureName.BUTTON_MENU_HOVER, "Textures/Button/Menu Hover.png");

		defineTexture(TextureName.ANDROID_COOLDOWN_0, "Textures/AndroidCooldowns/0.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_1, "Textures/AndroidCooldowns/1.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_2, "Textures/AndroidCooldowns/2.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_3, "Textures/AndroidCooldowns/3.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_4, "Textures/AndroidCooldowns/4.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_5, "Textures/AndroidCooldowns/5.png");

		defineTexture(TextureName.ANDROID_COOLDOWN_BLOCK, "Textures/AndroidCooldowns/Block Ability.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_HAMMER_SWING, "Textures/AndroidCooldowns/Hammer Swing Ability.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_FORTRESS, "Textures/AndroidCooldowns/Fortress Ability.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_DASH, "Textures/AndroidCooldowns/Dash Ability.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_SHURIKEN_THROW, "Textures/AndroidCooldowns/Shuriken Ability.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_CLEANSE, "Textures/AndroidCooldowns/Cleanse Ability.png");
		defineTexture(TextureName.ANDROID_COOLDOWN_SWITCH_CHARACTER, "Textures/AndroidCooldowns/Switch Character.png");

		defineTexture(TextureName.DESKTOP_COOLDOWN_0, "Textures/DesktopCooldowns/0.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_1, "Textures/DesktopCooldowns/1.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_2, "Textures/DesktopCooldowns/2.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_3, "Textures/DesktopCooldowns/3.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_4, "Textures/DesktopCooldowns/4.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_5, "Textures/DesktopCooldowns/5.png");

		defineTexture(TextureName.DESKTOP_COOLDOWN_BLOCK, "Textures/DesktopCooldowns/Block Ability.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_HAMMER_SWING, "Textures/DesktopCooldowns/Hammer Swing Ability.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_FORTRESS, "Textures/DesktopCooldowns/Fortress Ability.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_DASH, "Textures/DesktopCooldowns/Dash Ability.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_SHURIKEN_THROW, "Textures/DesktopCooldowns/Shuriken Ability.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_CLEANSE, "Textures/DesktopCooldowns/Cleanse Ability.png");
		defineTexture(TextureName.DESKTOP_COOLDOWN_SWITCH_CHARACTER, "Textures/DesktopCooldowns/Switch Character.png");

		defineTexture(TextureName.DEBUFF_STUNNED, "Textures/Debuff/Stunned.png");
		defineTexture(TextureName.DEBUFF_WEAK_SPOT, "Textures/Debuff/Weak Spot.png");

		defineTexture(TextureName.HIGHSCORES_TITLE, "Textures/Highscores/Title.png");
		defineTexture(TextureName.HIGHSCORES_ODD, "Textures/Highscores/Odd.png");
		defineTexture(TextureName.HIGHSCORES_EVEN, "Textures/Highscores/Even.png");

		defineTexture(TextureName.INFO_BAR_BACKGROUND, "Textures/InfoBar/Background.png");
		defineTexture(TextureName.HEALTH_BAR_TANK, "Textures/InfoBar/TankHealthBar.png");
		defineTexture(TextureName.HEALTH_BAR_ASSASSIN, "Textures/InfoBar/AssassinHealthBar.png");
		defineTexture(TextureName.STACK_BAR_ASSASSIN, "Textures/InfoBar/AssassinStackBar.png");
		defineTexture(TextureName.HEALTH_BAR_BOSS, "Textures/InfoBar/BossHealthBar.png");

		defineFont(FontName.MINECRAFT_8, "Fonts/Minecraft.ttf", 8);
		defineFont(FontName.MINECRAFT_16, "Fonts/Minecraft.ttf", 16);
		defineFont(FontName.MINECRAFT_24, "Fonts/Minecraft.ttf", 24);
		defineFont(FontName.MINECRAFT_32, "Fonts/Minecraft.ttf", 32);

		defineMusic(MusicName.MAIN_MENU, "Music/Main Menu.mp3");
		defineMusic(MusicName.BOSS, "Music/Boss.mp3");

		defineTankAnimation(TankAnimationName.STANDING, TANK_STANDING_PATH);
		defineTankAnimation(TankAnimationName.WALKING, TANK_WALKING_PATH);
		defineTankAnimation(TankAnimationName.BLOCK, TANK_BLOCK_PATH);
		defineTankAnimation(TankAnimationName.HAMMER_SWING, TANK_HAMMER_SWING_PATH);
		defineTankAnimation(TankAnimationName.FORTRESS, TANK_FORTRESS_PATH);
		defineTankAnimation(TankAnimationName.FORTRESS_STANDING, TANK_FORTRESS_STANDING_PATH);
		defineTankAnimation(TankAnimationName.FORTRESS_WALKING, TANK_FORTRESS_WALKING_PATH);
		defineTankAnimation(TankAnimationName.FORTRESS_BLOCK, TANK_FORTRESS_BLOCK_PATH);
		defineTankAnimation(TankAnimationName.FORTRESS_IMPALE, TANK_FORTRESS_IMPALE_PATH);

		defineAssassinAnimation(AssassinAnimationName.STANDING, ASSASSIN_STANDING_PATH);
		defineAssassinAnimation(AssassinAnimationName.WALKING, ASSASSIN_WALKING_PATH);
		defineAssassinAnimation(AssassinAnimationName.DASH, ASSASSIN_DASH_PATH);
		defineAssassinAnimation(AssassinAnimationName.SHURIKEN_THROW, ASSASSIN_SHURIKEN_THROW_PATH);
		defineAssassinAnimation(AssassinAnimationName.CLEANSE, ASSASSIN_CLEANSE_PATH);

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

	private void defineFont(FontName name, String path, int size) {
		fonts.put(name, new FontAsset(path, size));
	}

	private void defineMusic(MusicName name, String path) {
		music.put(name, new MusicAsset(path));
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

	/**
	 * Queue Asset for loading in {@link AssetManager}
	 *
	 * @param name {@link TextureName} to load.
	 */
	public void loadTexture(TextureName name) {
		TextureAsset asset = textures.get(name);
		asset.load = true;

		assetManager.load(asset.path, Texture.class);
	}

	/**
	 * Queue Asset for loading in {@link AssetManager}
	 *
	 * @param name {@link FontName} to load.
	 */
	public void loadFont(FontName name) {
		FontAsset asset = fonts.get(name);
		asset.load = true;

		FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		parameter.fontFileName = asset.path;
		parameter.fontParameters.size = asset.size;
		assetManager.load(asset.id, BitmapFont.class, parameter);
	}

	/**
	 * Queue Asset for loading in {@link AssetManager}
	 *
	 * @param name {@link MusicName} to load.
	 */
	public void loadMusic(MusicName name) {
		MusicAsset asset = music.get(name);
		asset.load = true;

		assetManager.load(music.get(name).path, Music.class);
	}

	/**
	 * Force load Animation.
	 *
	 * @param name {@link TankAnimationName} to load.
	 */
	public void loadTankAnimation(TankAnimationName name) {
		AnimationAsset<TankParts> asset = tankAnimations.get(name);
		FileHandle handle = Gdx.files.internal(asset.path);
		asset.animation = new Animation<>(handle, TANK_PARTS);
	}

	/**
	 * Force load Animation.
	 *
	 * @param name {@link AssassinAnimationName} to load.
	 */
	public void loadAssassinAnimation(AssassinAnimationName name) {
		AnimationAsset<AssassinParts> asset = assassinAnimations.get(name);
		FileHandle handle = Gdx.files.internal(asset.path);
		asset.animation = new Animation<>(handle, ASSASSIN_PARTS);
	}

	/**
	 * Force load Animation.
	 *
	 * @param name {@link Boss1AnimationName} to load.
	 */
	public void loadBoss1Animation(Boss1AnimationName name) {
		AnimationAsset<Boss1Parts> asset = boss1Animations.get(name);
		FileHandle handle = Gdx.files.internal(asset.path);
		asset.animation = new Animation<>(handle, BOSS1_PARTS);
	}

	/**
	 * Force load Animation.
	 *
	 * @param name {@link ShurikenAnimationName} to load.
	 */
	public void loadShurikenAnimation(ShurikenAnimationName name) {
		AnimationAsset<ShurikenParts> asset = shurikenAnimations.get(name);
		FileHandle handle = Gdx.files.internal(asset.path);
		asset.animation = new Animation<>(handle, SHURIKEN_PARTS);
	}

	/**
	 * Force load Animation.
	 *
	 * @param name {@link RockAnimationName} to load.
	 */
	public void loadRockAnimation(RockAnimationName name) {
		AnimationAsset<RockParts> asset = rockAnimations.get(name);
		FileHandle handle = Gdx.files.internal(asset.path);
		asset.animation = new Animation<>(handle, ROCK_PARTS);
	}

	/**
	 * Force load all assets in {@link AssetManager}
	 */
	public void load() {
		// Force load all
		assetManager.finishLoading();

		// Copy textures from assetManager back
		for (TextureAsset asset : textures.values()) {
			if (asset.load) {
				asset.texture = assetManager.get(asset.path, Texture.class);
			}
		}

		// Copy fonts from assetManager back
		for (FontAsset asset : fonts.values()) {
			if (asset.load) {
				asset.font = assetManager.get(asset.id, BitmapFont.class);
				asset.font.getData().markupEnabled = true;
				asset.font.getData().setLineHeight(asset.font.getLineHeight() * FONT_LINE_HEIGHT);
			}
		}

		// Copy music from assetManager back
		for (MusicAsset asset : music.values()) {
			if (asset.load) {
				asset.music = assetManager.get(asset.path, Music.class);
				asset.music.setLooping(true);
			}
		}
	}

	public Texture getTexture(TextureName name) {
		return textures.get(name).texture;
	}

	public BitmapFont getFont(FontName name) {
		return fonts.get(name).font;
	}

	public Music getMusic(MusicName name) {
		return music.get(name).music;
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
