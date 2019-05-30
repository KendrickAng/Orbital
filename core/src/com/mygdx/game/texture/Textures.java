package com.mygdx.game.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Reference class to get all textures used in the game. No functionality other than a store.
 */
public class Textures {
	static final ArrayList<TextureGroup> library = new ArrayList<>();

	/* TANK */
	public static final AssetDescriptor<Texture> TANK_STANDING = new AssetDescriptor<>(Gdx.files.internal("Tank/Standing.png"), Texture.class);
	public static final AssetDescriptor<Texture> TANK_PRIMARY = new AssetDescriptor<>(Gdx.files.internal("Tank/Primary.png"), Texture.class);
	public static final AssetDescriptor<Texture> TANK_SECONDARY = new AssetDescriptor<>(Gdx.files.internal("Tank/Secondary.png"), Texture.class);
	public static final AssetDescriptor<Texture> TANK_TERTIARY = new AssetDescriptor<>(Gdx.files.internal("Tank/Tertiary.png"), Texture.class);
	public static TextureGroup<Texture> TANK = new TextureGroup<Texture>()
			.addTexture(new TextureAsset<>(TANK_STANDING))
			.addTexture(new TextureAsset<>(TANK_PRIMARY))
			.addTexture(new TextureAsset<>(TANK_SECONDARY))
			.addTexture(new TextureAsset<>(TANK_TERTIARY));

	/* ASSASSIN */
	public static final AssetDescriptor<Texture> ASSASSIN_STANDING = new AssetDescriptor<>(Gdx.files.internal("Assassin/Standing.png"), Texture.class);
	public static final AssetDescriptor<Texture> ASSASSIN_PRIMARY = new AssetDescriptor<>(Gdx.files.internal("Assassin/Primary.png"), Texture.class);
	public static final AssetDescriptor<Texture> ASSASSIN_SECONDARY = new AssetDescriptor<>(Gdx.files.internal("Assassin/Secondary.png"), Texture.class);
	public static final AssetDescriptor<Texture> ASSASSIN_TERTIARY = new AssetDescriptor<>(Gdx.files.internal("Assassin/Tertiary.png"), Texture.class);
	public static TextureGroup<Texture> ASSASSIN = new TextureGroup<Texture>()
			.addTexture(new TextureAsset<>(ASSASSIN_STANDING))
			.addTexture(new TextureAsset<>(ASSASSIN_PRIMARY))
			.addTexture(new TextureAsset<>(ASSASSIN_SECONDARY))
			.addTexture(new TextureAsset<>(ASSASSIN_TERTIARY));
	// SHURIKEN
	public static final AssetDescriptor<Texture> SHURIKEN_FLYING = new AssetDescriptor<>(Gdx.files.internal("Assassin/Shuriken.png"), Texture.class);
	public static TextureGroup<Texture> SHURIKEN = new TextureGroup<Texture>()
			.addTexture(new TextureAsset<>(SHURIKEN_FLYING));

	/* BOSS1 */
	public static final AssetDescriptor<Texture> BOSS1_STANDING = new AssetDescriptor<>(Gdx.files.internal("Boss1/Standing.png"), Texture.class);
	public static TextureGroup<Texture> BOSS1 = new TextureGroup<Texture>()
			.addTexture(new TextureAsset<>(BOSS1_STANDING));

	/* ENVIRONMENT */
	public static final AssetDescriptor<Texture> ENVIRONMENT_BACKGROUND = new AssetDescriptor<>(Gdx.files.internal("Background.png"), Texture.class);
	public static final AssetDescriptor<Texture> ENVIRONMENT_FLOOR = new AssetDescriptor<>(Gdx.files.internal("Floor.png"), Texture.class);
	public static TextureGroup<Texture> ENVIRONMENT = new TextureGroup<Texture>()
			.addTexture(new TextureAsset<>(ENVIRONMENT_BACKGROUND))
			.addTexture(new TextureAsset<>(ENVIRONMENT_FLOOR));

	static {
		library.add(TANK);
		library.add(ASSASSIN);
		library.add(SHURIKEN);
		library.add(BOSS1);
		library.add(ENVIRONMENT);
	}
}
