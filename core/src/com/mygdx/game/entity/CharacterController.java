package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.GameScreen;

import java.util.HashSet;

import static com.mygdx.game.entity.Direction.RIGHT;
import static com.mygdx.game.entity.Direction.LEFT;
import static com.mygdx.game.entity.Direction.UP;
import static com.mygdx.game.entity.Direction.UP_LEFT;
import static com.mygdx.game.entity.Direction.UP_RIGHT;

public class CharacterController implements InputProcessor {
	private GameScreen game;
	private Character character;
	private HashSet<Direction> inputDirections;

	public CharacterController(GameScreen game) {
		this.game = game;
		Gdx.input.setInputProcessor(this);
		inputDirections = new HashSet<Direction>();
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public Character getCharacter() {
		return character;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.LEFT:
				inputDirections.add(Direction.LEFT);
				character.setInputDirection(resolveInputDirection());
				break;
			case Input.Keys.RIGHT:
				inputDirections.add(Direction.RIGHT);
				character.setInputDirection(resolveInputDirection());
				break;
			case Input.Keys.UP:
				inputDirections.add(UP);
				character.setInputDirection(resolveInputDirection());
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
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.LEFT:
				inputDirections.remove(Direction.LEFT);
				character.setInputDirection(resolveInputDirection());
				break;
			case Input.Keys.RIGHT:
				inputDirections.remove(Direction.RIGHT);
				character.setInputDirection(resolveInputDirection());
				break;
			case Input.Keys.UP:
				inputDirections.remove(UP);
				character.setInputDirection(resolveInputDirection());
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
