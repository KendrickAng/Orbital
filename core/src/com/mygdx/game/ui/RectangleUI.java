package com.mygdx.game.ui;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.shape.Rectangle;

import static com.mygdx.game.MyGdxGame.GAME_HEIGHT;
import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.ui.ContentAlignment.BOTTOM_LEFT;

/*
Handles positioning and alignment.
 */
public abstract class RectangleUI {
    private float x = 0; // bottom-left corner of content
    private float y = 0; // bottom-left corner of content
    private Rectangle rectangle; // bounding box
    private ContentAlignment alignment = BOTTOM_LEFT; // default alignment

    public RectangleUI() {
        this.rectangle = new Rectangle();
        this.rectangle.setX(0);
        this.rectangle.setY(0);
        this.rectangle.setWidth(GAME_WIDTH / 4f);
        this.rectangle.setHeight(GAME_HEIGHT / 4f);
    }

    public RectangleUI setX(float x) {
        this.x = x;
        updateRectangle();
        return this;
    }

    public RectangleUI setY(float y) {
        this.y = y;
        updateRectangle();
        return this;
    }

    public RectangleUI setWidth(float width) {
        this.rectangle.setWidth(width);
        updateRectangle();
        return this;
    }

    public RectangleUI setHeight(float height) {
        this.rectangle.setHeight(height);
        updateRectangle();
        return this;
    }

    public RectangleUI setAlignment(ContentAlignment alignment) {
        this.alignment = alignment;
        updateRectangle();
        return this;
    }

    public void updateRectangle() {
        switch(alignment) {
            case BOTTOM_LEFT:
                rectangle.setX(this.x - this.getWidth());
                rectangle.setY(this.y - this.getHeight());
                break;
            case TOP_LEFT:
                rectangle.setX(this.x - this.getWidth());
                rectangle.setY(this.y);
                break;
            case BOTTOM_RIGHT:
                rectangle.setX(this.x);
                rectangle.setY(this.y - this.getHeight());
                break;
            case TOP_RIGHT:
                rectangle.setX(this.x);
                rectangle.setY(this.y);
                break;
            case CENTER:
                rectangle.setX(this.x - this.getWidth() / 2);
                rectangle.setY(this.y - this.getHeight() / 2);
        }
    }

    public abstract void render();

    /* GETTERS */
    public float getHeight() { return this.rectangle.getHeight(); }
    public float getWidth() { return this.rectangle.getWidth(); }
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public Rectangle getRectangle() { return this.rectangle; }
    public ContentAlignment getAlignment() { return this.alignment; }
}
