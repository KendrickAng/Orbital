package com.mygdx.game.entity.rock;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.entity.Entity;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;
import com.mygdx.game.screens.GameScreen;

import static com.mygdx.game.UntitledGame.GAME_WIDTH;
import static com.mygdx.game.UntitledGame.MAP_HEIGHT;
import static com.mygdx.game.entity.EntityManager.ROCK_RENDER_PRIORITY;
import static com.mygdx.game.entity.rock.RockParts.BODY;
import static com.mygdx.game.entity.rock.RockStates.ERUPT;

public class Rock extends Entity<Enum, RockStates, RockParts> {
	private static final float ERUPT_ANIMATION_DURATION = 1.5f;
	private static final float DISTANCE = 200;
	private float damage;

	public Rock(GameScreen game, float damage) {
		super(game, ROCK_RENDER_PRIORITY);
		// Try to spawn near to the character.
		float characterX = game.getCharacter().getMiddleX();
		float minX = Math.max(0, characterX - DISTANCE);
		float maxX = Math.min(GAME_WIDTH - getHitbox(BODY).getWidth(), characterX + DISTANCE);
		getPosition().x = MathUtils.random(minX, maxX);
		getPosition().y = MAP_HEIGHT;
		this.damage = damage;
	}

	@Override
	protected void defineStates(States<Enum, RockStates> states) {
		states.add(new State<>(ERUPT));
	}

	@Override
	protected void defineAnimations(Animations<RockStates, RockParts> animations, Assets assets) {
		animations.map(ERUPT, assets.getRockAnimation(Assets.RockAnimationName.ERUPT)
				.setDuration(ERUPT_ANIMATION_DURATION)
				.defineFrameTask(1, () -> getGame().getCharacter()
						.damageTest(null, getHitbox(BODY), damage))
				.defineEnd(this::dispose));
	}

	@Override
	protected boolean canInput(Enum input) {
		return false;
	}
}
