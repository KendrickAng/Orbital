package com.mygdx.game.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import java.util.HashMap;

public class AnimationsGroup<P> implements Comparable<AnimationsGroup> {
	private int priority;
	private String directory;
	private HashMap<String, P> parts;
	private HashMap<P, Animation> animations;

	public AnimationsGroup(String directory, int priority) {
		this.priority = priority;
		this.directory = directory;
		this.parts = new HashMap<>();
		this.animations = new HashMap<>();
	}

	public AnimationsGroup<P> add(P part, String filename) {
		parts.put(filename, part);
		animations.put(part, new Animation());
		return this;
	}

	public AnimationsGroup<P> load() {
		FileHandle[] files = Gdx.files.internal(directory).list();
		for (FileHandle file : files) {
			String[] n = file.nameWithoutExtension().split("_");
			int frame = Integer.parseInt(n[0]);
			String name = n[1];
			P part = parts.get(name);
			if (part != null) {
				animations.get(part).put(frame, new Pixmap(file));
			}
		}
		return this;
	}

	public HashMap<P, Animation> getAnimations() {
		return animations;
	}

	@Override
	public int compareTo(AnimationsGroup o) {
		return o.priority - priority;
	}
}
