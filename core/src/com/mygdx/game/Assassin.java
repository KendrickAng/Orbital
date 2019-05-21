package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.mygdx.game.MyGdxGame.GRAVITATIONAL_ACC;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;

public class Assassin extends Character {
    private static final int JUMP_VELOCITY = 20;
    private static final int DASH_VELOCITY = 5;

    static {
        ASSASSIN_STANDING = new Sprite(new Texture(Gdx.files.internal("Tank/Standing.png")));
        ASSASSIN_PRIMARY = new Sprite(new Texture(Gdx.files.internal("Tank/Primary.png")));
        ASSASSIN_SECONDARY = new Sprite(new Texture(Gdx.files.internal("Tank/Secondary.png")));
        ASSASSIN_TERTIARY = new Sprite(new Texture(Gdx.files.internal("Tank/Tertiary.png")));
    }

    private static Sprite ASSASSIN_STANDING;
    private static Sprite ASSASSIN_PRIMARY;
    private static Sprite ASSASSIN_SECONDARY;
    private static Sprite ASSASSIN_TERTIARY;

    private Sprite current_sprite;
    private AssassinState state;
    private int y_velocity;
    private int x_velocity;

    /**
     * Initialises the tank at coordinates (0, MAP_HEIGHT).
     */
    public Assassin() {
        super(ASSASSIN_STANDING.getWidth(), ASSASSIN_STANDING.getHeight());
        state = new AssassinState();
        current_sprite = ASSASSIN_STANDING;
        y_velocity = 0;
        x_velocity = 0;
    }

    @Override
    public void render() {
        // draw character
        SpriteBatch batch = MyGdxGame.getSpriteBatch();
        current_sprite.setPosition(getX(), getY());
        batch.begin();
        // reset sprite if nothing is pressed
        if(!state.isPrimaryPressed() && !state.isSecondaryPressed() && !state.isTertiaryPressed()) {
            current_sprite = ASSASSIN_STANDING;
            current_sprite.setPosition(getX(), getY());
        }
        alignSprite();
        current_sprite.draw(batch);
        batch.end();

        // update persist states
        if(state.isPrimaryPressed()) {
            // cant jump again until the ground is touched
            Gdx.app.log("Assassin.java", "Primary skill pressed");
            if(!state.in_air) {
                state.in_air = true;
                current_sprite = ASSASSIN_PRIMARY;
                primary();
            }
            state.setPrimaryPressed(false);
        }

        // update y-position based on velocity
        int x = super.getX() + x_velocity;
        int y = super.getY() + y_velocity;
        // correct for edge cases
        if(y <= MAP_HEIGHT) {
            y_velocity = 0;
            x_velocity = 0;
            y = MAP_HEIGHT;
            state.in_air = false;
        } else {
            y_velocity += GRAVITATIONAL_ACC;
        }
        super.move(x, y);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void setDirection(Direction d) {
        state.setDirection(d);
    }

    @Override
    public void alignSprite() {
        switch(state.getDirection()) {
            case LEFT:
                current_sprite.setFlip(true, false);
                break;
            case RIGHT:
                current_sprite.setFlip(false, false);
                break;
        }
    }

    // Jump
    @Override
    public void primary() {
        y_velocity = JUMP_VELOCITY;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x_velocity = DASH_VELOCITY;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x_velocity = -DASH_VELOCITY;
        }
    }

    // Throwing stars
    @Override
    public void secondary() {

    }

    // Cleanse
    @Override
    public void tertiary() {

    }

    @Override
    public void setPrimaryPressed(boolean flag) { state.setPrimaryPressed(flag); }
    @Override
    public void setSecondaryPressed(boolean flag) { state.setSecondaryPressed(flag); }
    @Override
    public void setTertiaryPressed(boolean flag) { state.setTertiaryPressed(flag); }
}
