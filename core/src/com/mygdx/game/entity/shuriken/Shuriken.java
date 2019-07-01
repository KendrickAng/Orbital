package com.mygdx.game.entity.shuriken;

import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.entity.Entity;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.part.ShurikenParts;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;

import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.part.ShurikenParts.BODY;
import static com.mygdx.game.entity.shuriken.ShurikenStates.FLYING;

public class Shuriken extends Entity<Enum, ShurikenStates, ShurikenParts> {
	private static final float SHURIKEN_DAMAGE = 10;
	private static final int FLYING_SPEED = 20;

	public Shuriken(GameScreen game, float x, float y, boolean flipX) {
		super(game);
		getPosition().x = x;
		getPosition().y = y;
		getFlipX().set(flipX);
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
								.damageTest(getHitbox(BODY), SHURIKEN_DAMAGE)) {
							dispose();
						}
					}
				}));
	}

	@Override
	protected void defineAnimations(Animations<ShurikenStates, ShurikenParts> animations) {
		HashMap<ShurikenParts, String> filenames = new HashMap<>();
		filenames.put(BODY, "Body");

		Animation<ShurikenParts> flying =
				new Animation<>(0, 1, "Assassin/Shuriken", filenames);

		animations.map(FLYING, flying);
	}
}
