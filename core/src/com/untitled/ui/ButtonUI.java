package com.untitled.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A UI which can be clicked. Clicking will call a {@link ButtonCallback}.
 */
public class ButtonUI extends UI implements InputProcessor {
	private boolean touchedDown;
	private boolean hovering;

	private Viewport viewport;
	private Texture normal;
	private Texture hover;
	private ButtonCallback callback;

	/**
	 * @param align    where the origin (x, y) of the UI should be relative to the content.
	 * @param viewport {@link Viewport} which the button should use to detect clicking.
	 * @param callback {@link ButtonCallback} runnable to call when this ButtonUI is clicked.
	 */
	public ButtonUI(UIAlign align, Viewport viewport, ButtonCallback callback) {
		super(align);
		this.viewport = viewport;
		this.callback = callback;
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
		if (normal != null) {
			batch.draw(normal, getX(), getY(), getW(), getH());
		}

		if (hover != null && hovering) {
			batch.draw(hover, getX(), getY(), getW(), getH());
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
		updateHovering(screenX, screenY);

		// Ensures that button was pressed down and released on it
		if (button == 0 && hovering) {
			touchedDown = true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		updateHovering(screenX, screenY);

		// Left mouse click
		if (button == 0 && hovering && touchedDown) {
			callback.call();
		}
		touchedDown = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		updateHovering(screenX, screenY);
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
