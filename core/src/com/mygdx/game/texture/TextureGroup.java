package com.mygdx.game.texture;

import java.util.HashSet;

/**
 * Convenience class to store related textures together.
 *
 * @param <T> The class the textures all fall under. E.g Assassin, Tank, Boss.
 */
public class TextureGroup<T> {
	private HashSet<TextureAsset<T>> textures;

	public TextureGroup() {
		this.textures = new HashSet<>();
	}

	// Add a texture to the group
	public TextureGroup<T> addTexture(TextureAsset<T> texture) {
		textures.add(texture);
		return this;
	}

	public TextureAsset[] toArray() {
		return textures.toArray(new TextureAsset[0]);
	}
}
