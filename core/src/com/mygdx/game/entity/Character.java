package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.ability.Ability;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.Character.AbilityState.PRIMARY;
import static com.mygdx.game.entity.Character.AbilityState.SECONDARY;
import static com.mygdx.game.entity.Character.AbilityState.TERTIARY;
import static com.mygdx.game.entity.Character.MovingState.STANDING;
import static com.mygdx.game.entity.Character.MovingState.WALKING;

/**
 * Character is a LivingEntity with 3 abilities: Primary, Secondary, Tertiary.
 */
public abstract class Character extends LivingEntity<Character.MovingState, Character.AbilityState> {
	public enum MovingState {
		STANDING, WALKING
	}

	public enum AbilityState {
		PRIMARY, SECONDARY, TERTIARY
	}

	private static final int MOVESPEED = 4;
	private static final int FRICTION = 2;

	private boolean falling;

	public Character(GameScreen game) {
		super(game);
		setPosition(0, MAP_HEIGHT);
	}

	@Override
	protected MovingState basicState() {
		return STANDING;
	}

	/* Abilities */
	protected abstract Ability initPrimary();
	protected abstract Ability initSecondary();
	protected abstract Ability initTertiary();

	@Override
	protected Abilities<AbilityState> abilities() {
		return new Abilities<AbilityState>(getAbilityStates())
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
				setSpriteDirection(Direction.RIGHT);
				break;
			case LEFT:
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
						velocity.x += (float) MOVESPEED / 10;
						break;
					case LEFT:
						velocity.x -= (float) MOVESPEED / 10;
						break;
				}
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
					velocity.x += MOVESPEED;
					break;
				case LEFT:
					velocity.x -= MOVESPEED;
					break;
			}
			velocity.x /= FRICTION;
		}
	}

	/* Debug rendering. */
	protected abstract void isPrimaryDebug(ShapeRenderer shapeRenderer);
	protected abstract void isSecondaryDebug(ShapeRenderer shapeRenderer);
	protected abstract void isTertiaryDebug(ShapeRenderer shapeRenderer);

	@Override
	public void renderDebug(ShapeRenderer shapeRenderer) {
		getHitbox().renderDebug(shapeRenderer);
		if (getAbilityStates().contains(PRIMARY)) {
			isPrimaryDebug(shapeRenderer);
		}

		if (getAbilityStates().contains(SECONDARY)) {
			isSecondaryDebug(shapeRenderer);
		}

		if (getAbilityStates().contains(TERTIARY)) {
			isTertiaryDebug(shapeRenderer);
		}
	}

	@Override
	protected void updateDirection(Direction inputDirection) {
		switch (inputDirection) {
			case NONE:
				setBasicState(STANDING);
				break;
			case RIGHT:
			case LEFT:
				setBasicState(WALKING);
				break;
		}
	}

	/* Getters */
	public boolean isFalling() {
		return falling;
	}
}
