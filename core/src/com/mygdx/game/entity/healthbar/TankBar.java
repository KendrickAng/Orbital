package com.mygdx.game.entity.healthbar;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.entity.LivingEntity;

import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_TANK;

public class TankBar extends CharacterBar {
	public TankBar(Assets assets, LivingEntity entity) {
		super(assets, entity, assets.getTexture(HEALTH_BAR_TANK));
	}
}
