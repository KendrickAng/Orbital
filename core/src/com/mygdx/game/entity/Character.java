package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.entity.state.CharacterStates;
import com.mygdx.game.entity.state.States;

import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.debuff.DebuffType.IGNORE_FRICTION;
import static com.mygdx.game.entity.debuff.DebuffType.SLOW;
import static com.mygdx.game.entity.state.CharacterStates.PRIMARY;
import static com.mygdx.game.entity.state.CharacterStates.SECONDARY;
import static com.mygdx.game.entity.state.CharacterStates.STANDING;
import static com.mygdx.game.entity.state.CharacterStates.TERTIARY;
import static com.mygdx.game.entity.state.CharacterStates.WALKING;

/**
 * Character is a LivingEntity with 3 abilities: Primary, Secondary, Tertiary.
 */
public abstract class Character<R extends Enum> extends LivingEntity<CharacterStates, R> {
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
	protected void defineStates(States<CharacterStates> states) {
		states.addState(STANDING);
	}

	/* Abilities */
	protected abstract Ability initPrimary();

	protected abstract Ability initSecondary();

	protected abstract Ability initTertiary();

	@Override
	protected void defineAbilities(Abilities<CharacterStates> abilities) {
		abilities.map(PRIMARY, initPrimary())
				.map(SECONDARY, initSecondary())
				.map(TERTIARY, initTertiary());
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
	protected void defineDebuffs(Debuffs<DebuffType> debuffs) {
		Debuff slow = new Debuff()
				.setApply(modifier -> setMovespeed(MOVESPEED * (1 - modifier)))
				.setEnd(() -> setMovespeed(MOVESPEED));

		Debuff ignoreFriction = new Debuff()
				.setBegin(() -> setFriction(1))
				.setEnd(() -> setFriction(FRICTION));

		debuffs.map(SLOW, slow)
				.map(IGNORE_FRICTION, ignoreFriction);
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
