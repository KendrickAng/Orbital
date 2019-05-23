package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashSet;

/**
 * ADT to store all entities (interactive objects) and render them.
 */
public class EntityManager {
    private HashSet<Entity> entities;

    public EntityManager() {
        entities = new HashSet<Entity>();
    }

    public EntityManager add(Entity e) {
        entities.add(e);
        return this;
    }

    public void renderAll(SpriteBatch batch) {
        for(Entity e: entities) {
            e.render(batch);
        }
    }
}
