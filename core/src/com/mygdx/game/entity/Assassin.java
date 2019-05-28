package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Animations;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.ability.AbilityCallback;
import com.mygdx.game.ability.AbilityResetCondition;

import static com.mygdx.game.entity.Character.AbilityState.PRIMARY;
import static com.mygdx.game.entity.Character.AbilityState.SECONDARY;
import static com.mygdx.game.entity.Character.AbilityState.TERTIARY;
import static com.mygdx.game.entity.Character.MovingState.STANDING;
import static com.mygdx.game.entity.Character.MovingState.WALKING;
import static com.mygdx.game.texture.Textures.ASSASSIN_PRIMARY;
import static com.mygdx.game.texture.Textures.ASSASSIN_SECONDARY;
import static com.mygdx.game.texture.Textures.ASSASSIN_STANDING;
import static com.mygdx.game.texture.Textures.ASSASSIN_TERTIARY;

public class Assassin extends Character {
	// Skill cooldown in seconds.
	private static final float PRIMARY_COOLDOWN = 0;
	private static final float SECONDARY_COOLDOWN = 1;
	private static final float TERTIARY_COOLDOWN = 2;

	// Skill animation duration in seconds.
	private static final float PRIMARY_DURATION = 0.5f;
	private static final float SECONDARY_DURATION = 0.5f;
	private static final float TERTIARY_DURATION = 0.5f;

	// influencing dodging
	private static final int STRAFE_SPEED = 15;
	private static final int JUMP_SPEED = 15;

	public Assassin(GameScreen game) {
		super(game);
	}

	/* Dodge */
	@Override
	protected Ability initPrimary() {
		return new Ability(PRIMARY_DURATION, PRIMARY_COOLDOWN)
				.setAbilityBegin(new AbilityCallback() {
					@Override
					public void call() {
						Gdx.app.log("Assassin.java", "Primary");
						int x_vel = 0;
						switch (getInputDirection()) {
							case RIGHT:
								x_vel = STRAFE_SPEED;
								break;
							case LEFT:
								x_vel = -STRAFE_SPEED;
								break;
						}
						setVelocity(x_vel, JUMP_SPEED);
					}
				})
				.setResetCondition(new AbilityResetCondition() {
					@Override
					public boolean check(boolean isOnCooldown) {
						return !isFalling();
					}
				});
	}

	@Override
	public void isPrimaryDebug(ShapeRenderer shapeRenderer) {

	}

	/* Stars */
	@Override
	protected Ability initSecondary() {
		return new Ability(SECONDARY_DURATION, SECONDARY_COOLDOWN)
				.setAbilityBegin(new AbilityCallback() {
					@Override
					public void call() {
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
					}
				});
	}

	@Override
	public void isSecondaryDebug(ShapeRenderer shapeRenderer) {

	}

	/* Cleanse */
	@Override
	protected Ability initTertiary() {
		return new Ability(TERTIARY_DURATION, TERTIARY_COOLDOWN)
				.setAbilityBegin(new AbilityCallback() {
					@Override
					public void call() {
						Gdx.app.log("Assassin.java", "Tertiary");
					}
				});
	}

	@Override
	public void isTertiaryDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
	}

	/* Animations */
	@Override
	protected Animations<MovingState> basicAnimations() {
		Texture assassin_standing = getGame().getTextureManager().get(ASSASSIN_STANDING);

		return new Animations<MovingState>()
				.add(STANDING, assassin_standing, 1)
				.add(WALKING, assassin_standing, 1);
	}

	@Override
	protected Animations<AbilityState> abilityAnimations() {
		Texture assassin_primary = getGame().getTextureManager().get(ASSASSIN_PRIMARY);
		Texture assassin_secondary = getGame().getTextureManager().get(ASSASSIN_SECONDARY);
		Texture assassin_tertiary = getGame().getTextureManager().get(ASSASSIN_TERTIARY);

		return new Animations<AbilityState>()
				.add(PRIMARY, assassin_primary, 1)
				.add(SECONDARY, assassin_secondary, 1)
				.add(TERTIARY, assassin_tertiary, 1);
	}
}
