package com.mygdx.game.entity.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.Direction;
import com.mygdx.game.entity.Entity;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.Shuriken;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.part.AssassinParts;
import com.mygdx.game.entity.state.CharacterStates;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.part.AssassinParts.*;
import static com.mygdx.game.entity.state.CharacterStates.*;

public class Assassin extends Character<AssassinParts> {
	private static final float HEALTH = 50;

	// Skill cooldown in seconds.
	private static final float PRIMARY_COOLDOWN = 0;
	private static final float SECONDARY_COOLDOWN = 1;
	private static final float TERTIARY_COOLDOWN = 2;

	// Skill animation duration in seconds.
	private static final float STANDING_ANIMATION_DURATION = 1f;
	private static final float WALKING_ANIMATION_DURATION = 1f;
	private static final float PRIMARY_ANIMATION_DURATION = 0.05f;
	private static final float SECONDARY_ANIMATION_DURATION = 0.5f;
	private static final float TERTIARY_ANIMATION_DURATION = 2f;

	// Dodge speed
	private static final float DODGE_SPEED = 20;
	private static final float DODGE_DIAGONAL_SPEED = 15;

	private Direction dodgeDirection;

	public Assassin(GameScreen game) {
		super(game);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	/* Dodge */
	@Override
	protected Ability initPrimary() {
		return new Ability(PRIMARY_ANIMATION_DURATION, PRIMARY_COOLDOWN)
				.setAbilityBegin(() -> {
					dodgeDirection = getInputDirection();
					inflictDebuff(DebuffType.IGNORE_FRICTION, 1, PRIMARY_ANIMATION_DURATION);
				})
				.setAbilityUsing(() -> {
					float velX = 0;
					float velY = 0;
					switch (dodgeDirection) {
						case RIGHT:
							velX = DODGE_SPEED;
							break;
						case LEFT:
							velX = -DODGE_SPEED;
							break;
						case UP:
							velY = DODGE_SPEED;
							break;
						case UP_RIGHT:
							velX = DODGE_DIAGONAL_SPEED;
							velY = DODGE_DIAGONAL_SPEED;
							break;
						case UP_LEFT:
							velX = -DODGE_DIAGONAL_SPEED;
							velY = DODGE_DIAGONAL_SPEED;
							break;
					}
					setVelocity(velX, velY);
				})
				.setResetCondition(isOnCooldown -> !isFalling());
	}

	/* Stars */
	@Override
	protected Ability initSecondary() {
		return new Ability(SECONDARY_ANIMATION_DURATION, SECONDARY_COOLDOWN)
				.setAbilityBegin(() -> {
					Entity shuriken = new Shuriken(getGame());
					Hitbox body = super.getHitbox(BODY);
					float x = body.getX() + body.getWidth() / 2;
					float y = body.getY() + body.getHeight() / 2;
					int x_velocity = 0;
					switch (getSpriteDirection()) {
						case RIGHT:
							x_velocity = Shuriken.FLYING_SPEED;
							break;
						case LEFT:
							x_velocity = -Shuriken.FLYING_SPEED;
							break;
					}
					shuriken.setPosition(x, y);
					shuriken.setVelocity(x_velocity, 0);
				});
	}

	/* Cleanse */
	@Override
	protected Ability initTertiary() {
		return new Ability(TERTIARY_ANIMATION_DURATION, TERTIARY_COOLDOWN)
				.setAbilityBegin(() -> Gdx.app.log("Assassin.java", "Tertiary"));
	}

	/* Animations */
	@Override
	protected void defineAnimations(Animations<CharacterStates, AssassinParts> animations) {
		HashMap<String, AssassinParts> filenames = new HashMap<>();
		filenames.put("Body", BODY);
		filenames.put("LeftArm", LEFT_ARM);
		filenames.put("LeftLeg", LEFT_LEG);
		filenames.put("RightArm", RIGHT_ARM);
		filenames.put("RightLeg", RIGHT_LEG);

		Animation<AssassinParts> standing = new Animation<>(STANDING_ANIMATION_DURATION, true);
		Animation<AssassinParts> walking = new Animation<>(WALKING_ANIMATION_DURATION, true);
		Animation<AssassinParts> primary = new Animation<>(PRIMARY_ANIMATION_DURATION, false);
		Animation<AssassinParts> secondary = new Animation<>(SECONDARY_ANIMATION_DURATION, false);

		standing.load("Assassin/Standing", filenames);
		walking.load("Assassin/Walking", filenames);
		primary.load("Assassin/Primary", filenames);
		secondary.load("Assassin/Secondary", filenames);

		animations.map(Collections.singleton(STANDING), standing)
				.map(Arrays.asList(STANDING, SECONDARY), secondary)
				.map(Arrays.asList(STANDING, PRIMARY), primary)
				.map(Collections.singleton(WALKING), walking)
				.map(Arrays.asList(WALKING, SECONDARY), secondary)
				.map(Arrays.asList(WALKING, PRIMARY), primary);
	}

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
}
