package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

import static com.mygdx.game.CharacterType.TANK;
import static com.mygdx.game.CharacterType.ASSASSIN;
import static com.mygdx.game.CharacterType.BOSS;

public class CharacterController implements InputProcessor {
    private HashMap<CharacterType, Character> characters;
    private Character current;

    public CharacterController() {
        this.characters = new HashMap<CharacterType, Character>();
        init();
        this.current = characters.get(TANK);
    }

    // load characters
    public void init() {
        Gdx.input.setInputProcessor(this);
        this.characters.put(TANK, new Tank());
        this.characters.put(ASSASSIN, new Assassin());
    }

    public void setCharacter(CharacterType character) {
        this.current = characters.get(character);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.LEFT:
                current.setDirection(Direction.LEFT);
                current.setLeftMove(true);
                break;
            case Input.Keys.RIGHT:
                current.setDirection(Direction.RIGHT);
                current.setRightMove(true);
                break;
            case Input.Keys.Q:
                current.primary();
                break;
            case Input.Keys.W:
                current.secondary();
                break;
            case Input.Keys.E:
                current.tertiary();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Input.Keys.LEFT:
                current.setLeftMove(false);
                break;
            case Input.Keys.RIGHT:
                current.setRightMove(false);
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }
    @Override
    public boolean scrolled(int amount) {
        return true;
    }

    /* Getters */
    public Character getCharacter() {
        return current;
    }
}
