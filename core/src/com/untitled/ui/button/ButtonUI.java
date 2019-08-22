package com.untitled.ui.button;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.untitled.ui.UI;
import com.untitled.ui.UIAlign;

/**
 * A UI which can be clicked. Clicking will call a {@link ButtonUpCallback}.
 */
public class ButtonUI extends UI implements InputProcessor {
	private Integer touchDownPointer;
	private boolean touchedDown;
	private boolean hovering;

	private Viewport viewport;
	private Texture normal;
	private Texture hover;
	private ButtonDownCallback buttonDown;
	private ButtonUpCallback buttonUp;
	private ButtonReleaseCallback buttonRelease;

	/**
	 * @param align    where the origin (x, y) of the UI should be relative to the content.
	 * @param viewport {@link Viewport} which the button should use to detect clicking.
	 */
	public ButtonUI(UIAlign align, Viewport viewport) {
		super(align);
		this.viewport = viewport;
	}

	/**
	 * Texture that is displayed when this ButtonUI is not hovered by a mouse.
	 *
	 * @param normal {@link Texture} to display.
	 * @return this instance.
	 */
	public ButtonUI setNormalTexture(Texture normal) {
		this.normal = normal;
		return this;
	}

	/**
	 * Texture that is displayed when this ButtonUI is hovered by a mouse.
	 *
	 * @param hover {@link Texture} to display.
	 * @return this instance.
	 */
	public ButtonUI setHoverTexture(Texture hover) {
		this.hover = hover;
		return this;
	}

	/**
	 * @param buttonDown {@link ButtonUpCallback} runnable to call when this ButtonUI is pressed.
	 * @return this instance.
	 */
	public ButtonUI setButtonDown(ButtonDownCallback buttonDown) {
		this.buttonDown = buttonDown;
		return this;
	}

	/**
	 * @param buttonUp {@link ButtonUpCallback} runnable to call when this ButtonUI is released over the button.
	 * @return this instance.
	 */
	public ButtonUI setButtonUp(ButtonUpCallback buttonUp) {
		this.buttonUp = buttonUp;
		return this;
	}

	/**
	 * @param buttonRelease {@link ButtonReleaseCallback} runnable to call when this ButtonUI is released anywhere.
	 * @return this instance.
	 */
	public ButtonUI setButtonRelease(ButtonReleaseCallback buttonRelease) {
		this.buttonRelease = buttonRelease;
		return this;
	}

	@Override
	public ButtonUI setX(float x) {
		super.setX(x);
		return this;
	}

	@Override
	public ButtonUI setY(float y) {
		super.setY(y);
		return this;
	}

	@Override
	public ButtonUI setW(float w) {
		super.setW(w);
		return this;
	}

	@Override
	public ButtonUI setH(float h) {
		super.setH(h);
		return this;
	}

	@Override
	public void render(SpriteBatch batch) {
		if (this.normal != null) {
			batch.draw(this.normal, getX(), getY(), getW(), getH());
		}

		if (this.hover != null && this.hovering) {
			batch.draw(this.hover, getX(), getY(), getW(), getH());
		}
	}

	// InputProcessor
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
			updateHovering(screenX, screenY);

			// Left mouse click
			// Ensures that button is pressed down over it
			if (button == 0 && hovering) {
				this.touchedDown = true;
				this.touchDownPointer = pointer;

				if (this.buttonDown != null) {
					this.buttonDown.call();
				}
			}

		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// Ensure that this pointer is the same which started touchDown
		if (this.touchDownPointer != null && this.touchDownPointer == pointer) {
			updateHovering(screenX, screenY);

			// Ensures that button was pressed down over it
			if (button == 0 && this.touchedDown) {

				// If released over button, call buttonUp. Else call buttonRelease.
				if (this.hovering && this.buttonUp != null) {
					this.buttonUp.call();
				} else if (this.buttonRelease != null) {
					this.buttonRelease.call();
				}
			}

			this.touchedDown = false;
			this.touchDownPointer = null;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// Ensure that this pointer is the same which started touchDown
		if (this.touchDownPointer != null && this.touchDownPointer == pointer) {
			updateHovering(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		updateHovering(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	private void updateHovering(int screenX, int screenY) {
		Vector2 mouse = viewport.unproject(new Vector2(screenX, screenY));
		hovering = getX() < mouse.x && mouse.x < getX() + getW() &&
				getY() < mouse.y && mouse.y < getY() + getH();
	}
}
