package com.mygdx.game.entity.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.boss1.Boss1;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffDefinition;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.part.TankParts;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;
import com.mygdx.game.entity.state.TankStates;

import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.character.CharacterInput.LEFT_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.LEFT_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.PRIMARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.PRIMARY_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.RIGHT_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.RIGHT_KEYUP;
import static com.mygdx.game.entity.part.TankParts.BODY;
import static com.mygdx.game.entity.part.TankParts.LEFT_ARM;
import static com.mygdx.game.entity.part.TankParts.LEFT_LEG;
import static com.mygdx.game.entity.part.TankParts.RIGHT_ARM;
import static com.mygdx.game.entity.part.TankParts.RIGHT_LEG;
import static com.mygdx.game.entity.part.TankParts.SHIELD;
import static com.mygdx.game.entity.part.TankParts.WEAPON;
import static com.mygdx.game.entity.state.TankStates.PRIMARY_STANDING;
import static com.mygdx.game.entity.state.TankStates.PRIMARY_WALKING_LEFT;
import static com.mygdx.game.entity.state.TankStates.PRIMARY_WALKING_RIGHT;
import static com.mygdx.game.entity.state.TankStates.SECONDARY;
import static com.mygdx.game.entity.state.TankStates.STANDING;
import static com.mygdx.game.entity.state.TankStates.TERTIARY;
import static com.mygdx.game.entity.state.TankStates.WALKING_LEFT;
import static com.mygdx.game.entity.state.TankStates.WALKING_RIGHT;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character<CharacterInput, TankStates, TankParts> {
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
	protected void defineStates(States<CharacterInput, TankStates> states) {
		Debuff primaryDebuff = new Debuff(DebuffType.SLOW, PRIMARY_SLOW_MODIFIER, 0);
		states.add(new State<CharacterInput, TankStates>(STANDING)
						.addEdge(LEFT_KEYDOWN, WALKING_LEFT)
						.addEdge(RIGHT_KEYDOWN, WALKING_RIGHT)
						.addEdge(LEFT_KEYUP, WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, WALKING_LEFT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_STANDING))

				.add(new State<CharacterInput, TankStates>(WALKING_LEFT)
						.defineBegin(() -> setFlipX(true))
						.addEdge(LEFT_KEYUP, STANDING)
						.addEdge(RIGHT_KEYDOWN, STANDING)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_WALKING_LEFT))

				.add(new State<CharacterInput, TankStates>(WALKING_RIGHT)
						.defineBegin(() -> setFlipX(false))
						.addEdge(RIGHT_KEYUP, STANDING)
						.addEdge(LEFT_KEYDOWN, STANDING)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_WALKING_RIGHT))

				/* Block */
				.add(new State<CharacterInput, TankStates>(PRIMARY_STANDING)
						.defineBegin(() -> inflictDebuff(primaryDebuff))
						.defineEnd(() -> cancelDebuff(primaryDebuff))
						.addEdge(LEFT_KEYDOWN, PRIMARY_WALKING_LEFT)
						.addEdge(RIGHT_KEYDOWN, PRIMARY_WALKING_RIGHT)
						.addEdge(LEFT_KEYUP, PRIMARY_WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, PRIMARY_WALKING_LEFT)
						.addEdge(PRIMARY_KEYUP, STANDING))

				.add(new State<CharacterInput, TankStates>(PRIMARY_WALKING_LEFT)
						.defineBegin(() -> inflictDebuff(primaryDebuff))
						.defineEnd(() -> cancelDebuff(primaryDebuff))
						.addEdge(LEFT_KEYUP, PRIMARY_STANDING)
						.addEdge(RIGHT_KEYDOWN, PRIMARY_STANDING)
						.addEdge(PRIMARY_KEYUP, WALKING_LEFT))

				.add(new State<CharacterInput, TankStates>(PRIMARY_WALKING_RIGHT)
						.defineBegin(() -> inflictDebuff(primaryDebuff))
						.defineEnd(() -> cancelDebuff(primaryDebuff))
						.addEdge(RIGHT_KEYUP, PRIMARY_STANDING)
						.addEdge(LEFT_KEYDOWN, PRIMARY_STANDING)
						.addEdge(PRIMARY_KEYUP, WALKING_RIGHT));
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
		final Animation<TankParts> primary = new Animation<>(PRIMARY_ANIMATION_DURATION, false)
				.addFrameTask(1, () -> inflictDebuff(ARMOR));
		final Animation<TankParts> secondary = new Animation<>(SECONDARY_ANIMATION_DURATION, false)
				.addFrameTask(1, () -> {
					Boss1 boss = getGame().getBoss1();
					if (getHitbox(WEAPON).hitTest(boss.getHitbox(Boss1Parts.BODY))) {
						Gdx.app.log("Tank.java", "Boss was hit!");
						boss.damage(SECONDARY_DAMAGE);
					}
				});

		standing.load("Tank/Standing", filenames);
		walking.load("Tank/Walking", filenames);
		primary.load("Tank/Primary", filenames);
		secondary.load("Tank/Secondary", filenames);

		animations.map(STANDING, standing)
				.map(WALKING_LEFT, walking)
				.map(PRIMARY_STANDING, primary)
				.map(PRIMARY_WALKING_LEFT, primary)
				.map(PRIMARY_WALKING_RIGHT, primary)
				.map(SECONDARY, secondary);
	}

	@Override
	protected void defineAbilities(Abilities<TankStates> abilities) {
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

		abilities.map(SECONDARY, SECONDARY_COOLDOWN)
				.map(TERTIARY, TERTIARY_COOLDOWN);
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
			case WALKING_LEFT:
			case PRIMARY_WALKING_LEFT:
				if (getFlipX()) {
					velocity.x -= MOVESPEED;
				} else {
					velocity.x += MOVESPEED;
				}
				velocity.x *= FRICTION;
				break;
		}
	}

	protected void damage() {
		/*
		if (getState() == PRIMARY_PERFECT) {
			...
		}
		*/
	}
}
