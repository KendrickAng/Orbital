package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Animations;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.ability.AbilityCallback;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.shape.Rectangle;

import static com.mygdx.game.entity.Character.AbilityState.PRIMARY;
import static com.mygdx.game.entity.Character.AbilityState.SECONDARY;
import static com.mygdx.game.entity.Character.AbilityState.TERTIARY;
import static com.mygdx.game.entity.Character.MovingState.STANDING;
import static com.mygdx.game.entity.Character.MovingState.WALKING;
import static com.mygdx.game.texture.Textures.TANK_PRIMARY;
import static com.mygdx.game.texture.Textures.TANK_SECONDARY;
import static com.mygdx.game.texture.Textures.TANK_STANDING;
import static com.mygdx.game.texture.Textures.TANK_TERTIARY;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character {
	private static final float HEALTH = 100;

	private static final float SECONDARY_DAMAGE = 10;

	// Skill debuff duration in seconds.
	private static final float TERTIARY_DEBUFF_DURATION = 5f;

	// Skill debuff modifiers from 0f - 1f (0% - 100%)
	private static final float PRIMARY_SLOW_MODIFIER = 0.5f;
	private static final float SECONDARY_SLOW_MODIFIER = 0.5f;
	private static final float TERTIARY_SLOW_MODIFIER = 0.5f;

	// Skill cooldown in seconds.
	private static final float PRIMARY_COOLDOWN = 0;
	private static final float SECONDARY_COOLDOWN = 1;
	private static final float TERTIARY_COOLDOWN = TERTIARY_DEBUFF_DURATION + 2;

	// Skill animation duration in seconds.
	private static final float PRIMARY_ANIMATION_DURATION = 0.5f;
	private static final float SECONDARY_ANIMATION_DURATION = 0.5f;
	private static final float TERTIARY_ANIMATION_DURATION = 0.5f;

	/* Hitboxes */
	private Rectangle blockHitbox;
	private Rectangle slashHitbox;

	public Tank(GameScreen game) {
		super(game);
		blockHitbox = new Rectangle();
		slashHitbox = new Rectangle();
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	/* Block */
	@Override
	protected Ability initPrimary() {
		return new Ability(PRIMARY_ANIMATION_DURATION, PRIMARY_COOLDOWN)
				.setAbilityBegin(new AbilityCallback() {
					@Override
					public void call() {
						Gdx.app.log("Tank.java", "Primary");
						inflictDebuff(DebuffType.SLOW, PRIMARY_SLOW_MODIFIER, PRIMARY_ANIMATION_DURATION);
					}
				})
				.setAbilityUsing(new AbilityCallback() {
					@Override
					public void call() {
						// Update primary block hitbox
						float SHIELD_OFFSET = getHeight() / 10;
						blockHitbox.setX(getX() - SHIELD_OFFSET);
						blockHitbox.setY(getY() - SHIELD_OFFSET);
						blockHitbox.setWidth(getWidth() + 2 * SHIELD_OFFSET);
						blockHitbox.setHeight(getHeight() + 2 * SHIELD_OFFSET);
					}
				});
	}

	@Override
	public void isPrimaryDebug(ShapeRenderer shapeRenderer) {
		blockHitbox.renderDebug(shapeRenderer);
	}

	/* Slash */
	@Override
	protected Ability initSecondary() {
		return new Ability(SECONDARY_ANIMATION_DURATION, SECONDARY_COOLDOWN)
				.setAbilityBegin(new AbilityCallback() {
					@Override
					public void call() {
						Gdx.app.log("Tank.java", "Secondary");
						inflictDebuff(DebuffType.SLOW, SECONDARY_SLOW_MODIFIER, SECONDARY_ANIMATION_DURATION);
					}
				})
				.setAbilityUsing(new AbilityCallback() {
					@Override
					public void call() {
						// Update secondary slash hitbox
						slashHitbox.setY(getY() + getHeight() / 2);
						slashHitbox.setWidth(getHeight());
						slashHitbox.setHeight(getWidth() / 2);
						switch (getSpriteDirection()) {
							case RIGHT:
								slashHitbox.setX(getX() + getWidth() / 2);
								break;
							case LEFT:
								slashHitbox.setX(getX() - getHeight() + getWidth() / 2);
								break;
						}
					}
				})
				.addAbilityTask(new AbilityCallback() {
					@Override
					public void call() {
						Boss1 boss = getGame().getBoss();
						if (slashHitbox.hitTest(boss.getHitbox())) {
							Gdx.app.log("Tank.java", "Boss was hit!");
							boss.damage(SECONDARY_DAMAGE);
						}
					}
				}, SECONDARY_ANIMATION_DURATION / 2);
	}

	@Override
	public void isSecondaryDebug(ShapeRenderer shapeRenderer) {
		slashHitbox.renderDebug(shapeRenderer);
	}

	/* Fortress */
	@Override
	protected Ability initTertiary() {
		return new Ability(TERTIARY_ANIMATION_DURATION, TERTIARY_COOLDOWN)
				.setAbilityBegin(new AbilityCallback() {
					@Override
					public void call() {
						Gdx.app.log("Tank.java", "Tertiary");
						inflictDebuff(DebuffType.SLOW, TERTIARY_SLOW_MODIFIER, TERTIARY_DEBUFF_DURATION);
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
		Texture tank_standing = getGame().getTextureManager().get(TANK_STANDING);

		return new Animations<MovingState>()
				.add(STANDING, tank_standing, 1)
				.add(WALKING, tank_standing, 1);
	}

	@Override
	protected Animations<AbilityState> abilityAnimations() {
		Texture tank_primary = getGame().getTextureManager().get(TANK_PRIMARY);
		Texture tank_secondary = getGame().getTextureManager().get(TANK_SECONDARY);
		Texture tank_tertiary = getGame().getTextureManager().get(TANK_TERTIARY);

		return new Animations<AbilityState>()
				.add(PRIMARY, tank_primary, 1)
				.add(SECONDARY, tank_secondary, 1)
				.add(TERTIARY, tank_tertiary, 1);
	}
}
