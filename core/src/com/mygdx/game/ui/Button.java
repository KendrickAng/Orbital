package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.shape.Point;

import static com.mygdx.game.ui.ColorTextures.ColorEntry.DEEP_PURPLE_500;
import static com.mygdx.game.ui.ColorTextures.ColorEntry.DEFAULT;
import static com.mygdx.game.ui.ContentAlignment.*;
import static com.mygdx.game.ui.ColorTextures.ColorEntry;

public class Button extends RectangleUI implements InputProcessor, MultipleUI {
    private MyGdxGame game;
    private Text buttonText;
    private ButtonCallback buttonCallback;

    private boolean hovering;
    private float possibleWidth; // Text width takes precedence if its longer
    private float possibleHeight; // Text height takes precedence if its taller
    private float paddingX;
    private float paddingY;

    private ColorTexture colorTexture;

    public Button(String text, int fontSize, MyGdxGame game) {
        this.buttonText = new Text(text, fontSize, game).setAlignment(ContentAlignment.CENTER);
        this.buttonCallback = () -> { Gdx.app.log("Button.java", "No callback declared for this button"); };
        this.paddingY = 0;
        this.paddingX = 0;
        this.hovering = false;
        this.game = game;
        this.colorTexture = new ColorTexture(game)
                        .setRectangle(super.getRectangle())
                        .setTexture(game.getColorTextures().get(DEFAULT));
    }

    /* CONFIGURATION */

    // takes the larger of text possibleWidth or input possibleWidth
    public Button setMinWidth(float width) {
        this.possibleWidth = width;
        // set rectangle UI to include padding
        super.setWidth(Math.max(buttonText.getWidth() + paddingX * 2, this.possibleWidth));
        return this;
    }

    public Button setMinHeight(float height) {
        this.possibleHeight = height;
        // set rectangle UI to include padding
        super.setHeight(Math.max(buttonText.getHeight() + paddingY * 2, this.possibleHeight));
        return this;
    }

    public Button setHoverColor(ColorEntry color) {
        this.colorTexture.setTexture(game.getColorTextures().get(color));
        return this;
    }

    public Button setPaddingX(float paddingX) {
        this.paddingX = paddingX;
        // re-update possibleWidth if needed
        super.setWidth(Math.max(buttonText.getWidth() + paddingX * 2, this.possibleWidth));
        return this;
    }

    public Button setPaddingY(float paddingY) {
        this.paddingY = paddingY;
        // re-update possibleHeight
        super.setHeight(Math.max(buttonText.getHeight() + paddingY * 2, this.possibleHeight));
        return this;
    }

    public Button setCallback(ButtonCallback buttonCallback) {
        this.buttonCallback = buttonCallback;
        return this;
    }

    @Override
    public Button setX(float x) {
        super.setX(x);
        return this;
    }

    @Override
    public Button setY(float y) {
        super.setY(y);
        return this;
    }

    @Override
    public Button setAlignment(ContentAlignment alignment) {
        super.setAlignment(alignment);
        return this;
    }

    @Override
    public void render() {
        updateUI(super.getX(), super.getY(), super.getWidth(), super.getHeight(), super.getAlignment());
        if (hovering) {
            colorTexture.render();
        }
        buttonText.render();
    }

    /* Mouse input detection and handling */
    public void updateMouse(int screenX, int screenY) {
        Point gameWorldPos = this.game.unproject(screenX, screenY);
        this.hovering = super.getRectangle().hitTest(gameWorldPos);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (hovering) {
            buttonCallback.run();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        updateMouse(screenX, screenY);
        return false;
    }

    public void updateUI(float x, float y, float width, float height, ContentAlignment alignment) {
        switch (alignment) {
            case TOP_RIGHT:
                buttonText.setAlignment(TOP_RIGHT);
                buttonText.setX(x + paddingX);
                buttonText.setY(y + paddingY);
                break;
            case TOP_LEFT:
                buttonText.setAlignment(TOP_LEFT);
                buttonText.setX(x - paddingX);
                buttonText.setY(y + paddingY);
                break;
            case BOTTOM_RIGHT:
                buttonText.setAlignment(BOTTOM_RIGHT);
                buttonText.setX(x + paddingX);
                buttonText.setY(y - paddingY);
                break;
            case BOTTOM_LEFT:
                buttonText.setAlignment(BOTTOM_LEFT);
                buttonText.setX(x - paddingX);
                buttonText.setY(y - paddingY);
                break;
            case CENTER:
                buttonText.setAlignment(CENTER);
                buttonText.setX(x);
                buttonText.setY(y);
                break;
        }
    }

    /* UNUSED */
    @Override
    public boolean scrolled(int amount) { return false; }
    @Override
    public boolean keyDown(int keycode) { return false; }
    @Override
    public boolean keyUp(int keycode) { return false; }
    @Override
    public boolean keyTyped(char character) { return false; }
}
