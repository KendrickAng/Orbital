package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.boss1.Boss1;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.part.ShurikenParts;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;

import java.util.Collections;
import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.part.ShurikenParts.BODY;
import static com.mygdx.game.entity.ShurikenStates.FLYING;

public class Shuriken extends Entity<Enum, ShurikenStates, ShurikenParts> {
	private static final float SHURIKEN_DAMAGE = 10;
	public static final int FLYING_SPEED = 20;

	public Shuriken(GameScreen game) {
		super(game);
	}

	@Override
	protected void defineStates(States<Enum, ShurikenStates> states) {
		states.add(new State<Enum, ShurikenStates>(FLYING)
				.defineUpdateVelocity(velocity -> {
					if (isFlipX()) {
						velocity.x = FLYING_SPEED;
					} else {
						velocity.x = FLYING_SPEED;
					}
				}));
	}

	@Override
	protected void defineAnimations(Animations<ShurikenStates, ShurikenParts> animations) {
		HashMap<String, ShurikenParts> filenames = new HashMap<>();
		filenames.put("Body", BODY);

		Animation<ShurikenParts> flying = new Animation<>(0, false);
		flying.load("Assassin/Shuriken", filenames);

		animations.map(FLYING, flying);
	}

	@Override
	protected void updatePosition(Vector2 position) {
		if (position.x < 0) {
			dispose();
		}

		if (position.x > GAME_WIDTH - getHitbox(BODY).getWidth()) {
			dispose();
		}
	}

	@Override
	protected void update() {
		Boss1 boss = getGame().getBoss1();
		if (getHitbox(BODY).hitTest(boss.getHitbox(Boss1Parts.BODY))) {
			Gdx.app.log("Shuriken.java", "Boss was hit!");
			boss.damage(SHURIKEN_DAMAGE);
			dispose();
		}
	}
}
