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
    private CharacterType current;
    private MyGdxGame game;

    public CharacterController(MyGdxGame game) {
        this.characters = new HashMap<CharacterType, Character>();
        this.game = game;
        init();
        this.current = TANK;
    }

    // load characters
    public void init() {
        Gdx.input.setInputProcessor(this);
        this.characters.put(TANK, new Tank(game));
        this.characters.put(ASSASSIN, new Assassin(game));
    }

    public void setCharacter(CharacterType character) {
        this.current = character;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.LEFT:
                character().setDirection(Direction.LEFT);
                character().setLeftMove(true);
                break;
            case Input.Keys.RIGHT:
                character().setDirection(Direction.RIGHT);
                character().setRightMove(true);
                break;
            case Input.Keys.Q:
                character().primary();
                break;
            case Input.Keys.W:
                character().secondary();
                break;
            case Input.Keys.E:
                character().tertiary();
                break;
            case Input.Keys.R: // switch characters
                Gdx.app.log("CharacterController.java", "switched characters");
                current = switchCharacter();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Input.Keys.LEFT:
                character().setLeftMove(false);
                break;
            case Input.Keys.RIGHT:
                character().setRightMove(false);
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

    public Character character() {
        Character c = null;
        switch(current) {
            case ASSASSIN:
                c = characters.get(ASSASSIN);
                break;
            case TANK:
                c = characters.get(TANK);
                break;
        }
        return c;
    }

    public CharacterType switchCharacter() {
        CharacterType previous = current;
        CharacterType switched = previous == ASSASSIN ? TANK : ASSASSIN;
        Character prev = characters.get(previous);
        Character next = characters.get(switched);
        next.setDirection(prev.getDirection()); // set direction and position to be equal
        next.move(prev.getX(), prev.getY());
        next.setRightMove(prev.getRightMove());
        next.setLeftMove(prev.getLeftMove());
        return switched;
    }

    public MyGdxGame getGame() { return this.game; }
}
