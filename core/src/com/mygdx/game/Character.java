package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.ability.Callback;
import com.mygdx.game.state.States;

import static com.mygdx.game.MyGdxGame.*;
import static com.mygdx.game.state.EntityStates.*;

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
    private boolean falling;

    private Direction direction;
    private MyGdxGame game;

    // affected by key presses from input processor
    private boolean leftMove;
    private boolean rightMove;

    private Sprite sprite;

    private States<Character> states;
    private Abilities<Character> abilities;
    private Ability primary;
    private Ability secondary;
    private Ability tertiary;
    private Animations<Character> animations;
    // TODO: Effects/Debuffs Class (Crowd Control)

    /**
     * Initialises the character at coordinates (0, MAP_HEIGHT).
     */
    public Character(MyGdxGame game) {
        this.x = 0;
        this.y = MAP_HEIGHT;
        this.x_velocity = 0;
        this.y_velocity = 0;
        this.direction = Direction.RIGHT;
        this.leftMove = false;
        this.rightMove = false;
        this.game = game;

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
            isPrimaryBegin();
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
            isSecondaryBegin();
        }
    }

    public void tertiary() {
        if (!states.contains(ABILITIES_ULTIMATE) && abilities.ready(TERTIARY)) {
            states.add(TERTIARY);
            abilities.use(TERTIARY, new Callback() {
                @Override
                public void call() {
                    states.remove(TERTIARY);
                }
            });
            isTertiaryBegin();
        }
    }

    // Called once when skill is started.
    public abstract void isPrimaryBegin();
    public abstract void isSecondaryBegin();
    public abstract void isTertiaryBegin();

    // Logic for skills.
    public abstract void isPrimary();
    public abstract void isSecondary();
    public abstract void isTertiary();

    // Debug rendering.
    public abstract void isPrimaryDebug(ShapeRenderer shapeBatch);
    public abstract void isSecondaryDebug(ShapeRenderer shapeBatch);
    public abstract void isTertiaryDebug(ShapeRenderer shapeBatch);

    /* Render */
    public void render(SpriteBatch batch) {
        /* Update */

        /* Abilities */
        abilities.update();

        // Skill being used
        if (states.contains(PRIMARY)) {
            isPrimary();
        } else if (states.contains(SECONDARY)) {
            isSecondary();
        } else if (states.contains(TERTIARY)) {
            isTertiary();
        }

        /* Animations */
        sprite = animations.from(states);
        width = sprite.getWidth();
        height = sprite.getHeight();

        switch (direction) {
            case LEFT:
                sprite.setFlip(true, false);
                break;
            case RIGHT:
                sprite.setFlip(false, false);
                break;
        }

        // Check if character is airborne.
        if (y > MAP_HEIGHT) {
            falling = true;
        }

        // Airborne
        if (falling) {
            // Velocity from arrow keys
            if (leftMove) {
                x_velocity -= MOVESPEED / 10;
            }

            if (rightMove) {
                x_velocity += MOVESPEED / 10;
            }

            if (y > MAP_HEIGHT) {
                y_velocity += GRAVITY;
            } else {
                // Touched ground
                y_velocity = 0;
                y = MAP_HEIGHT;
                falling = false;
            }

        // Not Airborne
        } else {
            // Velocity from arrow keys
            if (leftMove) {
                x_velocity -= MOVESPEED;
            }

            if (rightMove) {
                x_velocity += MOVESPEED;
            }

            x_velocity /= FRICTION;
        }

        // Account for velocity
        x += x_velocity;
        y += y_velocity;


        // Map Bounds
        if (x < 0) {
            x = 0;
        }

        if (x > GAME_WIDTH - (int) width) {
            x = GAME_WIDTH - (int) width;
        }

        /* Rendering */
        sprite.setPosition(x, y);
        sprite.draw(batch);
    }

    public void renderDebug(ShapeRenderer shapeBatch) {
        if (states.contains(PRIMARY)) {
            isPrimaryDebug(shapeBatch);
        } else if (states.contains(SECONDARY)) {
            isSecondaryDebug(shapeBatch);
        } else if (states.contains(TERTIARY)) {
            isTertiaryDebug(shapeBatch);
        }
    }

    /* CharacterController */
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

    /* Called from InputProcessors */
    public void setLeftMove(boolean flag) {
        this.leftMove = flag;
    }

    public void setRightMove(boolean flag) {
        this.rightMove = flag;
    }

    /* Getters */
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean getLeftMove() {
        return this.leftMove;
    }

    public boolean getRightMove() {
        return this.rightMove;
    }

    public MyGdxGame getGame() {
        return this.game;
    }

    public boolean isFalling() {
        return this.falling;
    }
}
