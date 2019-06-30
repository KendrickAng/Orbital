package com.mygdx.game.entity.character;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.GameScreen;

import java.util.HashSet;

import static com.mygdx.game.entity.character.CharacterInput.LEFT_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.RIGHT_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.UP_KEYDOWN;

public class CharacterController implements InputProcessor {
	private GameScreen game;
	private Character character;
	private HashSet<CharacterInput> inputs;

	public CharacterController(GameScreen game) {
		this.game = game;
		this.character = game.getCharacter();
		this.inputs = new HashSet<>();
	}

	public void update() {
		character = game.getCharacter();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.LEFT:
				character.useLeft(true);
				inputs.add(LEFT_KEYDOWN);
				break;
			case Input.Keys.RIGHT:
				character.useRight(true);
				inputs.add(RIGHT_KEYDOWN);
				break;
			case Input.Keys.UP:
				character.useUp(true);
				inputs.add(UP_KEYDOWN);
				break;
			case Input.Keys.Q:
				character.usePrimary(true);
				break;
			case Input.Keys.W:
				character.useSecondary(true);
				break;
			case Input.Keys.E:
				character.useTertiary(true);
				break;
			case Input.Keys.R: // switch characters
				game.switchCharacter(inputs);
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
				character.useLeft(false);
				inputs.remove(LEFT_KEYDOWN);
				break;
			case Input.Keys.RIGHT:
				character.useRight(false);
				inputs.remove(RIGHT_KEYDOWN);
				break;
			case Input.Keys.UP:
				character.useUp(false);
				inputs.remove(UP_KEYDOWN);
				break;
			case Input.Keys.Q:
				character.usePrimary(false);
				break;
			case Input.Keys.W:
				character.useSecondary(false);
				break;
			case Input.Keys.E:
				character.useTertiary(false);
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
}
