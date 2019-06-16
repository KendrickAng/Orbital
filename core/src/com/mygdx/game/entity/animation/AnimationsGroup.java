package com.mygdx.game.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Contains all the animations for a certain group e.g Standing.
 * @param <P> the enum grouping all the parts that need to be animated.
 */
public class AnimationsGroup<P extends Enum> {
	private HashMap<P, Animation> parts; // a unique Part maps to the same Animation instance.
	private TreeMap<P, Animation> animations;

	public AnimationsGroup(String directory, HashMap<String, P> filenames) {
		parts = new HashMap<>();
		animations = new TreeMap<>();

		// map all parts to an empty animation, populates parts.
		for (P part : filenames.values()) {
			Animation animation = new Animation();
			parts.put(part, animation);
			animations.put(part, animation);
		}

		// returns filehandles for a directory.
		FileHandle[] files = Gdx.files.internal(directory).list();
		for (FileHandle file : files) {
			// populates the animations in order, ensuring order dictated in assets name is followed.
			String[] n = file.nameWithoutExtension().split("_");
			int frame = Integer.parseInt(n[0]);
			String name = n[1];

			P part = filenames.get(name);
			Animation animation = parts.get(part);
			animation.put(frame, new Pixmap(file));
		}
	}

	public void setPosition(Vector2 position) {
		for (Animation animation : parts.values()) {
			animation.setPosition(position);
		}
	}

	public void setDuration(float duration) {
		for (Animation animation : parts.values()) {
			animation.setDuration(duration);
		}
	}

	public HashMap<P, Animation> getParts() {
		return parts;
	}

	public TreeMap<P, Animation> getAnimations() {
		return animations;
	}
}
