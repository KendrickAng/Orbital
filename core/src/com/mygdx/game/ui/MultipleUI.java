package com.mygdx.game.ui;

// if a component has more than one RectangleUI, when one is updated all others must be updated.
public interface MultipleUI {
	void updateUI(float x, float y, float width, float height, ContentAlignment alignment);
}
