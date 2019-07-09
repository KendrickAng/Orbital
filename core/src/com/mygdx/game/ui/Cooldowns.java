package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.entity.ability.CooldownState;

import java.util.ArrayList;

import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_0;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_1;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_2;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_3;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_4;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_5;

public class Cooldowns {
	private ArrayList<Ability> abilities;

	private Texture cooldown0;
	private Texture cooldown1;
	private Texture cooldown2;
	private Texture cooldown3;
	private Texture cooldown4;
	private Texture cooldown5;

	private float x;
	private float y;

	public Cooldowns(Assets assets) {
		abilities = new ArrayList<>();

		cooldown0 = assets.getTexture(COOLDOWN_0);
		cooldown1 = assets.getTexture(COOLDOWN_1);
		cooldown2 = assets.getTexture(COOLDOWN_2);
		cooldown3 = assets.getTexture(COOLDOWN_3);
		cooldown4 = assets.getTexture(COOLDOWN_4);
		cooldown5 = assets.getTexture(COOLDOWN_5);
	}

	private class Ability {
		private CooldownState state;
		private Texture texture;

		private Ability(CooldownState state, Texture texture) {
			this.state = state;
			this.texture = texture;
		}
	}

	public Cooldowns setX(float x) {
		this.x = x;
		return this;
	}

	public Cooldowns setY(float y) {
		this.y = y;
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

			batch.draw(texture, x + i * 30, y);
			if (cooldown != null) {
				batch.draw(cooldown, x + i * 30, y);
			}
		}
	}
}
