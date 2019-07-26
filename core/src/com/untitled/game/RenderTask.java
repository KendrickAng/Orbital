package com.untitled.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A Consumer.
 */
public interface RenderTask {
	/**
	 * @param batch {@link SpriteBatch} to render the task on.
	 */
	void render(SpriteBatch batch);
}
