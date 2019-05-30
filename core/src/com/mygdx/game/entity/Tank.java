package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.animation.AnimationsGroup;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.part.TankParts;
import com.mygdx.game.entity.state.CharacterStates;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.part.TankParts.BODY;
import static com.mygdx.game.entity.part.TankParts.LEFT_ARM;
import static com.mygdx.game.entity.part.TankParts.LEFT_LEG;
import static com.mygdx.game.entity.part.TankParts.RIGHT_ARM;
import static com.mygdx.game.entity.part.TankParts.RIGHT_LEG;
import static com.mygdx.game.entity.part.TankParts.SHIELD;
import static com.mygdx.game.entity.part.TankParts.WEAPON;
import static com.mygdx.game.entity.state.CharacterStates.PRIMARY;
import static com.mygdx.game.entity.state.CharacterStates.SECONDARY;
import static com.mygdx.game.entity.state.CharacterStates.STANDING;
import static com.mygdx.game.entity.state.CharacterStates.TERTIARY;
import static com.mygdx.game.entity.state.CharacterStates.WALKING;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character<TankParts> {
	private static final float HEALTH = 100;

	private static final float SECONDARY_DAMAGE = 10;

	// Skill debuff duration in seconds.
	private static final float TERTIARY_DEBUFF_DURATION = 5f;

	// Skill debuff modifiers from 0f - 1f (0% - 100%)
	private static final float PRIMARY_SLOW_MODIFIER = 0.5f;
	private static final float SECONDARY_SLOW_MODIFIER = 0.5f;
	private static final float TERTIARY_SLOW_MODIFIER = 0.5f;

	// Skill cooldown in seconds.
	private static final float PRIMARY_COOLDOWN = 0;
	private static final float SECONDARY_COOLDOWN = 1;
	private static final float TERTIARY_COOLDOWN = TERTIARY_DEBUFF_DURATION + 2;

	// Skill animation duration in seconds.
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

	/* Block */
	@Override
	protected Ability initPrimary() {
		return new Ability(PRIMARY_ANIMATION_DURATION, PRIMARY_COOLDOWN)
				.setAbilityBegin(() -> {
					Gdx.app.log("Tank.java", "Primary");
					inflictDebuff(DebuffType.SLOW, PRIMARY_SLOW_MODIFIER, PRIMARY_ANIMATION_DURATION);
				});
	}

	/* Slash */
	@Override
	protected Ability initSecondary() {
		return new Ability(SECONDARY_ANIMATION_DURATION, SECONDARY_COOLDOWN)
				.setAbilityBegin(() -> {
					Gdx.app.log("Tank.java", "Secondary");
					inflictDebuff(DebuffType.SLOW, SECONDARY_SLOW_MODIFIER, SECONDARY_ANIMATION_DURATION);
				}).addAbilityTask(() -> {
					Boss1 boss = getGame().getBoss();
					if (getHitbox(WEAPON).hitTest(boss.getHitbox(Boss1Parts.BODY))) {
						Gdx.app.log("Tank.java", "Boss was hit!");
						boss.damage(SECONDARY_DAMAGE);
					}
				}, SECONDARY_ANIMATION_DURATION / 2);
	}

	/* Fortress */
	@Override
	protected Ability initTertiary() {
		return new Ability(TERTIARY_ANIMATION_DURATION, TERTIARY_COOLDOWN)
				.setAbilityBegin(() -> {
					Gdx.app.log("Tank.java", "Tertiary");
					inflictDebuff(DebuffType.SLOW, TERTIARY_SLOW_MODIFIER, TERTIARY_DEBUFF_DURATION);
				});
	}

	/* Animations */

	@Override
	protected void defineAnimations(Animations<CharacterStates, TankParts> animations) {
		AnimationsGroup<TankParts> standing = new AnimationsGroup<TankParts>("Tank/Standing", 1)
				.add(SHIELD, "Shield")
				.add(LEFT_ARM, "LeftArm")
				.add(LEFT_LEG, "LeftLeg")
				.add(BODY, "Body")
				.add(RIGHT_LEG, "RightLeg")
				.add(WEAPON, "Weapon")
				.add(RIGHT_ARM, "RightArm")
				.load();

		AnimationsGroup<TankParts> walking = new AnimationsGroup<TankParts>("Tank/Walking", 1)
				.add(SHIELD, "Shield")
				.add(LEFT_ARM, "LeftArm")
				.add(LEFT_LEG, "LeftLeg")
				.add(BODY, "Body")
				.add(RIGHT_LEG, "RightLeg")
				.add(WEAPON, "Weapon")
				.add(RIGHT_ARM, "RightArm")
				.load();

		AnimationsGroup<TankParts> primary = new AnimationsGroup<TankParts>("Tank/Primary", 2)
				.add(BODY, "Body")
				.load();

		AnimationsGroup<TankParts> secondary = new AnimationsGroup<TankParts>("Tank/Secondary", 2)
				.add(SHIELD, "Shield")
				.add(LEFT_ARM, "LeftArm")
				.add(LEFT_LEG, "LeftLeg")
				.add(BODY, "Body")
				.add(RIGHT_LEG, "RightLeg")
				.add(WEAPON, "Weapon")
				.add(RIGHT_ARM, "RightArm")
				.load();

		AnimationsGroup<TankParts> tertiary = new AnimationsGroup<TankParts>("Tank/Tertiary", 2)
				.add(BODY, "Body")
				.load();

		animations.map(STANDING, standing)
				.map(WALKING, walking)
				.map(PRIMARY, primary)
				.map(SECONDARY, secondary)
				.map(TERTIARY, tertiary);
	}


	/* Update */
	@Override
	protected void updatePosition(Vector2 position) {
		float x = getHitbox(BODY).getX();
		float width = getHitbox(BODY).getWidth();
		// TODO: Fix
		if (position.x < -x) {
			position.x = -x;
		}

		if (position.x > GAME_WIDTH - x - width) {
			position.x = GAME_WIDTH - x - width;
		}
	}
}
