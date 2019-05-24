package com.mygdx.game.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Assassin;
import com.mygdx.game.Entity;
import com.mygdx.game.Tank;

import java.util.ArrayList;

/**
 * Reference class to get all textures used in the game. No functionality other than a store.
 */
public class Textures {
    static final ArrayList<TextureGroup> library = new ArrayList<TextureGroup>();

    /* TANK */
    public static final AssetDescriptor<Texture> TANK_STANDING = new AssetDescriptor<Texture>(Gdx.files.internal("Tank/Standing.png"), Texture.class);
    public static final AssetDescriptor<Texture> TANK_PRIMARY = new AssetDescriptor<Texture>(Gdx.files.internal("Tank/Primary.png"), Texture.class);
    public static final AssetDescriptor<Texture> TANK_SECONDARY = new AssetDescriptor<Texture>(Gdx.files.internal("Tank/Secondary.png"), Texture.class);
    public static final AssetDescriptor<Texture> TANK_TERTIARY = new AssetDescriptor<Texture>(Gdx.files.internal("Tank/Tertiary.png"), Texture.class);
    public static TextureGroup<Tank> TANK = new TextureGroup<Tank>()
            .addTexture(new TextureAsset<Texture>(TANK_STANDING))
            .addTexture(new TextureAsset<Texture>(TANK_PRIMARY))
            .addTexture(new TextureAsset<Texture>(TANK_SECONDARY))
            .addTexture(new TextureAsset<Texture>(TANK_TERTIARY));

    /* ASSASSIN */
    public static final AssetDescriptor<Texture> ASSASSIN_STANDING = new AssetDescriptor<Texture>(Gdx.files.internal("Assassin/Standing.png"), Texture.class);
    public static final AssetDescriptor<Texture> ASSASSIN_PRIMARY = new AssetDescriptor<Texture>(Gdx.files.internal("Assassin/Primary.png"), Texture.class);
    public static final AssetDescriptor<Texture> ASSASSIN_SECONDARY = new AssetDescriptor<Texture>(Gdx.files.internal("Assassin/Secondary.png"), Texture.class);
    public static final AssetDescriptor<Texture> ASSASSIN_TERTIARY = new AssetDescriptor<Texture>(Gdx.files.internal("Assassin/Tertiary.png"), Texture.class);
    public static TextureGroup<Assassin> ASSASSIN = new TextureGroup<Assassin>()
            .addTexture(new TextureAsset<Texture>(ASSASSIN_STANDING))
            .addTexture(new TextureAsset<Texture>(ASSASSIN_PRIMARY))
            .addTexture(new TextureAsset<Texture>(ASSASSIN_SECONDARY))
            .addTexture(new TextureAsset<Texture>(ASSASSIN_TERTIARY));

    /* ENTITY */
    public static final AssetDescriptor<Texture> ENTITY_SHURIKEN = new AssetDescriptor<Texture>(Gdx.files.internal("Entity/Shuriken.png"), Texture.class);
    public static TextureGroup<Entity> ENTITY = new TextureGroup<Entity>()
            .addTexture(new TextureAsset<Texture>(ENTITY_SHURIKEN));

    static {
        library.add(TANK);
        library.add(ASSASSIN);
        library.add(ENTITY);
    }
}
