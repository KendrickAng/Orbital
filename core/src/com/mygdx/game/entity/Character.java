package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;

import java.util.HashSet;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.Character.States.PRIMARY;
import static com.mygdx.game.entity.Character.States.SECONDARY;
import static com.mygdx.game.entity.Character.States.STANDING;
import static com.mygdx.game.entity.Character.States.TERTIARY;
import static com.mygdx.game.entity.Character.States.WALKING;
import static com.mygdx.game.entity.debuff.DebuffType.IGNORE_FRICTION;
import static com.mygdx.game.entity.debuff.DebuffType.SLOW;

/**
 * Character is a LivingEntity with 3 abilities: Primary, Secondary, Tertiary.
 */
public abstract class Character<R extends Enum> extends LivingEntity<Character.States, R> {
	public enum States {
		STANDING, WALKING,
		PRIMARY, SECONDARY, TERTIARY
	}

	private static final float MOVESPEED = 2f;

	// Movespeed is multiplied by this constant in air
	private static final float AIR_MOVESPEED = 0.1f;

	// Velocity is multiplied by this constant
	private static final float FRICTION = 0.6f;
	private static final float AIR_FRICTION = 0.95f;

	private float movespeed;
	private float friction;
	private boolean falling;

	public Character(GameScreen game) {
		super(game);
		movespeed = MOVESPEED;
		friction = FRICTION;

		setPosition(0, MAP_HEIGHT);
	}

	@Override
	protected void states(HashSet<States> states) {
		states.add(STANDING);
	}

	/* Abilities */
	protected abstract Ability initPrimary();

	protected abstract Ability initSecondary();

	protected abstract Ability initTertiary();

	@Override
	protected Abilities<States> abilities() {
		return new Abilities<>(getStates())
				.add(PRIMARY, initPrimary())
				.add(SECONDARY, initSecondary())
				.add(TERTIARY, initTertiary());
	}

	public void usePrimary() {
		getAbilities().use(PRIMARY);
	}

	public void useSecondary() {
		getAbilities().use(SECONDARY);
	}

	public void useTertiary() {
		getAbilities().use(TERTIARY);
	}

	/* Debuffs */

	@Override
	protected Debuffs<DebuffType> debuffs() {
		Debuff slow = new Debuff()
				.setApply(modifier -> setMovespeed(MOVESPEED * (1 - modifier)))
				.setEnd(() -> setMovespeed(MOVESPEED));

		Debuff ignoreFriction = new Debuff()
				.setBegin(() -> setFriction(1))
				.setEnd(() -> setFriction(FRICTION));

		return new Debuffs<DebuffType>()
				.define(SLOW, slow)
				.define(IGNORE_FRICTION, ignoreFriction);
	}

	/* Update */
	@Override
	protected void updatePosition(Vector2 position) {
		if (position.x < 0) {
			position.x = 0;
		}

		if (position.x > GAME_WIDTH - getWidth()) {
			position.x = GAME_WIDTH - getWidth();
		}
	}

	@Override
	protected void updateVelocity(Vector2 position, Vector2 velocity) {
		switch (getInputDirection()) {
			case RIGHT:
			case UP_RIGHT:
				setSpriteDirection(Direction.RIGHT);
				break;
			case LEFT:
			case UP_LEFT:
				setSpriteDirection(Direction.LEFT);
				break;
		}

		if (position.y > MAP_HEIGHT) {
			falling = true;
		}

		// Airborne
		if (falling) {
			if (position.y > MAP_HEIGHT) {
				switch (getInputDirection()) {
					case RIGHT:
					case UP_RIGHT:
						velocity.x += movespeed * AIR_MOVESPEED;
						break;
					case LEFT:
					case UP_LEFT:
						velocity.x -= movespeed * AIR_MOVESPEED;
						break;
				}
				velocity.x *= AIR_FRICTION;
				velocity.y += GRAVITY;
			} else {
				// Touched ground
				velocity.y = 0;
				position.y = MAP_HEIGHT;
				falling = false;
			}

			// Not Airborne
		} else {
			switch (getInputDirection()) {
				case RIGHT:
				case UP_RIGHT:
					velocity.x += movespeed;
					break;
				case LEFT:
				case UP_LEFT:
					velocity.x -= movespeed;
					break;
			}
			velocity.x *= friction;
		}
	}

	/* Debug rendering. */
	protected abstract void isPrimaryDebug(ShapeRenderer shapeRenderer);

	protected abstract void isSecondaryDebug(ShapeRenderer shapeRenderer);

	protected abstract void isTertiaryDebug(ShapeRenderer shapeRenderer);

	@Override
	public void renderDebug(ShapeRenderer shapeRenderer) {
		getAnimations().renderDebug(shapeRenderer);
		if (getStates().contains(PRIMARY)) {
			isPrimaryDebug(shapeRenderer);
		}

		if (getStates().contains(SECONDARY)) {
			isSecondaryDebug(shapeRenderer);
		}

		if (getStates().contains(TERTIARY)) {
			isTertiaryDebug(shapeRenderer);
		}
	}

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

	/* Getters */
	public boolean isFalling() {
		return falling;
	}

	private void setMovespeed(float speed) {
		movespeed = speed;
	}

	private void setFriction(float friction) {
		this.friction = friction;
	}
}
