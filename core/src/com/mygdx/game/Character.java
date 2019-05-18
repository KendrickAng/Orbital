package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.mygdx.game.MyGdxGame.*;

public abstract class Character {
    // for cooldown checking
    private final CooldownUtils cooldownUtils = new CooldownUtils();

    private int x; // bottom left
    private int y; // bottom left
    private float width;
    private float height;

    public Character(float width, float height) {
        this.x = 0;
        this.y = MAP_HEIGHT;
        this.width = width;
        this.height = height;
    }

    public abstract void render();
    public abstract void dispose();

    // Skills to be implemented
    public abstract void primary();
    public abstract void secondary();
    public abstract void tertiary();

    // manipulate key press state
    public abstract void setPrimaryPressed(boolean flag);
    public abstract void setSecondaryPressed(boolean flag);
    public abstract void setTertiaryPressed(boolean flag);

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // returns true if a skill's cooldown timer has passed.
    public boolean isSkillAvailable(long prev, long cooldown) {
        return cooldownUtils.isSkillAvailable(prev, cooldown);
    }

    // returns true if a skill is still being cast.
    public boolean isSkillPersisting(long startCast, long persistTime) {
        return cooldownUtils.isSkillPersisting(startCast, persistTime);
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public float getWidth() { return this.width; }
    public float getHeight() { return this.height; }
}
