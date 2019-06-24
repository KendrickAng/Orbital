package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.GameScreen;

import java.util.HashSet;

import static com.mygdx.game.entity.Direction.*;

// TODO: The start of something new
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
        boolean eventHandled = false;
        switch(keycode) {
            case Input.Keys.J:
                Gdx.app.log("BossController.java", "handling left input DOWN");
                inputDirections.add(Direction.LEFT);
                boss.setInputDirection(resolveInputDirection());
                eventHandled = true;
                break;
            case Input.Keys.L:
                inputDirections.add(Direction.RIGHT);
                boss.setInputDirection(resolveInputDirection());
                eventHandled = true;
                break;
            case Input.Keys.Z: // primary smash
                boss.usePrimary();
                eventHandled = true;
                break;
            case Input.Keys.X: // secondary rocks
                boss.useSecondary();
                eventHandled = false;
        }
        return eventHandled;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean eventHandled = false;
        switch (keycode) {
            case Input.Keys.J:
                Gdx.app.log("BossController.java", "handling left input UP");
                inputDirections.remove(Direction.LEFT);
                boss.setInputDirection(resolveInputDirection());
                eventHandled = true;
                break;
            case Input.Keys.L:
                inputDirections.remove(Direction.RIGHT);
                boss.setInputDirection(resolveInputDirection());
                eventHandled = true;
                break;
        }
        return eventHandled;
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
