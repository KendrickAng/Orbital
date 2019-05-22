package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.ability.Callback;
import com.mygdx.game.state.States;

import static com.mygdx.game.MyGdxGame.*;
import static com.mygdx.game.state.EntityStates.ABILITIES;
import static com.mygdx.game.state.EntityStates.PRIMARY;
import static com.mygdx.game.state.EntityStates.SECONDARY;
import static com.mygdx.game.state.EntityStates.STANDING;
import static com.mygdx.game.state.EntityStates.TERTIARY;

/**
 * An Entity.
 */
public abstract class Character {
    private int x; // bottom left
    private int y; // bottom left
    private int x_velocity; // independent from movement using arrow keys
    private int y_velocity;
    private float width;
    private float height;
    private Direction direction;

    private Sprite sprite;

    private States<Character> states;
    private Abilities<Character> abilities;
    private Animations<Character> animations;
    // TODO: Effects/Debuffs Class (Crowd Control)

    /**
     * Initialises the character at coordinates (0, MAP_HEIGHT).
     */
    public Character() {
        this.x = 0;
        this.y = MAP_HEIGHT;
        this.x_velocity = 0;
        this.y_velocity = 0;
        this.direction = Direction.RIGHT;

        this.states = new States<Character>();
        states.add(STANDING);

        this.abilities = abilities();
        this.animations = animations();
        sprite = animations.from(states);
    }

    protected abstract Abilities<Character> abilities();
    protected abstract Animations<Character> animations();

    /* Abilities */
    public void primary() {
        if (!states.contains(ABILITIES) && abilities.ready(PRIMARY)) {
            states.add(PRIMARY);
            abilities.use(PRIMARY, new Callback() {
                @Override
                public void call() {
                    states.remove(PRIMARY);
                }
            });
        }
    }

    public void secondary() {
        if (!states.contains(ABILITIES) && abilities.ready(SECONDARY)) {
            states.add(SECONDARY);
            abilities.use(SECONDARY, new Callback() {
                @Override
                public void call() {
                    states.remove(SECONDARY);
                }
            });
        }
    }

    public void tertiary() {
        if (!states.contains(ABILITIES) && abilities.ready(TERTIARY)) {
            states.add(TERTIARY);
            abilities.use(TERTIARY, new Callback() {
                @Override
                public void call() {
                    states.remove(TERTIARY);
                }
            });
        }
    }

    // handles logic for the skills.
    public abstract void isPrimary(ShapeRenderer shapeBatch);
    public abstract void isSecondary(ShapeRenderer shapeBatch);
    public abstract void isTertiary(ShapeRenderer shapeBatch);

    /* Render */
    public void render(SpriteBatch batch) {
        // Updating
        sprite = animations.from(states);
        width = sprite.getWidth();
        height = sprite.getHeight();

        switch(direction) {
            case LEFT:
                sprite.setFlip(true, false);
                break;
            case RIGHT:
                sprite.setFlip(false, false);
                break;
        }

        moveExternalInBounds(); // apply movement from external forces

        // Rendering
        sprite.setPosition(x, y);
        sprite.draw(batch);
    }

    // movement coming from external sources (e.g abilities). generally used with setSpeed().
    private void moveExternalInBounds() {
        this.x += x_velocity;
        this.y += y_velocity;

        // apply gravity and position correction
        if(y < MAP_HEIGHT) {
            y = MAP_HEIGHT;
            y_velocity = 0; // speedup only while airborne
            x_velocity = 0;
        } else {
            y_velocity += GRAVITY;
        }
        if(x > GAME_WIDTH - (int) width) x = GAME_WIDTH - (int) width;
        if(x < 0) x = 0;
    }

    // TODO: This is weird. ShapeRenderer should be for debugging purposes only.
    // A possible fix would be to create 3 more methods called isPrimaryDebug, isSecondaryDebug, isTertiaryDebug.
    public void renderShape(ShapeRenderer shapeBatch) {
        if (states.contains(PRIMARY)) {
            isPrimary(shapeBatch);
        } else if (states.contains(SECONDARY)) {
            isSecondary(shapeBatch);
        } else if (states.contains(TERTIARY)) {
            isTertiary(shapeBatch);
        }
    }

    /* Called from GameScreen */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* Called from Character children */
    public void setSpeed(int x_vel, int y_vel) {
        this.x_velocity = x_vel;
        this.y_velocity = y_vel;
    }

    /* Getters */
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public float getWidth() { return this.width; }
    public float getHeight() { return this.height; }
    public Direction getDirection() { return this.direction; }
}
