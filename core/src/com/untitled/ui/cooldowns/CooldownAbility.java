package com.untitled.ui.cooldowns;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.untitled.game.ability.CooldownState;
import com.untitled.ui.button.ButtonUI;

class CooldownAbility {
	private CooldownState state;
	private Texture texture;
	private ButtonUI buttonUI;

	CooldownAbility(CooldownState state, Texture texture, ButtonUI buttonUI) {
		this.state = state;
		this.texture = texture;
		this.buttonUI = buttonUI;

		if (this.buttonUI != null) {
			this.buttonUI.setW(texture.getWidth());
			this.buttonUI.setH(texture.getHeight());
		}
	}

	public CooldownState getState() {
		return state;
	}

	int getWidth() {
		return texture.getWidth();
	}

	int getHeight() {
		return texture.getHeight();
	}

	void render(SpriteBatch batch, float x, float y) {
		batch.draw(this.texture, x, y);

		if (this.buttonUI != null) {
			this.buttonUI.setX(x);
			this.buttonUI.setY(y);
		}
	}
}