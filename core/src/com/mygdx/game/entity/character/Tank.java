package com.mygdx.game.entity.character;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.part.TankParts;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;
import com.mygdx.game.screens.GameScreen;

import java.util.Collection;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.character.TankInput.BLOCK_KEYDOWN;
import static com.mygdx.game.entity.character.TankInput.BLOCK_KEYUP;
import static com.mygdx.game.entity.character.TankInput.FORTRESS_KEYDOWN;
import static com.mygdx.game.entity.character.TankInput.FORTRESS_KEYUP;
import static com.mygdx.game.entity.character.TankInput.IMPALE_KEYDOWN;
import static com.mygdx.game.entity.character.TankInput.IMPALE_KEYUP;
import static com.mygdx.game.entity.character.TankInput.LEFT_KEYDOWN;
import static com.mygdx.game.entity.character.TankInput.LEFT_KEYUP;
import static com.mygdx.game.entity.character.TankInput.RIGHT_KEYDOWN;
import static com.mygdx.game.entity.character.TankInput.RIGHT_KEYUP;
import static com.mygdx.game.entity.character.TankInput.SWITCH_CHARACTER;
import static com.mygdx.game.entity.character.TankStates.BLOCK;
import static com.mygdx.game.entity.character.TankStates.BLOCK_LEFT;
import static com.mygdx.game.entity.character.TankStates.BLOCK_LEFT_RIGHT;
import static com.mygdx.game.entity.character.TankStates.BLOCK_RIGHT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_BLOCK;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_BLOCK_LEFT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_BLOCK_LEFT_RIGHT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_BLOCK_RIGHT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_IMPALE;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_IMPALE_LEFT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_IMPALE_LEFT_RIGHT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_IMPALE_RIGHT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_LEFT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_LEFT_RIGHT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_RIGHT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_STANDING;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_STANDING_LEFT_RIGHT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_WALKING_LEFT;
import static com.mygdx.game.entity.character.TankStates.FORTRESS_WALKING_RIGHT;
import static com.mygdx.game.entity.character.TankStates.IMPALE;
import static com.mygdx.game.entity.character.TankStates.IMPALE_LEFT;
import static com.mygdx.game.entity.character.TankStates.IMPALE_LEFT_RIGHT;
import static com.mygdx.game.entity.character.TankStates.IMPALE_RIGHT;
import static com.mygdx.game.entity.character.TankStates.STANDING;
import static com.mygdx.game.entity.character.TankStates.STANDING_LEFT_RIGHT;
import static com.mygdx.game.entity.character.TankStates.WALKING_LEFT;
import static com.mygdx.game.entity.character.TankStates.WALKING_RIGHT;
import static com.mygdx.game.entity.part.TankParts.BODY;
import static com.mygdx.game.entity.part.TankParts.LEFT_ARM;
import static com.mygdx.game.entity.part.TankParts.LEFT_LEG;
import static com.mygdx.game.entity.part.TankParts.RIGHT_ARM;
import static com.mygdx.game.entity.part.TankParts.RIGHT_LEG;
import static com.mygdx.game.entity.part.TankParts.WEAPON;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character<TankInput, TankStates, TankParts> {
	private static final float MOVESPEED = 2f;
	private static final float HEALTH = 100;

	// Skill debuff modifiers from 0f - 1f (0% - 100%)
	private static final float BLOCK_SLOW_MODIFIER = 0.5f;
	private static final float FORTRESS_SLOW_MODIFIER = 0.5f;

	// Skill cooldown in seconds.
	private static final float BLOCK_COOLDOWN = 0f;
	private static final float IMPALE_COOLDOWN = 1f;
	private static final float FORTRESS_DURATION = 10f;
	private static final float FORTRESS_COOLDOWN = FORTRESS_DURATION + 2f;

	// Skill animation duration in seconds.
	private static final float STANDING_ANIMATION_DURATION = 1f;
	private static final float WALKING_ANIMATION_DURATION = 1f;
	private static final float BLOCK_ANIMATION_DURATION = 0.5f;
	private static final float IMPALE_ANIMATION_DURATION = 0.5f;

	private static final float FORTRESS_ANIMATION_DURATION = 1f;
	private static final float FORTRESS_STANDING_ANIMATION_DURATION = 2f;
	private static final float FORTRESS_WALKING_ANIMATION_DURATION = 2f;

	private static final float IMPALE_DAMAGE = 10;

	private final Debuff blockArmorDebuff;

	public Tank(GameScreen game) {
		super(game);
		blockArmorDebuff = new Debuff(DebuffType.DAMAGE_REDUCTION, 1f, 0);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	/* Animations */
	@Override
	protected void defineStates(States<TankInput, TankStates> states) {
		states.add(new State<TankInput, TankStates>(STANDING)
				.addEdge(LEFT_KEYDOWN, WALKING_LEFT)
				.addEdge(RIGHT_KEYDOWN, WALKING_RIGHT)
				.addEdge(BLOCK_KEYDOWN, BLOCK)
				.addEdge(IMPALE_KEYDOWN, IMPALE)
				.addEdge(FORTRESS_KEYDOWN, FORTRESS)
				.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<TankInput, TankStates>(STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, WALKING_LEFT)
						.addEdge(BLOCK_KEYDOWN, BLOCK_LEFT_RIGHT)
						.addEdge(IMPALE_KEYDOWN, IMPALE_LEFT_RIGHT)
						.addEdge(FORTRESS_KEYDOWN, FORTRESS_LEFT_RIGHT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<TankInput, TankStates>(WALKING_LEFT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(this::walkLeft)
						.addEdge(LEFT_KEYUP, STANDING)
						.addEdge(RIGHT_KEYDOWN, STANDING_LEFT_RIGHT)
						.addEdge(BLOCK_KEYDOWN, BLOCK_LEFT)
						.addEdge(IMPALE_KEYDOWN, IMPALE_LEFT)
						.addEdge(FORTRESS_KEYDOWN, FORTRESS_LEFT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<TankInput, TankStates>(WALKING_RIGHT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(this::walkRight)
						.addEdge(RIGHT_KEYUP, STANDING)
						.addEdge(LEFT_KEYDOWN, STANDING_LEFT_RIGHT)
						.addEdge(BLOCK_KEYDOWN, BLOCK_RIGHT)
						.addEdge(IMPALE_KEYDOWN, IMPALE_RIGHT)
						.addEdge(FORTRESS_KEYDOWN, FORTRESS_RIGHT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				/* Block */
				.add(new State<TankInput, TankStates>(BLOCK)
						.addEdge(LEFT_KEYDOWN, BLOCK_LEFT)
						.addEdge(RIGHT_KEYDOWN, BLOCK_RIGHT)
						.addEdge(BLOCK_KEYUP, STANDING))

				.add(new State<TankInput, TankStates>(BLOCK_LEFT)
						.defineUpdate(this::walkLeft)
						.addEdge(LEFT_KEYUP, BLOCK)
						.addEdge(RIGHT_KEYDOWN, BLOCK_LEFT_RIGHT)
						.addEdge(BLOCK_KEYUP, WALKING_LEFT))

				.add(new State<TankInput, TankStates>(BLOCK_RIGHT)
						.defineUpdate(this::walkRight)
						.addEdge(RIGHT_KEYUP, BLOCK)
						.addEdge(LEFT_KEYDOWN, BLOCK_LEFT_RIGHT)
						.addEdge(BLOCK_KEYUP, WALKING_RIGHT))

				.add(new State<TankInput, TankStates>(BLOCK_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, BLOCK_RIGHT)
						.addEdge(RIGHT_KEYUP, BLOCK_LEFT)
						.addEdge(BLOCK_KEYUP, STANDING_LEFT_RIGHT))

				/* Slash */
				.add(new State<TankInput, TankStates>(IMPALE)
						.addEdge(LEFT_KEYDOWN, IMPALE_LEFT)
						.addEdge(RIGHT_KEYDOWN, IMPALE_RIGHT)
						.addEdge(IMPALE_KEYUP, STANDING))

				.add(new State<TankInput, TankStates>(IMPALE_LEFT)
						.addEdge(LEFT_KEYUP, IMPALE)
						.addEdge(RIGHT_KEYDOWN, IMPALE_LEFT_RIGHT)
						.addEdge(IMPALE_KEYUP, WALKING_LEFT))

				.add(new State<TankInput, TankStates>(IMPALE_RIGHT)
						.addEdge(RIGHT_KEYUP, IMPALE)
						.addEdge(LEFT_KEYDOWN, IMPALE_LEFT_RIGHT)
						.addEdge(IMPALE_KEYUP, WALKING_RIGHT))

				.add(new State<TankInput, TankStates>(IMPALE_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, IMPALE_RIGHT)
						.addEdge(RIGHT_KEYUP, IMPALE_LEFT)
						.addEdge(IMPALE_KEYUP, STANDING_LEFT_RIGHT))

				/* Fortress Animation */
				.add(new State<TankInput, TankStates>(FORTRESS)
						.addEdge(FORTRESS_KEYUP, FORTRESS_STANDING)
						.addEdge(LEFT_KEYDOWN, FORTRESS_LEFT)
						.addEdge(RIGHT_KEYDOWN, FORTRESS_RIGHT))

				.add(new State<TankInput, TankStates>(FORTRESS_LEFT_RIGHT)
						.addEdge(FORTRESS_KEYUP, FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, FORTRESS_RIGHT)
						.addEdge(RIGHT_KEYUP, FORTRESS_LEFT))

				.add(new State<TankInput, TankStates>(FORTRESS_LEFT)
						.addEdge(FORTRESS_KEYUP, FORTRESS_WALKING_LEFT)
						.addEdge(LEFT_KEYUP, FORTRESS)
						.addEdge(RIGHT_KEYDOWN, FORTRESS_LEFT_RIGHT))

				.add(new State<TankInput, TankStates>(FORTRESS_RIGHT)
						.addEdge(FORTRESS_KEYUP, FORTRESS_WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, FORTRESS)
						.addEdge(LEFT_KEYDOWN, FORTRESS_LEFT_RIGHT))

				/* Fortress */
				.add(new State<TankInput, TankStates>(FORTRESS_STANDING)
						.addEdge(LEFT_KEYDOWN, FORTRESS_WALKING_LEFT)
						.addEdge(RIGHT_KEYDOWN, FORTRESS_WALKING_RIGHT)
						.addEdge(BLOCK_KEYDOWN, FORTRESS_BLOCK)
						.addEdge(IMPALE_KEYDOWN, FORTRESS_IMPALE)
						.addEdge(FORTRESS_KEYUP, STANDING)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<TankInput, TankStates>(FORTRESS_WALKING_LEFT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(this::walkLeft)
						.addEdge(LEFT_KEYUP, FORTRESS_STANDING)
						.addEdge(RIGHT_KEYDOWN, FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(BLOCK_KEYDOWN, FORTRESS_BLOCK_LEFT)
						.addEdge(IMPALE_KEYDOWN, FORTRESS_IMPALE_LEFT)
						.addEdge(FORTRESS_KEYUP, WALKING_LEFT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<TankInput, TankStates>(FORTRESS_WALKING_RIGHT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(this::walkRight)
						.addEdge(RIGHT_KEYUP, FORTRESS_STANDING)
						.addEdge(LEFT_KEYDOWN, FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(BLOCK_KEYDOWN, FORTRESS_BLOCK_RIGHT)
						.addEdge(IMPALE_KEYDOWN, FORTRESS_IMPALE_RIGHT)
						.addEdge(FORTRESS_KEYUP, WALKING_RIGHT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<TankInput, TankStates>(FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, FORTRESS_WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, FORTRESS_WALKING_LEFT)
						.addEdge(BLOCK_KEYDOWN, FORTRESS_BLOCK_LEFT_RIGHT)
						.addEdge(IMPALE_KEYDOWN, FORTRESS_IMPALE_LEFT_RIGHT)
						.addEdge(FORTRESS_KEYUP, STANDING_LEFT_RIGHT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				/* Fortress Block */
				.add(new State<TankInput, TankStates>(FORTRESS_BLOCK)
						.addEdge(LEFT_KEYDOWN, FORTRESS_BLOCK_LEFT)
						.addEdge(RIGHT_KEYDOWN, FORTRESS_BLOCK_RIGHT)
						.addEdge(BLOCK_KEYUP, FORTRESS_STANDING)
						.addEdge(FORTRESS_KEYUP, BLOCK))

				.add(new State<TankInput, TankStates>(FORTRESS_BLOCK_LEFT)
						.defineUpdate(this::walkLeft)
						.addEdge(LEFT_KEYUP, FORTRESS_BLOCK)
						.addEdge(RIGHT_KEYDOWN, FORTRESS_BLOCK_LEFT_RIGHT)
						.addEdge(BLOCK_KEYUP, FORTRESS_WALKING_LEFT)
						.addEdge(FORTRESS_KEYUP, BLOCK_LEFT))

				.add(new State<TankInput, TankStates>(FORTRESS_BLOCK_RIGHT)
						.defineUpdate(this::walkRight)
						.addEdge(RIGHT_KEYUP, FORTRESS_BLOCK)
						.addEdge(LEFT_KEYDOWN, FORTRESS_BLOCK_LEFT_RIGHT)
						.addEdge(BLOCK_KEYUP, FORTRESS_WALKING_RIGHT)
						.addEdge(FORTRESS_KEYUP, BLOCK_RIGHT))

				.add(new State<TankInput, TankStates>(FORTRESS_BLOCK_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, FORTRESS_BLOCK_RIGHT)
						.addEdge(RIGHT_KEYUP, FORTRESS_BLOCK_LEFT)
						.addEdge(BLOCK_KEYUP, FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(FORTRESS_KEYUP, BLOCK_LEFT_RIGHT))

				/* Fortress Impale */
				.add(new State<TankInput, TankStates>(FORTRESS_IMPALE)
						.addEdge(LEFT_KEYDOWN, FORTRESS_IMPALE_LEFT)
						.addEdge(RIGHT_KEYDOWN, FORTRESS_IMPALE_RIGHT)
						.addEdge(IMPALE_KEYUP, FORTRESS_STANDING)
						.addEdge(FORTRESS_KEYUP, IMPALE))

				.add(new State<TankInput, TankStates>(FORTRESS_IMPALE_LEFT)
						.addEdge(LEFT_KEYUP, FORTRESS_IMPALE)
						.addEdge(RIGHT_KEYDOWN, FORTRESS_IMPALE_LEFT_RIGHT)
						.addEdge(IMPALE_KEYUP, FORTRESS_WALKING_LEFT)
						.addEdge(FORTRESS_KEYUP, IMPALE_LEFT))

				.add(new State<TankInput, TankStates>(FORTRESS_IMPALE_RIGHT)
						.addEdge(RIGHT_KEYUP, FORTRESS_IMPALE)
						.addEdge(LEFT_KEYDOWN, FORTRESS_IMPALE_LEFT_RIGHT)
						.addEdge(IMPALE_KEYUP, FORTRESS_WALKING_RIGHT)
						.addEdge(FORTRESS_KEYUP, IMPALE_RIGHT))

				.add(new State<TankInput, TankStates>(FORTRESS_IMPALE_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, FORTRESS_IMPALE_RIGHT)
						.addEdge(RIGHT_KEYUP, FORTRESS_IMPALE_LEFT)
						.addEdge(IMPALE_KEYUP, FORTRESS_STANDING_LEFT_RIGHT)
						.addEdge(FORTRESS_KEYUP, IMPALE_LEFT_RIGHT));
	}

	@Override
	protected void defineAnimations(Animations<TankStates, TankParts> animations, Assets assets) {
		Animation<TankParts> standing = assets.getTankAnimation(Assets.TankAnimationName.STANDING)
				.setDuration(STANDING_ANIMATION_DURATION)
				.setLoop();

		Animation<TankParts> walking = assets.getTankAnimation(Assets.TankAnimationName.WALKING)
				.setDuration(WALKING_ANIMATION_DURATION)
				.setLoop();

		Animation<TankParts> block = assets.getTankAnimation(Assets.TankAnimationName.BLOCK)
				.setDuration(BLOCK_ANIMATION_DURATION)
				.defineFrameTask(0, () -> inflictDebuff(blockArmorDebuff));

		Animation<TankParts> impale = assets.getTankAnimation(Assets.TankAnimationName.IMPALE)
				.setDuration(IMPALE_ANIMATION_DURATION)
				.defineFrameTask(0, () -> getGame().getBoss1()
						.damageTest(getHitbox(WEAPON), IMPALE_DAMAGE))
				.defineEnd(() -> input(IMPALE_KEYUP));

		Animation<TankParts> fortress = assets.getTankAnimation(Assets.TankAnimationName.FORTRESS)
				.setDuration(FORTRESS_ANIMATION_DURATION)
				.defineEnd(() -> input(FORTRESS_KEYUP));

		Animation<TankParts> fortressStanding = assets.getTankAnimation(Assets.TankAnimationName.FORTRESS_STANDING)
				.setDuration(FORTRESS_STANDING_ANIMATION_DURATION)
				.setLoop();

		Animation<TankParts> fortressWalking = assets.getTankAnimation(Assets.TankAnimationName.FORTRESS_WALKING)
				.setDuration(FORTRESS_WALKING_ANIMATION_DURATION)
				.setLoop();

		Animation<TankParts> fortressBlock = assets.getTankAnimation(Assets.TankAnimationName.FORTRESS_BLOCK)
				.setDuration(BLOCK_ANIMATION_DURATION)
				.defineFrameTask(0, () -> inflictDebuff(blockArmorDebuff));

		Animation<TankParts> fortressImpale = assets.getTankAnimation(Assets.TankAnimationName.FORTRESS_IMPALE)
				.setDuration(IMPALE_ANIMATION_DURATION)
				.defineFrameTask(0, () -> getGame().getBoss1()
						.damageTest(getHitbox(WEAPON), IMPALE_DAMAGE))
				.defineEnd(() -> input(IMPALE_KEYUP));

		animations.map(STANDING, standing)
				.map(STANDING_LEFT_RIGHT, standing)
				.map(WALKING_LEFT, walking)
				.map(WALKING_RIGHT, walking)

				.map(BLOCK, block)
				.map(BLOCK_LEFT_RIGHT, block)
				.map(BLOCK_LEFT, block)
				.map(BLOCK_RIGHT, block)

				.map(IMPALE, impale)
				.map(IMPALE_LEFT, impale)
				.map(IMPALE_RIGHT, impale)
				.map(IMPALE_LEFT_RIGHT, impale)

				.map(FORTRESS, fortress)
				.map(FORTRESS_LEFT, fortress)
				.map(FORTRESS_RIGHT, fortress)
				.map(FORTRESS_LEFT_RIGHT, fortress)

				.map(FORTRESS_STANDING, fortressStanding)
				.map(FORTRESS_STANDING_LEFT_RIGHT, fortressStanding)
				.map(FORTRESS_WALKING_LEFT, fortressWalking)
				.map(FORTRESS_WALKING_RIGHT, fortressWalking)

				.map(FORTRESS_BLOCK, fortressBlock)
				.map(FORTRESS_BLOCK_LEFT_RIGHT, fortressBlock)
				.map(FORTRESS_BLOCK_LEFT, fortressBlock)
				.map(FORTRESS_BLOCK_RIGHT, fortressBlock)

				.map(FORTRESS_IMPALE, fortressImpale)
				.map(FORTRESS_IMPALE_LEFT_RIGHT, fortressImpale)
				.map(FORTRESS_IMPALE_LEFT, fortressImpale)
				.map(FORTRESS_IMPALE_RIGHT, fortressImpale);
	}

	@Override
	protected void defineAbilities(Abilities<TankStates> abilities) {
		Debuff blockSlowDebuff = new Debuff(DebuffType.SLOW, BLOCK_SLOW_MODIFIER, 0);
		Debuff fortressDebuff = new Debuff(DebuffType.SLOW, FORTRESS_SLOW_MODIFIER, FORTRESS_DURATION)
				.defineDebuffEnd(() -> input(FORTRESS_KEYUP));

		Ability<TankStates> block = new Ability<>(BLOCK_COOLDOWN);
		block.defineBegin((state) -> inflictDebuff(blockSlowDebuff))
				.defineEnd(() -> {
					cancelDebuff(blockSlowDebuff);
					cancelDebuff(blockArmorDebuff);
					block.reset();
				});

		Ability<TankStates> impale = new Ability<>(IMPALE_COOLDOWN);

		Ability<TankStates> fortress = new Ability<TankStates>(FORTRESS_COOLDOWN)
				.defineBegin((state) -> inflictDebuff(fortressDebuff));

		abilities.addBegin(BLOCK, block)
				.addBegin(BLOCK_LEFT, block)
				.addBegin(BLOCK_RIGHT, block)
				.addBegin(BLOCK_LEFT_RIGHT, block)
				.addEnd(STANDING, block)
				.addEnd(WALKING_LEFT, block)
				.addEnd(WALKING_RIGHT, block)
				.addEnd(STANDING_LEFT_RIGHT, block)

				.addBegin(FORTRESS_BLOCK, block)
				.addBegin(FORTRESS_BLOCK_LEFT, block)
				.addBegin(FORTRESS_BLOCK_RIGHT, block)
				.addBegin(FORTRESS_BLOCK_LEFT_RIGHT, block)
				.addEnd(FORTRESS_STANDING, block)
				.addEnd(FORTRESS_WALKING_LEFT, block)
				.addEnd(FORTRESS_WALKING_RIGHT, block)
				.addEnd(FORTRESS_STANDING_LEFT_RIGHT, block)

				.addBegin(IMPALE, impale)
				.addBegin(IMPALE_LEFT, impale)
				.addBegin(IMPALE_RIGHT, impale)
				.addBegin(IMPALE_LEFT_RIGHT, impale)
				.addEnd(STANDING, impale)
				.addEnd(WALKING_LEFT, impale)
				.addEnd(WALKING_RIGHT, impale)
				.addEnd(STANDING_LEFT_RIGHT, impale)

				.addBegin(FORTRESS_IMPALE, impale)
				.addBegin(FORTRESS_IMPALE_LEFT, impale)
				.addBegin(FORTRESS_IMPALE_RIGHT, impale)
				.addBegin(FORTRESS_IMPALE_LEFT_RIGHT, impale)
				.addEnd(FORTRESS_STANDING, impale)
				.addEnd(FORTRESS_WALKING_LEFT, impale)
				.addEnd(FORTRESS_WALKING_RIGHT, impale)
				.addEnd(FORTRESS_STANDING_LEFT_RIGHT, impale)

				.addBegin(FORTRESS, fortress)
				.addBegin(FORTRESS_LEFT, fortress)
				.addBegin(FORTRESS_RIGHT, fortress)
				.addBegin(FORTRESS_LEFT_RIGHT, fortress)
				.addEnd(FORTRESS_STANDING, fortress)
				.addEnd(FORTRESS_WALKING_LEFT, fortress)
				.addEnd(FORTRESS_WALKING_RIGHT, fortress)
				.addEnd(FORTRESS_STANDING_LEFT_RIGHT, fortress);
	}

	private void walkLeft() {
		getPosition().x -= MOVESPEED * (1 - getSlow());
		checkWithinMap();
	}

	private void walkRight() {
		getPosition().x += MOVESPEED * (1 - getSlow());
		checkWithinMap();
	}

	private void checkWithinMap() {
		float x = getHitbox(BODY).getOffsetX();
		float width = getHitbox(BODY).getWidth();
		if (getPosition().x < -x) {
			getPosition().x = -x;
		}

		if (getPosition().x > GAME_WIDTH - x - width) {
			getPosition().x = GAME_WIDTH - x - width;
		}
	}

	@Override
	public boolean hitTest(Hitbox hitbox) {
		return !isDispose() &&
				(getHitbox(BODY).hitTest(hitbox) ||
						getHitbox(LEFT_LEG).hitTest(hitbox) ||
						getHitbox(RIGHT_LEG).hitTest(hitbox) ||
						getHitbox(LEFT_ARM).hitTest(hitbox) ||
						getHitbox(RIGHT_ARM).hitTest(hitbox));
	}

	// TODO: Abstract these out
	@Override
	protected void useLeft(boolean keydown) {
		if (keydown) {
			input(LEFT_KEYDOWN);
		} else {
			input(LEFT_KEYUP);
		}
	}

	@Override
	protected void useRight(boolean keydown) {
		if (keydown) {
			input(RIGHT_KEYDOWN);
		} else {
			input(RIGHT_KEYUP);
		}
	}

	@Override
	protected void useUp(boolean keydown) {

	}

	@Override
	protected void usePrimary(boolean keydown) {
		if (keydown) {
			input(BLOCK_KEYDOWN);
		} else {
			input(BLOCK_KEYUP);
		}
	}

	@Override
	protected void useSecondary(boolean keydown) {
		if (keydown) {
			input(IMPALE_KEYDOWN);
		}
	}

	@Override
	protected void useTertiary(boolean keydown) {
		if (keydown) {
			input(FORTRESS_KEYDOWN);
		}
	}

	@Override
	public boolean useSwitchCharacter() {
		return input(SWITCH_CHARACTER);
	}

	@Override
	public void setInput(Collection<CharacterControllerInput> inputs) {
		for (CharacterControllerInput input : inputs) {
			switch (input) {
				case LEFT:
					input(LEFT_KEYDOWN);
					break;
				case RIGHT:
					input(RIGHT_KEYDOWN);
					break;
			}
		}
	}
}
