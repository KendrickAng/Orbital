package com.mygdx.game.entity.shuriken;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.entity.Entity;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.EntityManager.SHURIKEN_RENDER_PRIORITY;
import static com.mygdx.game.entity.shuriken.ShurikenParts.BODY;
import static com.mygdx.game.entity.shuriken.ShurikenStates.FLYING;

public class Shuriken extends Entity<Enum, ShurikenStates, ShurikenParts> {
	private float damage;
	private static final float FLYING_ANIMATION_DURATION = 0.05f;
	private static final int FLYING_SPEED = 20;

	public Shuriken(GameScreen game, float x, float y, boolean flipX, float damage) {
		super(game, SHURIKEN_RENDER_PRIORITY);
		getPosition().x = x;
		getPosition().y = y;
		getFlipX().set(flipX);
		this.damage = damage;
	}

	@Override
	protected void defineStates(States<Enum, ShurikenStates> states) {
		states.add(new State<Enum, ShurikenStates>(FLYING)
				.defineUpdate(() -> {
					if (getFlipX().get()) {
						getPosition().x -= FLYING_SPEED;
					} else {
						getPosition().x += FLYING_SPEED;
					}

					if (getPosition().x < 0) {
						dispose();
					} else if (getPosition().x > GAME_WIDTH - getHitbox(BODY).getWidth()) {
						dispose();
					} else {
						if (getGame().getBoss1()
								.damageTest(null, getHitbox(BODY), damage)) {
							dispose();
						}
					}
				}));
	}

	@Override
	protected boolean canInput() {
		return false;
	}

	@Override
	protected void defineAnimations(Animations<ShurikenStates, ShurikenParts> animations, Assets assets) {
		animations.map(FLYING, assets.getShurikenAnimation(Assets.ShurikenAnimationName.FLYING)
				.setDuration(FLYING_ANIMATION_DURATION)
				.setLoop());
	}
}
