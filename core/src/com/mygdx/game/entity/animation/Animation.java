package com.mygdx.game.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.entity.EntityData;
import com.mygdx.game.entity.Hitbox;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.FileHandler;

/**
 * Contains all the animations for a certain state e.g Standing.
 *
 * @param <P> the enum grouping all the parts that need to be animated.
 */
public class Animation<P extends Enum> {
	// Total animation duration
	private float duration;
	private String directory;
	private HashMap<P, String> filenames;

	private int frame;
	private int frames;
	private boolean loaded;
	private boolean loop;

	private Timer timer;
	private HashMap<P, AnimationPart> parts; // Hashmap allows O(1) retrieval, and used for debug
	private TreeMap<P, AnimationPart> animations; // Treemap parts are rendered in order
	private HashMap<Integer, AnimationFrameTask> tasks;

	private AnimationEnd animationEnd;

	public Animation(float duration, int frames, String directory, HashMap<P, String> filenames) {
		this.duration = duration;
		this.frames = frames;
		this.directory = directory;
		this.filenames = filenames;

		this.timer = new Timer();
		this.parts = new HashMap<>();
		this.animations = new TreeMap<>();
		this.tasks = new HashMap<>();
	}

	// Load Animation assets
	public void load(EntityData entityData) {
		if (!loaded) {
			loaded = true;

			// map all parts to an empty animation, populates parts.
			for (P part : filenames.keySet()) {
				AnimationPart animation = new AnimationPart(entityData);
				parts.put(part, animation);
				animations.put(part, animation);
			}

			for (P part : filenames.keySet()) {
				AnimationPart animation = new AnimationPart(entityData);
				parts.put(part, animation);
				animations.put(part, animation);

				for (int i = 0; i < frames; i++) {
					String name = directory + "/" + i + "_" + filenames.get(part) + ".png";
					FileHandle fileHandle = Gdx.files.internal(name);
					if (fileHandle.exists()) {
						animation.put(i, new Pixmap(fileHandle));
					}
				}
			}
		}
	}

	public Animation<P> defineFrameTask(int frame, AnimationFrameTask task) {
		tasks.put(frame, task);
		return this;
	}

	public Animation<P> defineEnd(AnimationEnd animationEnd) {
		this.animationEnd = animationEnd;
		return this;
	}

	public void begin() {
		frame = 0;
		updateFrame();
	}

	private void updateFrame() {
		AnimationFrameTask task = tasks.get(frame);
		if (task != null) {
			task.call();
		}

		if (frame < frames - 1) {
			timer.clear();
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					Animation.this.frame++;
					Animation.this.updateFrame();
				}
			}, duration / frames);
		} else if (loop) {
			timer.clear();
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					Animation.this.frame = 0;
					Animation.this.updateFrame();
				}
			}, duration / frames);
		} else if (animationEnd != null) {
			timer.clear();
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					animationEnd.end();
				}
			}, duration / frames);
		}
	}

	public void render(SpriteBatch batch, EntityData entityData) {
		for (AnimationPart part : animations.values()) {
			Sprite sprite = part.getSprite(frame);

			// Ignore undefined sprites
			if (sprite != null) {
				sprite.setPosition(entityData.getPosition().x, entityData.getPosition().y);
				sprite.setFlip(entityData.getFlipX().get(), false);
				sprite.setColor(entityData.getColor());
				sprite.draw(batch);
			}
		}
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		for (AnimationPart part : animations.values()) {
			Hitbox hitbox = part.getHitbox(frame);

			// Ignore undefined hitboxes
			if (hitbox != null) {
				hitbox.renderDebug(shapeRenderer);
			}
		}
	}

	public Animation<P> loop() {
		this.loop = true;
		return this;
	}

	/* Getters */
	public Hitbox getHitbox(P part) {
		return parts.get(part).getHitbox(frame);
	}
}
