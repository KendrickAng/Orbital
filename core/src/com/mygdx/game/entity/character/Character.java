package com.mygdx.game.entity.character;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.Direction;
import com.mygdx.game.entity.LivingEntity;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.entity.state.CharacterStates;
import com.mygdx.game.entity.state.States;

import java.util.Arrays;

import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.debuff.DebuffType.IGNORE_MOVE_DIRECTION;
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
public abstract class Character<S extends Enum, R extends Enum> extends LivingEntity<S, R> {
	private float movespeed;
	private float friction;
	private boolean falling;

	// these are just definitions of the Ability.
	private Ability primary;
	private Ability secondary;
	private Ability tertiary;

	public Character(GameScreen game) {
		super(game);

		setPosition(0, MAP_HEIGHT);
	}

	/* Debuffs */
	// where all debuffs for all characters are defined.
	@Override
	protected void defineDebuffs(Debuffs<DebuffType> debuffs) {
		Debuff slow = new Debuff()
				.setApply(modifier -> {
					// Slow can't go above 100%.
					if (modifier > 1) {
						modifier = 1;
					}
					// accounts for percentage slow, e.g 40% slow -> modifier = 0.4.
					this.movespeed = MOVESPEED * (1 - modifier);
				})
				.setEnd(() -> this.movespeed = MOVESPEED);

		debuffs.map(SLOW, slow)
				.map(IGNORE_FRICTION, ignoreFriction)
				.map(IGNORE_MOVE_DIRECTION, ignoreMoveDirection);
	}

	public void setFlipX(boolean flip) {
		super.setFlipX(flip);
	}

	public void setStanding() {
		setState(STANDING);
	}

	public void setWalking() {
		setState(WALKING);
	}

	protected abstract void usePrimary();
	protected abstract void useSecondary();
	protected abstract void useTertiary();

	/* Getters */
	public boolean isFalling() {
		return falling;
	}
}
