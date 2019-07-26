package com.untitled.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.untitled.assets.Assets;
import com.untitled.assets.TextureName;
import com.untitled.game.ability.CooldownState;

import java.util.ArrayList;

public class Cooldowns extends UI {
	private ArrayList<Ability> abilities;

	private Texture cooldown0;
	private Texture cooldown1;
	private Texture cooldown2;
	private Texture cooldown3;
	private Texture cooldown4;
	private Texture cooldown5;

	public Cooldowns(UIAlign align, Assets assets) {
		super(align);
		abilities = new ArrayList<>();

		cooldown0 = assets.getTexture(TextureName.COOLDOWN_0);
		cooldown1 = assets.getTexture(TextureName.COOLDOWN_1);
		cooldown2 = assets.getTexture(TextureName.COOLDOWN_2);
		cooldown3 = assets.getTexture(TextureName.COOLDOWN_3);
		cooldown4 = assets.getTexture(TextureName.COOLDOWN_4);
		cooldown5 = assets.getTexture(TextureName.COOLDOWN_5);

		setH(cooldown0.getHeight());
	}

	private class Ability {
		private CooldownState state;
		private Texture texture;

		private Ability(CooldownState state, Texture texture) {
			this.state = state;
			this.texture = texture;
		}
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

	public Cooldowns add(CooldownState state, Texture texture) {
		abilities.add(new Ability(state, texture));
		return this;
	}

	public void render(SpriteBatch batch) {
		for (int i = 0; i < abilities.size(); i++) {
			Ability ability = abilities.get(i);
			Texture texture = ability.texture;
			Texture cooldown = null;
			switch (ability.state.get()) {
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

			batch.draw(texture, getX() + i * 30, getY());
			if (cooldown != null) {
				batch.draw(cooldown, getX() + i * 30, getY());
			}
		}
	}
}
