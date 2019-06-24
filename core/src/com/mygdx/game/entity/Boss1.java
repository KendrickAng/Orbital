package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.animation.AnimationsGroup;
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
	private static final float FRICTION = 0.6f;

	private static final float PRIMARY_COOLDOWN = 1f;
	private static final float SECONDARY_COOLDOWN = 1f;
	private static final float TERTIARY_COOLDOWN = 1f;

	private static final float PRIMARY_SLOW_MODIFIER = 1f;
	private static final float SECONDARY_SLOW_MODIFIER = 1f;

	private static final float PRIMARY_ANIMATION_DURATION = 1f;
	private static final float SECONDARY_ANIMATION_DURATION = 1f;
	private static final float TERTIARY_ANIMATION_DURATION = 2f;

	private float movespeed;
	private float friction;

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
		return new Ability(PRIMARY_COOLDOWN, PRIMARY_ANIMATION_DURATION)
				.setAbilityBegin(() -> {
					Gdx.app.log("Boss1.java", "Primary");
					inflictDebuff(SLOW, PRIMARY_SLOW_MODIFIER, PRIMARY_ANIMATION_DURATION);
				});
	}

	public Ability initSecondary() {
		return new Ability(SECONDARY_COOLDOWN, SECONDARY_ANIMATION_DURATION)
				.setAbilityBegin(() -> {
					Gdx.app.log("Boss1.java", "Secondary");
					inflictDebuff(SLOW, SECONDARY_SLOW_MODIFIER, SECONDARY_ANIMATION_DURATION);
				});
	}

	public Ability initTertiary() {
		// TODO: define ability
		return new Ability(TERTIARY_COOLDOWN, TERTIARY_ANIMATION_DURATION);
	}

	@Override
	protected void defineDebuffs(Debuffs<DebuffType> debuffs) {
		Debuff slow = new Debuff()
				.setApply(modifier -> {
					// modifiers must never go above 1. See overallModifier() in Debuffs.
					if (modifier > 1) {
						modifier = 1;
					}
					// accounts for percentage slow, e.g 40% slow -> modifier = 0.4.
					this.movespeed = (MOVESPEED * (1 - modifier));
				})
				.setEnd(() -> this.movespeed = MOVESPEED);

		debuffs.map(SLOW, slow);
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

		final AnimationsGroup<Boss1Parts> standing = new AnimationsGroup<>("Boss1/Standing", filenames);
		standing.setDuration(2);
		final AnimationsGroup<Boss1Parts> primary = new AnimationsGroup<>("Boss1/Smash", filenames);
		final AnimationsGroup<Boss1Parts> secondary = new AnimationsGroup<>("Boss1/Earthquake", filenames);
		final AnimationsGroup<Boss1Parts> tertiary = new AnimationsGroup<>("Boss1/Roll", filenames);

		animations.map(Collections.singleton(STANDING), standing)
				.map(Collections.singleton(WALKING), standing)
				.map(Arrays.asList(STANDING, PRIMARY), primary)
				.map(Arrays.asList(WALKING, PRIMARY), primary)
				.map(Arrays.asList(STANDING, SECONDARY), secondary)
				.map(Arrays.asList(WALKING, SECONDARY), secondary)
				.map(Arrays.asList(STANDING, TERTIARY), tertiary)
				.map(Arrays.asList(WALKING, TERTIARY), tertiary);
	}

	/* Update */
	@Override
	protected void updateDirection(Direction inputDirection) {
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
		switch (super.getInputDirection()) {
			case LEFT:
				setSpriteDirection(RIGHT); // from Entity
				break;
			case RIGHT:
				setSpriteDirection(LEFT); // TODO: Weird sprite behaviour
				break;
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
