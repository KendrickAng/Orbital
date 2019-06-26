package com.mygdx.game.entity.boss1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.Direction;
import com.mygdx.game.entity.LivingEntity;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.state.Boss1States;
import com.mygdx.game.entity.state.States;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.Direction.LEFT;
import static com.mygdx.game.entity.Direction.RIGHT;
import static com.mygdx.game.entity.debuff.DebuffType.IGNORE_MOVE_INPUT;
import static com.mygdx.game.entity.debuff.DebuffType.ROLLING;
import static com.mygdx.game.entity.part.Boss1Parts.BODY;
import static com.mygdx.game.entity.part.Boss1Parts.LEFT_ARM;
import static com.mygdx.game.entity.part.Boss1Parts.LEFT_LEG;
import static com.mygdx.game.entity.part.Boss1Parts.RIGHT_ARM;
import static com.mygdx.game.entity.part.Boss1Parts.RIGHT_LEG;
import static com.mygdx.game.entity.part.Boss1Parts.SHOCKWAVE;
import static com.mygdx.game.entity.debuff.DebuffType.SLOW;
import static com.mygdx.game.entity.state.Boss1States.*;

/*
Responsibilities: Defines abilities, maps Ability states to Ability instances, handles
sprite position, direction, motion.
 */
public class Boss1 extends LivingEntity<Boss1States, Boss1Parts> {
	private static final float HEALTH = 1000;
	private static final float MOVESPEED = 1f;
	private static final float ROLL_SPEED = 4f;
	private static final float FRICTION = 0.6f;

	private static final float PRIMARY_COOLDOWN = 1f;
	private static final float SECONDARY_COOLDOWN = 1f;
	private static final float TERTIARY_COOLDOWN = 1f;

	private static final float PRIMARY_SLOW_MODIFIER = 1f;
	private static final float SECONDARY_SLOW_MODIFIER = 1f;

	private static final float STANDING_ANIMATION_DURATION = 2f;
	private static final float PRIMARY_ANIMATION_DURATION = 1f;
	private static final float SECONDARY_ANIMATION_DURATION = 1f;
	private static final float TERTIARY_ANIMATION_DURATION = 1f;

	private float movespeed;
	private float friction;
	private boolean ignoreMoveInput;
	private boolean rolling;

	private Ability primary;
	private Ability secondary;
	private Ability tertiary;

	public Boss1(GameScreen game) {
		super(game);
		movespeed = MOVESPEED;
		friction = FRICTION;
		// use width of pixmap
		int xOffset = new Pixmap(Gdx.files.internal("Boss1/Standing/0_Body.png")).getWidth();
		setPosition(GAME_WIDTH - xOffset, MAP_HEIGHT);
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
		// smash
		primary = initPrimary();
		secondary = initSecondary();
		tertiary = initTertiary();

		abilities.map(PRIMARY, primary)
				.map(SECONDARY, secondary)
				.map(TERTIARY, tertiary);
	}

	public void usePrimary() {
		super.scheduleState(PRIMARY, primary.getDuration());
	}

	public void useSecondary() {
		super.scheduleState(SECONDARY, secondary.getDuration());
	}

	public void useTertiary() { super.scheduleState(TERTIARY, tertiary.getDuration()); }

	/* Abilities */
	public Ability initPrimary() {
		return new Ability(PRIMARY_ANIMATION_DURATION, PRIMARY_COOLDOWN)
				.setAbilityBegin(() -> {
					addState(STANDING);
					removeState(WALKING);
					inflictDebuff(SLOW, PRIMARY_SLOW_MODIFIER, PRIMARY_ANIMATION_DURATION);
					inflictDebuff(IGNORE_MOVE_INPUT, 0, PRIMARY_ANIMATION_DURATION);
				});
	}

	public Ability initSecondary() {
		return new Ability(SECONDARY_ANIMATION_DURATION, SECONDARY_COOLDOWN)
				.setAbilityBegin(() -> {
					addState(STANDING);
					removeState(WALKING);
					inflictDebuff(SLOW, SECONDARY_SLOW_MODIFIER, SECONDARY_ANIMATION_DURATION);
					inflictDebuff(IGNORE_MOVE_INPUT, 0, SECONDARY_ANIMATION_DURATION);
				});
	}

	public Ability initTertiary() {
		return new Ability(TERTIARY_ANIMATION_DURATION, TERTIARY_COOLDOWN)
				.setAbilityBegin(() -> {
					addState(STANDING);
					removeState(WALKING);
					inflictDebuff(IGNORE_MOVE_INPUT, 0, TERTIARY_ANIMATION_DURATION);
				}).addAbilityTask(() -> {
					inflictDebuff(ROLLING, 0, TERTIARY_ANIMATION_DURATION - 0.3f);
				}, 0.3f);
	}

	@Override
	protected void defineDebuffs(Debuffs<DebuffType> debuffs) {
		Debuff slow = new Debuff()
				.setApply(modifier -> {
					// Slow can't go above 100%.
					if (modifier > 1) {
						modifier = 1;
					}
					// accounts for percentage slow, e.g 40% slow -> modifier = 0.4.
					this.movespeed = MOVESPEED * (1 - modifier);
				})
				.setEnd(() -> this.movespeed = MOVESPEED);

		Debuff ignoreMoveInput = new Debuff()
				.setBegin(() -> this.ignoreMoveInput = true)
				.setEnd(() -> this.ignoreMoveInput = false);

		Debuff rolling = new Debuff()
				.setBegin(() -> this.rolling = true)
				.setEnd(() -> this.rolling = false);

		debuffs.map(SLOW, slow)
				.map(IGNORE_MOVE_INPUT, ignoreMoveInput)
				.map(ROLLING, rolling);
	}

	/* Animations */
	@Override
	protected void defineAnimations(Animations<Boss1States, Boss1Parts> animations) {
		HashMap<String, Boss1Parts> filenames = new HashMap<>();
		filenames.put("RightArm", RIGHT_ARM);
		filenames.put("Body", BODY);
		filenames.put("RightLeg", RIGHT_LEG);
		filenames.put("LeftLeg", LEFT_LEG);
		filenames.put("LeftArm", LEFT_ARM);
		filenames.put("Shockwave", SHOCKWAVE);

		Animation<Boss1Parts> standing = new Animation<>(STANDING_ANIMATION_DURATION, true);
		Animation<Boss1Parts> primary = new Animation<>(PRIMARY_ANIMATION_DURATION, false);
		Animation<Boss1Parts> secondary = new Animation<>(SECONDARY_ANIMATION_DURATION, false);
		Animation<Boss1Parts> tertiary = new Animation<>(TERTIARY_ANIMATION_DURATION, false);

		standing.load("Boss1/Standing", filenames);
		primary.load("Boss1/Smash", filenames);
		secondary.load("Boss1/Earthquake", filenames);
		tertiary.load("Boss1/Roll", filenames);

		animations.map(Collections.singleton(STANDING), standing)
				.map(Collections.singleton(WALKING), standing)
				.map(Arrays.asList(STANDING, PRIMARY), primary)
				.map(Arrays.asList(STANDING, SECONDARY), secondary)
				.map(Arrays.asList(STANDING, TERTIARY), tertiary);
	}

	/* Update */
	@Override
	protected void updateDirection(Direction inputDirection) {
		if (!ignoreMoveInput) {
			switch (inputDirection) {
				case NONE:
					addState(STANDING);
					removeState(WALKING);
					break;
				case RIGHT:
				case LEFT:
					addState(WALKING);
					removeState(STANDING);
					break;
			}
		}
	}

	@Override
	protected void updatePosition(Vector2 position) {
		// prevent boss from moving off screen
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
		// set sprite direction
		if (!ignoreMoveInput) {
			switch (super.getInputDirection()) {
				case LEFT:
					setSpriteDirection(RIGHT); // from Entity
					break;
				case RIGHT:
					setSpriteDirection(LEFT); // TODO: Weird sprite behaviour
					break;
			}
		}

		if (rolling) {
			switch (super.getSpriteDirection()) {
				case LEFT:
					velocity.x += ROLL_SPEED;
					break;
				case RIGHT:
					velocity.x -= ROLL_SPEED;
					break;
			}
		}

		// calculate change in position due to velocity.
		switch (super.getInputDirection()) {
			case RIGHT:
				velocity.x += movespeed;
				break;
			case LEFT:
				velocity.x -= movespeed;
				break;
		}
		velocity.x *= friction;
	}
}
