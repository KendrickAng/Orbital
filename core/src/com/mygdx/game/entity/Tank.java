package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.animation.Animations;
import com.mygdx.game.animation.AnimationsGroup;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.shape.Rectangle;

import static com.mygdx.game.entity.Character.States.PRIMARY;
import static com.mygdx.game.entity.Character.States.SECONDARY;
import static com.mygdx.game.entity.Character.States.STANDING;
import static com.mygdx.game.entity.Character.States.TERTIARY;
import static com.mygdx.game.entity.Character.States.WALKING;
import static com.mygdx.game.entity.Tank.Parts.BODY;
import static com.mygdx.game.entity.Tank.Parts.LEFT_ARM;
import static com.mygdx.game.entity.Tank.Parts.LEFT_LEG;
import static com.mygdx.game.entity.Tank.Parts.RIGHT_ARM;
import static com.mygdx.game.entity.Tank.Parts.RIGHT_LEG;
import static com.mygdx.game.entity.Tank.Parts.SHIELD;
import static com.mygdx.game.entity.Tank.Parts.WEAPON;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character<Tank.Parts> {
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

	/*
	 Order is important! Last enum will be rendered on top.
	*/
	public enum Parts {
		SHIELD, LEFT_ARM, LEFT_LEG, BODY, RIGHT_LEG, WEAPON, RIGHT_ARM
	}

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
				.setAbilityBegin(() -> {
					Gdx.app.log("Tank.java", "Primary");
					inflictDebuff(DebuffType.SLOW, PRIMARY_SLOW_MODIFIER, PRIMARY_ANIMATION_DURATION);
				})
				.setAbilityUsing(() -> {
					// Update primary block hitbox
					float SHIELD_OFFSET = getHeight() / 10;
					blockHitbox.setX(getX() - SHIELD_OFFSET);
					blockHitbox.setY(getY() - SHIELD_OFFSET);
					blockHitbox.setWidth(getWidth() + 2 * SHIELD_OFFSET);
					blockHitbox.setHeight(getHeight() + 2 * SHIELD_OFFSET);
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
				.setAbilityBegin(() -> {
					Gdx.app.log("Tank.java", "Secondary");
					inflictDebuff(DebuffType.SLOW, SECONDARY_SLOW_MODIFIER, SECONDARY_ANIMATION_DURATION);
				})
				.setAbilityUsing(() -> {
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
				})
				.addAbilityTask(() -> {
					Boss1 boss = getGame().getBoss();
					if (slashHitbox.hitTest(boss.getHitbox())) {
						Gdx.app.log("Tank.java", "Boss was hit!");
						boss.damage(SECONDARY_DAMAGE);
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
				.setAbilityBegin(() -> {
					Gdx.app.log("Tank.java", "Tertiary");
					inflictDebuff(DebuffType.SLOW, TERTIARY_SLOW_MODIFIER, TERTIARY_DEBUFF_DURATION);
				});
	}

	@Override
	public void isTertiaryDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
	}

	/* Animations */
	@Override
	protected Animations<States, Parts> animations() {
		AnimationsGroup<Parts> standing = new AnimationsGroup<Parts>("Tank/Standing")
				.add(SHIELD, "Shield")
				.add(LEFT_ARM, "LeftArm")
				.add(LEFT_LEG, "LeftLeg")
				.add(BODY, "Body")
				.add(RIGHT_LEG, "RightLeg")
				.add(WEAPON, "Weapon")
				.add(RIGHT_ARM, "RightArm")
				.load();

		AnimationsGroup<Parts> walking = new AnimationsGroup<Parts>("Tank/Standing")
				.add(SHIELD, "Shield")
				.add(LEFT_ARM, "LeftArm")
				.add(LEFT_LEG, "LeftLeg")
				.add(BODY, "Body")
				.add(RIGHT_LEG, "RightLeg")
				.add(WEAPON, "Weapon")
				.add(RIGHT_ARM, "RightArm")
				.load();

		AnimationsGroup<Parts> primary = new AnimationsGroup<Parts>("Tank/Primary")
				.add(BODY, "Body")
				.load();

		AnimationsGroup<Parts> secondary = new AnimationsGroup<Parts>("Tank/Secondary")
				.add(BODY, "Body")
				.load();

		AnimationsGroup<Parts> tertiary = new AnimationsGroup<Parts>("Tank/Tertiary")
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
