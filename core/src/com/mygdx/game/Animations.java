package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.state.State;
import com.mygdx.game.state.States;

import java.util.HashMap;

/**
 * Eventually change from sprites to animations.
 */
public class Animations<T> {
	private HashMap<State<T>, Sprite> sprites;
	private HashMap<State<T>, Integer> priorities;

	public Animations() {
		sprites = new HashMap<State<T>, Sprite>();
		priorities = new HashMap<State<T>, Integer>();
	}

	// TODO: Priorities are just a temporary fix.
	// Ideally, the animation should be able to display a character walking & doing something else at the same time.
	public Animations<T> add(State<T> state, Texture texture, Integer priority) {
		sprites.put(state, new Sprite(texture));
		priorities.put(state, priority);
		return this;
	}

	public Sprite from(States<T> states) {
		Sprite sprite = null;
		Integer priority = 0;
		for (State state : states.toArray()) {
			Sprite s = sprites.get(state);
			Integer p = priorities.get(state);
			if (s != null && p > priority) {
				sprite = s;
				priority = p;
			}
		}
		return sprite;
	}
}
