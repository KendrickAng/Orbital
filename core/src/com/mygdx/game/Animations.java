package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Eventually change from sprites to animations.
 */
public class Animations<T> {
	private HashMap<T, Sprite> sprites;
	private HashMap<T, Integer> priorities;

	public Animations() {
		sprites = new HashMap<T, Sprite>();
		priorities = new HashMap<T, Integer>();
	}

	// Maps a state to a texture & priority
	public Animations<T> add(T state, Texture texture, Integer priority) {
		sprites.put(state, new Sprite(texture));
		priorities.put(state, priority);
		return this;
	}

	// Returns a sprite based on the given state
	public Sprite from(T state) {
		return sprites.get(state);
	}

	// Returns the best animation based on priorities.
	public Sprite from(HashSet<T> states) {
		Sprite sprite = null;
		Integer priority = 0;
		for (T state : states) {
			Sprite s = sprites.get(state);
			Integer p = priorities.get(state);
			if (s != null && p > priority) {
				sprite = s;
				priority = p;
			}
		}
		return sprite;
	}
}
