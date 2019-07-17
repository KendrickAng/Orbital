package com.mygdx.game.screens.game.animation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.XmlReader;
import com.mygdx.game.screens.game.EntityData;
import com.mygdx.game.screens.game.Hitbox;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Contains all the animations for a certain state e.g Standing.
 *
 * @param <P> the enum grouping all the parts that need to be animated.
 */
public class Animation<P extends Enum> {
	// Total animation duration
	private float duration;

	private int frame;
	private AnimationFrame<P> animationFrame;
	private int frames;
	private boolean loop;

	private Timer timer;
	private HashMap<Integer, AnimationFrame<P>> animationFrames;
	private HashMap<Integer, AnimationFrameTask> tasks;

	private AnimationEnd animationEnd;

	public Animation(FileHandle handle, HashMap<String, P> parts) {
		this.animationFrames = new HashMap<>();
		this.timer = new Timer();
		this.tasks = new HashMap<>();

		// Load Animation assets
		XmlReader.Element rootAnimationElement = new XmlReader().parse(handle.child("Animation.xml"));
		int width = Integer.parseInt(rootAnimationElement.getAttribute("width"));
		int height = Integer.parseInt(rootAnimationElement.getAttribute("height"));

		Array<XmlReader.Element> frameElements = rootAnimationElement.getChildrenByName("frame");
		this.frames = frameElements.size;

		Iterator<XmlReader.Element> frameElementsIterator = new Array.ArrayIterator<>(frameElements);
		for (int i = 0; i < frames; i++) {
			AnimationFrame<P> animationFrame = new AnimationFrame<>(handle.child(i + ".png"));

			XmlReader.Element frameElement = frameElementsIterator.next();
			for (XmlReader.Element hitbox : new Array.ArrayIterator<>(frameElement.getChildrenByName("hitbox"))) {
				String name = hitbox.getAttribute("name");
				int minX = Integer.parseInt(hitbox.getAttribute("minX"));
				int maxX = Integer.parseInt(hitbox.getAttribute("maxX"));
				int minY = Integer.parseInt(hitbox.getAttribute("minY"));
				int maxY = Integer.parseInt(hitbox.getAttribute("maxY"));
				P part = parts.get(name);
				animationFrame.addHitbox(part, minX, maxX, minY, maxY, width, height);
			}

			this.animationFrames.put(i, animationFrame);
		}
	}

	public Animation(Animation<P> animation) {
		this.duration = animation.duration;
		this.frame = animation.frame;
		this.animationFrame = animation.animationFrame;
		this.frames = animation.frames;
		this.loop = animation.loop;

		this.timer = new Timer();
		this.animationFrames = new HashMap<>();
		for (Map.Entry<Integer, AnimationFrame<P>> entry : animation.animationFrames.entrySet()) {
			this.animationFrames.put(entry.getKey(), new AnimationFrame<>(entry.getValue()));
		}
		this.tasks = new HashMap<>(animation.tasks);

		this.animationEnd = animation.animationEnd;
	}

	public Animation<P> defineFrameTask(int frame, AnimationFrameTask task) {
		tasks.put(frame, task);
		return this;
	}

	public Animation<P> defineEnd(AnimationEnd animationEnd) {
		this.animationEnd = animationEnd;
		return this;
	}

	public Animation<P> setDuration(float duration) {
		this.duration = duration;
		return this;
	}

	public Animation<P> setLoop() {
		this.loop = true;
		return this;
	}

	void setEntityData(EntityData entityData) {
		for (AnimationFrame<P> frame : animationFrames.values()) {
			frame.setEntityData(entityData);
		}
	}

	public void begin() {
		frame = 0;
		updateFrame();
	}

	void end() {
		timer.clear();
	}

	private void updateFrame() {
		this.animationFrame = animationFrames.get(frame);
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

	public void render(SpriteBatch batch) {
		animationFrame.render(batch);
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		animationFrame.renderDebug(shapeRenderer);
	}

	/* Getters */
	public Hitbox getHitbox(P part) {
		return animationFrame.getHitbox(part);
	}
}
