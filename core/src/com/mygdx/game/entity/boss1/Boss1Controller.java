package com.mygdx.game.entity.boss1;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.Direction;

import java.util.HashSet;

import static com.mygdx.game.entity.Direction.*;

public class Boss1Controller implements InputProcessor {
    private Boss1 boss1;
    private HashSet<Direction> inputDirections;

    public Boss1Controller(GameScreen game) {
        this.boss1 = game.getBoss1();
        inputDirections = new HashSet<>();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.J:
                inputDirections.add(Direction.LEFT);
                boss1.setInputDirection(resolveInputDirection());
                break;
            case Input.Keys.L:
                inputDirections.add(Direction.RIGHT);
                boss1.setInputDirection(resolveInputDirection());
                break;
            case Input.Keys.Z: // primary smash
                boss1.usePrimary();
                break;
            case Input.Keys.X: // secondary rocks
                boss1.useSecondary();
                break;
            case Input.Keys.C: // tertiary roll
                boss1.useTertiary();
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
                boss1.setInputDirection(resolveInputDirection());
                break;
            case Input.Keys.L:
                inputDirections.remove(Direction.RIGHT);
                boss1.setInputDirection(resolveInputDirection());
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
