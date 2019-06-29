package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.shape.Rectangle;

public class Text extends RectangleUI {
    private MyGdxGame game;
    private String text;
    private BitmapFont bitmapFont;

    public Text(String text, int size) {
        this.generateBitmapFont(size);
        this.generateText(text);
    }

    public Text attach(MyGdxGame game) {
        this.game = game;
        return this;
    }

    private Text generateText(String text) {
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(this.bitmapFont, text);
        super.setWidth(glyphLayout.width); // glyphlayout calculates the text dimensions
        super.setHeight(glyphLayout.height);
        this.text = text;
        return this;
    }

    private Text generateBitmapFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/minecraft.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = size;
        this.bitmapFont = generator.generateFont(parameter);
        generator.dispose();
        return this;
    }

    @Override
    public Text setX(float x) {
        super.setX(x);
        return this;
    }

    @Override
    public Text setY(float y) {
        super.setY(y);
        return this;
    }

    @Override
    public Text setAlignment(ContentAlignment alignment) {
        super.setAlignment(alignment);
        return this;
    }

    @Override
    public void render() {
        Rectangle rectangle = super.getRectangle();
        float xOrigin = rectangle.getX();
        float yOrigin = rectangle.getY() + rectangle.getHeight(); // OpenGL is y-down
        SpriteBatch batch = this.game.getSpriteBatch();
        bitmapFont.draw(batch, text, xOrigin, yOrigin);
    }
}
