package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

import static com.mygdx.game.screens.NameScreen.CHARACTERS;

public class NameProcessor implements InputProcessor {
	private Array<Character> stack;

	public NameProcessor(Array<Character> stack) {
		this.stack = stack;
	}

	@Override
	public boolean keyDown(int keycode) {
		// Add to stack
		if (Input.Keys.A <= keycode && keycode <= Input.Keys.Z
				&& stack.size < CHARACTERS) {
			stack.add((char) (keycode - Input.Keys.A + 'A'));

			// Remove from stack
		} else if (keycode == Input.Keys.BACKSPACE
				&& !stack.isEmpty()) {
			stack.pop();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
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
}
