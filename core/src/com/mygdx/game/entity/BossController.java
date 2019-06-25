package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.GameScreen;

import java.util.HashSet;

import static com.mygdx.game.entity.Direction.*;

public class BossController implements InputProcessor {
    private GameScreen game;
    private Boss1 boss;
    private HashSet<Direction> inputDirections;

    public BossController(GameScreen game) {
        this.game = game;
        inputDirections = new HashSet<>();
    }

    public void setBoss(Boss1 boss) { this.boss = boss;}
    public Boss1 getBoss() { return this.boss; }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.J:
                inputDirections.add(Direction.LEFT);
                boss.setInputDirection(resolveInputDirection());
                break;
            case Input.Keys.L:
                inputDirections.add(Direction.RIGHT);
                boss.setInputDirection(resolveInputDirection());
                break;
            case Input.Keys.Z: // primary smash
                boss.usePrimary();
                break;
            case Input.Keys.X: // secondary rocks
                boss.useSecondary();
                break;
            case Input.Keys.C: // tertiary roll
                boss.useTertiary();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.J:
                inputDirections.remove(Direction.LEFT);
                boss.setInputDirection(resolveInputDirection());
                break;
            case Input.Keys.L:
                inputDirections.remove(Direction.RIGHT);
                boss.setInputDirection(resolveInputDirection());
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private Direction resolveInputDirection() {
        if (!inputDirections.contains(Direction.RIGHT)
                && !inputDirections.contains(Direction.LEFT)
                && inputDirections.contains(UP)) {
            return UP;
        }

        if (inputDirections.contains(RIGHT)
                && !inputDirections.contains(LEFT)
                && !inputDirections.contains(UP)) {
            return RIGHT;
        }

        if (!inputDirections.contains(Direction.RIGHT)
                && inputDirections.contains(Direction.LEFT)
                && !inputDirections.contains(UP)) {
            return LEFT;
        }

        return Direction.NONE;
    }
}
