package com.untitled.ui.cooldowns;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.untitled.ui.UI;
import com.untitled.ui.UIAlign;

/**
 * A {@link UI} which renders multiple cooldowns in a horizonal row.
 */
public class VerticalCooldowns extends Cooldowns {
	private float padding;

	/**
	 * @param align     where the origin (x, y) of the UI should be relative to the content.
	 * @param cooldown0 Texture to display when skill is on 6/6 cooldown.
	 * @param cooldown1 Texture to display when skill is on 5/6 cooldown.
	 * @param cooldown2 Texture to display when skill is on 4/6 cooldown.
	 * @param cooldown3 Texture to display when skill is on 3/6 cooldown.
	 * @param cooldown4 Texture to display when skill is on 2/6 cooldown.
	 * @param cooldown5 Texture to display when skill is on 1/6 cooldown.
	 */
	public VerticalCooldowns(UIAlign align, float padding, Texture cooldown0, Texture cooldown1, Texture cooldown2, Texture cooldown3, Texture cooldown4, Texture cooldown5) {
		super(align, cooldown0, cooldown1, cooldown2, cooldown3, cooldown4, cooldown5);
		this.padding = padding;
	}

	@Override
	protected void update(Vector2 position, CooldownAbility ability) {
		position.y -= ability.getHeight() + padding;
	}
}
