package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.state.States;

import static com.mygdx.game.state.EntityStates.FLYING;

/**
 * Represents interactive objects (not characters) such as throwing stars/falling rocks etc.
 */
public abstract class Entity {
    private int x;
    private int y;
    private int x_velocity;
    private int y_velocity;
    private float width;
    private float height;

    private Sprite sprite;

    private States<Entity> states;
    private Animations<Entity> animations;

    public Entity() {
        this.x = 0;
        this.y = 0;
        this.x_velocity = 0; // unmoving by default
        this.y_velocity = 0;

        this.states = new States<Entity>();
        states.add(FLYING);

        this.animations = animations();
        this.sprite = animations.from(states);
    }

    protected abstract Animations<Entity> animations();

    protected void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void setVelocity(int x_vel, int y_vel) {
        this.x_velocity = x_vel;
        this.y_velocity = y_vel;
    }

    public void update() {
        this.x += x_velocity;
        this.y += y_velocity;
    }

    public void render(SpriteBatch batch) {
        // update
        this.sprite = animations.from(states);
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();

        update();

        sprite.setPosition(x, y);
        sprite.draw(batch);
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
}
