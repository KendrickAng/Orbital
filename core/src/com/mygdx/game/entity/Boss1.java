package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Animations;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.state.Boss1States;
import com.mygdx.game.state.States;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.state.Boss1States.STANDING;
import static com.mygdx.game.texture.Textures.BOSS1_STANDING;

public class Boss1 extends LivingEntity<Boss1> {
	public Boss1(MyGdxGame game) {
		super(game);
		setPosition(GAME_WIDTH - getWidth(), MAP_HEIGHT);
	}

	@Override
	protected Abilities<Boss1> abilities() {
		return new Abilities<Boss1>();
	}

	@Override
	protected States<Boss1> states() {
		return new States<Boss1>()
				.add(STANDING);
	}

	@Override
	protected Animations<Boss1> animations() {
		return new Animations<Boss1>()
				.add(STANDING, getGame().getTextureManager().get(BOSS1_STANDING), 1);
	}

	@Override
	protected void updatePosition(Vector2 position) {

	}

	@Override
	protected void updateVelocity(Vector2 position, Vector2 velocity) {

	}
}
