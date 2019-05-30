package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.animation.AnimationsGroup;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.state.Boss1States;
import com.mygdx.game.entity.state.States;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.part.Boss1Parts.BODY;
import static com.mygdx.game.entity.state.Boss1States.STANDING;

public class Boss1 extends LivingEntity<Boss1States, Boss1Parts> {
	private static final float HEALTH = 1000;

	public Boss1(GameScreen game) {
		super(game);
		setPosition(GAME_WIDTH - getHitbox(BODY).getWidth(), MAP_HEIGHT);
	}

	@Override
	protected void defineStates(States<Boss1States> states) {
		states.addState(STANDING);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	@Override
	protected void defineAbilities(Abilities<Boss1States> abilities) {

	}

	@Override
	protected void defineDebuffs(Debuffs<DebuffType> debuffs) {

	}

	/* Animations */
	@Override
	protected void defineAnimations(Animations<Boss1States, Boss1Parts> animations) {
		AnimationsGroup<Boss1Parts> standing = new AnimationsGroup<Boss1Parts>("Boss1/Standing", 1)
				.add(BODY, "Body")
				.load();

		animations.map(STANDING, standing);
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
