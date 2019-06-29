package com.mygdx.game.entity.ability;

import com.badlogic.gdx.utils.Timer;

public class Ability {
	// Ability will be off cooldown after this time
	private float cooldown;

	private boolean using;
	private boolean ready;

	private Timer timer;

	// Called once when ability begins
	private AbilityBegin abilityBegin;

	// Called once when ability ends
	private AbilityEnd abilityEnd;

	public Ability(float cooldown) {
		this.cooldown = cooldown;
		this.ready = true;
		this.timer = new Timer();
	}

	/* Calls */
	public void begin() {
		// Ensure that abilityBegin is called only once.
		if (!using) {
			using = true;

			if (abilityBegin != null) {
				abilityBegin.begin();
			}
		}
	}

	public void end() {
		// Ensure that abilityEnd is called only once.
		if (using) {
			using = false;
			ready = false;
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					ready = true;
				}
			}, cooldown);

			if (abilityEnd != null) {
				abilityEnd.end();
			}
		}
	}

	// Reset cooldown of the ability
	public void reset() {
		timer.clear();
		ready = true;
	}

	/* Setters */
	public Ability defineBegin(AbilityBegin abilityBegin) {
		this.abilityBegin = abilityBegin;
		return this;
	}

	public Ability defineEnd(AbilityEnd abilityEnd) {
		this.abilityEnd = abilityEnd;
		return this;
	}

	/* Getters */
	public boolean isReady() {
		return ready;
	}
}
