package com.untitled.desktop.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.untitled.UntitledGame;
import com.untitled.assets.Assets;
import com.untitled.assets.TextureName;
import com.untitled.desktop.ui.DesktopCooldowns;
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
public class DesktopGameScreen extends GameScreen {
	private static final float CHARACTER_COOLDOWNS_X = CHARACTER_TEXT_X;
	private static final float CHARACTER_HELP_COOLDOWNS_Y = CHARACTER_TEXT_Y - 16f;
	private static final float CHARACTER_COOLDOWNS_PADDING = 10f;

	private static final float TANK_COOLDOWNS_Y = CHARACTER_HEALTH_BAR_Y - 16f;
	private static final float ASSASSIN_COOLDOWNS_Y = ASSASSIN_STACK_BAR_Y - 16f;

	public DesktopGameScreen(UntitledGame game) {
		super(game);
	}

	@Override
	protected void initGame() {

	}

	@Override
	protected Cooldowns initTankHelpCooldowns(Assets A, Tank tank, CooldownState switchCharacter) {
		return new DesktopCooldowns(UIAlign.TOP_LEFT, CHARACTER_COOLDOWNS_PADDING, A)
				.setX(CHARACTER_COOLDOWNS_X)
				.setY(CHARACTER_HELP_COOLDOWNS_Y)
				.add(tank.getBlockState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_BLOCK),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(Q),
						() -> getCharacterController().keyUp(Q))
				.add(tank.getImpaleState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_HAMMER_SWING),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(W),
						() -> getCharacterController().keyUp(W))
				.add(tank.getFortressState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_FORTRESS),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(E),
						() -> getCharacterController().keyUp(E))
				.add(switchCharacter, A.getTexture(TextureName.DESKTOP_COOLDOWN_SWITCH_CHARACTER),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(R),
						() -> getCharacterController().keyUp(R));
	}

	@Override
	protected Cooldowns initAssassinHelpCooldowns(Assets A, Assassin assassin, CooldownState switchCharacter) {
		return new DesktopCooldowns(UIAlign.TOP_LEFT, CHARACTER_COOLDOWNS_PADDING, A)
				.setX(CHARACTER_COOLDOWNS_X)
				.setY(CHARACTER_HELP_COOLDOWNS_Y)
				.add(assassin.getDashState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_DASH),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(Q),
						() -> getCharacterController().keyUp(Q))
				.add(assassin.getShurikenState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_SHURIKEN_THROW),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(W),
						() -> getCharacterController().keyUp(W))
				.add(assassin.getCleanseState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_CLEANSE),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(E),
						() -> getCharacterController().keyUp(E))
				.add(switchCharacter, A.getTexture(TextureName.DESKTOP_COOLDOWN_SWITCH_CHARACTER),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(R),
						() -> getCharacterController().keyUp(R));
	}

	@Override
	protected Cooldowns initTankCooldowns(Assets A, Tank tank, CooldownState switchCharacter) {
		return new DesktopCooldowns(UIAlign.TOP_LEFT, CHARACTER_COOLDOWNS_PADDING, A)
				.setX(CHARACTER_COOLDOWNS_X)
				.setY(TANK_COOLDOWNS_Y)
				.add(tank.getBlockState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_BLOCK),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(Q),
						() -> getCharacterController().keyUp(Q))
				.add(tank.getImpaleState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_HAMMER_SWING),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(W),
						() -> getCharacterController().keyUp(W))
				.add(tank.getFortressState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_FORTRESS),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(E),
						() -> getCharacterController().keyUp(E))
				.add(switchCharacter, A.getTexture(TextureName.DESKTOP_COOLDOWN_SWITCH_CHARACTER),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(R),
						() -> getCharacterController().keyUp(R));
	}

	@Override
	protected Cooldowns initAssassinCooldowns(Assets A, Assassin assassin, CooldownState switchCharacter) {
		return new DesktopCooldowns(UIAlign.TOP_LEFT, CHARACTER_COOLDOWNS_PADDING, A)
				.setX(CHARACTER_COOLDOWNS_X)
				.setY(ASSASSIN_COOLDOWNS_Y)
				.add(assassin.getDashState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_DASH),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(Q),
						() -> getCharacterController().keyUp(Q))
				.add(assassin.getShurikenState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_SHURIKEN_THROW),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(W),
						() -> getCharacterController().keyUp(W))
				.add(assassin.getCleanseState(), A.getTexture(TextureName.DESKTOP_COOLDOWN_CLEANSE),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(E),
						() -> getCharacterController().keyUp(E))
				.add(switchCharacter, A.getTexture(TextureName.DESKTOP_COOLDOWN_SWITCH_CHARACTER),
						getInputMultiplexer(),
						getViewportUI(),
						() -> getCharacterController().keyDown(R),
						() -> getCharacterController().keyUp(R));
	}

	@Override
	protected void renderAbstract(SpriteBatch batch) {

	}
}
