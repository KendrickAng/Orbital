package com.mygdx.game.entity.animation;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Hitbox;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * The animation of one body part in a given state. Once instance of this is responsible for the
 * entire animation cycle of a state (E.g Standing). Check parts to see the order of render.
 */
public class AnimationPart {
	private HashMap<Integer, Sprite> sprites;
	private HashMap<Integer, Hitbox> hitboxes;

	public AnimationPart() {
		sprites = new HashMap<>();
		hitboxes = new HashMap<>();
	}

	// maps the pixmap Sprite and Hitbox to corresponding frame number.
	public void put(int frame, Pixmap pixmap) {
		sprites.put(frame, new Sprite(new Texture(pixmap)));
		hitboxes.put(frame, new Hitbox(pixmap));
		pixmap.dispose();
	}

	public Sprite getSprite(int frame) {
		return sprites.get(frame);
	}

	public Hitbox getHitbox(int frame) {
		return hitboxes.get(frame);
	}

	public Collection<Hitbox> getHitboxes() { return hitboxes.values(); }
}
