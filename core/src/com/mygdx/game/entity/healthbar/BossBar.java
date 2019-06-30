package com.mygdx.game.entity.healthbar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.entity.LivingEntity;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;

public class BossBar extends HealthBar {
	private static final int PADDING = 20;

	public BossBar(LivingEntity entity) {
		super(entity, new Texture(Gdx.files.internal("HealthBar/BossBar.png")));
		setX(PADDING);
		setY(PADDING);
		setWidth(GAME_WIDTH - PADDING * 2);
	}
}