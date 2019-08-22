package com.untitled.ui.cooldowns;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.untitled.game.ability.CooldownState;
import com.untitled.ui.UI;
import com.untitled.ui.UIAlign;
import com.untitled.ui.button.ButtonDownCallback;
import com.untitled.ui.button.ButtonReleaseCallback;
import com.untitled.ui.button.ButtonUI;

import java.util.ArrayList;

/**
 * A {@link UI} which renders multiple cooldowns in a horizonal row.
 */
public abstract class Cooldowns extends UI {
	private ArrayList<CooldownAbility> abilities;

	private Texture cooldown0;
	private Texture cooldown1;
	private Texture cooldown2;
	private Texture cooldown3;
	private Texture cooldown4;
	private Texture cooldown5;

	/**
	 * @param align     where the origin (x, y) of the UI should be relative to the content.
	 * @param cooldown0 Texture to display when skill is on 6/6 cooldown.
	 * @param cooldown1 Texture to display when skill is on 5/6 cooldown.
	 * @param cooldown2 Texture to display when skill is on 4/6 cooldown.
	 * @param cooldown3 Texture to display when skill is on 3/6 cooldown.
	 * @param cooldown4 Texture to display when skill is on 2/6 cooldown.
	 * @param cooldown5 Texture to display when skill is on 1/6 cooldown.
	 */
	public Cooldowns(UIAlign align, Texture cooldown0, Texture cooldown1, Texture cooldown2, Texture cooldown3, Texture cooldown4, Texture cooldown5) {
		super(align);
		abilities = new ArrayList<>();

		this.cooldown0 = cooldown0;
		this.cooldown1 = cooldown1;
		this.cooldown2 = cooldown2;
		this.cooldown3 = cooldown3;
		this.cooldown4 = cooldown4;
		this.cooldown5 = cooldown5;

		setH(cooldown0.getHeight());
	}

	@Override
	public Cooldowns setX(float x) {
		super.setX(x);
		return this;
	}

	@Override
	public Cooldowns setY(float y) {
		super.setY(y);
		return this;
	}

	/**
	 * Maps a {@link CooldownState} to a {@link Texture}.
	 *
	 * @param state   a {@link CooldownState} of (usually) an Ability.
	 * @param texture a {@link Texture} to graphically represent the Ability.
	 * @return this instance
	 */
	public Cooldowns add(CooldownState state, Texture texture) {
		abilities.add(new CooldownAbility(state, texture, null));
		return this;
	}

	/**
	 * Maps a {@link CooldownState} to a {@link Texture}.
	 *
	 * @param state            a {@link CooldownState} of (usually) an Ability.
	 * @param texture          a {@link Texture} to graphically represent the Ability.
	 * @param inputMultiplexer inputMultiplexer to add button to.
	 * @param viewport         Viewport which button should be in.
	 * @param buttonDown       a {@link ButtonDownCallback} which is called when the cooldown is pressed.
	 * @param buttonRelease    a {@link ButtonReleaseCallback} which is called when the cooldown is released.
	 * @return this instance
	 */
	public Cooldowns add(CooldownState state, Texture texture, InputMultiplexer inputMultiplexer, Viewport viewport, ButtonDownCallback buttonDown, ButtonReleaseCallback buttonRelease) {
		ButtonUI buttonUI = new ButtonUI(UIAlign.BOTTOM_LEFT, viewport)
				.setButtonDown(buttonDown)
				.setButtonRelease(buttonRelease);
		inputMultiplexer.addProcessor(buttonUI);
		abilities.add(new CooldownAbility(state, texture, buttonUI));
		return this;
	}

	@Override
	public void render(SpriteBatch batch) {
		Vector2 position = new Vector2(getX(), getY());

		for (CooldownAbility ability : abilities) {
			Texture cooldown = null;
			switch (ability.getState().get()) {
				case 0:
					cooldown = cooldown0;
					break;
				case 1:
					cooldown = cooldown1;
					break;
				case 2:
					cooldown = cooldown2;
					break;
				case 3:
					cooldown = cooldown3;
					break;
				case 4:
					cooldown = cooldown4;
					break;
				case 5:
					cooldown = cooldown5;
					break;
			}

			ability.render(batch, position.x, position.y);
			if (cooldown != null) {
				batch.draw(cooldown, position.x, position.y);
			}

			update(position, ability);
		}
	}

	protected abstract void update(Vector2 position, CooldownAbility ability);
}
