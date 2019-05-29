package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * ADT to store all entities and render them.
 */
public class EntityManager {
	private int priority;
	private TreeMap<Integer, Entity> entities;
	private HashMap<Entity, Integer> priorities;

	public EntityManager() {
		entities = new TreeMap<Integer, Entity>();
		priorities = new HashMap<Entity, Integer>();
	}

	// Order is important. Last in will be rendered on top.
	// TODO: Possibly don't use increasing priorities
	public EntityManager add(Entity e) {
		entities.put(priority, e);
		priorities.put(e, priority++);
		return this;
	}

	public void renderAll(SpriteBatch batch) {
		Iterator<Entity> iterator = entities.values().iterator();
		while (iterator.hasNext()) {
			Entity e = iterator.next();
			if (e.isDispose()) {
				// TODO: Doesn't actually dispose class, only removes from EntityManager
				Gdx.app.log("EntityManager.java", "Entity disposed");
				priorities.remove(e);
				iterator.remove();
				continue;
			}

			if (e.isVisible()) {
				e.render(batch);
			}
		}
	}

	public void renderDebugAll(ShapeRenderer renderer) {
		for (Entity e : entities.values()) {
			if (e.isVisible()) {
				e.renderDebug(renderer);
			}
		}
	}
}
