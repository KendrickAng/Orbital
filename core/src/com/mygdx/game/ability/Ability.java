package com.mygdx.game.ability;

public class Ability {
	// Set the state of the entity for this duration
	private float duration;

	// Ability will be off cooldown in this time
	private float cooldown;

	// Function to check if ability is ready to go off cooldown
	private AbilityReady ready;

	public Ability(float duration, float cooldown) {
		this.duration = duration;
		this.cooldown = cooldown;
		this.ready = new AbilityReady() {
			@Override
			public boolean check(boolean isOnCooldown) {
				return !isOnCooldown;
			}
		};
	}

	public Ability setReady(AbilityReady ready) {
		this.ready = ready;
		return this;
	}

	public boolean isReady(boolean isOnCooldown) {
		return ready.check(isOnCooldown);
	}

	public float getDuration() {
		return duration;
	}

	public float getCooldown() {
		return cooldown;
	}
}
