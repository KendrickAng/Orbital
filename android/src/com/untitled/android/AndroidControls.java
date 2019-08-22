package com.untitled.android;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.untitled.game.character.CharacterController;

import java.util.HashSet;

public class AndroidControls implements InputProcessor {
	private static final float RADIUS = 20f;
	private Viewport viewport;
	private CharacterController characterController;
	private HashSet<Integer> keys;

	private Integer touchDownPointer;
	private Vector2 touchDown;

	public AndroidControls(Viewport viewport, CharacterController characterController) {
		this.viewport = viewport;
		this.characterController = characterController;
		this.keys = new HashSet<>();
	}

	@Override
	public boolean keyDown(int keycode) {
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
		if (this.touchDownPointer == null) {
			this.touchDown = viewport.unproject(new Vector2(screenX, screenY));
			this.touchDownPointer = pointer;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// Ensure that this pointer is the same which started touchDown
		if (this.touchDownPointer != null && this.touchDownPointer == pointer) {
			for (Integer key : this.keys) {
				this.characterController.keyUp(key);
			}

			this.keys.clear();
			this.touchDownPointer = null;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// Ensure that this pointer is the same which started touchDown
		if (this.touchDownPointer != null && this.touchDownPointer == pointer) {
			Vector2 touch = this.viewport.unproject(new Vector2(screenX, screenY));
			if (touch.x - this.touchDown.x > RADIUS) {
				if (!this.keys.contains(Input.Keys.RIGHT)) {
					this.keys.add(Input.Keys.RIGHT);
					this.characterController.keyDown(Input.Keys.RIGHT);
				}

				if (this.keys.contains(Input.Keys.LEFT)) {
					this.keys.remove(Input.Keys.LEFT);
					this.characterController.keyUp(Input.Keys.LEFT);
				}
			} else if (touch.x - this.touchDown.x < -RADIUS) {
				if (!this.keys.contains(Input.Keys.LEFT)) {
					this.keys.add(Input.Keys.LEFT);
					this.characterController.keyDown(Input.Keys.LEFT);
				}

				if (this.keys.contains(Input.Keys.RIGHT)) {
					this.keys.remove(Input.Keys.RIGHT);
					this.characterController.keyUp(Input.Keys.RIGHT);
				}
			} else {
				if (this.keys.contains(Input.Keys.LEFT)) {
					this.keys.remove(Input.Keys.LEFT);
					this.characterController.keyUp(Input.Keys.LEFT);
				}

				if (this.keys.contains(Input.Keys.RIGHT)) {
					this.keys.remove(Input.Keys.RIGHT);
					this.characterController.keyUp(Input.Keys.RIGHT);
				}
			}

			if (touch.y - touchDown.y > RADIUS) {
				if (!this.keys.contains(Input.Keys.UP)) {
					this.keys.add(Input.Keys.UP);
					this.characterController.keyDown(Input.Keys.UP);
				}
			} else {
				if (this.keys.contains(Input.Keys.UP)) {
					this.keys.remove(Input.Keys.UP);
					this.characterController.keyUp(Input.Keys.UP);
				}
			}
		}
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
