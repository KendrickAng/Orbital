package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.part.ShurikenParts;
import com.mygdx.game.entity.state.ShurikenStates;
import com.mygdx.game.entity.state.States;

import java.util.Collections;
import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.part.ShurikenParts.BODY;
import static com.mygdx.game.entity.state.ShurikenStates.FLYING;

public class Shuriken extends Entity<ShurikenStates, ShurikenParts> {
	private static final float SHURIKEN_DAMAGE = 10;

	public static final int FLYING_SPEED = 20;

	public Shuriken(GameScreen game) {
		super(game);
	}

	@Override
	protected void defineStates(States<ShurikenStates> states) {
		states.addState(FLYING);
	}

	@Override
	protected void defineAnimations(Animations<ShurikenStates, ShurikenParts> animations) {
		HashMap<String, ShurikenParts> filenames = new HashMap<>();
		filenames.put("Body", BODY);

		Animation<ShurikenParts> flying = new Animation<>(0, false);
		flying.load("Assassin/Shuriken", filenames);

		animations.map(Collections.singleton(FLYING), flying);
	}

	@Override
	protected void update() {
		Boss1 boss = getGame().getBoss();
		if (getHitbox(BODY).hitTest(boss.getHitbox(Boss1Parts.BODY))) {
			Gdx.app.log("Shuriken.java", "Boss was hit!");
			boss.damage(SHURIKEN_DAMAGE);
			dispose();
		}
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
	protected void updateVelocity(Vector2 position, Vector2 velocity) {

	}
}
