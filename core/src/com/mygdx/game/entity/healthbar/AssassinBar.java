package com.mygdx.game.entity.healthbar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.entity.LivingEntity;

public class AssassinBar extends CharacterBar {
	public AssassinBar(LivingEntity entity) {
		super(entity, new Texture(Gdx.files.internal("HealthBar/AssassinBar.png")));
	}
}
