package com.mygdx.game.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ButtonUI extends UI implements InputProcessor {
	private boolean hovering;

	private Viewport viewport;
	private Texture normal;
	private Texture hover;
	private ButtonCallback callback;

	public ButtonUI(UIAlign align, Viewport viewport, ButtonCallback callback) {
		super(align);
		this.viewport = viewport;
		this.callback = callback;
	}

	public ButtonUI setNormalTexture(Texture normal) {
		this.normal = normal;
		return this;
	}

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
		if (hovering) {
			callback.call();
		}
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
		Vector2 mouse = viewport.unproject(new Vector2(screenX, screenY));
		hovering = getX() < mouse.x && mouse.x < getX() + getW() &&
				getY() < mouse.y && mouse.y < getY() + getH();
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
