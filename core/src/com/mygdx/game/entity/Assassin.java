package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.animation.Animations;
import com.mygdx.game.animation.AnimationsGroup;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.shape.Rectangle;

import static com.mygdx.game.entity.Assassin.Parts.BODY;
import static com.mygdx.game.entity.Character.States.PRIMARY;
import static com.mygdx.game.entity.Character.States.SECONDARY;
import static com.mygdx.game.entity.Character.States.STANDING;
import static com.mygdx.game.entity.Character.States.TERTIARY;
import static com.mygdx.game.entity.Character.States.WALKING;

public class Assassin extends Character<Assassin.Parts> {
	private static final float HEALTH = 10;

	// Skill cooldown in seconds.
	private static final float PRIMARY_COOLDOWN = 0;
	private static final float SECONDARY_COOLDOWN = 1;
	private static final float TERTIARY_COOLDOWN = 2;

	// Skill animation duration in seconds.
	private static final float PRIMARY_DURATION = 0.05f;
	private static final float SECONDARY_DURATION = 0.5f;
	private static final float TERTIARY_DURATION = 0.5f;

	// Dodge speed
	private static final float DODGE_SPEED = 20;
	private static final float DODGE_DIAGONAL_SPEED = 15;

	private Direction dodgeDirection;

	public enum Parts {
		BODY
	}

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
		return new Ability(PRIMARY_DURATION, PRIMARY_COOLDOWN)
				.setAbilityBegin(() -> {
					Gdx.app.log("Assassin.java", "Primary");
					dodgeDirection = getInputDirection();
					inflictDebuff(DebuffType.IGNORE_FRICTION, 1, PRIMARY_DURATION);
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

	@Override
	public void isPrimaryDebug(ShapeRenderer shapeRenderer) {

	}

	/* Stars */
	@Override
	protected Ability initSecondary() {
		return new Ability(SECONDARY_DURATION, SECONDARY_COOLDOWN)
				.setAbilityBegin(() -> {
					Gdx.app.log("Assassin.java", "Secondary");
					Entity shuriken = new Shuriken(getGame());
					float x = getX() + getWidth() / 2;
					float y = getY() + getHeight() / 2;
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

	@Override
	public void isSecondaryDebug(ShapeRenderer shapeRenderer) {

	}

	/* Cleanse */
	@Override
	protected Ability initTertiary() {
		return new Ability(TERTIARY_DURATION, TERTIARY_COOLDOWN)
				.setAbilityBegin(() -> Gdx.app.log("Assassin.java", "Tertiary"));
	}

	@Override
	public void isTertiaryDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
	}

	/* Animations */
	@Override
	protected Animations<States, Parts> animations() {
		AnimationsGroup<Parts> standing = new AnimationsGroup<Parts>("Assassin/Standing")
				.add(BODY, "Body")
				.load();

		AnimationsGroup<Parts> walking = new AnimationsGroup<Parts>("Assassin/Standing")
				.add(BODY, "Body")
				.load();

		AnimationsGroup<Parts> primary = new AnimationsGroup<Parts>("Assassin/Primary")
				.add(BODY, "Body")
				.load();

		AnimationsGroup<Parts> secondary = new AnimationsGroup<Parts>("Assassin/Secondary")
				.add(BODY, "Body")
				.load();

		AnimationsGroup<Parts> tertiary = new AnimationsGroup<Parts>("Assassin/Tertiary")
				.add(BODY, "Body")
				.load();

		return new Animations<States, Parts>(getStates())
				.add(STANDING, standing, 1)
				.add(WALKING, walking, 1)
				.add(PRIMARY, primary, 2)
				.add(SECONDARY, secondary, 2)
				.add(TERTIARY, tertiary, 2)
				.done();
	}

	@Override
	protected Rectangle hitbox() {
		return getAnimations().getHitbox(BODY);
	}
}
