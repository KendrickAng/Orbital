package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entity.Entity;

import java.util.HashSet;

/**
 * ADT to store all entities (interactive objects) and render them.
 */
public class EntityManager {
	private HashSet<com.mygdx.game.entity.Entity> entities;

	public EntityManager() {
		entities = new HashSet<com.mygdx.game.entity.Entity>();
	}

	public EntityManager add(com.mygdx.game.entity.Entity e) {
		entities.add(e);
		return this;
	}

	public void renderAll(SpriteBatch batch) {
		for (Entity e : entities) {
			e.render(batch);
		}
	}
}
