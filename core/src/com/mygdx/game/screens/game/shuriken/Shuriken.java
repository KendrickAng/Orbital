package com.mygdx.game.screens.game.shuriken;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.ShurikenAnimationName;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.game.Entity;
import com.mygdx.game.screens.game.animation.Animations;
import com.mygdx.game.screens.game.boss1.Boss1;
import com.mygdx.game.screens.game.debuff.Debuff;
import com.mygdx.game.screens.game.state.State;
import com.mygdx.game.screens.game.state.States;

import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.screens.game.EntityManager.SHURIKEN_RENDER_PRIORITY;
import static com.mygdx.game.screens.game.shuriken.ShurikenParts.BODY;
import static com.mygdx.game.screens.game.shuriken.ShurikenStates.FLYING;

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
					getPosition().x += Math.sin(theta) * FLYING_SPEED * 60 * Gdx.graphics.getRawDeltaTime();
					getPosition().y += Math.cos(theta) * FLYING_SPEED * 60 * Gdx.graphics.getRawDeltaTime();
					float x = getPosition().x - CAMERA_WIDTH / 2f;
					float y = getPosition().y - CAMERA_HEIGHT / 2f;

					if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) > 1000) {
						dispose(0);
					} else {
						Boss1 boss1 = getGame().getBoss1();
						if (getGame().getBoss1()
								.damageTest(null, getHitbox(BODY), damage)) {
							if (debuff != null) {
								boss1.inflictDebuff(debuff);
							}
							dispose(0);
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
