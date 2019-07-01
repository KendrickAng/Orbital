package com.mygdx.game.entity.character;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffType;
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
import static com.mygdx.game.entity.character.CharacterInput.SWITCH_CHARACTER;
import static com.mygdx.game.entity.character.CharacterInput.TERTIARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.TERTIARY_KEYUP;
import static com.mygdx.game.entity.character.TankStates.PRIMARY;
import static com.mygdx.game.entity.character.TankStates.PRIMARY_LEFT;
import static com.mygdx.game.entity.character.TankStates.PRIMARY_LEFT_RIGHT;
import static com.mygdx.game.entity.character.TankStates.PRIMARY_RIGHT;
import static com.mygdx.game.entity.character.TankStates.SECONDARY;
import static com.mygdx.game.entity.character.TankStates.SECONDARY_LEFT;
import static com.mygdx.game.entity.character.TankStates.SECONDARY_LEFT_RIGHT;
import static com.mygdx.game.entity.character.TankStates.SECONDARY_RIGHT;
import static com.mygdx.game.entity.character.TankStates.STANDING;
import static com.mygdx.game.entity.character.TankStates.STANDING_LEFT_RIGHT;
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
public class Tank extends Character<TankStates, TankParts> {
	private static final float MOVESPEED = 2f;

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
		primaryArmorDebuff = new Debuff(DebuffType.DAMAGE_REDUCTION, 1f, 0);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	/* Animations */
	@Override
	protected void defineStates(States<CharacterInput, TankStates> states) {
		states.add(new State<CharacterInput, TankStates>(STANDING)
				.addEdge(LEFT_KEYDOWN, WALKING_LEFT)
				.addEdge(RIGHT_KEYDOWN, WALKING_RIGHT)
				.addEdge(PRIMARY_KEYDOWN, PRIMARY)
				.addEdge(SECONDARY_KEYDOWN, SECONDARY)
				.addEdge(TERTIARY_KEYDOWN, TERTIARY)
				.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<CharacterInput, TankStates>(STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, WALKING_LEFT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_LEFT_RIGHT)
						.addEdge(SECONDARY_KEYDOWN, SECONDARY_LEFT_RIGHT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<CharacterInput, TankStates>(WALKING_LEFT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(() -> {
							getPosition().x -= MOVESPEED * (1 - getSlow());
							checkWithinMap();
						})
						.addEdge(LEFT_KEYUP, STANDING)
						.addEdge(RIGHT_KEYDOWN, STANDING_LEFT_RIGHT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_LEFT)
						.addEdge(SECONDARY_KEYDOWN, SECONDARY_LEFT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<CharacterInput, TankStates>(WALKING_RIGHT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(() -> {
							getPosition().x += MOVESPEED * (1 - getSlow());
							checkWithinMap();
						})
						.addEdge(RIGHT_KEYUP, STANDING)
						.addEdge(LEFT_KEYDOWN, STANDING_LEFT_RIGHT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_RIGHT)
						.addEdge(SECONDARY_KEYDOWN, SECONDARY_RIGHT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				/* Block */
				.add(new State<CharacterInput, TankStates>(PRIMARY)
						.addEdge(LEFT_KEYDOWN, PRIMARY_LEFT)
						.addEdge(RIGHT_KEYDOWN, PRIMARY_RIGHT)
						.addEdge(PRIMARY_KEYUP, STANDING))

				.add(new State<CharacterInput, TankStates>(PRIMARY_LEFT)
						.defineUpdate(() -> {
							getPosition().x -= MOVESPEED * (1 - getSlow());
							checkWithinMap();
						})
						.addEdge(LEFT_KEYUP, PRIMARY)
						.addEdge(RIGHT_KEYDOWN, PRIMARY_LEFT_RIGHT)
						.addEdge(PRIMARY_KEYUP, WALKING_LEFT))

				.add(new State<CharacterInput, TankStates>(PRIMARY_RIGHT)
						.defineUpdate(() -> {
							getPosition().x += MOVESPEED * (1 - getSlow());
							checkWithinMap();
						})
						.addEdge(RIGHT_KEYUP, PRIMARY)
						.addEdge(LEFT_KEYDOWN, PRIMARY_LEFT_RIGHT)
						.addEdge(PRIMARY_KEYUP, WALKING_RIGHT))

				.add(new State<CharacterInput, TankStates>(PRIMARY_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, PRIMARY_RIGHT)
						.addEdge(RIGHT_KEYUP, PRIMARY_LEFT)
						.addEdge(PRIMARY_KEYUP, STANDING_LEFT_RIGHT))

				/* Slash */
				.add(new State<CharacterInput, TankStates>(SECONDARY)
						.addEdge(LEFT_KEYDOWN, SECONDARY_LEFT)
						.addEdge(RIGHT_KEYDOWN, SECONDARY_RIGHT)
						.addEdge(SECONDARY_KEYUP, STANDING))

				.add(new State<CharacterInput, TankStates>(SECONDARY_LEFT)
						.addEdge(LEFT_KEYUP, SECONDARY)
						.addEdge(RIGHT_KEYDOWN, SECONDARY_LEFT_RIGHT)
						.addEdge(SECONDARY_KEYUP, WALKING_LEFT))

				.add(new State<CharacterInput, TankStates>(SECONDARY_RIGHT)
						.addEdge(RIGHT_KEYUP, SECONDARY)
						.addEdge(LEFT_KEYDOWN, SECONDARY_LEFT_RIGHT)
						.addEdge(SECONDARY_KEYUP, WALKING_RIGHT))

				.add(new State<CharacterInput, TankStates>(SECONDARY_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, SECONDARY_RIGHT)
						.addEdge(RIGHT_KEYUP, SECONDARY_LEFT)
						.addEdge(SECONDARY_KEYUP, STANDING_LEFT_RIGHT))

				/* Fortress */
				.add(new State<CharacterInput, TankStates>(TERTIARY)
						.addEdge(TERTIARY_KEYUP, STANDING));
	}

	@Override
	protected void defineAnimations(Animations<TankStates, TankParts> animations) {
		HashMap<TankParts, String> filenames = new HashMap<>();
		filenames.put(SHIELD, "Shield");
		filenames.put(LEFT_ARM, "LeftArm");
		filenames.put(LEFT_LEG, "LeftLeg");
		filenames.put(BODY, "Body");
		filenames.put(RIGHT_LEG, "RightLeg");
		filenames.put(WEAPON, "Weapon");
		filenames.put(RIGHT_ARM, "RightArm");

		Animation<TankParts> standing =
				new Animation<>(STANDING_ANIMATION_DURATION, 2, "Tank/Standing", filenames)
						.loop();

		Animation<TankParts> walking =
				new Animation<>(WALKING_ANIMATION_DURATION, 2, "Tank/Walking", filenames)
						.loop();

		Animation<TankParts> primary =
				new Animation<>(PRIMARY_ANIMATION_DURATION, 1, "Tank/Primary", filenames)
						.defineFrameTask(0, () -> inflictDebuff(primaryArmorDebuff));

		Animation<TankParts> secondary =
				new Animation<>(SECONDARY_ANIMATION_DURATION, 1, "Tank/Secondary", filenames)
						.defineFrameTask(0, () -> getGame().getBoss1()
								.damageTest(getHitbox(WEAPON), SECONDARY_DAMAGE))
						.defineEnd(() -> input(SECONDARY_KEYUP));

		animations.map(STANDING, standing)
				.map(STANDING_LEFT_RIGHT, standing)
				.map(WALKING_LEFT, walking)
				.map(WALKING_RIGHT, walking)

				.map(PRIMARY, primary)
				.map(PRIMARY_LEFT_RIGHT, primary)
				.map(PRIMARY_LEFT, primary)
				.map(PRIMARY_RIGHT, primary)

				.map(SECONDARY, secondary)
				.map(SECONDARY_LEFT, secondary)
				.map(SECONDARY_RIGHT, secondary)
				.map(SECONDARY_LEFT_RIGHT, secondary);
	}

	@Override
	protected void defineAbilities(Abilities<TankStates> abilities) {
		Debuff primarySlowDebuff = new Debuff(DebuffType.SLOW, PRIMARY_SLOW_MODIFIER, 0);
		Debuff tertiaryDebuff = new Debuff(DebuffType.SLOW, TERTIARY_SLOW_MODIFIER, TERTIARY_DEBUFF_DURATION);

		/* Block */
		Ability<TankStates> primary = new Ability<>(PRIMARY_COOLDOWN);
		primary.defineBegin((state) -> inflictDebuff(primarySlowDebuff))
				.defineEnd(() -> {
					cancelDebuff(primarySlowDebuff);
					cancelDebuff(primaryArmorDebuff);
					primary.reset();
				});

		/* Slash */
		Ability<TankStates> secondary = new Ability<>(SECONDARY_COOLDOWN);

		/* Fortress */
		Ability<TankStates> tertiary = new Ability<TankStates>(TERTIARY_COOLDOWN)
				.defineBegin((state) -> {
					Gdx.app.log("Tank.java", "Tertiary");
					inflictDebuff(tertiaryDebuff);
				});

		abilities.addBegin(PRIMARY, primary)
				.addBegin(PRIMARY_LEFT, primary)
				.addBegin(PRIMARY_RIGHT, primary)
				.addBegin(PRIMARY_LEFT_RIGHT, primary)
				.addEnd(STANDING, primary)
				.addEnd(WALKING_LEFT, primary)
				.addEnd(WALKING_RIGHT, primary)
				.addEnd(STANDING_LEFT_RIGHT, primary)

				.addBegin(SECONDARY, secondary)
				.addBegin(SECONDARY_LEFT, secondary)
				.addBegin(SECONDARY_RIGHT, secondary)
				.addBegin(SECONDARY_LEFT_RIGHT, secondary)
				.addEnd(STANDING, secondary)
				.addEnd(WALKING_LEFT, secondary)
				.addEnd(WALKING_RIGHT, secondary)
				.addEnd(STANDING_LEFT_RIGHT, secondary)

				.addBegin(TERTIARY, tertiary)
				.addEnd(STANDING, tertiary);
	}

	private void checkWithinMap() {
		float x = getHitbox(BODY).getOffsetX();
		float width = getHitbox(BODY).getWidth();
		if (getPosition().x < -x) {
			getPosition().x = -x;
		}

		if (getPosition().x > GAME_WIDTH - x - width) {
			getPosition().x = GAME_WIDTH - x - width;
		}
	}

	@Override
	public boolean hitTest(Hitbox hitbox) {
		return !isDispose() &&
				(getHitbox(BODY).hitTest(hitbox) ||
						getHitbox(LEFT_LEG).hitTest(hitbox) ||
						getHitbox(RIGHT_LEG).hitTest(hitbox) ||
						getHitbox(LEFT_ARM).hitTest(hitbox) ||
						getHitbox(RIGHT_ARM).hitTest(hitbox));
	}

	// TODO: Abstract these out
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
	protected void useUp(boolean keydown) {

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

	@Override
	public boolean useSwitchCharacter() {
		return input(SWITCH_CHARACTER);
	}
}
