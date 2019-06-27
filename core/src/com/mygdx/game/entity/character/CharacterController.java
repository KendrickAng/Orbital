package com.mygdx.game.entity.character;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.Direction;

import java.util.HashSet;

import static com.mygdx.game.entity.Direction.LEFT;
import static com.mygdx.game.entity.Direction.NONE;
import static com.mygdx.game.entity.Direction.RIGHT;
import static com.mygdx.game.entity.Direction.UP;
import static com.mygdx.game.entity.Direction.UP_LEFT;
import static com.mygdx.game.entity.Direction.UP_RIGHT;
import static com.mygdx.game.entity.state.CharacterStates.STANDING;
import static com.mygdx.game.entity.state.CharacterStates.WALKING;

public class CharacterController implements InputProcessor {
	private GameScreen game;
	private Character character;
	private HashSet<Direction> inputDirections;

	public CharacterController(GameScreen game) {
		this.game = game;
		this.character = game.getCharacter();
		inputDirections = new HashSet<>();
	}

	public void update() {
		character = game.getCharacter();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.LEFT:
				inputDirections.add(Direction.LEFT);
				switch (resolveInputDirection()) {
					case LEFT:
					case UP_LEFT:
						character.setWalking();
						break;
				}
				break;
			case Input.Keys.RIGHT:
				inputDirections.add(Direction.RIGHT);
				switch (resolveInputDirection()) {
					case RIGHT:
					case UP_RIGHT:
						character.setWalking();
						break;
				}
				break;
			case Input.Keys.UP:
				inputDirections.add(UP);
				break;
			case Input.Keys.Q:
				character.usePrimary();
				break;
			case Input.Keys.W:
				character.useSecondary();
				break;
			case Input.Keys.E:
				character.useTertiary();
				break;
			case Input.Keys.R: // switch characters
				game.switchCharacter();
				break;
			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.LEFT:
				inputDirections.remove(Direction.LEFT);
				if (resolveInputDirection() == NONE) {
					character.setStanding();
				}
				break;
			case Input.Keys.RIGHT:
				inputDirections.remove(Direction.RIGHT);
				if (resolveInputDirection() == NONE) {
					character.setStanding();
				}
				break;
			case Input.Keys.UP:
				inputDirections.remove(UP);
				break;
			default:
				return false;
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

		if (!inputDirections.contains(Direction.RIGHT)
				&& inputDirections.contains(Direction.LEFT)
				&& inputDirections.contains(UP)) {
			return UP_LEFT;
		}

		if (inputDirections.contains(Direction.RIGHT)
				&& !inputDirections.contains(Direction.LEFT)
				&& inputDirections.contains(UP)) {
			return UP_RIGHT;
		}

		return Direction.NONE;
	}
}
