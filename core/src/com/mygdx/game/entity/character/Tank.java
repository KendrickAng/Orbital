package com.mygdx.game.entity.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.boss1.Boss1;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.part.TankParts;
import com.mygdx.game.entity.state.States;
import com.mygdx.game.entity.state.TankStates;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.part.TankParts.BODY;
import static com.mygdx.game.entity.part.TankParts.LEFT_ARM;
import static com.mygdx.game.entity.part.TankParts.LEFT_LEG;
import static com.mygdx.game.entity.part.TankParts.RIGHT_ARM;
import static com.mygdx.game.entity.part.TankParts.RIGHT_LEG;
import static com.mygdx.game.entity.part.TankParts.SHIELD;
import static com.mygdx.game.entity.part.TankParts.WEAPON;
import static com.mygdx.game.entity.state.TankStates.PRIMARY_STANDING;
import static com.mygdx.game.entity.state.TankStates.PRIMARY_WALKING;
import static com.mygdx.game.entity.state.TankStates.SECONDARY;
import static com.mygdx.game.entity.state.TankStates.STANDING;
import static com.mygdx.game.entity.state.TankStates.TERTIARY;
import static com.mygdx.game.entity.state.TankStates.WALKING;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character<TankStates, TankParts> {
	private static final float MOVESPEED = 2f;
	private static final float FRICTION = 0.6f;

	private static final float HEALTH = 100;

	private static final float SECONDARY_DAMAGE = 10;

	// Skill debuff duration in seconds.
	private static final float TERTIARY_DEBUFF_DURATION = 5f;

	// Skill debuff modifiers from 0f - 1f (0% - 100%)
	private static final float PRIMARY_SLOW_MODIFIER = 0.5f;
	private static final float SECONDARY_SLOW_MODIFIER = 0.9f;
	private static final float TERTIARY_SLOW_MODIFIER = 0.5f;

	// Skill cooldown in seconds.
	private static final float PRIMARY_COOLDOWN = 0f;
	private static final float SECONDARY_COOLDOWN = 1f;
	private static final float TERTIARY_COOLDOWN = TERTIARY_DEBUFF_DURATION + 2f;

	// Skill animation duration in seconds.
	private static final float STANDING_ANIMATION_DURATION = 1f;
	private static final float WALKING_ANIMATION_DURATION = 1f;
	private static final float PRIMARY_ANIMATION_DURATION = 0.5f;
	private static final float SECONDARY_ANIMATION_DURATION = 0.5f;
	private static final float TERTIARY_ANIMATION_DURATION = 0.5f;

	public Tank(GameScreen game) {
		super(game);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	/* Animations */
	@Override
	protected void defineStates(States<TankStates> states) {
		states.mapState(STANDING, Arrays.asList(WALKING, PRIMARY_STANDING, SECONDARY))
				.mapState(WALKING, Arrays.asList(STANDING, PRIMARY_WALKING, SECONDARY))
				.setState(STANDING);
	}

	@Override
	protected void defineAnimations(Animations<TankStates, TankParts> animations) {
		HashMap<String, TankParts> filenames = new HashMap<>();
		filenames.put("Shield", SHIELD);
		filenames.put("LeftArm", LEFT_ARM);
		filenames.put("LeftLeg", LEFT_LEG);
		filenames.put("Body", BODY);
		filenames.put("RightLeg", RIGHT_LEG);
		filenames.put("Weapon", WEAPON);
		filenames.put("RightArm", RIGHT_ARM);

		// Static animations. Will not be mutated again.
		final Animation<TankParts> standing = new Animation<>(STANDING_ANIMATION_DURATION, true);
		final Animation<TankParts> walking = new Animation<>(WALKING_ANIMATION_DURATION, true);
		final Animation<TankParts> primary = new Animation<>(PRIMARY_ANIMATION_DURATION, false);
		final Animation<TankParts> secondary = new Animation<>(SECONDARY_ANIMATION_DURATION, false);

		standing.load("Tank/Standing", filenames);
		walking.load("Tank/Walking", filenames);
		primary.load("Tank/Primary", filenames);
		secondary.load("Tank/Secondary", filenames);

		animations.map(STANDING, standing)
				.map(WALKING, walking)
				.map(PRIMARY_STANDING, primary)
				.map(PRIMARY_WALKING, primary)
				.map(SECONDARY, secondary);
	}

	@Override
	protected void defineAbilities(Abilities<TankStates> abilities) {
		/* Block */
		Ability primary = new Ability(PRIMARY_ANIMATION_DURATION, PRIMARY_COOLDOWN)
				.setAbilityBegin(() -> {
					inflictDebuff(DebuffType.SLOW, PRIMARY_SLOW_MODIFIER, PRIMARY_ANIMATION_DURATION);
					inflictDebuff(DebuffType.IGNORE_MOVE_DIRECTION, 0, PRIMARY_ANIMATION_DURATION);
				});

		/* Slash */
		Ability secondary = new Ability(SECONDARY_ANIMATION_DURATION, SECONDARY_COOLDOWN)
				.setAbilityBegin(() -> {
					inflictDebuff(DebuffType.SLOW, SECONDARY_SLOW_MODIFIER, SECONDARY_ANIMATION_DURATION);
					inflictDebuff(DebuffType.IGNORE_MOVE_DIRECTION, 0, SECONDARY_ANIMATION_DURATION);
				}).addAbilityTask(() -> {
					Boss1 boss = getGame().getBoss1();
					if (getHitbox(WEAPON).hitTest(boss.getHitbox(Boss1Parts.BODY))) {
						Gdx.app.log("Tank.java", "Boss was hit!");
						boss.damage(SECONDARY_DAMAGE);
					}
				}, SECONDARY_ANIMATION_DURATION / 2);

		/* Fortress */
		Ability tertiary = new Ability(TERTIARY_ANIMATION_DURATION, TERTIARY_COOLDOWN)
				.setAbilityBegin(() -> {
					Gdx.app.log("Tank.java", "Tertiary");
					inflictDebuff(DebuffType.SLOW, TERTIARY_SLOW_MODIFIER, TERTIARY_DEBUFF_DURATION);
				});

		abilities.map(PRIMARY_STANDING, primary)
				.map(PRIMARY_WALKING, primary)
				.map(SECONDARY, secondary)
				.map(TERTIARY, tertiary);
	}


	/* Update */
	@Override
	protected void updatePosition(Vector2 position) {
		float x = getHitbox(BODY).getOffsetX();
		float width = getHitbox(BODY).getWidth();
		if (position.x < -x) {
			position.x = -x;
		}

		if (position.x > GAME_WIDTH - x - width) {
			position.x = GAME_WIDTH - x - width;
		}
	}

	@Override
	protected void updateVelocity(Vector2 position, Vector2 velocity) {
		switch (getState()) {
			case WALKING:
			case PRIMARY_WALKING:
				if (getFlipX()) {
					velocity.x -= MOVESPEED;
				} else {
					velocity.x += MOVESPEED;
				}
				velocity.x *= FRICTION;
				break;
		}
	}

	@Override
	protected void usePrimary() {
		switch (getState()) {
			case STANDING:
				setState(PRIMARY_STANDING);
				break;
			case WALKING:
				setState(PRIMARY_WALKING);
				break;
		}
	}

	@Override
	protected void useSecondary() {
		setState(SECONDARY);
	}

	@Override
	protected void useTertiary() {

	}
}
