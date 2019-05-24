package com.mygdx.game.texture;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.files.FileHandle;

/**
 * Stores a specific kind of texture.
 * @param <T> the type of asset stored (E.g PixMap, TextureAtlas, Texture).
 */
public class TextureAsset<T> {
    private AssetDescriptor<T> descriptor;

    public TextureAsset(AssetDescriptor<T> descriptor) {
        this.descriptor = descriptor;
    }

    public AssetDescriptor<T> getDescriptor() {
        return this.descriptor;
    }
}
