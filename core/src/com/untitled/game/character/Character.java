package com.untitled.game.character;

import com.badlogic.gdx.graphics.Color;
import com.untitled.game.Hitbox;
import com.untitled.game.LivingEntity;
import com.untitled.game.debuff.DebuffDefinition;
import com.untitled.game.debuff.Debuffs;
import com.untitled.screens.GameScreen;

import static com.untitled.game.EntityManager.CHARACTER_RENDER_PRIORITY;
import static com.untitled.game.debuff.DebuffType.SLOW;
import static com.untitled.screens.GameScreen.GAME_FLOOR_HEIGHT;

/**
 * A {@link LivingEntity} with certain debuff definitions.
 * Also can use 3 abilities: Primary, Secondary, Tertiary.
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

	/**
	 * @return The percentage of slow of this character.
	 */
	protected float getSlow() {
		return slow;
	}

	/**
	 * Test if a {@link LivingEntity} can damage this Character.
	 *
	 * @param entity the attacker.
	 * @param hitbox the hitbox of the attacker.
	 * @param damage the amount of damage the attacker will deal.
	 * @return whether the attack was successful.
	 */
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

	/**
	 * Determines whether the attackers hitbox touches this Character.
	 *
	 * @param hitbox the hitbox of an attacker
	 * @return wheter the attack was successful.
	 */
	protected abstract boolean hitTest(Hitbox hitbox);

	// TODO: Abstract these out

	/**
	 * Determines what this Character does to use the left key.
	 *
	 * @param keydown false: keyup, true: keydown.
	 */
	protected abstract void useLeft(boolean keydown);

	/**
	 * Determines what this Character does to use the right key.
	 *
	 * @param keydown false: keyup, true: keydown.
	 */
	protected abstract void useRight(boolean keydown);

	/**
	 * Determines what this Character does to use the up key.
	 *
	 * @param keydown false: keyup, true: keydown.
	 */
	protected abstract void useUp(boolean keydown);

	/**
	 * Determines what this Character does to use the Primary skill.
	 *
	 * @param keydown false: keyup, true: keydown.
	 */
	protected abstract void usePrimary(boolean keydown);

	/**
	 * Determines what this Character does to use the Secondary skill.
	 *
	 * @param keydown false: keyup, true: keydown.
	 */
	protected abstract void useSecondary(boolean keydown);

	/**
	 * Determines what this Character does to use the Tertiary skill.
	 *
	 * @param keydown false: keyup, true: keydown.
	 */
	protected abstract void useTertiary(boolean keydown);

	/**
	 * Determines what this Character does to switch characters.
	 */
	public abstract void useSwitchCharacter();
}
