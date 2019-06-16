package com.mygdx.game.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Hitbox;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Contains all the animations for a certain group e.g Standing.
 * @param <P> the enum grouping all the parts that need to be animated.
 */
public class AnimationsGroup<P extends Enum> {
	/* -----------------MIGRATED FROM PARTS----------------- */
	private Vector2 position;// bottom left of the 160 by 80.
	private boolean flipX;
	private boolean flipY;
	/* -----------------MIGRATED FROM PARTS----------------- */

	private HashMap<P, Animation> parts; // Hashmap allows O(1) retrieval, and used for debug
	private TreeMap<P, Animation> animations; // Treemap ensures player is rendered above Boss

	// For dynamic AnimationsGroup belonging to Animations instance.
	public AnimationsGroup(Vector2 position) {
		this.position = position;
	}

	// For loading of static AnimationGroup stores.
	public AnimationsGroup(String directory, HashMap<String, P> filenames) {
		/* -----------------MIGRATED FROM PARTS----------------- */
		position = new Vector2();
		/* -----------------MIGRATED FROM PARTS----------------- */

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

	/* -----------------MIGRATED FROM PARTS----------------- */
	public void render(SpriteBatch batch) {
		for (Animation animation : animations.values()) {
			animation.setFlip(flipX, flipY);
			animation.render(batch);
		}
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		for (Animation animation : parts.values()) {
			animation.getHitbox().renderDebug(shapeRenderer);
		}
	}

	/* Setters */
	public void setFlip(boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
	}

	// locks in a new animation group to render() in Entity.
	public void setAnimationsGroup(AnimationsGroup<P> group) {
		group.setPosition(position);
		this.parts = group.getParts();
		this.animations = group.getAnimations();
	}

	public Hitbox getHitbox(P part) {
		return parts.get(part).getHitbox();
	}
	/* -----------------MIGRATED FROM PARTS----------------- */

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
