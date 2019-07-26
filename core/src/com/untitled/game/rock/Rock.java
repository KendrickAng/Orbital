package com.untitled.game.rock;

import com.untitled.assets.Assets;
import com.untitled.assets.RockAnimationName;
import com.untitled.game.Entity;
import com.untitled.game.EntityManager;
import com.untitled.game.animation.Animations;
import com.untitled.game.state.State;
import com.untitled.game.state.States;
import com.untitled.screens.GameScreen;

import static com.untitled.game.rock.RockStates.ERUPT;

public class Rock extends Entity<Enum, RockStates, RockParts> {
	private static final float ERUPT_ANIMATION_DURATION = 0.5f;
	private float damage;

	public Rock(GameScreen game, float x, float damage) {
		super(game, EntityManager.ROCK_RENDER_PRIORITY);

		getPosition().x = x - getHitbox(RockParts.BODY).getWidth() / 2;
		getPosition().y = GameScreen.GAME_FLOOR_HEIGHT;
		this.damage = damage;
	}

	@Override
	protected void defineStates(States<Enum, RockStates> states) {
		states.add(new State<>(ERUPT));
	}

	@Override
	protected void defineAnimations(Animations<RockStates, RockParts> animations, Assets assets) {
		animations.map(ERUPT, assets.getRockAnimation(RockAnimationName.ERUPT)
				.setDuration(ERUPT_ANIMATION_DURATION)
				.defineFrameTask(1, () -> getGame().getCharacter()
						.damageTest(null, getHitbox(RockParts.BODY), damage))
				.defineEnd(() -> dispose(0)));
	}

	@Override
	protected boolean canInput(Enum input) {
		return false;
	}
}
