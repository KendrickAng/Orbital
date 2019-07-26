package com.untitled.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.untitled.UntitledGame;
import com.untitled.assets.AssassinAnimationName;
import com.untitled.assets.Assets;
import com.untitled.game.Entity;
import com.untitled.game.Hitbox;
import com.untitled.game.ability.Abilities;
import com.untitled.game.ability.Ability;
import com.untitled.game.ability.CooldownState;
import com.untitled.game.animation.Animation;
import com.untitled.game.animation.Animations;
import com.untitled.game.boss1.Boss1;
import com.untitled.game.debuff.Debuff;
import com.untitled.game.shuriken.Shuriken;
import com.untitled.game.state.State;
import com.untitled.game.state.States;
import com.untitled.screens.GameScreen;

import static com.untitled.game.character.AssassinInput.CLEANSE_KEYDOWN;
import static com.untitled.game.character.AssassinInput.CLEANSE_KEYUP;
import static com.untitled.game.character.AssassinInput.CROWD_CONTROL;
import static com.untitled.game.character.AssassinInput.DASH_KEYDOWN;
import static com.untitled.game.character.AssassinInput.DASH_KEYUP;
import static com.untitled.game.character.AssassinInput.LEFT_KEYDOWN;
import static com.untitled.game.character.AssassinInput.LEFT_KEYUP;
import static com.untitled.game.character.AssassinInput.RIGHT_KEYDOWN;
import static com.untitled.game.character.AssassinInput.RIGHT_KEYUP;
import static com.untitled.game.character.AssassinInput.SHURIKEN_THROW_KEYDOWN;
import static com.untitled.game.character.AssassinInput.SHURIKEN_THROW_KEYUP;
import static com.untitled.game.character.AssassinInput.SWITCH_CHARACTER;
import static com.untitled.game.character.AssassinInput.UP_KEYDOWN;
import static com.untitled.game.character.AssassinInput.UP_KEYUP;
import static com.untitled.game.character.AssassinParts.BODY;
import static com.untitled.game.character.AssassinParts.LEFT_ARM;
import static com.untitled.game.character.AssassinParts.LEFT_LEG;
import static com.untitled.game.character.AssassinParts.RIGHT_ARM;
import static com.untitled.game.character.AssassinParts.RIGHT_LEG;
import static com.untitled.game.debuff.DebuffType.DAMAGE_REDUCTION;
import static com.untitled.game.debuff.DebuffType.STUN;
import static com.untitled.game.debuff.DebuffType.WEAK;
import static com.untitled.screens.GameScreen.GAME_FLOOR_HEIGHT;
import static com.untitled.screens.GameScreen.GAME_WIDTH;

/**
 * Assassin character.
 */
public class Assassin extends Character<AssassinInput, AssassinStates, AssassinParts> {
	private static final float MOVESPEED = 2f;
	// Movespeed is multiplied by this constant in air
	private static final float AIR_MOVESPEED = 0.1f;

	// Velocity is multiplied by these constants
	private static final float FRICTION = 0.6f;
	private static final float AIR_FRICTION = 0.95f;

	private static final float HEALTH = 80f;

	// Skill modifiers
	private static final float DASH_SPEED = 15f;
	private static final float DASH_DIAGONAL_SPEED = 10f;
	private static final float DASH_TRUE_DAMAGE = 80f;

	private static final int MAX_STACKS = 3;
	private static final float SHURIKEN_DAMAGE = 10f;
	private static final float SHURIKEN_BONUS_DAMAGE = SHURIKEN_DAMAGE + 10f;

	private static final float PERFECT_CLEANSE_DURATION = 0.25f;
	private static final float WEAK_SPOT_DURATION = 4f;

	private static final float LIGHT_REALM_DURATION = 10f;
	private static final float LIGHT_REALM_DASH_SPEED = 20f;
	private static final float LIGHT_REALM_DASH_DIAGONAL_SPEED = 15f;

	private static final float LIGHT_REALM_SHURIKEN_DISTANCE =
			(float) Math.sqrt(Math.pow(UntitledGame.CAMERA_WIDTH, 2) + Math.pow(UntitledGame.CAMERA_HEIGHT, 2));
	private static final float LIGHT_REALM_SHURIKEN_MIN_INTERVAL = 0.5f;
	private static final float LIGHT_REALM_SHURIKEN_MAX_INTERVAL = 1f;

	// Skill cooldown in seconds.
	private static final float DASH_COOLDOWN = 1f;
	private static final float SHURIKEN_THROW_COOLDOWN = 0.5f;
	private static final float CLEANSE_COOLDOWN = LIGHT_REALM_DURATION + 20f;

	// Skill animation duration in seconds.
	private static final float STANDING_ANIMATION_DURATION = 1f;
	private static final float WALKING_ANIMATION_DURATION = 0.5f;
	private static final float DASH_ANIMATION_DURATION = 0.05f;
	private static final float PERFECT_DASH_DURATION = 0.2f;
	private static final float SHURIKEN_THROW_ANIMATION_DURATION = 0.2f;
	private static final float CLEANSE_ANIMATION_DURATION = 0.25f;

	// Scores
	private static final int STACKS_SCORE = 10;
	private static final int PERFECT_DASH_SCORE = 50;
	private static final int PERFECT_CLEANSE_SCORE = 250;

	private static final String STACKS_TEXT = MAX_STACKS + " STACKS";

	private int stacks;
	private boolean falling;
	private boolean dashTrueDamage;
	private boolean perfectCleanse;
	private boolean lightRealm;

	private Timer timer;
	private Vector2 velocity;

	private Ability<AssassinStates> dashAbility;
	private Ability<AssassinStates> shurikenAbility;
	private Ability<AssassinStates> cleanseAbility;

	private Debuff shurikenDebuff;
	private final Debuff dashDebuff;

	public Assassin(GameScreen game) {
		super(game);

		this.stacks = 1;
		this.timer = new Timer();
		this.velocity = new Vector2();
		this.dashDebuff = new Debuff(DAMAGE_REDUCTION, 1f, PERFECT_DASH_DURATION);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	@Override
	protected void defineStates(States<AssassinInput, AssassinStates> states) {
		states.add(new State<AssassinInput, AssassinStates>(AssassinStates.STANDING)
				.defineUpdate(this::updatePhysics)
				.addEdge(LEFT_KEYDOWN, AssassinStates.WALKING_LEFT)
				.addEdge(RIGHT_KEYDOWN, AssassinStates.WALKING_RIGHT)
				.addEdge(UP_KEYDOWN, AssassinStates.STANDING_UP)
				.addEdge(SHURIKEN_THROW_KEYDOWN, AssassinStates.SHURIKEN_THROW)
				.addEdge(CLEANSE_KEYDOWN, AssassinStates.CLEANSE)
				.addEdge(SWITCH_CHARACTER, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.STANDING_LEFT_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(LEFT_KEYUP, AssassinStates.WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.WALKING_LEFT)
						.addEdge(UP_KEYDOWN, AssassinStates.STANDING_UP_LEFT_RIGHT)
						.addEdge(DASH_KEYDOWN, AssassinStates.DASH_LEFT_RIGHT)
						.addEdge(SHURIKEN_THROW_KEYDOWN, AssassinStates.SHURIKEN_THROW_LEFT_RIGHT)
						.addEdge(CLEANSE_KEYDOWN, AssassinStates.CLEANSE_LEFT_RIGHT)
						.addEdge(SWITCH_CHARACTER, AssassinStates.STANDING)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.WALKING_LEFT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(() -> {
							addVelocityX(-MOVESPEED * (1 - getSlow()));
							updatePhysics();
						})
						.addEdge(LEFT_KEYUP, AssassinStates.STANDING)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.STANDING_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.WALKING_UP_LEFT)
						.addEdge(DASH_KEYDOWN, AssassinStates.DASH_LEFT)
						.addEdge(SHURIKEN_THROW_KEYDOWN, AssassinStates.SHURIKEN_THROW_LEFT)
						.addEdge(CLEANSE_KEYDOWN, AssassinStates.CLEANSE_LEFT)
						.addEdge(SWITCH_CHARACTER, AssassinStates.STANDING)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.WALKING_RIGHT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(() -> {
							addVelocityX(MOVESPEED * (1 - getSlow()));
							updatePhysics();
						})
						.addEdge(RIGHT_KEYUP, AssassinStates.STANDING)
						.addEdge(LEFT_KEYDOWN, AssassinStates.STANDING_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.WALKING_UP_RIGHT)
						.addEdge(DASH_KEYDOWN, AssassinStates.DASH_RIGHT)
						.addEdge(SHURIKEN_THROW_KEYDOWN, AssassinStates.SHURIKEN_THROW_RIGHT)
						.addEdge(CLEANSE_KEYDOWN, AssassinStates.CLEANSE_RIGHT)
						.addEdge(SWITCH_CHARACTER, AssassinStates.STANDING)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.STANDING_UP)
						.defineUpdate(this::updatePhysics)
						.addEdge(UP_KEYUP, AssassinStates.STANDING)
						.addEdge(LEFT_KEYDOWN, AssassinStates.WALKING_UP_LEFT)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.WALKING_UP_RIGHT)
						.addEdge(DASH_KEYDOWN, AssassinStates.DASH_UP)
						.addEdge(SHURIKEN_THROW_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP)
						.addEdge(CLEANSE_KEYDOWN, AssassinStates.CLEANSE_UP)
						.addEdge(SWITCH_CHARACTER, AssassinStates.STANDING)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.STANDING_UP_LEFT_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(UP_KEYUP, AssassinStates.STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, AssassinStates.WALKING_UP_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.WALKING_UP_LEFT)
						.addEdge(DASH_KEYDOWN, AssassinStates.DASH_UP_LEFT_RIGHT)
						.addEdge(SHURIKEN_THROW_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP_LEFT_RIGHT)
						.addEdge(CLEANSE_KEYDOWN, AssassinStates.CLEANSE_UP_LEFT_RIGHT)
						.addEdge(SWITCH_CHARACTER, AssassinStates.STANDING)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))


				.add(new State<AssassinInput, AssassinStates>(AssassinStates.WALKING_UP_LEFT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(() -> {
							addVelocityX(-MOVESPEED * (1 - getSlow()));
							updatePhysics();
						})
						.addEdge(UP_KEYUP, AssassinStates.WALKING_LEFT)
						.addEdge(LEFT_KEYUP, AssassinStates.STANDING_UP)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.STANDING_UP_LEFT_RIGHT)
						.addEdge(DASH_KEYDOWN, AssassinStates.DASH_UP_LEFT)
						.addEdge(SHURIKEN_THROW_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP_LEFT)
						.addEdge(CLEANSE_KEYDOWN, AssassinStates.CLEANSE_UP_LEFT)
						.addEdge(SWITCH_CHARACTER, AssassinStates.STANDING)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.WALKING_UP_RIGHT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(() -> {
							addVelocityX(MOVESPEED * (1 - getSlow()));
							updatePhysics();
						})
						.addEdge(UP_KEYUP, AssassinStates.WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.STANDING_UP)
						.addEdge(LEFT_KEYDOWN, AssassinStates.STANDING_UP_LEFT_RIGHT)
						.addEdge(DASH_KEYDOWN, AssassinStates.DASH_UP_RIGHT)
						.addEdge(SHURIKEN_THROW_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP_RIGHT)
						.addEdge(CLEANSE_KEYDOWN, AssassinStates.CLEANSE_UP_RIGHT)
						.addEdge(SWITCH_CHARACTER, AssassinStates.STANDING)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				/* Dash */
				.add(new State<AssassinInput, AssassinStates>(AssassinStates.DASH)
						.defineUpdate(this::dash)
						.addEdge(LEFT_KEYDOWN, AssassinStates.DASH_LEFT)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.DASH_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.DASH_UP)
						.addEdge(DASH_KEYUP, AssassinStates.STANDING)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.DASH_LEFT)
						.defineUpdate(this::dash)
						.addEdge(LEFT_KEYUP, AssassinStates.DASH)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.DASH_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.DASH_UP_LEFT)
						.addEdge(DASH_KEYUP, AssassinStates.WALKING_LEFT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.DASH_RIGHT)
						.defineUpdate(this::dash)
						.addEdge(RIGHT_KEYUP, AssassinStates.DASH)
						.addEdge(LEFT_KEYDOWN, AssassinStates.DASH_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.DASH_UP_RIGHT)
						.addEdge(DASH_KEYUP, AssassinStates.WALKING_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.DASH_LEFT_RIGHT)
						.defineUpdate(this::dash)
						.addEdge(LEFT_KEYUP, AssassinStates.DASH_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.DASH_LEFT)
						.addEdge(UP_KEYDOWN, AssassinStates.DASH_UP_LEFT_RIGHT)
						.addEdge(DASH_KEYUP, AssassinStates.STANDING_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.DASH_UP)
						.defineUpdate(this::dash)
						.addEdge(UP_KEYUP, AssassinStates.DASH)
						.addEdge(LEFT_KEYDOWN, AssassinStates.DASH_UP_LEFT)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.DASH_UP_RIGHT)
						.addEdge(DASH_KEYUP, AssassinStates.STANDING_UP)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.DASH_UP_LEFT)
						.defineUpdate(this::dash)
						.addEdge(UP_KEYUP, AssassinStates.DASH_LEFT)
						.addEdge(LEFT_KEYUP, AssassinStates.DASH_UP)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.DASH_UP_LEFT_RIGHT)
						.addEdge(DASH_KEYUP, AssassinStates.WALKING_UP_LEFT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.DASH_UP_RIGHT)
						.defineUpdate(this::dash)
						.addEdge(UP_KEYUP, AssassinStates.DASH_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.DASH_UP)
						.addEdge(LEFT_KEYDOWN, AssassinStates.DASH_UP_LEFT_RIGHT)
						.addEdge(DASH_KEYUP, AssassinStates.WALKING_UP_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.DASH_UP_LEFT_RIGHT)
						.defineUpdate(this::dash)
						.addEdge(UP_KEYUP, AssassinStates.DASH_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, AssassinStates.DASH_UP_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.DASH_UP_LEFT)
						.addEdge(DASH_KEYUP, AssassinStates.STANDING_UP_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				/* Shuriken Throw */
				.add(new State<AssassinInput, AssassinStates>(AssassinStates.SHURIKEN_THROW)
						.defineUpdate(this::updatePhysics)
						.addEdge(SHURIKEN_THROW_KEYUP, AssassinStates.STANDING)
						.addEdge(LEFT_KEYDOWN, AssassinStates.SHURIKEN_THROW_LEFT)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.SHURIKEN_THROW_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.SHURIKEN_THROW_LEFT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SHURIKEN_THROW_KEYUP, AssassinStates.WALKING_LEFT)
						.addEdge(LEFT_KEYUP, AssassinStates.SHURIKEN_THROW)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.SHURIKEN_THROW_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP_LEFT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.SHURIKEN_THROW_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SHURIKEN_THROW_KEYUP, AssassinStates.WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.SHURIKEN_THROW)
						.addEdge(LEFT_KEYDOWN, AssassinStates.SHURIKEN_THROW_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.SHURIKEN_THROW_LEFT_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SHURIKEN_THROW_KEYUP, AssassinStates.STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, AssassinStates.SHURIKEN_THROW_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.SHURIKEN_THROW_LEFT)
						.addEdge(UP_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.SHURIKEN_THROW_UP)
						.defineUpdate(this::updatePhysics)
						.addEdge(SHURIKEN_THROW_KEYUP, AssassinStates.STANDING_UP)
						.addEdge(UP_KEYUP, AssassinStates.SHURIKEN_THROW)
						.addEdge(LEFT_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP_LEFT)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.SHURIKEN_THROW_UP_LEFT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SHURIKEN_THROW_KEYUP, AssassinStates.WALKING_UP_LEFT)
						.addEdge(UP_KEYUP, AssassinStates.SHURIKEN_THROW_LEFT)
						.addEdge(LEFT_KEYUP, AssassinStates.SHURIKEN_THROW_UP)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.SHURIKEN_THROW_UP_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SHURIKEN_THROW_KEYUP, AssassinStates.WALKING_UP_RIGHT)
						.addEdge(UP_KEYUP, AssassinStates.SHURIKEN_THROW_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.SHURIKEN_THROW_UP)
						.addEdge(LEFT_KEYDOWN, AssassinStates.SHURIKEN_THROW_UP_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.SHURIKEN_THROW_UP_LEFT_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SHURIKEN_THROW_KEYUP, AssassinStates.STANDING_UP_LEFT_RIGHT)
						.addEdge(UP_KEYUP, AssassinStates.SHURIKEN_THROW_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, AssassinStates.SHURIKEN_THROW_UP_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.SHURIKEN_THROW_UP_LEFT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				/* Cleanse */
				.add(new State<AssassinInput, AssassinStates>(AssassinStates.CLEANSE)
						.defineUpdate(this::updatePhysics)
						.addEdge(CLEANSE_KEYUP, AssassinStates.STANDING)
						.addEdge(LEFT_KEYDOWN, AssassinStates.CLEANSE_LEFT)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.CLEANSE_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.CLEANSE_UP)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.CLEANSE_LEFT)
						.defineUpdate(this::updatePhysics)
						.addEdge(CLEANSE_KEYUP, AssassinStates.WALKING_LEFT)
						.addEdge(LEFT_KEYUP, AssassinStates.CLEANSE)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.CLEANSE_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.CLEANSE_UP_LEFT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.CLEANSE_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(CLEANSE_KEYUP, AssassinStates.WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.CLEANSE)
						.addEdge(LEFT_KEYDOWN, AssassinStates.CLEANSE_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, AssassinStates.CLEANSE_UP_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.CLEANSE_LEFT_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(CLEANSE_KEYUP, AssassinStates.STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, AssassinStates.CLEANSE_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.CLEANSE_LEFT)
						.addEdge(UP_KEYDOWN, AssassinStates.CLEANSE_UP_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.CLEANSE_UP)
						.defineUpdate(this::updatePhysics)
						.addEdge(CLEANSE_KEYUP, AssassinStates.STANDING_UP)
						.addEdge(LEFT_KEYDOWN, AssassinStates.CLEANSE_UP_LEFT)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.CLEANSE_UP_RIGHT)
						.addEdge(UP_KEYUP, AssassinStates.CLEANSE)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.CLEANSE_UP_LEFT)
						.defineUpdate(this::updatePhysics)
						.addEdge(CLEANSE_KEYUP, AssassinStates.WALKING_UP_LEFT)
						.addEdge(LEFT_KEYUP, AssassinStates.CLEANSE_UP)
						.addEdge(RIGHT_KEYDOWN, AssassinStates.CLEANSE_UP_LEFT_RIGHT)
						.addEdge(UP_KEYUP, AssassinStates.CLEANSE_LEFT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.CLEANSE_UP_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(CLEANSE_KEYUP, AssassinStates.WALKING_UP_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.CLEANSE_UP)
						.addEdge(LEFT_KEYDOWN, AssassinStates.CLEANSE_UP_LEFT_RIGHT)
						.addEdge(UP_KEYUP, AssassinStates.CLEANSE_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING))

				.add(new State<AssassinInput, AssassinStates>(AssassinStates.CLEANSE_UP_LEFT_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(CLEANSE_KEYUP, AssassinStates.STANDING_UP_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, AssassinStates.CLEANSE_UP_RIGHT)
						.addEdge(RIGHT_KEYUP, AssassinStates.CLEANSE_UP_LEFT)
						.addEdge(UP_KEYUP, AssassinStates.CLEANSE_LEFT_RIGHT)
						.addEdge(CROWD_CONTROL, AssassinStates.STANDING));
	}

	@Override
	protected void defineAnimations(Animations<AssassinStates, AssassinParts> animations, Assets assets) {
		Animation<AssassinParts> standing = assets.getAssassinAnimation(AssassinAnimationName.STANDING)
				.setDuration(STANDING_ANIMATION_DURATION)
				.setLoop();

		Animation<AssassinParts> walking = assets.getAssassinAnimation(AssassinAnimationName.WALKING)
				.setDuration(WALKING_ANIMATION_DURATION)
				.setLoop();

		Animation<AssassinParts> dash = assets.getAssassinAnimation(AssassinAnimationName.DASH)
				.setDuration(DASH_ANIMATION_DURATION)
				.defineEnd(() -> input(DASH_KEYUP));

		Animation<AssassinParts> shurikenThrow = assets.getAssassinAnimation(AssassinAnimationName.SHURIKEN_THROW)
				.setDuration(SHURIKEN_THROW_ANIMATION_DURATION)
				.defineEnd(() -> input(SHURIKEN_THROW_KEYUP));

		Animation<AssassinParts> cleanse = assets.getAssassinAnimation(AssassinAnimationName.CLEANSE)
				.setDuration(CLEANSE_ANIMATION_DURATION)
				.defineEnd(() -> input(CLEANSE_KEYUP));

		animations.map(AssassinStates.STANDING, standing)
				.map(AssassinStates.STANDING_LEFT_RIGHT, standing)
				.map(AssassinStates.WALKING_LEFT, walking)
				.map(AssassinStates.WALKING_RIGHT, walking)
				.map(AssassinStates.STANDING_UP, standing)
				.map(AssassinStates.STANDING_UP_LEFT_RIGHT, standing)
				.map(AssassinStates.WALKING_UP_LEFT, walking)
				.map(AssassinStates.WALKING_UP_RIGHT, walking)

				.map(AssassinStates.DASH_LEFT, dash)
				.map(AssassinStates.DASH_RIGHT, dash)
				.map(AssassinStates.DASH_UP, dash)
				.map(AssassinStates.DASH_UP_LEFT, dash)
				.map(AssassinStates.DASH_UP_RIGHT, dash)
				.map(AssassinStates.DASH_UP_LEFT_RIGHT, dash)

				.map(AssassinStates.SHURIKEN_THROW, shurikenThrow)
				.map(AssassinStates.SHURIKEN_THROW_LEFT, shurikenThrow)
				.map(AssassinStates.SHURIKEN_THROW_RIGHT, shurikenThrow)
				.map(AssassinStates.SHURIKEN_THROW_LEFT_RIGHT, shurikenThrow)
				.map(AssassinStates.SHURIKEN_THROW_UP, shurikenThrow)
				.map(AssassinStates.SHURIKEN_THROW_UP_LEFT, shurikenThrow)
				.map(AssassinStates.SHURIKEN_THROW_UP_RIGHT, shurikenThrow)
				.map(AssassinStates.SHURIKEN_THROW_UP_LEFT_RIGHT, shurikenThrow)

				.map(AssassinStates.CLEANSE, cleanse)
				.map(AssassinStates.CLEANSE_LEFT_RIGHT, cleanse)
				.map(AssassinStates.CLEANSE_LEFT, cleanse)
				.map(AssassinStates.CLEANSE_RIGHT, cleanse)
				.map(AssassinStates.CLEANSE_UP, cleanse)
				.map(AssassinStates.CLEANSE_UP_LEFT_RIGHT, cleanse)
				.map(AssassinStates.CLEANSE_UP_LEFT, cleanse)
				.map(AssassinStates.CLEANSE_UP_RIGHT, cleanse);
	}

	@Override
	protected void defineAbilities(Abilities<AssassinStates> abilities) {
		dashAbility = new Ability<AssassinStates>(DASH_COOLDOWN)
				.defineBegin((state) -> {
					float dashSpeed = lightRealm ? LIGHT_REALM_DASH_SPEED : DASH_SPEED;
					float dashDiagonalSpeed = lightRealm ? LIGHT_REALM_DASH_DIAGONAL_SPEED : DASH_DIAGONAL_SPEED;
					switch (state) {
						case DASH_LEFT:
							velocity.x = -dashSpeed;
							break;
						case DASH_RIGHT:
							velocity.x = dashSpeed;
							break;
						case DASH_UP_LEFT:
							velocity.x = -dashDiagonalSpeed;
							break;
						case DASH_UP_RIGHT:
							velocity.x = dashDiagonalSpeed;
							break;
					}

					falling = true;
					switch (state) {
						case DASH_UP:
						case DASH_UP_LEFT_RIGHT:
							velocity.y = dashSpeed;
							break;
						case DASH_UP_LEFT:
							velocity.y = dashDiagonalSpeed;
							break;
						case DASH_UP_RIGHT:
							velocity.y = dashDiagonalSpeed;
							break;
						default:
							falling = false;
					}
					dashTrueDamage = true;
					inflictDebuff(dashDebuff);
				});

		shurikenAbility = new Ability<AssassinStates>(SHURIKEN_THROW_COOLDOWN)
				.defineBegin((state) -> {
					Hitbox body = getHitbox(BODY);
					float x = body.getX() + body.getWidth() / 2;
					float y = body.getY() + body.getHeight() / 2;
					float degree = getFlipX().get() ? 270 : 90;

					if (stacks == MAX_STACKS) {
//						Gdx.app.log("Assassin.java", "Max Stacks!");
						getGame().addScore(STACKS_SCORE);

						getGame().getFloatingTextManager()
								.addFloatingText(getMiddleX(), getTopY(), STACKS_TEXT, FLOATING_TEXT_COLOR);

						new Shuriken(getGame(), x, y, degree, SHURIKEN_BONUS_DAMAGE, shurikenDebuff);
						stacks = 0;
					} else {
						new Shuriken(getGame(), x, y, degree, SHURIKEN_DAMAGE, shurikenDebuff);
					}
					shurikenDebuff = null;
				});

		// TODO: Cleansing when boss damages somehow crashed the game (Can't replicate)
		cleanseAbility = new Ability<AssassinStates>(CLEANSE_COOLDOWN)
				.defineBegin((state) -> {
					if (isCrowdControl()) {
						clearCrowdControl();
					}

					if (perfectCleanse) {
//						Gdx.app.log("Assassin.java", "Perfect Cleanse!");
						getGame().addScore(PERFECT_CLEANSE_SCORE);

						getGame().getFloatingTextManager()
								.addFloatingText(getMiddleX(), getTopY(), PERFECT_TEXT, FLOATING_TEXT_COLOR);

						shurikenDebuff = new Debuff(WEAK, 0, WEAK_SPOT_DURATION);
					}

					lightRealm = true;
					timer.scheduleTask(new Timer.Task() {
						@Override
						public void run() {
							lightRealm = false;
						}
					}, LIGHT_REALM_DURATION);
					lightRealmShuriken();
				});

		abilities.addBegin(AssassinStates.DASH_LEFT, dashAbility)
				.addBegin(AssassinStates.DASH_RIGHT, dashAbility)
				.addBegin(AssassinStates.DASH_UP, dashAbility)
				.addBegin(AssassinStates.DASH_UP_LEFT, dashAbility)
				.addBegin(AssassinStates.DASH_UP_RIGHT, dashAbility)
				.addBegin(AssassinStates.DASH_UP_LEFT_RIGHT, dashAbility)
				.addEnd(AssassinStates.WALKING_LEFT, dashAbility)
				.addEnd(AssassinStates.WALKING_RIGHT, dashAbility)
				.addEnd(AssassinStates.STANDING_UP, dashAbility)
				.addEnd(AssassinStates.WALKING_UP_LEFT, dashAbility)
				.addEnd(AssassinStates.WALKING_UP_RIGHT, dashAbility)
				.addEnd(AssassinStates.STANDING_UP_LEFT_RIGHT, dashAbility)

				.addBegin(AssassinStates.SHURIKEN_THROW, shurikenAbility)
				.addBegin(AssassinStates.SHURIKEN_THROW_LEFT, shurikenAbility)
				.addBegin(AssassinStates.SHURIKEN_THROW_RIGHT, shurikenAbility)
				.addBegin(AssassinStates.SHURIKEN_THROW_LEFT_RIGHT, shurikenAbility)
				.addBegin(AssassinStates.SHURIKEN_THROW_UP, shurikenAbility)
				.addBegin(AssassinStates.SHURIKEN_THROW_UP_LEFT, shurikenAbility)
				.addBegin(AssassinStates.SHURIKEN_THROW_UP_RIGHT, shurikenAbility)
				.addBegin(AssassinStates.SHURIKEN_THROW_UP_LEFT_RIGHT, shurikenAbility)
				.addEnd(AssassinStates.STANDING, shurikenAbility)
				.addEnd(AssassinStates.WALKING_LEFT, shurikenAbility)
				.addEnd(AssassinStates.WALKING_RIGHT, shurikenAbility)
				.addEnd(AssassinStates.STANDING_LEFT_RIGHT, shurikenAbility)
				.addEnd(AssassinStates.STANDING_UP, shurikenAbility)
				.addEnd(AssassinStates.WALKING_UP_LEFT, shurikenAbility)
				.addEnd(AssassinStates.WALKING_UP_RIGHT, shurikenAbility)
				.addEnd(AssassinStates.STANDING_UP_LEFT_RIGHT, shurikenAbility)

				.addBegin(AssassinStates.CLEANSE, cleanseAbility)
				.addBegin(AssassinStates.CLEANSE_LEFT, cleanseAbility)
				.addBegin(AssassinStates.CLEANSE_RIGHT, cleanseAbility)
				.addBegin(AssassinStates.CLEANSE_LEFT_RIGHT, cleanseAbility)
				.addBegin(AssassinStates.CLEANSE_UP, cleanseAbility)
				.addBegin(AssassinStates.CLEANSE_UP_LEFT, cleanseAbility)
				.addBegin(AssassinStates.CLEANSE_UP_RIGHT, cleanseAbility)
				.addBegin(AssassinStates.CLEANSE_UP_LEFT_RIGHT, cleanseAbility)
				.addEnd(AssassinStates.STANDING, cleanseAbility)
				.addEnd(AssassinStates.WALKING_LEFT, cleanseAbility)
				.addEnd(AssassinStates.WALKING_RIGHT, cleanseAbility)
				.addEnd(AssassinStates.STANDING_LEFT_RIGHT, cleanseAbility)
				.addEnd(AssassinStates.STANDING_UP, cleanseAbility)
				.addEnd(AssassinStates.WALKING_UP_LEFT, cleanseAbility)
				.addEnd(AssassinStates.WALKING_UP_RIGHT, cleanseAbility)
				.addEnd(AssassinStates.STANDING_UP_LEFT_RIGHT, cleanseAbility);
	}

	private void addVelocityX(float movespeed) {
		if (falling) {
			velocity.x += movespeed * AIR_MOVESPEED * 60 * Gdx.graphics.getRawDeltaTime();
		} else {
			velocity.x += movespeed * 60 * Gdx.graphics.getRawDeltaTime();
		}
	}

	private void updatePhysics() {
		if (falling) {
			velocity.y += Entity.GRAVITY * 60 * Gdx.graphics.getRawDeltaTime();
			velocity.x *= Math.pow(Math.pow(AIR_FRICTION, 60), Gdx.graphics.getRawDeltaTime());
			if (getPosition().y < GAME_FLOOR_HEIGHT) {
				falling = false;
				velocity.y = 0;
				getPosition().y = GAME_FLOOR_HEIGHT;
			}
		} else {
			// f ^ 60 = g ^ fps
			velocity.x *= Math.pow(Math.pow(FRICTION, 60), Gdx.graphics.getRawDeltaTime());
		}
		updatePosition();
	}

	private void updatePosition() {
		getPosition().x += velocity.x * 60 * Gdx.graphics.getRawDeltaTime();
		getPosition().y += velocity.y * 60 * Gdx.graphics.getRawDeltaTime();
		checkWithinMap();
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

	private void dash() {
		updatePosition();
		Boss1 boss1 = getGame().getBoss1();
		if (boss1 != null
				&& boss1.isStunned() && dashTrueDamage) {
			dashTrueDamage = false;
			boss1.trueDamageTest(this, getHitbox(BODY), DASH_TRUE_DAMAGE);
		}
	}

	private void lightRealmShuriken() {
		timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				if (lightRealm && !isDispose()) {
					int degrees = MathUtils.random(0, 360);
					float rad = (float) (degrees * Math.PI / 180);
					float x = (float) Math.sin(rad) * LIGHT_REALM_SHURIKEN_DISTANCE + UntitledGame.CAMERA_WIDTH / 2f;
					float y = (float) Math.cos(rad) * LIGHT_REALM_SHURIKEN_DISTANCE + UntitledGame.CAMERA_HEIGHT / 2f;
					new Shuriken(getGame(), x, y, degrees - 180, SHURIKEN_DAMAGE, null);
					lightRealmShuriken();
				}
			}
		}, MathUtils.random(LIGHT_REALM_SHURIKEN_MIN_INTERVAL, LIGHT_REALM_SHURIKEN_MAX_INTERVAL));
	}

	@Override
	public boolean hitTest(Hitbox hitbox) {
		return getHitbox(BODY).hitTest(hitbox) ||
				getHitbox(LEFT_LEG).hitTest(hitbox) ||
				getHitbox(RIGHT_LEG).hitTest(hitbox) ||
				getHitbox(LEFT_ARM).hitTest(hitbox) ||
				getHitbox(RIGHT_ARM).hitTest(hitbox);
	}

	@Override
	public float getMiddleX() {
		return getPosition().x + getHitbox(BODY).getOffsetX() + getHitbox(BODY).getWidth() / 2;
	}

	@Override
	public float getTopY() {
		return getPosition().y + getHitbox(BODY).getOffsetY() + getHitbox(BODY).getHeight();
	}

	@Override
	protected void damage() {
		if (dashDebuff.isInflicted()) {
//			Gdx.app.log("Assassin.java", "Perfect Dash!");
			getGame().addScore(PERFECT_DASH_SCORE);

			getGame().getFloatingTextManager()
					.addFloatingText(getMiddleX(), getTopY(), PERFECT_TEXT, FLOATING_TEXT_COLOR);

			if (stacks < MAX_STACKS) {
				stacks++;
			}
		}
	}

	@Override
	protected void debuff(Debuff debuff) {
		if (debuff.getType() == STUN) {
			perfectCleanse = true;
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					perfectCleanse = false;
				}
			}, PERFECT_CLEANSE_DURATION);
		}
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
				case UP:
					input(UP_KEYDOWN);
					break;
			}
		}
	}

	@Override
	protected boolean canInput(AssassinInput input) {
		return !isStunned() || input == CLEANSE_KEYDOWN;
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
		if (keydown) {
			input(UP_KEYDOWN);
		} else {
			input(UP_KEYUP);
		}
	}

	@Override
	protected void usePrimary(boolean keydown) {
		if (keydown) {
			input(DASH_KEYDOWN);
		}
	}

	@Override
	protected void useSecondary(boolean keydown) {
		if (keydown) {
			input(SHURIKEN_THROW_KEYDOWN);
		}
	}

	@Override
	protected void useTertiary(boolean keydown) {
		if (keydown) {
			input(CLEANSE_KEYDOWN);
		}
	}

	@Override
	public void useSwitchCharacter() {
		if (input(SWITCH_CHARACTER)) {
			velocity.x = 0;
			velocity.y = 0;
			falling = false;
			dashAbility.reset();
			getGame().switchCharacter();
		}
	}

	public CooldownState getDashState() {
		return dashAbility.getCooldownState();
	}

	public CooldownState getShurikenState() {
		return shurikenAbility.getCooldownState();
	}

	public CooldownState getCleanseState() {
		return cleanseAbility.getCooldownState();
	}

	public int getStacks() {
		return stacks;
	}

	public int getMaxStacks() {
		return MAX_STACKS;
	}
}
