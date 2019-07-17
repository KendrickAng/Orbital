package com.mygdx.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.screens.game.Background;
import com.mygdx.game.screens.name.NameProcessor;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.TextUI;

import static com.mygdx.game.UntitledGame.BUTTON_HEIGHT;
import static com.mygdx.game.UntitledGame.BUTTON_WIDTH;
import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.assets.FontName.MINECRAFT_16;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.assets.TextureName.BUTTON_NORMAL;
import static com.mygdx.game.ui.UIAlign.MIDDLE;

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

	private Array<Character> characterStack;

	private Background background;
	private TextUI yourNameText;
	private TextUI[] backgroundTexts;
	private TextUI[] inputTexts;

	private TextUI continueText;
	private ButtonUI continueButton;

	public NameScreen(UntitledGame game, ScreenName next) {
		super(game);
		Assets A = game.getAssets();
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.characterStack = new Array<>();
		this.background = new Background(A);

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

		this.continueText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
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
				.setW(BUTTON_WIDTH)
				.setH(BUTTON_HEIGHT)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		multiplexer.addProcessor(new NameProcessor(characterStack));
		multiplexer.addProcessor(continueButton);
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {
		this.background.render(batch);
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
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}
}
