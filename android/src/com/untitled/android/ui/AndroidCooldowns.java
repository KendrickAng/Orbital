package com.untitled.android.ui;

import com.untitled.assets.Assets;
import com.untitled.assets.TextureName;
import com.untitled.ui.UI;
import com.untitled.ui.UIAlign;
import com.untitled.ui.cooldowns.VerticalCooldowns;

/**
 * A {@link UI} which renders multiple cooldowns in a horizonal row.
 */
public class AndroidCooldowns extends VerticalCooldowns {

	/**
	 * @param align  where the origin (x, y) of the UI should be relative to the content.
	 * @param assets Untitled {@link Assets}.
	 */
	public AndroidCooldowns(UIAlign align, float padding, Assets assets) {
		super(align, padding, assets.getTexture(TextureName.ANDROID_COOLDOWN_0),
				assets.getTexture(TextureName.ANDROID_COOLDOWN_1),
				assets.getTexture(TextureName.ANDROID_COOLDOWN_2),
				assets.getTexture(TextureName.ANDROID_COOLDOWN_3),
				assets.getTexture(TextureName.ANDROID_COOLDOWN_4),
				assets.getTexture(TextureName.ANDROID_COOLDOWN_5));
	}
}
