package com.untitled.android.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.untitled.UntitledGame;
import com.untitled.android.AndroidControls;
import com.untitled.android.ui.AndroidCooldowns;
import com.untitled.assets.Assets;
import com.untitled.assets.TextureName;
import com.untitled.game.ability.CooldownState;
import com.untitled.game.character.Assassin;
import com.untitled.game.character.Tank;
import com.untitled.screens.GameScreen;
import com.untitled.ui.UIAlign;
import com.untitled.ui.cooldowns.Cooldowns;

import static com.badlogic.gdx.Input.Keys.E;
import static com.badlogic.gdx.Input.Keys.Q;
import static com.badlogic.gdx.Input.Keys.R;
import static com.badlogic.gdx.Input.Keys.W;

/**
 * Main game screen (Play) of Untitled
 */
public class AndroidGameScreen extends GameScreen {
	private static final float CHARACTER_COOLDOWNS_X = GAME_WIDTH - 80f;
	private static final float CHARACTER_COOLDOWNS_Y = GAME_HEIGHT - 70f;
	private static final float CHARACTER_COOLDOWNS_PADDING = 20f;

	private AndroidControls controls;

	public AndroidGameScreen(UntitledGame game) {
		super(game);

		this.controls = new AndroidControls(getAssets(), getViewportUI(), getCharacterController());
		initGame();
	}

	@Override
	protected void initGame() {
		getInputMultiplexer().addProcessor(this.controls);
	}

	@Override
	public void renderAbstract(SpriteBatch batch) {
		controls.render(batch);
	}

	@Override
	protected Cooldowns initTankHelpCooldowns(Assets A, Tank tank, CooldownState switchCharacter) {
		return new AndroidCooldowns(UIAlign.TOP_LEFT, CHARACTER_COOLDOWNS_PADDING, A)
				.setX(CHARACTER_COOLDOWNS_X)
				.setY(CHARACTER_COOLDOWNS_Y)
				.add(tank.getBlockState(), A.getTexture(TextureName.ANDROID_COOLDOWN_BLOCK),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(Q),
						() -> getCharacterController().keyUp(Q))
				.add(tank.getImpaleState(), A.getTexture(TextureName.ANDROID_COOLDOWN_HAMMER_SWING),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(W),
						() -> getCharacterController().keyUp(W))
				.add(tank.getFortressState(), A.getTexture(TextureName.ANDROID_COOLDOWN_FORTRESS),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(E),
						() -> getCharacterController().keyUp(E))
				.add(switchCharacter, A.getTexture(TextureName.ANDROID_COOLDOWN_SWITCH_CHARACTER),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(R),
						() -> getCharacterController().keyUp(R));
	}

	@Override
	protected Cooldowns initAssassinHelpCooldowns(Assets A, Assassin assassin, CooldownState switchCharacter) {
		return new AndroidCooldowns(UIAlign.TOP_LEFT, CHARACTER_COOLDOWNS_PADDING, A)
				.setX(CHARACTER_COOLDOWNS_X)
				.setY(CHARACTER_COOLDOWNS_Y)
				.add(assassin.getDashState(), A.getTexture(TextureName.ANDROID_COOLDOWN_DASH),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(Q),
						() -> getCharacterController().keyUp(Q))
				.add(assassin.getShurikenState(), A.getTexture(TextureName.ANDROID_COOLDOWN_SHURIKEN_THROW),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(W),
						() -> getCharacterController().keyUp(W))
				.add(assassin.getCleanseState(), A.getTexture(TextureName.ANDROID_COOLDOWN_CLEANSE),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(E),
						() -> getCharacterController().keyUp(E))
				.add(switchCharacter, A.getTexture(TextureName.ANDROID_COOLDOWN_SWITCH_CHARACTER),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(R),
						() -> getCharacterController().keyUp(R));
	}

	@Override
	protected Cooldowns initTankCooldowns(Assets A, Tank tank, CooldownState switchCharacter) {
		return initTankHelpCooldowns(A, tank, switchCharacter);
	}

	@Override
	protected Cooldowns initAssassinCooldowns(Assets A, Assassin assassin, CooldownState switchCharacter) {
		return initAssassinHelpCooldowns(A, assassin, switchCharacter);
	}
}
