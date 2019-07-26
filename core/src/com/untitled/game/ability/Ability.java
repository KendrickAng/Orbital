package com.untitled.game.ability;

import com.badlogic.gdx.utils.Timer;

/**
 * A character skill with an optional cooldown.
 *
 * @param <S> a State enum.
 */
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

	/**
	 * @param cooldown Enter a negative number for infinite cooldown,
	 *                 0 for no cooldown, and positive for normal cooldown.
	 */
	public Ability(float cooldown) {
		this.cooldown = cooldown;
		this.cooldownState = new CooldownState(cooldown);

		this.ready = true;
		this.timer = new Timer();
	}

	/**
	 * Starts the ability.
	 *
	 * @param state the State enum that begun that ability.
	 */
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

	/**
	 * Ends the ability.
	 */
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
							cooldownState.end();
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

	/**
	 * Resets cooldown of the ability.
	 */
	public void reset() {
		timer.clear();
		cooldownState.end();
		ready = true;
	}

	/* Setters */

	/**
	 * Sets the AbilityBegin consumer that is consumed when begin() is called.
	 *
	 * @param abilityBegin the consumer to be consumed.
	 * @return the current Ability instance.
	 */
	public Ability<S> defineBegin(AbilityBegin<S> abilityBegin) {
		this.abilityBegin = abilityBegin;
		return this;
	}

	/**
	 * Sets the AbilityEnd runnable that is run when end() is called.
	 *
	 * @param abilityEnd the runnable to be run.
	 * @return the current Ability instance.
	 */
	public Ability<S> defineEnd(AbilityEnd abilityEnd) {
		this.abilityEnd = abilityEnd;
		return this;
	}

	/* Getters */

	/**
	 * Checks if the ability is ready to be used.
	 *
	 * @return true if the ability is off cooldown, false otherwise.
	 */
	boolean isReady() {
		return ready;
	}

	public CooldownState getCooldownState() {
		return cooldownState;
	}
}
