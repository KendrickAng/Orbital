package com.mygdx.game.entity.boss1;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.Direction;
import com.mygdx.game.entity.character.Character;

public class Boss1AI {
	private GameScreen game;
	private Boss1 boss1;
	private Timer timer;

	private static final float WALKING_DELAY = 0.5f;
	private static final float PRIMARY_DELAY = 1.5f;
	private static final float SECONDARY_DELAY = 1.5f;
	private static final float TERTIARY_DELAY = 2f;

	public Boss1AI(GameScreen game) {
		this.game = game;
		this.boss1 = game.getBoss1();
		this.timer = new Timer();
		run();
	}

	private void run() {
		Character character = game.getCharacter();
		float displacement = boss1.getPosition().x + 80 - character.getPosition().x;
		float distance = Math.abs(displacement);
		float duration = 0;

		boss1.setInputDirection(Direction.NONE);
		if (distance > 80) {
			switch (MathUtils.random(0, 2)) {
				case 0:
					if (displacement > 0) {
						boss1.setInputDirection(Direction.LEFT);
					} else {
						boss1.setInputDirection(Direction.RIGHT);
					}
					duration = WALKING_DELAY;
					break;
				case 1:
					boss1.useSecondary();
					duration = SECONDARY_DELAY;
					break;
				case 2:
					if (displacement > 0) {
						boss1.setSpriteDirection(Direction.RIGHT);
					} else {
						boss1.setSpriteDirection(Direction.LEFT);
					}
					boss1.useTertiary();
					duration = TERTIARY_DELAY;
					break;
			}
		} else {
			switch (MathUtils.random(0, 2)) {
				case 0:
					boss1.usePrimary();
					duration = PRIMARY_DELAY;
					break;
				case 1:
					boss1.useSecondary();
					duration = SECONDARY_DELAY;
					break;
				case 2:
					if (displacement > 0) {
						boss1.setSpriteDirection(Direction.RIGHT);
					} else {
						boss1.setSpriteDirection(Direction.LEFT);
					}
					boss1.useTertiary();
					duration = TERTIARY_DELAY;
					break;
			}
		}

		timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				Boss1AI.this.run();
			}
		}, duration);
	}
}
