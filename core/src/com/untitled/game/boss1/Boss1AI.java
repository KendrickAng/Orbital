package com.untitled.game.boss1;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.untitled.game.character.Character;
import com.untitled.screens.GameScreen;

import static com.untitled.game.boss1.Boss1.EARTHQUAKE_ANIMATION_DURATION;
import static com.untitled.game.boss1.Boss1.ROLL_ANIMATION_DURATION;
import static com.untitled.game.boss1.Boss1.SLAM_ANIMATION_DURATION;
import static com.untitled.game.boss1.Boss1Input.EARTHQUAKE_KEYDOWN;
import static com.untitled.game.boss1.Boss1Input.LEFT_KEYDOWN;
import static com.untitled.game.boss1.Boss1Input.LEFT_KEYUP;
import static com.untitled.game.boss1.Boss1Input.RIGHT_KEYDOWN;
import static com.untitled.game.boss1.Boss1Input.RIGHT_KEYUP;
import static com.untitled.game.boss1.Boss1Input.ROLL_KEYDOWN;
import static com.untitled.game.boss1.Boss1Input.SLAM_KEYDOWN;

public class Boss1AI {
	private static final float MIN_RESTING_DURATION = 2f;
	private static final float MAX_RESTING_DURATION = 3f;
	private static final float ABILITY_BUFFER_DURATION = 0.2f;
	private static final float FACE_CHARACTER_DURATION = 0.5f;
	private static final float WALKING_DURATION = 0.5f;
	private static final float ROLL_DISTANCE = 140f;

	private GameScreen game;
	private Boss1 boss1;
	private State state;
	private Timer timer;

	private enum State {
		RESTING, FACE_CHARACTER, WALKING,
		SLAM, EARTHQUAKE, ROLL,
		SLAM_ONCE
	}

	public Boss1AI(GameScreen game) {
		this.game = game;
		this.timer = new Timer();
		this.state = State.RESTING;
		this.boss1 = game.getBoss1();
		nextState(2f);
	}

	private void nextState(float delay) {
		if (game.getTank().isDispose() && game.getAssassin().isDispose()) {
			return;
		}

		timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				Character character = game.getCharacter();
				float displacement = character.getPosition().x - (boss1.getPosition().x + 80);
				float distance = Math.abs(displacement);
//				Gdx.app.log("Displacement", displacement + "");

				// Check if boss is facing character.
				boolean isFacing = boss1.getFlipX().get() && displacement > 0
						|| !boss1.getFlipX().get() && displacement < 0;

				switch (state) {
					case RESTING:
						faceCharacter(isFacing);
						break;
					case FACE_CHARACTER:
						if (distance > ROLL_DISTANCE) {
							switch (MathUtils.random(0, 3)) {
								case 0:
									walk();
									break;
								case 1:
									faceCharacter(isFacing);
									break;
								case 2:
									earthquake();
									break;
								case 3:
									roll();
									break;
							}
						} else {
							switch (MathUtils.random(0, 2)) {
								case 0:
									slam();
									break;
								case 1:
									earthquake();
									break;
								case 2:
									roll();
									break;
							}
						}
						break;
					case WALKING:
						boss1.input(LEFT_KEYUP);
						boss1.input(RIGHT_KEYUP);
						faceCharacter(isFacing);
						break;
					case SLAM:
						switch (MathUtils.random(0, 2)) {
							case 0:
								slamOnce();
								break;
							case 1:
								earthquake();
								break;
							case 2:
								roll();
								break;
						}
						break;
					case SLAM_ONCE:
						rest();
						break;
					case EARTHQUAKE:
						if (distance > ROLL_DISTANCE) {
							switch (MathUtils.random(0, 2)) {
								case 0:
									rest();
									break;
								case 1:
									earthquake();
									break;
								case 2:
									roll();
									break;
							}
						} else {
							switch (MathUtils.random(0, 1)) {
								case 0:
									rest();
									break;
								case 1:
									slamOnce();
									break;
							}
						}
						break;
					case ROLL:
						if (distance > ROLL_DISTANCE) {
							if (isFacing) {
								roll();
							} else {
								faceCharacter(false);
							}
						} else {
							switch (MathUtils.random(0, 1)) {
								case 0:
									slamOnce();
									break;
								case 1:
									earthquake();
									break;
							}
						}
				}
			}
		}, delay);
	}

	private void faceCharacter(boolean isFacing) {
		state = State.FACE_CHARACTER;
		if (isFacing) {
			nextState(0);
		} else {
			if (boss1.getFlipX().get()) {
				boss1.input(LEFT_KEYDOWN);
				boss1.input(LEFT_KEYUP);
			} else {
				boss1.input(RIGHT_KEYDOWN);
				boss1.input(RIGHT_KEYUP);
			}
			nextState(FACE_CHARACTER_DURATION);
		}
	}

	private void rest() {
		state = State.RESTING;
		nextState(MathUtils.random(MIN_RESTING_DURATION, MAX_RESTING_DURATION));
	}

	private void walk() {
		state = State.WALKING;
		if (boss1.getFlipX().get()) {
			boss1.input(RIGHT_KEYDOWN);
		} else {
			boss1.input(LEFT_KEYDOWN);
		}
		nextState(WALKING_DURATION);
	}

	private void slam() {
		state = State.SLAM;
		boss1.input(SLAM_KEYDOWN);
		nextState(SLAM_ANIMATION_DURATION + ABILITY_BUFFER_DURATION);
	}

	private void slamOnce() {
		state = State.SLAM_ONCE;
		boss1.input(SLAM_KEYDOWN);
		nextState(SLAM_ANIMATION_DURATION + ABILITY_BUFFER_DURATION);
	}

	private void earthquake() {
		state = State.EARTHQUAKE;
		boss1.input(EARTHQUAKE_KEYDOWN);
		nextState(EARTHQUAKE_ANIMATION_DURATION + ABILITY_BUFFER_DURATION);
	}

	private void roll() {
		state = State.ROLL;
		boss1.input(ROLL_KEYDOWN);
		nextState(ROLL_ANIMATION_DURATION + ABILITY_BUFFER_DURATION);
	}
}
