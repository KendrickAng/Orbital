package com.mygdx.game.entity.healthbar;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.entity.LivingEntity;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_BOSS;

public class BossBar extends HealthBar {
	private static final int PADDING = 20;

	public BossBar(Assets assets, LivingEntity entity) {
		super(assets, entity, assets.getTexture(HEALTH_BAR_BOSS));
		setX(PADDING);
		setY(PADDING);
		setWidth(GAME_WIDTH - PADDING * 2);
	}
}