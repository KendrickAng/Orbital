package com.untitled;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import static com.untitled.assets.Assets.ASSASSIN_CLEANSE_PATH;
import static com.untitled.assets.Assets.ASSASSIN_DASH_PATH;
import static com.untitled.assets.Assets.ASSASSIN_PARTS;
import static com.untitled.assets.Assets.ASSASSIN_SHURIKEN_THROW_PATH;
import static com.untitled.assets.Assets.ASSASSIN_STANDING_PATH;
import static com.untitled.assets.Assets.ASSASSIN_WALKING_PATH;
import static com.untitled.assets.Assets.BOSS1_EARTHQUAKE_PATH;
import static com.untitled.assets.Assets.BOSS1_GROUND_SMASH_PATH;
import static com.untitled.assets.Assets.BOSS1_PARTS;
import static com.untitled.assets.Assets.BOSS1_ROLL_PATH;
import static com.untitled.assets.Assets.BOSS1_STANDING_PATH;
import static com.untitled.assets.Assets.ROCK_ERUPT_PATH;
import static com.untitled.assets.Assets.ROCK_PARTS;
import static com.untitled.assets.Assets.SHURIKEN_FLYING_PATH;
import static com.untitled.assets.Assets.SHURIKEN_PARTS;
import static com.untitled.assets.Assets.TANK_BLOCK_PATH;
import static com.untitled.assets.Assets.TANK_FORTRESS_BLOCK_PATH;
import static com.untitled.assets.Assets.TANK_FORTRESS_IMPALE_PATH;
import static com.untitled.assets.Assets.TANK_FORTRESS_PATH;
import static com.untitled.assets.Assets.TANK_FORTRESS_STANDING_PATH;
import static com.untitled.assets.Assets.TANK_FORTRESS_WALKING_PATH;
import static com.untitled.assets.Assets.TANK_HAMMER_SWING_PATH;
import static com.untitled.assets.Assets.TANK_PARTS;
import static com.untitled.assets.Assets.TANK_STANDING_PATH;
import static com.untitled.assets.Assets.TANK_WALKING_PATH;

public class AnimationsBuilder {
	public static void main(String[] args) throws IOException {
		AnimationsBuilder builder = new AnimationsBuilder();
		builder.buildDirectory(TANK_STANDING_PATH, TANK_PARTS.keySet());
		builder.buildDirectory(TANK_WALKING_PATH, TANK_PARTS.keySet());
		builder.buildDirectory(TANK_BLOCK_PATH, TANK_PARTS.keySet());
		builder.buildDirectory(TANK_HAMMER_SWING_PATH, TANK_PARTS.keySet());
		builder.buildDirectory(TANK_FORTRESS_PATH, TANK_PARTS.keySet());
		builder.buildDirectory(TANK_FORTRESS_WALKING_PATH, TANK_PARTS.keySet());
		builder.buildDirectory(TANK_FORTRESS_STANDING_PATH, TANK_PARTS.keySet());
		builder.buildDirectory(TANK_FORTRESS_BLOCK_PATH, TANK_PARTS.keySet());
		builder.buildDirectory(TANK_FORTRESS_IMPALE_PATH, TANK_PARTS.keySet());

		builder.buildDirectory(ASSASSIN_STANDING_PATH, ASSASSIN_PARTS.keySet());
		builder.buildDirectory(ASSASSIN_WALKING_PATH, ASSASSIN_PARTS.keySet());
		builder.buildDirectory(ASSASSIN_DASH_PATH, ASSASSIN_PARTS.keySet());
		builder.buildDirectory(ASSASSIN_SHURIKEN_THROW_PATH, ASSASSIN_PARTS.keySet());
		builder.buildDirectory(ASSASSIN_CLEANSE_PATH, ASSASSIN_PARTS.keySet());

		builder.buildDirectory(BOSS1_STANDING_PATH, BOSS1_PARTS.keySet());
		builder.buildDirectory(BOSS1_GROUND_SMASH_PATH, BOSS1_PARTS.keySet());
		builder.buildDirectory(BOSS1_EARTHQUAKE_PATH, BOSS1_PARTS.keySet());
		builder.buildDirectory(BOSS1_ROLL_PATH, BOSS1_PARTS.keySet());

		builder.buildDirectory(SHURIKEN_FLYING_PATH, SHURIKEN_PARTS.keySet());
		builder.buildDirectory(ROCK_ERUPT_PATH, ROCK_PARTS.keySet());
	}

	private void buildDirectory(String directory, Collection<String> parts) throws IOException {
		ZipFile zipFile = new ZipFile(directory + "/Animation.ora");
		XmlReader.Element rootImageElement = new XmlReader().parse(zipFileInputStream(zipFile, "stack.xml"));
		int width = Integer.parseInt(rootImageElement.getAttribute("w"));
		int height = Integer.parseInt(rootImageElement.getAttribute("h"));

		XmlReader.Element rootStackElement = rootImageElement.getChild(0);
		Array<XmlReader.Element> stacks = rootStackElement.getChildrenByName("stack");
		Array.ArrayIterator<XmlReader.Element> stacksIterator = new Array.ArrayIterator<>(stacks);

		// Hitbox XML writer
		Writer fileWriter = new FileWriter(directory + "/Animation.xml");
		XmlWriter xmlWriter = new XmlWriter(fileWriter)
				.element("animation")
				.attribute("width", width)
				.attribute("height", height);

		for (int frame = 0; frame < stacks.size; frame++) {
			/*
			Each stack is one frame in the animation.
			Each stack has multiple layers (Parts).
			Calculate the hitbox for each layer.
			Draw the layers on top of each other to get a resultant stack image.
			*/
			XmlReader.Element stackElement = stacksIterator.next();
			BufferedImage stack = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics stackGraphics = stack.getGraphics();

			// Reverse layers for drawing layers correctly.
			Array<XmlReader.Element> layers = stackElement.getChildrenByName("layer");
			layers.reverse();

			// Add #frame to each part (GIMP limitation)
			HashMap<String, String> partsFrame = new HashMap<>();
			for (String part : parts) {
				partsFrame.put(part + " #" + frame, part);
			}

			xmlWriter.element("frame");
			for (XmlReader.Element layerElement : new Array.ArrayIterator<>(layers)) {
				String name = layerElement.getAttribute("name");
				String src = layerElement.getAttribute("src");

				BufferedImage layer = ImageIO.read(zipFileInputStream(zipFile, src));
				stackGraphics.drawImage(layer, 0, 0, null);

				if (partsFrame.containsKey(name)) {
					// Hitbox calculation
					byte[] pixels = ((DataBufferByte) layer.getRaster().getDataBuffer()).getData();

					int minX = Integer.MAX_VALUE;
					int maxX = Integer.MIN_VALUE;
					int minY = Integer.MAX_VALUE;
					int maxY = Integer.MIN_VALUE;
					for (int y = 0; y < height; y++) {
						for (int x = 0; x < width; x++) {
							byte alpha = pixels[x * 4 + y * width * 4];
							// Check that pixel is not transparent.
							if (alpha != 0) {
								minX = Math.min(minX, x);
								maxX = Math.max(maxX, x);
								minY = Math.min(minY, y);
								maxY = Math.max(maxY, y);
							}
						}
					}

					xmlWriter.element("hitbox")
							.attribute("name", partsFrame.get(name))
							.attribute("minX", minX)
							.attribute("maxX", maxX)
							.attribute("minY", minY)
							.attribute("maxY", maxY)
							.pop();
				}
			}
			xmlWriter.pop();

			// Write resultant stack image to assets.
			stackGraphics.dispose();
			ImageIO.write(stack, "PNG", new File(directory + "/" + frame + ".png"));
		}

		// Close hitboxes writer.
		xmlWriter.close();
	}

	private InputStream zipFileInputStream(ZipFile zipFile, String in) throws IOException {
		return zipFile.getInputStream(zipFile.getEntry(in));
	}
}
