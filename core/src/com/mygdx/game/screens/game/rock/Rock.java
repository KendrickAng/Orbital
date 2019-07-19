package com.mygdx.game.screens.game.rock;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.RockAnimationName;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.game.Entity;
import com.mygdx.game.screens.game.animation.Animations;
import com.mygdx.game.screens.game.state.State;
import com.mygdx.game.screens.game.state.States;

import static com.mygdx.game.screens.GameScreen.GAME_FLOOR_HEIGHT;
import static com.mygdx.game.screens.game.EntityManager.ROCK_RENDER_PRIORITY;
import static com.mygdx.game.screens.game.rock.RockParts.BODY;
import static com.mygdx.game.screens.game.rock.RockStates.ERUPT;

public class Rock extends Entity<Enum, RockStates, RockParts> {
	private static final float ERUPT_ANIMATION_DURATION = 0.5f;
	private float damage;

	public Rock(GameScreen game, float x, float damage) {
		super(game, ROCK_RENDER_PRIORITY);

		getPosition().x = x - getHitbox(BODY).getWidth() / 2;
		getPosition().y = GAME_FLOOR_HEIGHT;
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
						.damageTest(null, getHitbox(BODY), damage))
				.defineEnd(() -> dispose(0)));
	}

	@Override
	protected boolean canInput(Enum input) {
		return false;
	}
}
