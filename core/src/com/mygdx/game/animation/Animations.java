package com.mygdx.game.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shape.Rectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * Eventually change from sprites to animations.
 */
public class Animations<T extends Enum, R extends Enum> {
	private Vector2 position;
	private boolean flipX;
	private boolean flipY;

	private HashSet<T> states;
	private HashMap<T, PriorityGroup> groups;
	private TreeMap<R, Animation> renderer;

	private class PriorityGroup implements Comparable<PriorityGroup> {
		int priority;
		AnimationsGroup<R> group;

		PriorityGroup(AnimationsGroup<R> group, int priority) {
			this.group = group;
			this.priority = priority;
		}

		@Override
		public int compareTo(PriorityGroup o) {
			return o.priority - priority;
		}
	}

	public Animations(HashSet<T> states) {
		this.states = states;
		groups = new HashMap<>();
	}

	// Maps a state to a group & priority
	public Animations<T, R> add(T state, AnimationsGroup<R> group, Integer priority) {
		groups.put(state, new PriorityGroup(group, priority));
		return this;
	}

	public Animations<T, R> done() {
		updateRenderer();
		return this;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public void setFlip(boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
	}

	// Finds the best animations based on priorities.
	private void updateRenderer() {
		PriorityQueue<PriorityGroup> queue = new PriorityQueue<>();
		for (T state : states) {
			queue.add(groups.get(state));
		}

		renderer = new TreeMap<>();
		while (!queue.isEmpty()) {
			AnimationsGroup<R> group = queue.poll().group;
			for (Map.Entry<R, Animation> entry : group.getAnimations().entrySet()) {
				R part = entry.getKey();
				Animation animation = entry.getValue();
				if (!renderer.containsKey(part)) {
					renderer.put(part, animation);
				}
			}
		}
	}

	public void render(SpriteBatch batch) {
		updateRenderer();
		for (Animation animation : renderer.values()) {
			animation.setPosition(position);
			animation.setFlip(flipX, flipY);
			animation.render(batch);
		}
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		for (Animation animation : renderer.values()) {
			animation.getHitbox().renderDebug(shapeRenderer);
		}
	}

	public Rectangle getHitbox(R part) {
		return renderer.get(part).getHitbox();
	}
}
