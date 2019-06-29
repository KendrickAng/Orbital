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
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.part.TankParts;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;

import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.character.CharacterInput.LEFT_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.LEFT_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.PRIMARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.PRIMARY_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.RIGHT_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.RIGHT_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.SECONDARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.SECONDARY_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.TERTIARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.TERTIARY_KEYUP;
import static com.mygdx.game.entity.character.TankStates.PRIMARY_STANDING;
import static com.mygdx.game.entity.character.TankStates.PRIMARY_WALKING_LEFT;
import static com.mygdx.game.entity.character.TankStates.PRIMARY_WALKING_RIGHT;
import static com.mygdx.game.entity.character.TankStates.SECONDARY;
import static com.mygdx.game.entity.character.TankStates.STANDING;
import static com.mygdx.game.entity.character.TankStates.TERTIARY;
import static com.mygdx.game.entity.character.TankStates.WALKING_LEFT;
import static com.mygdx.game.entity.character.TankStates.WALKING_RIGHT;
import static com.mygdx.game.entity.part.TankParts.BODY;
import static com.mygdx.game.entity.part.TankParts.LEFT_ARM;
import static com.mygdx.game.entity.part.TankParts.LEFT_LEG;
import static com.mygdx.game.entity.part.TankParts.RIGHT_ARM;
import static com.mygdx.game.entity.part.TankParts.RIGHT_LEG;
import static com.mygdx.game.entity.part.TankParts.SHIELD;
import static com.mygdx.game.entity.part.TankParts.WEAPON;

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

	private final Debuff primaryArmorDebuff;

	public Tank(GameScreen game) {
		super(game);
		primaryArmorDebuff = new Debuff(DebuffType.DAMAGE_REDUCTION, 0, 0);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	/* Animations */
	@Override
	protected void defineStates(States<CharacterInput, TankStates> states) {
		states.add(new State<CharacterInput, TankStates>(STANDING)
				.defineUpdateVelocity((velocity) -> velocity.x *= FRICTION)
				.addEdge(LEFT_KEYDOWN, WALKING_LEFT)
				.addEdge(RIGHT_KEYDOWN, WALKING_RIGHT)
				.addEdge(LEFT_KEYUP, WALKING_RIGHT)
				.addEdge(RIGHT_KEYUP, WALKING_LEFT)
				.addEdge(PRIMARY_KEYDOWN, PRIMARY_STANDING)
				.addEdge(SECONDARY_KEYDOWN, SECONDARY)
				.addEdge(TERTIARY_KEYDOWN, TERTIARY))

				.add(new State<CharacterInput, TankStates>(WALKING_LEFT)
						.defineBegin(() -> setFlipX(true))
						.defineUpdateVelocity((velocity) -> {
							velocity.x -= MOVESPEED * (1 - getSlow());
							velocity.x *= FRICTION;
						})
						.addEdge(LEFT_KEYUP, STANDING)
						.addEdge(RIGHT_KEYDOWN, STANDING)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_WALKING_LEFT))

				.add(new State<CharacterInput, TankStates>(WALKING_RIGHT)
						.defineBegin(() -> setFlipX(false))
						.defineUpdateVelocity((velocity) -> {
							velocity.x += MOVESPEED * (1 - getSlow());
							velocity.x *= FRICTION;
						})
						.addEdge(RIGHT_KEYUP, STANDING)
						.addEdge(LEFT_KEYDOWN, STANDING)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_WALKING_RIGHT))

				/* Block */
				.add(new State<CharacterInput, TankStates>(PRIMARY_STANDING)
						.defineUpdateVelocity((velocity) -> velocity.x *= FRICTION)
						.addEdge(LEFT_KEYDOWN, PRIMARY_WALKING_LEFT)
						.addEdge(RIGHT_KEYDOWN, PRIMARY_WALKING_RIGHT)
						.addEdge(LEFT_KEYUP, PRIMARY_WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, PRIMARY_WALKING_LEFT)
						.addEdge(PRIMARY_KEYUP, STANDING))

				.add(new State<CharacterInput, TankStates>(PRIMARY_WALKING_LEFT)
						.defineUpdateVelocity((velocity) -> {
							velocity.x -= MOVESPEED * (1 - getSlow());
							velocity.x *= FRICTION;
						})
						.addEdge(LEFT_KEYUP, PRIMARY_STANDING)
						.addEdge(RIGHT_KEYDOWN, PRIMARY_STANDING)
						.addEdge(PRIMARY_KEYUP, WALKING_LEFT))

				.add(new State<CharacterInput, TankStates>(PRIMARY_WALKING_RIGHT)
						.defineUpdateVelocity((velocity) -> {
							velocity.x += MOVESPEED * (1 - getSlow());
							velocity.x *= FRICTION;
						})
						.addEdge(RIGHT_KEYUP, PRIMARY_STANDING)
						.addEdge(LEFT_KEYDOWN, PRIMARY_STANDING)
						.addEdge(PRIMARY_KEYUP, WALKING_RIGHT))

				/* Slash */
				.add(new State<CharacterInput, TankStates>(SECONDARY)
						.defineUpdateVelocity((velocity) -> velocity.x *= FRICTION)
						.addEdge(SECONDARY_KEYUP, STANDING))

				/* Fortress */
				.add(new State<CharacterInput, TankStates>(TERTIARY)
						.addEdge(TERTIARY_KEYUP, STANDING));
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

		Animation<TankParts> standing = new Animation<>(STANDING_ANIMATION_DURATION, true);
		Animation<TankParts> walking = new Animation<>(WALKING_ANIMATION_DURATION, true);
		Animation<TankParts> primary = new Animation<TankParts>(PRIMARY_ANIMATION_DURATION, false)
				.defineFrameTask(1, () -> inflictDebuff(primaryArmorDebuff));
		Animation<TankParts> secondary = new Animation<TankParts>(SECONDARY_ANIMATION_DURATION, false)
				.defineFrameTask(1, () -> {
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
				.map(WALKING_RIGHT, walking)
				.map(PRIMARY_STANDING, primary)
				.map(PRIMARY_WALKING_LEFT, primary)
				.map(PRIMARY_WALKING_RIGHT, primary)
				.map(SECONDARY, secondary);
	}

	@Override
	protected void defineAbilities(Abilities<TankStates> abilities) {
		Debuff primarySlowDebuff = new Debuff(DebuffType.SLOW, PRIMARY_SLOW_MODIFIER, 0);
		Debuff tertiaryDebuff = new Debuff(DebuffType.SLOW, TERTIARY_SLOW_MODIFIER, TERTIARY_DEBUFF_DURATION);

		/* Block */
		Ability primary = new Ability(PRIMARY_COOLDOWN)
				.defineBegin(() -> inflictDebuff(primarySlowDebuff))
				.defineEnd(() -> {
					cancelDebuff(primarySlowDebuff);
					cancelDebuff(primaryArmorDebuff);
				});

		/* Slash */
		Ability secondary = new Ability(SECONDARY_COOLDOWN);

		/* Fortress */
		Ability tertiary = new Ability(TERTIARY_COOLDOWN)
				.defineBegin(() -> {
					Gdx.app.log("Tank.java", "Tertiary");
					inflictDebuff(tertiaryDebuff);
				});

		abilities.addCancel(STANDING, primary)
				.addCancel(WALKING_LEFT, primary)
				.addCancel(WALKING_RIGHT, primary)
				.defineUse(PRIMARY_STANDING, primary)
				.defineUse(PRIMARY_WALKING_LEFT, primary)
				.defineUse(PRIMARY_WALKING_RIGHT, primary)

				.addCancel(STANDING, secondary)
				.defineUse(SECONDARY, secondary)

				.addCancel(STANDING, tertiary)
				.defineUse(TERTIARY, tertiary);
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

	protected void damage() {
		/*
		if (getState() == PRIMARY_PERFECT) {
			...
		}
		*/
	}

	@Override
	protected void useLeft(boolean keydown) {
		if (keydown) {
			input(LEFT_KEYDOWN);
		} else {
			input(LEFT_KEYUP);
		}
	}

	@Override
	protected void useRight(boolean keydown) {
		if (keydown) {
			input(RIGHT_KEYDOWN);
		} else {
			input(RIGHT_KEYUP);
		}
	}

	@Override
	protected void usePrimary(boolean keydown) {
		if (keydown) {
			input(PRIMARY_KEYDOWN);
		} else {
			input(PRIMARY_KEYUP);
		}
	}

	@Override
	protected void useSecondary(boolean keydown) {
		if (keydown) {
			input(SECONDARY_KEYDOWN);
		} else {
			input(SECONDARY_KEYUP);
		}
	}

	@Override
	protected void useTertiary(boolean keydown) {
		if (keydown) {
			input(TERTIARY_KEYDOWN);
		} else {
			input(TERTIARY_KEYUP);
		}
	}
}
