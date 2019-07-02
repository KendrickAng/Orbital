package com.mygdx.game.entity.healthbar;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.entity.LivingEntity;

import static com.mygdx.game.MyGdxGame.GAME_HEIGHT;
import static com.mygdx.game.MyGdxGame.GAME_WIDTH;

public abstract class CharacterBar extends HealthBar {
	private static final int PADDING = 20;

	public CharacterBar(Assets assets, LivingEntity entity, Texture texture) {
		super(assets, entity, texture);
		setX(PADDING);
		setY(GAME_HEIGHT - PADDING);
		setWidth(GAME_WIDTH / 2);
	}
}
