package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.ability.Callback;
import com.mygdx.game.state.States;

import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.state.CharacterStates.ABILITIES;
import static com.mygdx.game.state.CharacterStates.PRIMARY;
import static com.mygdx.game.state.CharacterStates.SECONDARY;
import static com.mygdx.game.state.CharacterStates.STANDING;
import static com.mygdx.game.state.CharacterStates.TERTIARY;

/**
 * An Entity.
 */
public abstract class Character {
    private int x; // bottom left
    private int y; // bottom left
    private float width;
    private float height;
    private Direction direction;

    private Sprite sprite;

    private States<Character> states;
    private Abilities<Character> abilities;
    private Animations<Character> animations;
    // TODO: Effects/Debuffs Class (Crowd Control)

    /**
     * Initliases the character at coordinates (0, MAP_HEIGHT).
     */
    public Character() {
        this.x = 0;
        this.y = MAP_HEIGHT;
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

        // Rendering
        sprite.setPosition(x, y);
        sprite.draw(batch);
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

    /* Getters */
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public float getWidth() { return this.width; }
    public float getHeight() { return this.height; }
}
