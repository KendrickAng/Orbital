package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all color textures used in the UI. Acts as a library of sorts.
 */
public class ColorTextures {
    // ColorEntry resources: https://material.io/design/color/the-color-system.html#tools-for-picking-colors
    public enum ColorEntry {
        DEFAULT("#FAFAFA"),
        PURPLE_300("#BA68C8"),
        DEEP_PURPLE_500("#7E57C2");

        private final String colorCode;

        ColorEntry(String colorCode) {
            this.colorCode = colorCode;
        }

        public String getColorCode() { return this.colorCode; }
    }

    private HashMap<ColorEntry, Texture> colorTextureMap;

    public ColorTextures() {
        this.colorTextureMap = new HashMap<>();
        // populate hashmap with textures of size 1 by 1
        for (ColorEntry c: ColorEntry.values()) {
            Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            p.setColor(Color.valueOf(c.getColorCode()));
            p.drawPixel(0, 0);
            Texture t = new Texture(p);
            colorTextureMap.put(c, t);
        }
    }

    public Texture get(ColorEntry colorEntry) {
        Texture t = this.colorTextureMap.get(colorEntry);
        if (t == null) {
            throw new NullPointerException("No such colorEntry mapped!");
        } else {
            return t;
        }
    }

    public void dispose() {
        for (Map.Entry<ColorEntry, Texture> entry: colorTextureMap.entrySet()) {
            entry.getValue().dispose();
        }
    }
}
