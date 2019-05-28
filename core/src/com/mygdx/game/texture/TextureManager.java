package com.mygdx.game.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

/**
 * Handles loading, retrieval and disposal of assets.
 */
public class TextureManager extends AssetManager {
	public TextureManager() {
	}

	public void loadTextures() {
		for (TextureGroup group : Textures.library) {
			for (TextureAsset texture : group.toArray()) {
				this.load(texture.getDescriptor());
			}
		}
		updateLoading();
	}

	public void updateLoading() {
		// do something nice while waiting for loading
		while (!this.update()) {
			Gdx.app.log("TextureManager.java", "Loading... " + this.getProgress());
		}
		Gdx.app.log("TextureManager.java", "Load complete! :)");
	}
}
