package com.untitled;

/**
 * Boolean that can be modified with a variable reference.
 */
public class MutableBoolean {
	private boolean value;

	public MutableBoolean(boolean value) {
		this.value = value;
	}

	public void set(boolean value) {
		this.value = value;
	}

	public boolean get() {
		return value;
	}
}
