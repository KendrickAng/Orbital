package com.mygdx.game.entity.character;

import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.LivingEntity;
import com.mygdx.game.entity.debuff.DebuffDefinition;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.screens.GameScreen;

import static com.mygdx.game.UntitledGame.FLOOR_HEIGHT;
import static com.mygdx.game.entity.EntityManager.CHARACTER_RENDER_PRIORITY;
import static com.mygdx.game.entity.debuff.DebuffType.SLOW;

/**
 * Character is a LivingEntity with 3 abilities: Secondary, Secondary, Tertiary.
 */
public abstract class Character<I extends Enum, S extends Enum, P extends Enum> extends LivingEntity<I, S, P> {
	private static final float DAMAGED_DURATION = 1f;
	private float slow;

	public Character(GameScreen game) {
		super(game, CHARACTER_RENDER_PRIORITY);
		getPosition().y = FLOOR_HEIGHT;
	}

	/* Debuffs */
	// where all debuffs for all characters are defined.
	@Override
	protected void defineDebuffs(Debuffs debuffs) {
		debuffs.map(SLOW, new DebuffDefinition()
				.defineUpdate(modifier -> {
					// Slow can't go above 100%.
					if (modifier > 1) {
						modifier = 1;
					}
					// accounts for percentage slow, e.g 40% slow -> modifier = 0.4.
					this.slow = modifier;
				})
				.defineEnd(() -> this.slow = 0));
	}

	@Override
	protected float damagedDuration() {
		return DAMAGED_DURATION;
	}

	public float getSlow() {
		return slow;
	}

	public boolean damageTest(LivingEntity entity, Hitbox hitbox, float damage) {
		// Not a LivingEntity
		if ((entity == null ||
				// Damager is not disposed or cc'ed
				(!entity.isDispose() && !entity.isCrowdControl())) &&
				// Self is not disposed
				!isDispose() &&
				hitTest(hitbox)) {
			return inflictDamage(entity, damage);
		}
		return false;
	}

	protected abstract boolean hitTest(Hitbox hitbox);

	// TODO: Abstract these out
	protected abstract void useLeft(boolean keydown);

	protected abstract void useRight(boolean keydown);

	protected abstract void useUp(boolean keydown);

	protected abstract void usePrimary(boolean keydown);

	protected abstract void useSecondary(boolean keydown);

	protected abstract void useTertiary(boolean keydown);

	public abstract boolean useSwitchCharacter();
}
