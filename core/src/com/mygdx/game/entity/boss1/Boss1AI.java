package com.mygdx.game.entity.boss1;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.character.Character;

import static com.mygdx.game.entity.boss1.Boss1Input.EARTHQUAKE_KEYDOWN;
import static com.mygdx.game.entity.boss1.Boss1Input.LEFT_KEYDOWN;
import static com.mygdx.game.entity.boss1.Boss1Input.LEFT_KEYUP;
import static com.mygdx.game.entity.boss1.Boss1Input.RIGHT_KEYDOWN;
import static com.mygdx.game.entity.boss1.Boss1Input.RIGHT_KEYUP;
import static com.mygdx.game.entity.boss1.Boss1Input.ROLL_KEYDOWN;
import static com.mygdx.game.entity.boss1.Boss1Input.SLAM_KEYDOWN;

public class Boss1AI {
	private GameScreen game;
	private Boss1 boss1;
	private Timer timer;

	private static final float WALKING_DURATION = 0.5f;
	private static final float PRIMARY_DURATION = 2f;
	private static final float SECONDARY_DURATION = 2f;
	private static final float TERTIARY_DURATION = 2f;

	public Boss1AI(GameScreen game) {
		this.game = game;
		this.timer = new Timer();
		this.boss1 = game.getBoss1();
		timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				Boss1AI.this.run();
			}
		}, 2);
	}

	private void run() {
		Character character = game.getCharacter();
		if (character.isDispose() || boss1.isDispose()) {
			return;
		}

		float displacement = boss1.getPosition().x + 80 - character.getPosition().x;
		float distance = Math.abs(displacement);
		float duration = 0;

		boss1.input(LEFT_KEYUP);
		boss1.input(RIGHT_KEYUP);
		if (distance > 80) {
			switch (MathUtils.random(0, 2)) {
				case 0:
					if (displacement > 0) {
						boss1.input(LEFT_KEYDOWN);
					} else {
						boss1.input(RIGHT_KEYDOWN);
					}
					duration = WALKING_DURATION;
					break;
				case 1:
					boss1.input(EARTHQUAKE_KEYDOWN);
					duration = SECONDARY_DURATION;
					break;
				case 2:
					if (displacement > 0) {
						boss1.input(LEFT_KEYDOWN);
						boss1.input(LEFT_KEYUP);
					} else {
						boss1.input(RIGHT_KEYDOWN);
						boss1.input(RIGHT_KEYUP);
					}
					boss1.input(ROLL_KEYDOWN);
					duration = TERTIARY_DURATION;
					break;
			}
		} else {
			switch (MathUtils.random(0, 2)) {
				case 0:
					boss1.input(SLAM_KEYDOWN);
					duration = PRIMARY_DURATION;
					break;
				case 1:
					boss1.input(EARTHQUAKE_KEYDOWN);
					duration = SECONDARY_DURATION;
					break;
				case 2:
					if (displacement > 0) {
						boss1.input(LEFT_KEYDOWN);
						boss1.input(LEFT_KEYUP);
					} else {
						boss1.input(RIGHT_KEYDOWN);
						boss1.input(RIGHT_KEYUP);
					}
					boss1.input(ROLL_KEYDOWN);
					duration = TERTIARY_DURATION;
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
