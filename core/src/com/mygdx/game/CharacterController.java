package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.entity.Assassin;
import com.mygdx.game.entity.Character;
import com.mygdx.game.entity.Direction;
import com.mygdx.game.entity.Tank;

import java.util.HashMap;
import java.util.HashSet;

import static com.mygdx.game.CharacterType.ASSASSIN;
import static com.mygdx.game.CharacterType.TANK;

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
		Direction inputDirection = Direction.NONE;
		// Check that inputDirections doesn't contain RIGHT & LEFT
		if (!(inputDirections.contains(Direction.RIGHT) && inputDirections.contains(Direction.LEFT))) {
			if (inputDirections.contains(Direction.RIGHT)) {
				inputDirection = Direction.RIGHT;
			} else if (inputDirections.contains(Direction.LEFT)) {
				inputDirection = Direction.LEFT;
			}
		}
		return inputDirection;
	}
}
