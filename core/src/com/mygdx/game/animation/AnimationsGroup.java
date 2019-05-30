package com.mygdx.game.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import java.util.HashMap;

public class AnimationsGroup<T> {
	private String directory;
	private HashMap<String, T> parts;
	private HashMap<T, Animation> animations;

	public AnimationsGroup(String directory) {
		this.directory = directory;
		this.parts = new HashMap<>();
		this.animations = new HashMap<>();
	}

	public AnimationsGroup<T> add(T part, String filename) {
		parts.put(filename, part);
		animations.put(part, new Animation());
		return this;
	}

	public AnimationsGroup<T> load() {
		FileHandle[] files = Gdx.files.internal(directory).list();
		for (FileHandle file : files) {
			String[] n = file.nameWithoutExtension().split("_");
			int id = Integer.parseInt(n[0]);
			String name = n[1];
			T part = parts.get(name);
			if (part != null) {
				animations.get(part).put(id, new Pixmap(file));
			}
		}
		return this;
	}

	public HashMap<T, Animation> getAnimations() {
		return animations;
	}
}
