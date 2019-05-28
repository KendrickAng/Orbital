package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Animations;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.ability.AbilityReady;

import static com.mygdx.game.state.CharacterStates.PRIMARY;
import static com.mygdx.game.state.CharacterStates.SECONDARY;
import static com.mygdx.game.state.CharacterStates.STANDING;
import static com.mygdx.game.state.CharacterStates.TERTIARY;
import static com.mygdx.game.texture.Textures.ASSASSIN_PRIMARY;
import static com.mygdx.game.texture.Textures.ASSASSIN_SECONDARY;
import static com.mygdx.game.texture.Textures.ASSASSIN_STANDING;
import static com.mygdx.game.texture.Textures.ASSASSIN_TERTIARY;

public class Assassin extends Character {
	// Skill cd in seconds.
	private static final float PRIMARY_COOLDOWN = 0;
	private static final float SECONDARY_COOLDOWN = 1;
	private static final float TERTIARY_COOLDOWN = 2;

	// Skill lasting time in seconds.
	private static final float PRIMARY_DURATION = 0.5f;
	private static final float SECONDARY_DURATION = 0.5f;
	private static final float TERTIARY_DURATION = 5;

	// influencing dodging
	private static final int STRAFE_SPEED = 15;
	private static final int JUMP_SPEED = 15;

	public Assassin(MyGdxGame game) {
		super(game);
	}

	@Override
	protected Abilities<Character> abilities() {
		Ability primary = new Ability(PRIMARY_DURATION, PRIMARY_COOLDOWN)
				.setReady(new AbilityReady() {
					@Override
					public boolean check(boolean isOnCooldown) {
						return !isFalling();
					}
				});
		return new Abilities<Character>()
				.add(PRIMARY, primary)
				.add(SECONDARY, new Ability(SECONDARY_DURATION, SECONDARY_COOLDOWN))
				.add(TERTIARY, new Ability(TERTIARY_DURATION, TERTIARY_COOLDOWN));
	}

	@Override
	protected Animations<Character> animations() {
		Texture assassin_standing = getGame().getTextureManager().get(ASSASSIN_STANDING);
		Texture assassin_primary = getGame().getTextureManager().get(ASSASSIN_PRIMARY);
		Texture assassin_secondary = getGame().getTextureManager().get(ASSASSIN_SECONDARY);
		Texture assassin_tertiary = getGame().getTextureManager().get(ASSASSIN_TERTIARY);

		return new Animations<Character>()
				.add(STANDING, assassin_standing, 1)
				.add(PRIMARY, assassin_primary, 2)
				.add(SECONDARY, assassin_secondary, 2)
				.add(TERTIARY, assassin_tertiary, 2);
	}

	@Override
	public void isPrimaryBegin() {
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
		super.setVelocity(x_vel, JUMP_SPEED);
	}

	@Override
	public void isSecondaryBegin() {
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
		super.getGame().getEntityManager().add(shuriken);
	}

	@Override
	public void isTertiaryBegin() {
		Gdx.app.log("Assassin.java", "Tertiary");
	}

	@Override
	public void isPrimary() {

	}

	@Override
	public void isSecondary() {

	}

	@Override
	public void isTertiary() {

	}

	// Dodge
	@Override
	public void isPrimaryDebug(ShapeRenderer shapeBatch) {
	}

	// Stars
	@Override
	public void isSecondaryDebug(ShapeRenderer shapeBatch) {
	}

	// Cleanse
	@Override
	public void isTertiaryDebug(ShapeRenderer shapeBatch) {
		shapeBatch.rect(getX(), getY(), getWidth(), getHeight());
	}
}
