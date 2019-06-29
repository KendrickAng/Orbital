package com.mygdx.game.entity.character;

import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.LivingEntity;
import com.mygdx.game.entity.debuff.DebuffDefinition;
import com.mygdx.game.entity.debuff.Debuffs;

import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.debuff.DebuffType.DAMAGE_REDUCTION;
import static com.mygdx.game.entity.debuff.DebuffType.SLOW;

/**
 * Character is a LivingEntity with 3 abilities: Primary, Secondary, Tertiary.
 */
public abstract class Character<I extends Enum, S extends Enum, P extends Enum> extends LivingEntity<I, S, P> {
	private float slow;

	public Character(GameScreen game) {
		super(game);
		setPosition(0, MAP_HEIGHT);
	}

	/* Debuffs */
	// where all debuffs for all characters are defined.
	@Override
	protected void defineDebuffs(Debuffs debuffs) {
		DebuffDefinition slow = new DebuffDefinition()
				.setApply(modifier -> {
					// Slow can't go above 100%.
					if (modifier > 1) {
						modifier = 1;
					}
					// accounts for percentage slow, e.g 40% slow -> modifier = 0.4.
					this.slow = modifier;
				})
				.setEnd(() -> this.slow = 0);

		DebuffDefinition damageReduction = new DebuffDefinition()
				.setApply(modifier -> {
					if (modifier > 1) {
						modifier = 1;
					}
				});

		debuffs.map(SLOW, slow)
				.map(DAMAGE_REDUCTION, damageReduction);
	}

	public float getSlow() {
		return slow;
	}

	public void setFlipX(boolean flip) {
		super.setFlipX(flip);
	}

	protected abstract void useLeft(boolean keydown);
	protected abstract void useRight(boolean keydown);
	protected abstract void usePrimary(boolean keydown);
	protected abstract void useSecondary(boolean keydown);
	protected abstract void useTertiary(boolean keydown);
}
