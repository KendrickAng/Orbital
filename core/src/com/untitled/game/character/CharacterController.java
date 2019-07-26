package com.untitled.game.character;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.Collection;
import java.util.HashSet;

/**
 * An {@link InputProcessor} which allows any user to control a {@link Character}.
 */
public class CharacterController implements InputProcessor {
	private Character character;
	private HashSet<CharacterControllerInput> inputs;

	/**
	 * @param character the {@link Character} to control.
	 */
	public CharacterController(Character character) {
		this.character = character;
		this.inputs = new HashSet<>();
	}

	/**
	 * @param character the {@link Character} to control.
	 */
	public void setCharacter(Character character) {
		this.character = character;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.LEFT:
				character.useLeft(true);
				inputs.add(CharacterControllerInput.LEFT);
				break;
			case Input.Keys.RIGHT:
				character.useRight(true);
				inputs.add(CharacterControllerInput.RIGHT);
				break;
			case Input.Keys.UP:
				character.useUp(true);
				inputs.add(CharacterControllerInput.UP);
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
				character.useSwitchCharacter();
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
				inputs.remove(CharacterControllerInput.LEFT);
				break;
			case Input.Keys.RIGHT:
				character.useRight(false);
				inputs.remove(CharacterControllerInput.RIGHT);
				break;
			case Input.Keys.UP:
				character.useUp(false);
				inputs.remove(CharacterControllerInput.UP);
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

	Collection<CharacterControllerInput> getInputs() {
		return inputs;
	}
}
