package com.untitled.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.untitled.assets.Assets;
import com.untitled.assets.TankAnimationName;
import com.untitled.game.Hitbox;
import com.untitled.game.ability.Abilities;
import com.untitled.game.ability.Ability;
import com.untitled.game.ability.CooldownState;
import com.untitled.game.animation.Animation;
import com.untitled.game.animation.AnimationFrameTask;
import com.untitled.game.animation.Animations;
import com.untitled.game.boss1.Boss1;
import com.untitled.game.debuff.Debuff;
import com.untitled.game.state.State;
import com.untitled.game.state.States;
import com.untitled.screens.GameScreen;

import static com.untitled.game.character.TankInput.BLOCK_INPUT;
import static com.untitled.game.character.TankInput.BLOCK_KEYDOWN;
import static com.untitled.game.character.TankInput.BLOCK_KEYUP;
import static com.untitled.game.character.TankInput.CROWD_CONTROL;
import static com.untitled.game.character.TankInput.FORTRESS_INPUT;
import static com.untitled.game.character.TankInput.FORTRESS_KEYDOWN;
import static com.untitled.game.character.TankInput.FORTRESS_KEYUP;
import static com.untitled.game.character.TankInput.HAMMER_SWING_KEYDOWN;
import static com.untitled.game.character.TankInput.HAMMER_SWING_KEYUP;
import static com.untitled.game.character.TankInput.LEFT_KEYDOWN;
import static com.untitled.game.character.TankInput.LEFT_KEYUP;
import static com.untitled.game.character.TankInput.RIGHT_KEYDOWN;
import static com.untitled.game.character.TankInput.RIGHT_KEYUP;
import static com.untitled.game.character.TankInput.SWITCH_CHARACTER;
import static com.untitled.game.debuff.DebuffType.DAMAGE_REDUCTION;
import static com.untitled.game.debuff.DebuffType.DAMAGE_REFLECT;
import static com.untitled.game.debuff.DebuffType.SLOW;
import static com.untitled.game.debuff.DebuffType.STUN;
import static com.untitled.screens.GameScreen.GAME_WIDTH;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character<TankInput, TankStates, TankParts> {
	private static final float MOVESPEED = 2f;
	private static final float HEALTH = 120f;

	// Skill cooldown in seconds.
	private static final float BLOCK_COOLDOWN = 0.2f;
	private static final float HAMMER_SWING_COOLDOWN = 1f;
	private static final float FORTRESS_DURATION = 10f;
	private static final float FORTRESS_COOLDOWN = FORTRESS_DURATION + 20f;

	// Skill animation duration in seconds.
	private static final float STANDING_ANIMATION_DURATION = 1f;
	private static final float WALKING_ANIMATION_DURATION = 1f;
	private static final float BLOCK_ANIMATION_DURATION = 0.25f;
	private static final float HAMMER_SWING_ANIMATION_DURATION = 0.75f;

	private static final float FORTRESS_ANIMATION_DURATION = 0.75f;
	private static final float FORTRESS_STANDING_ANIMATION_DURATION = 2f;
	private static final float FORTRESS_WALKING_ANIMATION_DURATION = 2f;

	// Skill modifiers
	private static final float HAMMER_SWING_DAMAGE = 20f;
	private static final float HAMMER_SWING_BONUS_DAMAGE = HAMMER_SWING_DAMAGE + 20f;

	private static final float BLOCK_SLOW_MODIFIER = 0.5f;
	private static final float FORTRESS_SLOW_MODIFIER = 0.5f;
	private static final float FORTRESS_DAMAGE_REDUCTION = 0.5f;
	private static final float FORTRESS_DAMAGE_REFLECT = 0.25f;
	private static final float FORTRESS_HEAL = 40f;

	private static final float SHIELD_BASH_DAMAGE = 40f;
	private static final float SHIELD_BASH_STUN_DURATION = 2f;

	// Scores
	private static final int SHIELD_BASH_SCORE = 10;
	private static final int PERFECT_BLOCK_SCORE = 50;
	private static final int PERFECT_FORTRESS_SCORE = 250;
	private static final int FORTRESS_HEAL_SCORE = 250;

	private static final String HEAL_TEXT = "HEAL";
	private static final Color HEAL_TEXT_COLOR = Color.valueOf("a5d6a7");

	private Ability<TankStates> blockAbility;
	private Ability<TankStates> hammerSwingAbility;
	private Ability<TankStates> fortressAbility;

	private Animation<TankParts> hammerSwingAnimation;
	private Animation<TankParts> blockAnimation;
	private Animation<TankParts> fortressBlockAnimation;

	private final Debuff blockDebuff;
	private final Debuff blockPerfectDebuff;
	private Debuff fortressDebuff;
	private final Debuff fortressPerfectDebuff;

	public Tank(GameScreen game) {
		super(game);

		blockDebuff = new Debuff(DAMAGE_REDUCTION, 0.5f, 0);
		blockPerfectDebuff = new Debuff(DAMAGE_REDUCTION, 1f, 0);
		fortressPerfectDebuff = new Debuff(DAMAGE_REDUCTION, 1f, 0);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	/* Animations */
	@Override
	protected void defineStates(States<TankInput, TankStates> states) {
		states.add(new State<TankInput, TankStates>(TankStates.STANDING)
				.addEdge(LEFT_KEYDOWN, TankStates.WALKING_LEFT)
				.addEdge(RIGHT_KEYDOWN, TankStates.WALKING_RIGHT)
				.addEdge(BLOCK_KEYDOWN, TankStates.BLOCK)
				.addEdge(HAMMER_SWING_KEYDOWN, TankStates.HAMMER_SWING)
				.addEdge(FORTRESS_KEYDOWN, TankStates.FORTRESS)
				.addEdge(SWITCH_CHARACTER, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, TankStates.WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.WALKING_LEFT)
						.addEdge(BLOCK_KEYDOWN, TankStates.BLOCK_LEFT_RIGHT)
						.addEdge(HAMMER_SWING_KEYDOWN, TankStates.HAMMER_SWING_LEFT_RIGHT)
						.addEdge(FORTRESS_KEYDOWN, TankStates.FORTRESS_LEFT_RIGHT)
						.addEdge(SWITCH_CHARACTER, TankStates.STANDING)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.WALKING_LEFT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(this::walkLeft)
						.addEdge(LEFT_KEYUP, TankStates.STANDING)
						.addEdge(RIGHT_KEYDOWN, TankStates.STANDING_LEFT_RIGHT)
						.addEdge(BLOCK_KEYDOWN, TankStates.BLOCK_LEFT)
						.addEdge(HAMMER_SWING_KEYDOWN, TankStates.HAMMER_SWING_LEFT)
						.addEdge(FORTRESS_KEYDOWN, TankStates.FORTRESS_LEFT)
						.addEdge(SWITCH_CHARACTER, TankStates.STANDING)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.WALKING_RIGHT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(this::walkRight)
						.addEdge(RIGHT_KEYUP, TankStates.STANDING)
						.addEdge(LEFT_KEYDOWN, TankStates.STANDING_LEFT_RIGHT)
						.addEdge(BLOCK_KEYDOWN, TankStates.BLOCK_RIGHT)
						.addEdge(HAMMER_SWING_KEYDOWN, TankStates.HAMMER_SWING_RIGHT)
						.addEdge(FORTRESS_KEYDOWN, TankStates.FORTRESS_RIGHT)
						.addEdge(SWITCH_CHARACTER, TankStates.STANDING)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				/* Block Animation */
				.add(new State<TankInput, TankStates>(TankStates.BLOCK)
						.addEdge(LEFT_KEYDOWN, TankStates.BLOCK_LEFT)
						.addEdge(RIGHT_KEYDOWN, TankStates.BLOCK_RIGHT)
						.addEdge(BLOCK_KEYUP, TankStates.BLOCK_UP)
						.addEdge(BLOCK_INPUT, TankStates.BLOCK_STANDING))

				.add(new State<TankInput, TankStates>(TankStates.BLOCK_LEFT)
						.defineUpdate(this::walkLeft)
						.addEdge(LEFT_KEYUP, TankStates.BLOCK)
						.addEdge(RIGHT_KEYDOWN, TankStates.BLOCK_LEFT_RIGHT)
						.addEdge(BLOCK_KEYUP, TankStates.BLOCK_UP_LEFT)
						.addEdge(BLOCK_INPUT, TankStates.BLOCK_WALKING_LEFT))

				.add(new State<TankInput, TankStates>(TankStates.BLOCK_RIGHT)
						.defineUpdate(this::walkRight)
						.addEdge(RIGHT_KEYUP, TankStates.BLOCK)
						.addEdge(LEFT_KEYDOWN, TankStates.BLOCK_LEFT_RIGHT)
						.addEdge(BLOCK_KEYUP, TankStates.BLOCK_UP_RIGHT)
						.addEdge(BLOCK_INPUT, TankStates.BLOCK_WALKING_RIGHT))

				.add(new State<TankInput, TankStates>(TankStates.BLOCK_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, TankStates.BLOCK_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.BLOCK_LEFT)
						.addEdge(BLOCK_KEYUP, TankStates.BLOCK_UP_LEFT_RIGHT)
						.addEdge(BLOCK_INPUT, TankStates.BLOCK_STANDING_LEFT_RIGHT))

				.add(new State<TankInput, TankStates>(TankStates.BLOCK_UP)
						.addEdge(LEFT_KEYDOWN, TankStates.BLOCK_UP_LEFT)
						.addEdge(RIGHT_KEYDOWN, TankStates.BLOCK_UP_RIGHT)
						.addEdge(BLOCK_KEYDOWN, TankStates.BLOCK)
						.addEdge(BLOCK_INPUT, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.BLOCK_UP_LEFT)
						.defineUpdate(this::walkLeft)
						.addEdge(LEFT_KEYUP, TankStates.BLOCK_UP)
						.addEdge(RIGHT_KEYDOWN, TankStates.BLOCK_UP_LEFT_RIGHT)
						.addEdge(BLOCK_KEYDOWN, TankStates.BLOCK_UP_LEFT)
						.addEdge(BLOCK_INPUT, TankStates.WALKING_LEFT))

				.add(new State<TankInput, TankStates>(TankStates.BLOCK_UP_RIGHT)
						.defineUpdate(this::walkRight)
						.addEdge(RIGHT_KEYUP, TankStates.BLOCK_UP)
						.addEdge(LEFT_KEYDOWN, TankStates.BLOCK_UP_LEFT_RIGHT)
						.addEdge(BLOCK_KEYDOWN, TankStates.BLOCK_RIGHT)
						.addEdge(BLOCK_INPUT, TankStates.WALKING_RIGHT))

				.add(new State<TankInput, TankStates>(TankStates.BLOCK_UP_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, TankStates.BLOCK_UP_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.BLOCK_UP_LEFT)
						.addEdge(BLOCK_KEYDOWN, TankStates.BLOCK_LEFT_RIGHT)
						.addEdge(BLOCK_INPUT, TankStates.STANDING_LEFT_RIGHT))

				/* Block */
				.add(new State<TankInput, TankStates>(TankStates.BLOCK_STANDING)
						.addEdge(LEFT_KEYDOWN, TankStates.BLOCK_WALKING_LEFT)
						.addEdge(RIGHT_KEYDOWN, TankStates.BLOCK_WALKING_RIGHT)
						.addEdge(BLOCK_KEYUP, TankStates.STANDING)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.BLOCK_WALKING_LEFT)
						.defineUpdate(this::walkLeft)
						.addEdge(LEFT_KEYUP, TankStates.BLOCK_STANDING)
						.addEdge(RIGHT_KEYDOWN, TankStates.BLOCK_STANDING_LEFT_RIGHT)
						.addEdge(BLOCK_KEYUP, TankStates.WALKING_LEFT)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.BLOCK_WALKING_RIGHT)
						.defineUpdate(this::walkRight)
						.addEdge(RIGHT_KEYUP, TankStates.BLOCK_STANDING)
						.addEdge(LEFT_KEYDOWN, TankStates.BLOCK_STANDING_LEFT_RIGHT)
						.addEdge(BLOCK_KEYUP, TankStates.WALKING_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.BLOCK_STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, TankStates.BLOCK_WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.BLOCK_WALKING_LEFT)
						.addEdge(BLOCK_KEYUP, TankStates.STANDING_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				/* Slash */
				.add(new State<TankInput, TankStates>(TankStates.HAMMER_SWING)
						.addEdge(LEFT_KEYDOWN, TankStates.HAMMER_SWING_LEFT)
						.addEdge(RIGHT_KEYDOWN, TankStates.HAMMER_SWING_RIGHT)
						.addEdge(HAMMER_SWING_KEYUP, TankStates.STANDING)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.HAMMER_SWING_LEFT)
						.addEdge(LEFT_KEYUP, TankStates.HAMMER_SWING)
						.addEdge(RIGHT_KEYDOWN, TankStates.HAMMER_SWING_LEFT_RIGHT)
						.addEdge(HAMMER_SWING_KEYUP, TankStates.WALKING_LEFT)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.HAMMER_SWING_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.HAMMER_SWING)
						.addEdge(LEFT_KEYDOWN, TankStates.HAMMER_SWING_LEFT_RIGHT)
						.addEdge(HAMMER_SWING_KEYUP, TankStates.WALKING_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.HAMMER_SWING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, TankStates.HAMMER_SWING_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.HAMMER_SWING_LEFT)
						.addEdge(HAMMER_SWING_KEYUP, TankStates.STANDING_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				/* Fortress Animation */
				.add(new State<TankInput, TankStates>(TankStates.FORTRESS)
						.addEdge(FORTRESS_INPUT, TankStates.FORTRESS_STANDING)
						.addEdge(LEFT_KEYDOWN, TankStates.FORTRESS_LEFT)
						.addEdge(RIGHT_KEYDOWN, TankStates.FORTRESS_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_LEFT_RIGHT)
						.addEdge(FORTRESS_INPUT, TankStates.FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, TankStates.FORTRESS_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.FORTRESS_LEFT)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_LEFT)
						.addEdge(FORTRESS_INPUT, TankStates.FORTRESS_WALKING_LEFT)
						.addEdge(LEFT_KEYUP, TankStates.FORTRESS)
						.addEdge(RIGHT_KEYDOWN, TankStates.FORTRESS_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_RIGHT)
						.addEdge(FORTRESS_INPUT, TankStates.FORTRESS_WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.FORTRESS)
						.addEdge(LEFT_KEYDOWN, TankStates.FORTRESS_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.STANDING))

				/* Fortress */
				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_STANDING)
						.addEdge(LEFT_KEYDOWN, TankStates.FORTRESS_WALKING_LEFT)
						.addEdge(RIGHT_KEYDOWN, TankStates.FORTRESS_WALKING_RIGHT)
						.addEdge(BLOCK_KEYDOWN, TankStates.FORTRESS_BLOCK)
						.addEdge(HAMMER_SWING_KEYDOWN, TankStates.FORTRESS_IMPALE)
						.addEdge(FORTRESS_KEYUP, TankStates.STANDING)
						.addEdge(SWITCH_CHARACTER, TankStates.FORTRESS_STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_WALKING_LEFT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(this::walkLeft)
						.addEdge(LEFT_KEYUP, TankStates.FORTRESS_STANDING)
						.addEdge(RIGHT_KEYDOWN, TankStates.FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(BLOCK_KEYDOWN, TankStates.FORTRESS_BLOCK_LEFT)
						.addEdge(HAMMER_SWING_KEYDOWN, TankStates.FORTRESS_IMPALE_LEFT)
						.addEdge(FORTRESS_KEYUP, TankStates.WALKING_LEFT)
						.addEdge(SWITCH_CHARACTER, TankStates.FORTRESS_STANDING)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_WALKING_RIGHT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(this::walkRight)
						.addEdge(RIGHT_KEYUP, TankStates.FORTRESS_STANDING)
						.addEdge(LEFT_KEYDOWN, TankStates.FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(BLOCK_KEYDOWN, TankStates.FORTRESS_BLOCK_RIGHT)
						.addEdge(HAMMER_SWING_KEYDOWN, TankStates.FORTRESS_IMPALE_RIGHT)
						.addEdge(FORTRESS_KEYUP, TankStates.WALKING_RIGHT)
						.addEdge(SWITCH_CHARACTER, TankStates.FORTRESS_STANDING)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, TankStates.FORTRESS_WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.FORTRESS_WALKING_LEFT)
						.addEdge(BLOCK_KEYDOWN, TankStates.FORTRESS_BLOCK_LEFT_RIGHT)
						.addEdge(HAMMER_SWING_KEYDOWN, TankStates.FORTRESS_IMPALE_LEFT_RIGHT)
						.addEdge(FORTRESS_KEYUP, TankStates.STANDING_LEFT_RIGHT)
						.addEdge(SWITCH_CHARACTER, TankStates.FORTRESS_STANDING)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING))

				/* Fortress Block */
				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_BLOCK)
						.addEdge(LEFT_KEYDOWN, TankStates.FORTRESS_BLOCK_LEFT)
						.addEdge(RIGHT_KEYDOWN, TankStates.FORTRESS_BLOCK_RIGHT)
						.addEdge(BLOCK_KEYUP, TankStates.FORTRESS_STANDING)
						.addEdge(FORTRESS_KEYUP, TankStates.BLOCK)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_BLOCK_LEFT)
						.defineUpdate(this::walkLeft)
						.addEdge(LEFT_KEYUP, TankStates.FORTRESS_BLOCK)
						.addEdge(RIGHT_KEYDOWN, TankStates.FORTRESS_BLOCK_LEFT_RIGHT)
						.addEdge(BLOCK_KEYUP, TankStates.FORTRESS_WALKING_LEFT)
						.addEdge(FORTRESS_KEYUP, TankStates.BLOCK_LEFT)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_BLOCK_RIGHT)
						.defineUpdate(this::walkRight)
						.addEdge(RIGHT_KEYUP, TankStates.FORTRESS_BLOCK)
						.addEdge(LEFT_KEYDOWN, TankStates.FORTRESS_BLOCK_LEFT_RIGHT)
						.addEdge(BLOCK_KEYUP, TankStates.FORTRESS_WALKING_RIGHT)
						.addEdge(FORTRESS_KEYUP, TankStates.BLOCK_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_BLOCK_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, TankStates.FORTRESS_BLOCK_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.FORTRESS_BLOCK_LEFT)
						.addEdge(BLOCK_KEYUP, TankStates.FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(FORTRESS_KEYUP, TankStates.BLOCK_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING))

				/* Fortress Impale */
				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_IMPALE)
						.addEdge(LEFT_KEYDOWN, TankStates.FORTRESS_IMPALE_LEFT)
						.addEdge(RIGHT_KEYDOWN, TankStates.FORTRESS_IMPALE_RIGHT)
						.addEdge(HAMMER_SWING_KEYUP, TankStates.FORTRESS_STANDING)
						.addEdge(FORTRESS_KEYUP, TankStates.HAMMER_SWING)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_IMPALE_LEFT)
						.addEdge(LEFT_KEYUP, TankStates.FORTRESS_IMPALE)
						.addEdge(RIGHT_KEYDOWN, TankStates.FORTRESS_IMPALE_LEFT_RIGHT)
						.addEdge(HAMMER_SWING_KEYUP, TankStates.FORTRESS_WALKING_LEFT)
						.addEdge(FORTRESS_KEYUP, TankStates.HAMMER_SWING_LEFT)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_IMPALE_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.FORTRESS_IMPALE)
						.addEdge(LEFT_KEYDOWN, TankStates.FORTRESS_IMPALE_LEFT_RIGHT)
						.addEdge(HAMMER_SWING_KEYUP, TankStates.FORTRESS_WALKING_RIGHT)
						.addEdge(FORTRESS_KEYUP, TankStates.HAMMER_SWING_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING))

				.add(new State<TankInput, TankStates>(TankStates.FORTRESS_IMPALE_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, TankStates.FORTRESS_IMPALE_RIGHT)
						.addEdge(RIGHT_KEYUP, TankStates.FORTRESS_IMPALE_LEFT)
						.addEdge(HAMMER_SWING_KEYUP, TankStates.FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(FORTRESS_KEYUP, TankStates.HAMMER_SWING_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, TankStates.FORTRESS_STANDING));
	}

	@Override
	protected void defineAnimations(Animations<TankStates, TankParts> animations, Assets assets) {
		Animation<TankParts> standing = assets.getTankAnimation(TankAnimationName.STANDING)
				.setDuration(STANDING_ANIMATION_DURATION)
				.setLoop();

		Animation<TankParts> walking = assets.getTankAnimation(TankAnimationName.WALKING)
				.setDuration(WALKING_ANIMATION_DURATION)
				.setLoop();

		blockAnimation = assets.getTankAnimation(TankAnimationName.BLOCK)
				.setDuration(BLOCK_ANIMATION_DURATION)
				.defineEnd(() -> {
					cancelDebuff(blockPerfectDebuff);
					inflictDebuff(blockDebuff);
					input(BLOCK_INPUT);
				});

		hammerSwingAnimation = assets.getTankAnimation(TankAnimationName.HAMMER_SWING)
				.setDuration(HAMMER_SWING_ANIMATION_DURATION)
				.defineEnd(() -> input(HAMMER_SWING_KEYUP));
		initImpaleHitTest();

		Animation<TankParts> fortress = assets.getTankAnimation(TankAnimationName.FORTRESS)
				.setDuration(FORTRESS_ANIMATION_DURATION)
				.defineFrameTask(5, () -> inflictDebuff(fortressPerfectDebuff))
				.defineFrameTask(9, () -> cancelDebuff(fortressPerfectDebuff))
				.defineEnd(() -> input(FORTRESS_INPUT));

		Animation<TankParts> fortressStanding = assets.getTankAnimation(TankAnimationName.FORTRESS_STANDING)
				.setDuration(FORTRESS_STANDING_ANIMATION_DURATION)
				.setLoop();

		Animation<TankParts> fortressWalking = assets.getTankAnimation(TankAnimationName.FORTRESS_WALKING)
				.setDuration(FORTRESS_WALKING_ANIMATION_DURATION)
				.setLoop();

		fortressBlockAnimation = assets.getTankAnimation(TankAnimationName.FORTRESS_BLOCK)
				.setDuration(BLOCK_ANIMATION_DURATION)
				.defineEnd(() -> {
					cancelDebuff(blockPerfectDebuff);
					inflictDebuff(blockDebuff);
					input(BLOCK_INPUT);
				});

		Animation<TankParts> fortressImpale = assets.getTankAnimation(TankAnimationName.FORTRESS_IMPALE)
				.setDuration(HAMMER_SWING_ANIMATION_DURATION)
				.defineFrameTask(2, () -> {
					Boss1 boss1 = getGame().getBoss1();
					if (boss1 != null) {
						boss1.damageTest(this, getHitbox(TankParts.WEAPON), HAMMER_SWING_DAMAGE);
					}
				})
				.defineEnd(() -> input(HAMMER_SWING_KEYUP));

		animations.map(TankStates.STANDING, standing)
				.map(TankStates.STANDING_LEFT_RIGHT, standing)
				.map(TankStates.WALKING_LEFT, walking)
				.map(TankStates.WALKING_RIGHT, walking)

				.map(TankStates.BLOCK, blockAnimation)
				.map(TankStates.BLOCK_LEFT_RIGHT, blockAnimation)
				.map(TankStates.BLOCK_LEFT, blockAnimation)
				.map(TankStates.BLOCK_RIGHT, blockAnimation)

				.map(TankStates.HAMMER_SWING, hammerSwingAnimation)
				.map(TankStates.HAMMER_SWING_LEFT, hammerSwingAnimation)
				.map(TankStates.HAMMER_SWING_RIGHT, hammerSwingAnimation)
				.map(TankStates.HAMMER_SWING_LEFT_RIGHT, hammerSwingAnimation)

				.map(TankStates.FORTRESS, fortress)
				.map(TankStates.FORTRESS_LEFT, fortress)
				.map(TankStates.FORTRESS_RIGHT, fortress)
				.map(TankStates.FORTRESS_LEFT_RIGHT, fortress)

				.map(TankStates.FORTRESS_STANDING, fortressStanding)
				.map(TankStates.FORTRESS_STANDING_LEFT_RIGHT, fortressStanding)
				.map(TankStates.FORTRESS_WALKING_LEFT, fortressWalking)
				.map(TankStates.FORTRESS_WALKING_RIGHT, fortressWalking)

				.map(TankStates.FORTRESS_BLOCK, fortressBlockAnimation)
				.map(TankStates.FORTRESS_BLOCK_LEFT_RIGHT, fortressBlockAnimation)
				.map(TankStates.FORTRESS_BLOCK_LEFT, fortressBlockAnimation)
				.map(TankStates.FORTRESS_BLOCK_RIGHT, fortressBlockAnimation)

				.map(TankStates.FORTRESS_IMPALE, fortressImpale)
				.map(TankStates.FORTRESS_IMPALE_LEFT_RIGHT, fortressImpale)
				.map(TankStates.FORTRESS_IMPALE_LEFT, fortressImpale)
				.map(TankStates.FORTRESS_IMPALE_RIGHT, fortressImpale);
	}

	@Override
	protected void defineAbilities(Abilities<TankStates> abilities) {
		Debuff blockSlowDebuff = new Debuff(SLOW, BLOCK_SLOW_MODIFIER, 0);
		fortressDebuff = new Debuff(SLOW, FORTRESS_SLOW_MODIFIER, FORTRESS_DURATION)
				.defineDebuffEnd(() -> input(FORTRESS_KEYUP));

		blockAbility = new Ability<>(BLOCK_COOLDOWN);
		blockAbility.defineBegin((state) -> {
			inflictDebuff(blockSlowDebuff);
			inflictDebuff(blockPerfectDebuff);
		})
				.defineEnd(() -> {
					cancelDebuff(blockSlowDebuff);
					cancelDebuff(blockPerfectDebuff);
					cancelDebuff(blockDebuff);
				});

		hammerSwingAbility = new Ability<>(HAMMER_SWING_COOLDOWN);

		fortressAbility = new Ability<>(FORTRESS_COOLDOWN);
		initFortress();

		abilities.addBegin(TankStates.BLOCK, blockAbility)
				.addBegin(TankStates.BLOCK_LEFT, blockAbility)
				.addBegin(TankStates.BLOCK_RIGHT, blockAbility)
				.addBegin(TankStates.BLOCK_LEFT_RIGHT, blockAbility)
				.addEnd(TankStates.STANDING, blockAbility)
				.addEnd(TankStates.WALKING_LEFT, blockAbility)
				.addEnd(TankStates.WALKING_RIGHT, blockAbility)
				.addEnd(TankStates.STANDING_LEFT_RIGHT, blockAbility)

				.addBegin(TankStates.FORTRESS_BLOCK, blockAbility)
				.addBegin(TankStates.FORTRESS_BLOCK_LEFT, blockAbility)
				.addBegin(TankStates.FORTRESS_BLOCK_RIGHT, blockAbility)
				.addBegin(TankStates.FORTRESS_BLOCK_LEFT_RIGHT, blockAbility)
				.addEnd(TankStates.FORTRESS_STANDING, blockAbility)
				.addEnd(TankStates.FORTRESS_WALKING_LEFT, blockAbility)
				.addEnd(TankStates.FORTRESS_WALKING_RIGHT, blockAbility)
				.addEnd(TankStates.FORTRESS_STANDING_LEFT_RIGHT, blockAbility)

				.addBegin(TankStates.HAMMER_SWING, hammerSwingAbility)
				.addBegin(TankStates.HAMMER_SWING_LEFT, hammerSwingAbility)
				.addBegin(TankStates.HAMMER_SWING_RIGHT, hammerSwingAbility)
				.addBegin(TankStates.HAMMER_SWING_LEFT_RIGHT, hammerSwingAbility)
				.addEnd(TankStates.STANDING, hammerSwingAbility)
				.addEnd(TankStates.WALKING_LEFT, hammerSwingAbility)
				.addEnd(TankStates.WALKING_RIGHT, hammerSwingAbility)
				.addEnd(TankStates.STANDING_LEFT_RIGHT, hammerSwingAbility)

				.addBegin(TankStates.FORTRESS_IMPALE, hammerSwingAbility)
				.addBegin(TankStates.FORTRESS_IMPALE_LEFT, hammerSwingAbility)
				.addBegin(TankStates.FORTRESS_IMPALE_RIGHT, hammerSwingAbility)
				.addBegin(TankStates.FORTRESS_IMPALE_LEFT_RIGHT, hammerSwingAbility)
				.addEnd(TankStates.FORTRESS_STANDING, hammerSwingAbility)
				.addEnd(TankStates.FORTRESS_WALKING_LEFT, hammerSwingAbility)
				.addEnd(TankStates.FORTRESS_WALKING_RIGHT, hammerSwingAbility)
				.addEnd(TankStates.FORTRESS_STANDING_LEFT_RIGHT, hammerSwingAbility)

				.addBegin(TankStates.FORTRESS, fortressAbility)
				.addBegin(TankStates.FORTRESS_LEFT, fortressAbility)
				.addBegin(TankStates.FORTRESS_RIGHT, fortressAbility)
				.addBegin(TankStates.FORTRESS_LEFT_RIGHT, fortressAbility)
				.addEnd(TankStates.FORTRESS_STANDING, fortressAbility)
				.addEnd(TankStates.FORTRESS_WALKING_LEFT, fortressAbility)
				.addEnd(TankStates.FORTRESS_WALKING_RIGHT, fortressAbility)
				.addEnd(TankStates.FORTRESS_STANDING_LEFT_RIGHT, fortressAbility)
				// Stunned
				.addEnd(TankStates.STANDING, fortressAbility);
	}

	private void walkLeft() {
		getPosition().x -= MOVESPEED * (1 - getSlow()) * 60 * Gdx.graphics.getRawDeltaTime();
		checkWithinMap();
	}

	private void walkRight() {
		getPosition().x += MOVESPEED * (1 - getSlow()) * 60 * Gdx.graphics.getRawDeltaTime();
		checkWithinMap();
	}

	private void checkWithinMap() {
		float x = getHitbox(TankParts.BODY).getOffsetX();
		float width = getHitbox(TankParts.BODY).getWidth();
		if (getPosition().x < -x) {
			getPosition().x = -x;
		}

		if (getPosition().x > GAME_WIDTH - x - width) {
			getPosition().x = GAME_WIDTH - x - width;
		}
	}

	@Override
	public float getMiddleX() {
		return getPosition().x + getHitbox(TankParts.BODY).getOffsetX() + getHitbox(TankParts.BODY).getWidth() / 2;
	}

	@Override
	public float getTopY() {
		return getPosition().y + getHitbox(TankParts.BODY).getOffsetY() + getHitbox(TankParts.BODY).getHeight();
	}

	@Override
	public boolean hitTest(Hitbox hitbox) {
		return getHitbox(TankParts.BODY).hitTest(hitbox) ||
				getHitbox(TankParts.LEFT_LEG).hitTest(hitbox) ||
				getHitbox(TankParts.RIGHT_LEG).hitTest(hitbox) ||
				getHitbox(TankParts.LEFT_ARM).hitTest(hitbox) ||
				getHitbox(TankParts.RIGHT_ARM).hitTest(hitbox);
	}

	@Override
	protected void damage() {
		if (blockPerfectDebuff.isInflicted()) {
//			Gdx.app.log("Tank.java", "Perfect Block!");
			getGame().addScore(PERFECT_BLOCK_SCORE);

			getGame().getFloatingTextManager()
					.addFloatingText(getMiddleX(), getTopY(), PERFECT_TEXT, FLOATING_TEXT_COLOR);

			hammerSwingAbility.reset();
			hammerSwingAnimation.defineFrameTask(2, () -> {
				Boss1 boss1 = getGame().getBoss1();
				if (boss1.damageTest(this, getHitbox(TankParts.WEAPON), HAMMER_SWING_BONUS_DAMAGE)) {
					if (boss1.isWeak()) {
						setFortressHeal();
					}
					initImpaleHitTest();
				}
			});
		} else if (fortressPerfectDebuff.isInflicted()) {
//			Gdx.app.log("Tank.java", "Perfect Fortress!");
			getGame().addScore(PERFECT_FORTRESS_SCORE);

			getGame().getFloatingTextManager()
					.addFloatingText(getMiddleX(), getTopY(), PERFECT_TEXT, FLOATING_TEXT_COLOR);

			AnimationFrameTask shieldBashTask = () -> {
				Boss1 boss1 = getGame().getBoss1();
				if (boss1.damageTest(this, getHitbox(TankParts.SHIELD), SHIELD_BASH_DAMAGE)) {
					getGame().addScore(SHIELD_BASH_SCORE);

					boss1.inflictDebuff(new Debuff(STUN, 0, SHIELD_BASH_STUN_DURATION));
				}

				initBlockHitTest();
			};

			blockAnimation.defineFrameTask(0, shieldBashTask);
			fortressBlockAnimation.defineFrameTask(0, shieldBashTask);
		}
	}

	@Override
	protected void debuff(Debuff debuff) {

	}

	@Override
	protected void beginCrowdControl() {
		input(CROWD_CONTROL);
	}

	@Override
	public void endCrowdControl() {
		for (CharacterControllerInput input : getGame().getCharacterController().getInputs()) {
			switch (input) {
				case LEFT:
					input(LEFT_KEYDOWN);
					break;
				case RIGHT:
					input(RIGHT_KEYDOWN);
					break;
			}
		}
	}

	private void initBlockHitTest() {
		blockAnimation.defineFrameTask(0, null);
		fortressBlockAnimation.defineFrameTask(0, null);
	}

	private void initImpaleHitTest() {
		hammerSwingAnimation.defineFrameTask(2, () -> {
			Boss1 boss1 = getGame().getBoss1();
			if (boss1 != null
					&& boss1.damageTest(this, getHitbox(TankParts.WEAPON), HAMMER_SWING_DAMAGE)) {
				if (boss1.isWeak()) {
					setFortressHeal();
				}
			}
		});
	}

	private void initFortress() {
		fortressAbility.defineBegin((state) -> {
			inflictDebuff(fortressDebuff);
			inflictDebuff(new Debuff(DAMAGE_REDUCTION, FORTRESS_DAMAGE_REDUCTION, FORTRESS_DURATION));
			inflictDebuff(new Debuff(DAMAGE_REFLECT, FORTRESS_DAMAGE_REFLECT, FORTRESS_DURATION));
		});
	}

	private void setFortressHeal() {
		fortressAbility.defineBegin((state) -> {
			getGame().addScore(FORTRESS_HEAL_SCORE);

			getGame().getFloatingTextManager()
					.addFloatingText(getMiddleX(), getTopY(), HEAL_TEXT, HEAL_TEXT_COLOR);

			getGame().getTank().heal(FORTRESS_HEAL);
			getGame().getAssassin().heal(FORTRESS_HEAL);

			inflictDebuff(fortressDebuff);
			inflictDebuff(new Debuff(DAMAGE_REDUCTION, FORTRESS_DAMAGE_REDUCTION, FORTRESS_DURATION));
			inflictDebuff(new Debuff(DAMAGE_REFLECT, FORTRESS_DAMAGE_REFLECT, FORTRESS_DURATION));
			initFortress();
		});
	}

	@Override
	protected boolean canInput(TankInput input) {
		return !isStunned() || input == FORTRESS_KEYUP;
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
			input(BLOCK_KEYDOWN);
		} else {
			input(BLOCK_KEYUP);
		}
	}

	@Override
	protected void useSecondary(boolean keydown) {
		if (keydown) {
			input(HAMMER_SWING_KEYDOWN);
		}
	}

	@Override
	protected void useTertiary(boolean keydown) {
		if (keydown) {
			input(FORTRESS_KEYDOWN);
		}
	}

	@Override
	public void useSwitchCharacter() {
		if (input(SWITCH_CHARACTER)) {
			getGame().switchCharacter();
		}
	}

	public CooldownState getBlockState() {
		return blockAbility.getCooldownState();
	}

	public CooldownState getImpaleState() {
		return hammerSwingAbility.getCooldownState();
	}

	public CooldownState getFortressState() {
		return fortressAbility.getCooldownState();
	}
}
