package com.mygdx.game.screens.game.character;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.game.Hitbox;
import com.mygdx.game.screens.game.LivingEntity;
import com.mygdx.game.screens.game.debuff.DebuffDefinition;
import com.mygdx.game.screens.game.debuff.Debuffs;

import static com.mygdx.game.screens.GameScreen.GAME_FLOOR_HEIGHT;
import static com.mygdx.game.screens.game.EntityManager.CHARACTER_RENDER_PRIORITY;
import static com.mygdx.game.screens.game.debuff.DebuffType.SLOW;

/**
 * Character is a LivingEntity with 3 abilities: Secondary, Secondary, Tertiary.
 */
public abstract class Character<I extends Enum, S extends Enum, P extends Enum> extends LivingEntity<I, S, P> {
	static final String PERFECT_TEXT = "PERFECT";
	static final Color FLOATING_TEXT_COLOR = Color.valueOf("ca9b52");

	private static final float DAMAGED_DURATION = 0.75f;
	private float slow;

	public Character(GameScreen game) {
		super(game, CHARACTER_RENDER_PRIORITY);
		getPosition().y = GAME_FLOOR_HEIGHT;
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

	public abstract void useSwitchCharacter();
}
