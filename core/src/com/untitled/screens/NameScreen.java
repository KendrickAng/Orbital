package com.untitled.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.untitled.UntitledGame;
import com.untitled.assets.Assets;
import com.untitled.assets.MusicName;
import com.untitled.name.NameProcessor;
import com.untitled.ui.ButtonUI;
import com.untitled.ui.TextUI;

import static com.untitled.UntitledGame.BUTTON_H;
import static com.untitled.UntitledGame.BUTTON_W;
import static com.untitled.UntitledGame.CAMERA_HEIGHT;
import static com.untitled.UntitledGame.CAMERA_WIDTH;
import static com.untitled.assets.FontName.MINECRAFT_16;
import static com.untitled.assets.FontName.MINECRAFT_8;
import static com.untitled.assets.TextureName.BUTTON_HOVER;
import static com.untitled.assets.TextureName.BUTTON_NORMAL;
import static com.untitled.assets.TextureName.MENU_BACKGROUND;
import static com.untitled.ui.UIAlign.MIDDLE;

public class NameScreen extends UntitledScreen {
	public static final int CHARACTERS = 3;

	private static final String BACKGROUND_TEXT = "_";
	private static final float INPUT_TEXT_Y = CAMERA_HEIGHT / 2f;
	private static final float BACKGROUND_TEXT_Y = INPUT_TEXT_Y - 10f;
	private static final float CHARACTER_W = 30f;

	private static final String YOUR_NAME_TEXT = "WHAT IS YOUR NAME?";
	private static final float YOUR_NAME_X = CAMERA_WIDTH / 2f;
	private static final float YOUR_NAME_Y = INPUT_TEXT_Y + 50f;

	private static final String CONTINUE_TEXT = "CONTINUE";
	private static final float CONTINUE_X = CAMERA_WIDTH / 2f;
	private static final float CONTINUE_Y = 50f;

	private Assets A;
	private OrthographicCamera camera;
	private Array<Character> characterStack;

	private TextUI yourNameText;
	private TextUI[] backgroundTexts;
	private TextUI[] inputTexts;

	private TextUI continueText;
	private ButtonUI continueButton;

	public NameScreen(UntitledGame game, ScreenName next) {
		super(game);
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.A = game.getAssets();
		this.camera = game.getCamera();
		this.characterStack = new Array<>();

		this.yourNameText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(YOUR_NAME_X)
				.setY(YOUR_NAME_Y)
				.setText(YOUR_NAME_TEXT);

		this.backgroundTexts = new TextUI[CHARACTERS];
		this.inputTexts = new TextUI[CHARACTERS];

		for (int i = 0; i < CHARACTERS; i++) {
			float x = CAMERA_WIDTH / 2f + CHARACTER_W * (i - (CHARACTERS - 1) / 2f);
			backgroundTexts[i] = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
					.setX(x)
					.setY(BACKGROUND_TEXT_Y)
					.setText(BACKGROUND_TEXT);

			inputTexts[i] = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
					.setX(x)
					.setY(INPUT_TEXT_Y)
					.setColor(Color.GOLD);
		}

		this.continueText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(CONTINUE_X)
				.setY(CONTINUE_Y)
				.setText(CONTINUE_TEXT);

		this.continueButton = new ButtonUI(MIDDLE, viewport, () -> {
			if (characterStack.size == CHARACTERS) {
				game.setName(characterStack.toString(""));
				setScreen(next);
			}
		})
				.setX(CONTINUE_X)
				.setY(CONTINUE_Y)
				.setW(BUTTON_W)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		multiplexer.addProcessor(new NameProcessor(characterStack));
		multiplexer.addProcessor(continueButton);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.draw(A.getTexture(MENU_BACKGROUND), 0, 0, 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		this.yourNameText.render(batch);

		for (int i = 0; i < CHARACTERS; i++) {
			if (characterStack.size > i) {
				inputTexts[i].setText(String.valueOf(characterStack.get(i)));
			} else {
				inputTexts[i].setText("");
			}

			backgroundTexts[i].render(batch);
			inputTexts[i].render(batch);
		}

		if (characterStack.size == CHARACTERS) {
			this.continueButton.render(batch);
			this.continueText.render(batch);
		}

		batch.end();
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}

	@Override
	public void pauseScreen() {
		A.getMusic(MusicName.MAIN_MENU).pause();
	}

	@Override
	public void resumeScreen() {
		A.getMusic(MusicName.MAIN_MENU).play();
	}
}
