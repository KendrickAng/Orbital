package com.untitled.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A {@link UI} that displays Text.
 */
public class TextUI extends UI {
	private BitmapFont font;
	private String text;
	private Color color;
	private int align;

	/**
	 * @param align where the origin (x, y) of the UI should be relative to the content.
	 * @param font the font which this UI should use.
	 */
	public TextUI(UIAlign align, BitmapFont font) {
		super(align);
		this.text = "";
		this.font = font;
		this.color = Color.WHITE;
		setTextAlign(TextUIAlign.LEFT);
	}

	@Override
	public TextUI setX(float x) {
		super.setX(x);
		return this;
	}

	@Override
	public TextUI setY(float y) {
		super.setY(y);
		return this;
	}

	public TextUI setText(String text) {
		this.text = text;

		GlyphLayout layout = new GlyphLayout(font, text);
		super.setW(layout.width);
		super.setH(layout.height);
		return this;
	}

	public TextUI setColor(Color color) {
		this.color = color;
		return this;
	}

	/**
	 * How the text should be aligned inside the bounding box.
	 *
	 * @param textAlign the TextUIAlign enum
	 * @return this instance
	 */
	public TextUI setTextAlign(TextUIAlign textAlign) {
		switch (textAlign) {
			case LEFT:
				this.align = -1;
				break;
			case RIGHT:
				this.align = 0;
				break;
			case MIDDLE:
				this.align = 1;
				break;
		}
		return this;
	}

	@Override
	public void render(SpriteBatch batch) {
		// Use markup
		String text = "[#" + color.toString() + "]" + this.text + "[]";
		font.draw(batch, text, getX(), getY() + getH(), getW(), align, false);
	}
}
