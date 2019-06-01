package com.mygdx.game.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.TreeMap;

public class AnimationsGroup<P extends Enum> {
	private HashMap<P, Animation> parts;
	private TreeMap<P, Animation> animations;

	public AnimationsGroup(String directory, HashMap<String, P> filenames) {
		parts = new HashMap<>();
		animations = new TreeMap<>();

		for (P part : filenames.values()) {
			Animation animation = new Animation();
			parts.put(part, animation);
			animations.put(part, animation);
		}

		FileHandle[] files = Gdx.files.internal(directory).list();
		for (FileHandle file : files) {
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
