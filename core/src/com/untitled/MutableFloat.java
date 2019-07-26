package com.untitled;

/**
 * Float that can be modified with a variable reference.
 */
public class MutableFloat {
	private float value;

	public MutableFloat(float value) {
		this.value = value;
	}

	public void set(float value) {
		this.value = value;
	}

	public float get() {
		return value;
	}
}
