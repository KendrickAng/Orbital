package com.untitled.game.boss1;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.untitled.screens.GameScreen;

public class Boss1Controller implements InputProcessor {
	private Boss1 boss1;

	public Boss1Controller(GameScreen game) {
		this.boss1 = game.getBoss1();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.J:
				boss1.input(Boss1Input.LEFT_KEYDOWN);
				break;
			case Input.Keys.L:
				boss1.input(Boss1Input.RIGHT_KEYDOWN);
				break;
			case Input.Keys.Z: // primary smash
				boss1.input(Boss1Input.SLAM_KEYDOWN);
				break;
			case Input.Keys.X: // secondary rocks
				boss1.input(Boss1Input.EARTHQUAKE_KEYDOWN);
				break;
			case Input.Keys.C: // tertiary roll
				boss1.input(Boss1Input.ROLL_KEYDOWN);
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
				boss1.input(Boss1Input.LEFT_KEYUP);
				break;
			case Input.Keys.L:
				boss1.input(Boss1Input.RIGHT_KEYUP);
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

    /*
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
    */
}
