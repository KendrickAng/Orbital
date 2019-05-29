package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Animations;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.entity.debuff.Debuffs;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.Boss1.MovingState.STANDING;
import static com.mygdx.game.texture.Textures.BOSS1_STANDING;

public class Boss1 extends LivingEntity<Boss1.MovingState, Boss1.AbilityState> {
	private static final float HEALTH = 1000;
	public enum MovingState {
		STANDING, WALKING
	}

	public enum AbilityState {
		PRIMARY, SECONDARY
	}

	public Boss1(GameScreen game) {
		super(game);
		setPosition(GAME_WIDTH - getWidth(), MAP_HEIGHT);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	@Override
	protected Abilities<AbilityState> abilities() {
		return new Abilities<AbilityState>(getAbilityStates());
	}


	@Override
	protected MovingState basicState() {
		return STANDING;
	}

	/* Animations */
	@Override
	protected Animations<MovingState> basicAnimations() {
		return new Animations<MovingState>()
				.add(STANDING, getGame().getTextureManager().get(BOSS1_STANDING), 1);
	}

	@Override
	protected Animations<AbilityState> abilityAnimations() {
		return null;
	}

	@Override
	protected Debuffs debuffs() {
		return new Debuffs();
	}

	/* Update */
	@Override
	protected void updateDirection(Direction inputDirection) {

	}

	@Override
	protected void updatePosition(Vector2 position) {

	}

	@Override
	protected void updateVelocity(Vector2 position, Vector2 velocity) {

	}
}
