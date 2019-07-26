package com.untitled.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.untitled.assets.Assets;
import com.untitled.assets.FontName;
import com.untitled.ui.TextUI;

import java.util.HashSet;

import static com.untitled.ui.UIAlign.MIDDLE;

public class FloatingTextManager {
	private static final float FLOATING_TIME = 1f;
	private static final float FLOATING_SPEED = 5f;

	private Assets A;
	private HashSet<TextUI> floatingText;
	private Timer timer;

	public FloatingTextManager(Assets A) {
		this.A = A;
		this.floatingText = new HashSet<>();
		this.timer = new Timer();
	}

	public void addFloatingText(float x, float y, String text, Color color) {
		TextUI textUI = new TextUI(MIDDLE, A.getFont(FontName.MINECRAFT_8))
				.setX(x)
				.setY(y)
				.setColor(color)
				.setText(text);

		floatingText.add(textUI);
		timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				floatingText.remove(textUI);
			}
		}, FLOATING_TIME);
	}

	public void render(SpriteBatch batch) {
		for (TextUI textUI : floatingText) {
			textUI.render(batch);
			textUI.y += FLOATING_SPEED * Gdx.graphics.getRawDeltaTime();
		}
	}
}
