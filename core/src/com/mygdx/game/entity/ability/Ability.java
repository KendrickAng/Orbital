package com.mygdx.game.entity.ability;

import java.util.LinkedList;

public class Ability {
	// Set the state of the entity for this duration
	private float duration;

	// Ability will be off cooldown in this time
	private float cooldown;

	// Function to check if ability can be used
	private AbilityUseCondition useCondition;

	// Function to check if ability is ready to go off cooldown (reset)
	private AbilityResetCondition resetCondition;

	// Called once when ability begins
	private AbilityCallback abilityBegin;

	// Called when ability is active
	private AbilityCallback abilityUsing;

	// Called once when ability ends
	private AbilityCallback abilityEnd;

	// Each task is called once after a set delay
	private LinkedList<AbilityTask> abilityTasks;

	class AbilityTask {
		AbilityCallback callback;
		float delay;

		AbilityTask(AbilityCallback callback, float delay) {
			this.callback = callback;
			this.delay = delay;
		}
	}

	public Ability(float duration, float cooldown) {
		this.duration = duration;
		this.cooldown = cooldown;

		this.useCondition = using -> using == 0;
		this.resetCondition = isOnCooldown -> !isOnCooldown;
		this.abilityBegin = () -> {};
		this.abilityUsing = () -> {};
		this.abilityEnd = () -> {};
		this.abilityTasks = new LinkedList<>();
	}

	/* Calls */
	public void begin() {
		abilityBegin.call();
	}

	public void using() {
		abilityUsing.call();
	}

	public void end() {
		abilityEnd.call();
	}

	/* Setters */
	public Ability setUseCondition(AbilityUseCondition useCondition) {
		this.useCondition = useCondition;
		return this;
	}

	public Ability setResetCondition(AbilityResetCondition resetCondition) {
		this.resetCondition = resetCondition;
		return this;
	}

	public Ability setAbilityBegin(AbilityCallback abilityBegin) {
		this.abilityBegin = abilityBegin;
		return this;
	}

	public Ability setAbilityUsing(AbilityCallback abilityUsing) {
		this.abilityUsing = abilityUsing;
		return this;
	}

	public Ability setAbilityEnd(AbilityCallback abilityEnd) {
		this.abilityEnd = abilityEnd;
		return this;
	}

	public Ability addAbilityTask(AbilityCallback abilityCallback, float delay) {
		abilityTasks.add(new AbilityTask(abilityCallback, delay));
		return this;
	}

	/* Getters */
	public boolean canUse(int using) {
		return useCondition.check(using);
	}

	public boolean canReset(boolean isOnCooldown) {
		return resetCondition.check(isOnCooldown);
	}

	public float getDuration() {
		return duration;
	}

	public float getCooldown() {
		return cooldown;
	}

	public LinkedList<AbilityTask> getAbilityTasks() {
		return abilityTasks;
	}
}
