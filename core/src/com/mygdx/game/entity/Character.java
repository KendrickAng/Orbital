package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ability.Callback;
import com.mygdx.game.state.States;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.state.CharacterStates.ABILITIES;
import static com.mygdx.game.state.CharacterStates.ABILITIES_ULTIMATE;
import static com.mygdx.game.state.CharacterStates.PRIMARY;
import static com.mygdx.game.state.CharacterStates.SECONDARY;
import static com.mygdx.game.state.CharacterStates.STANDING;
import static com.mygdx.game.state.CharacterStates.TERTIARY;

/**
 * Character is a LivingEntity with 3 abilities: Primary, Secondary, Tertiary.
 */
public abstract class Character extends LivingEntity<Character> {
	private static final int MOVESPEED = 5;
	private static final int FRICTION = 2;
	private boolean falling;

	public Character(MyGdxGame game) {
		super(game);
		setPosition(0, MAP_HEIGHT);
	}

	// Called once when skill is started.
	public abstract void isPrimaryBegin();

	public abstract void isSecondaryBegin();

	public abstract void isTertiaryBegin();

	// Logic for skills.
	public abstract void isPrimary();

	public abstract void isSecondary();

	public abstract void isTertiary();

	// Debug rendering.
	public abstract void isPrimaryDebug(ShapeRenderer shapeBatch);

	public abstract void isSecondaryDebug(ShapeRenderer shapeBatch);

	public abstract void isTertiaryDebug(ShapeRenderer shapeBatch);

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
						velocity.x += MOVESPEED / 10;
						break;
					case LEFT:
						velocity.x -= MOVESPEED / 10;
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

	@Override
	protected States<Character> states() {
		return new States<Character>()
				.add(STANDING);
	}

	/* Abilities */
	public void primary() {
		if (!getStates().contains(ABILITIES) && getAbilities().ready(PRIMARY)) {
			getStates().add(PRIMARY);
			getAbilities().use(PRIMARY, new Callback() {
				@Override
				public void call() {
					getStates().remove(PRIMARY);
				}
			});
			isPrimaryBegin();
		}
	}

	public void secondary() {
		if (!getStates().contains(ABILITIES) && getAbilities().ready(SECONDARY)) {
			getStates().add(SECONDARY);
			getAbilities().use(SECONDARY, new Callback() {
				@Override
				public void call() {
					getStates().remove(SECONDARY);
				}
			});
			isSecondaryBegin();
		}
	}

	public void tertiary() {
		if (!getStates().contains(ABILITIES_ULTIMATE) && getAbilities().ready(TERTIARY)) {
			getStates().add(TERTIARY);
			getAbilities().use(TERTIARY, new Callback() {
				@Override
				public void call() {
					getStates().remove(TERTIARY);
				}
			});
			isTertiaryBegin();
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);

		// Skill being used
		if (getStates().contains(PRIMARY)) {
			isPrimary();
		}

		if (getStates().contains(SECONDARY)) {
			isSecondary();
		}

		if (getStates().contains(TERTIARY)) {
			isTertiary();
		}
	}

	public void renderDebug(ShapeRenderer shapeBatch) {
		if (getStates().contains(PRIMARY)) {
			isPrimaryDebug(shapeBatch);
		}

		if (getStates().contains(SECONDARY)) {
			isSecondaryDebug(shapeBatch);
		}

		if (getStates().contains(TERTIARY)) {
			isTertiaryDebug(shapeBatch);
		}
	}

	/* Getters */
	public boolean isFalling() {
		return falling;
	}
}
