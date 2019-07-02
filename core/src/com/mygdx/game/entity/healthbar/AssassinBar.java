package com.mygdx.game.entity.healthbar;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.entity.LivingEntity;

import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_ASSASSIN;

public class AssassinBar extends CharacterBar {
	public AssassinBar(Assets assets, LivingEntity entity) {
		super(assets, entity, assets.getTexture(HEALTH_BAR_ASSASSIN));
	}
}
