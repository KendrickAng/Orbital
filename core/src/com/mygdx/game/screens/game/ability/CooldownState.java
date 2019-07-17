package com.mygdx.game.screens.game.ability;

import com.badlogic.gdx.utils.Timer;

public class CooldownState {
	private static final int COOLDOWN_STATES = 6;

	private float cooldown;
	private int state;
	private Timer timer;

	public CooldownState(float cooldown) {
		this.cooldown = cooldown;
		this.state = COOLDOWN_STATES;
		this.timer = new Timer();
	}

	void begin() {
		this.state = 0;
	}

	void end() {
		timer.clear();
		this.state = COOLDOWN_STATES;
	}

	void run() {
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

	public int get() {
		return state;
	}
}
