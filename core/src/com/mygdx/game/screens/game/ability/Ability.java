package com.mygdx.game.screens.game.ability;

import com.badlogic.gdx.utils.Timer;

public class Ability<S extends Enum> {

	// Ability will be off cooldown after this time
	private float cooldown;
	private CooldownState cooldownState;

	private boolean using;
	private boolean ready;

	private Timer timer;

	// Called once when ability begins
	private AbilityBegin<S> abilityBegin;

	// Called once when ability ends
	private AbilityEnd abilityEnd;

	public Ability(float cooldown) {
		this.cooldown = cooldown;
		this.cooldownState = new CooldownState(cooldown);

		this.ready = true;
		this.timer = new Timer();
	}

	/* Calls */
	void begin(S state) {
		// Ensure that abilityBegin is called only once.
		if (!using) {
			using = true;
			cooldownState.begin();

			if (abilityBegin != null) {
				abilityBegin.begin(state);
			}
		}
	}

	void end() {
		// Ensure that abilityEnd is called only once.
		if (using) {
			using = false;
			ready = false;

			// Abilities with negative cooldown will be infinitely long.
			if (cooldown >= 0) {
				if (cooldown == 0) {
					ready = true;
				} else {
					timer.scheduleTask(new Timer.Task() {
						@Override
						public void run() {
							ready = true;
						}
					}, cooldown);
				}
				cooldownState.run();
			}

			if (abilityEnd != null) {
				abilityEnd.end();
			}
		}
	}

	// Reset cooldown of the ability
	public void reset() {
		timer.clear();
		cooldownState.end();
		ready = true;
	}

	/* Setters */
	public Ability<S> defineBegin(AbilityBegin<S> abilityBegin) {
		this.abilityBegin = abilityBegin;
		return this;
	}

	public Ability<S> defineEnd(AbilityEnd abilityEnd) {
		this.abilityEnd = abilityEnd;
		return this;
	}

	/* Getters */
	boolean isReady() {
		return ready;
	}

	public CooldownState getCooldownState() {
		return cooldownState;
	}
}
