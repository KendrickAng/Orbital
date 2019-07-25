package com.mygdx.game.screens.game.ability;

import com.badlogic.gdx.utils.Timer;

/**
 * Represents the state of the cooldown indicator UI.
 */
public class CooldownState {
	public static final int COOLDOWN_STATES = 6;

	private float cooldown;
	private int state;
	private Timer timer;

	/**
	 *
	 * @param cooldown the cooldown of the ability to represent.
	 */
	public CooldownState(float cooldown) {
		this.cooldown = cooldown;
		this.state = COOLDOWN_STATES;
		this.timer = new Timer();
	}

	/**
	 * Called when the ability begins.
	 */
	public void begin() {
		this.state = 0;
	}

	/**
	 * Called when the ability ends.
	 */
	public void end() {
		timer.clear();
		this.state = COOLDOWN_STATES;
	}

	/**
	 * Starts the cooldown countdown.
	 */
	public void run() {
		if (state < COOLDOWN_STATES) {
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					state++;
					CooldownState.this.run();
				}
			}, cooldown / COOLDOWN_STATES);
		}
	}

	/**
	 * Gets the state.
	 * @return the actual state of the indicator.
	 */
	public int get() {
		return state;
	}
}
