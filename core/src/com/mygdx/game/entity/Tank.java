package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Animations;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.ability.Ability;

import static com.mygdx.game.state.CharacterStates.PRIMARY;
import static com.mygdx.game.state.CharacterStates.SECONDARY;
import static com.mygdx.game.state.CharacterStates.STANDING;
import static com.mygdx.game.state.CharacterStates.TERTIARY;
import static com.mygdx.game.texture.Textures.TANK_PRIMARY;
import static com.mygdx.game.texture.Textures.TANK_SECONDARY;
import static com.mygdx.game.texture.Textures.TANK_STANDING;
import static com.mygdx.game.texture.Textures.TANK_TERTIARY;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character {
	// Skill cd in seconds.
	private static final float PRIMARY_COOLDOWN = 0;
	private static final float SECONDARY_COOLDOWN = 1;
	private static final float TERTIARY_COOLDOWN = 2;

	// Skill lasting time in seconds.
	private static final float PRIMARY_DURATION = 0.5f;
	private static final float SECONDARY_DURATION = 0.5f;
	private static final float TERTIARY_DURATION = 5;

	/* Hitboxes */
	private float SHIELD_OFFSET;
	private float SWORD_LENGTH;
	private float SWORD_WIDTH;

	public Tank(MyGdxGame game) {
		super(game);
	}

	@Override
	protected Abilities<Character> abilities() {
		return new Abilities<Character>()
				.add(PRIMARY, new Ability(PRIMARY_DURATION, PRIMARY_COOLDOWN))
				.add(SECONDARY, new Ability(SECONDARY_DURATION, SECONDARY_COOLDOWN))
				.add(TERTIARY, new Ability(TERTIARY_DURATION, TERTIARY_COOLDOWN));
	}

	@Override
	protected Animations<Character> animations() {
		/*
		 * Load Textures
		 */
		Texture tank_standing = getGame().getTextureManager().get(TANK_STANDING);
		Texture tank_primary = getGame().getTextureManager().get(TANK_PRIMARY);
		Texture tank_secondary = getGame().getTextureManager().get(TANK_SECONDARY);
		Texture tank_tertiary = getGame().getTextureManager().get(TANK_TERTIARY);

		return new Animations<Character>()
				.add(STANDING, tank_standing, 1)
				.add(PRIMARY, tank_primary, 2)
				.add(SECONDARY, tank_secondary, 2)
				.add(TERTIARY, tank_tertiary, 2);
	}

	@Override
	public void isPrimaryBegin() {
		Gdx.app.log("Tank.java", "Primary");
	}

	@Override
	public void isSecondaryBegin() {
		Gdx.app.log("Tank.java", "Secondary");
	}

	@Override
	public void isTertiaryBegin() {
		Gdx.app.log("Tank.java", "Tertiary");
	}

	@Override
	public void isPrimary() {
		// Update primary block hitbox
		SHIELD_OFFSET = super.getHeight() / 10;
	}

	@Override
	public void isSecondary() {
		// Update secondary slash hitbox
		SWORD_LENGTH = super.getHeight();
		SWORD_WIDTH = super.getWidth() / 2;
	}

	@Override
	public void isTertiary() {

	}

	/* Block */
	@Override
	public void isPrimaryDebug(ShapeRenderer shapeBatch) {
		shapeBatch.rect(getX() - SHIELD_OFFSET, getY() - SHIELD_OFFSET,
				getWidth() + 2 * SHIELD_OFFSET, getHeight() + 2 * SHIELD_OFFSET);
	}

	/* Slash */
	@Override
	public void isSecondaryDebug(ShapeRenderer shapeBatch) {
		shapeBatch.rect(getX() + getWidth() / 2, getY() + getHeight() / 2,
				SWORD_LENGTH, SWORD_WIDTH);
	}

	/* Fortress */
	@Override
	public void isTertiaryDebug(ShapeRenderer shapeBatch) {
		shapeBatch.rect(getX(), getY(), getWidth(), getHeight());
	}
}
