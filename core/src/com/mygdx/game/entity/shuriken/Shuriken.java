package com.mygdx.game.entity.shuriken;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.ShurikenAnimationName;
import com.mygdx.game.entity.Entity;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.boss1.Boss1;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;
import com.mygdx.game.screens.GameScreen;

import static com.mygdx.game.UntitledGame.WINDOW_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_WIDTH;
import static com.mygdx.game.entity.EntityManager.SHURIKEN_RENDER_PRIORITY;
import static com.mygdx.game.entity.shuriken.ShurikenParts.BODY;
import static com.mygdx.game.entity.shuriken.ShurikenStates.FLYING;

public class Shuriken extends Entity<Enum, ShurikenStates, ShurikenParts> {
	private static final float FLYING_ANIMATION_DURATION = 0.05f;
	private static final int FLYING_SPEED = 20;

	private float damage;
	private double theta;
	private Debuff debuff;

	public Shuriken(GameScreen game, float x, float y, float degree, float damage, Debuff debuff) {
		super(game, SHURIKEN_RENDER_PRIORITY);
		getPosition().x = x;
		getPosition().y = y;
		this.theta = degree / 180 * Math.PI;
		this.damage = damage;
		this.debuff = debuff;
	}

	@Override
	protected void defineStates(States<Enum, ShurikenStates> states) {
		states.add(new State<Enum, ShurikenStates>(FLYING)
				.defineUpdate(() -> {
					getPosition().x += Math.sin(theta) * FLYING_SPEED;
					getPosition().y += Math.cos(theta) * FLYING_SPEED;
					float x = getPosition().x - WINDOW_WIDTH / 2f;
					float y = getPosition().y - WINDOW_HEIGHT / 2f;

					if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) > 1000) {
						dispose();
					} else {
						Boss1 boss1 = getGame().getBoss1();
						if (getGame().getBoss1()
								.damageTest(null, getHitbox(BODY), damage)) {
							if (debuff != null) {
								boss1.inflictDebuff(debuff);
							}
							dispose();
						}
					}
				}));
	}

	@Override
	protected void defineAnimations(Animations<ShurikenStates, ShurikenParts> animations, Assets assets) {
		animations.map(FLYING, assets.getShurikenAnimation(ShurikenAnimationName.FLYING)
				.setDuration(FLYING_ANIMATION_DURATION)
				.setLoop());
	}

	@Override
	protected boolean canInput(Enum input) {
		return false;
	}
}
