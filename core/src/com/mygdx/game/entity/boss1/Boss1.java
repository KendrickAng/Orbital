package com.mygdx.game.entity.boss1;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.LivingEntity;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.character.Character;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;

import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.boss1.Boss1Input.EARTHQUAKE_KEYDOWN;
import static com.mygdx.game.entity.boss1.Boss1Input.EARTHQUAKE_KEYUP;
import static com.mygdx.game.entity.boss1.Boss1Input.LEFT_KEYDOWN;
import static com.mygdx.game.entity.boss1.Boss1Input.LEFT_KEYUP;
import static com.mygdx.game.entity.boss1.Boss1Input.RIGHT_KEYDOWN;
import static com.mygdx.game.entity.boss1.Boss1Input.RIGHT_KEYUP;
import static com.mygdx.game.entity.boss1.Boss1Input.ROLL_KEYDOWN;
import static com.mygdx.game.entity.boss1.Boss1Input.ROLL_KEYUP;
import static com.mygdx.game.entity.boss1.Boss1Input.SLAM_KEYDOWN;
import static com.mygdx.game.entity.boss1.Boss1Input.SLAM_KEYUP;
import static com.mygdx.game.entity.boss1.Boss1States.EARTHQUAKE;
import static com.mygdx.game.entity.boss1.Boss1States.ROLL;
import static com.mygdx.game.entity.boss1.Boss1States.SLAM;
import static com.mygdx.game.entity.boss1.Boss1States.STANDING;
import static com.mygdx.game.entity.boss1.Boss1States.WALKING_LEFT;
import static com.mygdx.game.entity.boss1.Boss1States.WALKING_RIGHT;
import static com.mygdx.game.entity.part.Boss1Parts.BODY;
import static com.mygdx.game.entity.part.Boss1Parts.LEFT_ARM;
import static com.mygdx.game.entity.part.Boss1Parts.LEFT_LEG;
import static com.mygdx.game.entity.part.Boss1Parts.RIGHT_ARM;
import static com.mygdx.game.entity.part.Boss1Parts.RIGHT_LEG;
import static com.mygdx.game.entity.part.Boss1Parts.SHOCKWAVE;

/*
Responsibilities: Defines abilities, maps Ability states to Ability instances, handles
sprite position, direction, motion.
 */
public class Boss1 extends LivingEntity<Boss1Input, Boss1States, Boss1Parts> {
	private static final float HEALTH = 1000;
	private static final float WALKING_SPEED = 1f;
	private static final float ROLL_SPEED = 4f;
	private static final float FRICTION = 0.6f;

	private static final float SLAM_DAMAGE = 20;
	private static final float EARTHQUAKE_DAMAGE = 20;
	private static final float ROLL_DAMAGE = 10;

	private static final float SLAM_COOLDOWN = 1f;
	private static final float EARTHQUAKE_COOLDOWN = 1f;
	private static final float ROLL_COOLDOWN = 1f;

	private static final float STANDING_ANIMATION_DURATION = 2f;
	private static final float SLAM_ANIMATION_DURATION = 1f;
	private static final float EARTHQUAKE_ANIMATION_DURATION = 1f;
	private static final float ROLL_ANIMATION_DURATION = 1f;

	private boolean rolling;

	public Boss1(GameScreen game) {
		super(game);
		getPosition().x = GAME_WIDTH - 320;
		getPosition().y = MAP_HEIGHT;
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	@Override
	protected void defineStates(States<Boss1Input, Boss1States> states) {
		states.add(new State<Boss1Input, Boss1States>(STANDING)
				.addEdge(LEFT_KEYDOWN, WALKING_LEFT)
				.addEdge(RIGHT_KEYDOWN, WALKING_RIGHT)
				.addEdge(SLAM_KEYDOWN, SLAM)
				.addEdge(EARTHQUAKE_KEYDOWN, EARTHQUAKE)
				.addEdge(ROLL_KEYDOWN, ROLL))

				.add(new State<Boss1Input, Boss1States>(WALKING_LEFT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(() -> {
							getPosition().x -= WALKING_SPEED;
							checkWithinMap();
						})
						.addEdge(LEFT_KEYUP, STANDING))

				.add(new State<Boss1Input, Boss1States>(WALKING_RIGHT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(() -> {
							getPosition().x += WALKING_SPEED;
							checkWithinMap();
						})
						.addEdge(RIGHT_KEYUP, STANDING))

				.add(new State<Boss1Input, Boss1States>(SLAM)
						.addEdge(SLAM_KEYUP, STANDING))

				.add(new State<Boss1Input, Boss1States>(EARTHQUAKE)
						.addEdge(EARTHQUAKE_KEYUP, STANDING))

				.add(new State<Boss1Input, Boss1States>(ROLL)
						.defineUpdate(() -> {
							if (rolling) {
								if (getFlipX().get()) {
									getPosition().x += ROLL_SPEED;
								} else {
									getPosition().x -= ROLL_SPEED;
								}
								checkWithinMap();

								Character character = getGame().getCharacter();
								character.damageTest(getHitbox(BODY), ROLL_DAMAGE);
							}
						})
						.addEdge(ROLL_KEYUP, STANDING));
	}

	@Override
	protected void defineAbilities(Abilities<Boss1States> abilities) {
		Ability<Boss1States> slam = new Ability<>(SLAM_COOLDOWN);
		Ability<Boss1States> earthquake = new Ability<>(EARTHQUAKE_COOLDOWN);
		Ability<Boss1States> roll = new Ability<Boss1States>(ROLL_COOLDOWN)
				.defineEnd(() -> rolling = false);

		abilities.addBegin(SLAM, slam)
				.addEnd(STANDING, slam)

				.addBegin(EARTHQUAKE, earthquake)
				.addEnd(STANDING, earthquake)

				.addBegin(ROLL, roll)
				.addEnd(STANDING, roll);
	}

	@Override
	protected void defineDebuffs(Debuffs debuffs) {

	}

	@Override
	protected void damage() {

	}

	/* Animations */
	@Override
	protected void defineAnimations(Animations<Boss1States, Boss1Parts> animations, Assets assets) {
		Animation<Boss1Parts> standing = assets.getBoss1Animation(Assets.Boss1AnimationName.STANDING)
				.setDuration(STANDING_ANIMATION_DURATION)
				.setLoop();

		Animation<Boss1Parts> slam = assets.getBoss1Animation(Assets.Boss1AnimationName.GROUND_SMASH)
				.setDuration(SLAM_ANIMATION_DURATION)
				.defineFrameTask(1, () -> {
					Character character = getGame().getCharacter();
					character.damageTest(getHitbox(RIGHT_ARM), SLAM_DAMAGE);
					character.damageTest(getHitbox(LEFT_ARM), SLAM_DAMAGE);
				})
				.defineEnd(() -> input(SLAM_KEYUP));

		Animation<Boss1Parts> earthquake = assets.getBoss1Animation(Assets.Boss1AnimationName.EARTHQUAKE)
				.setDuration(EARTHQUAKE_ANIMATION_DURATION)
				.defineFrameTask(1, () -> {
					Character character = getGame().getCharacter();
					character.damageTest(getHitbox(SHOCKWAVE), EARTHQUAKE_DAMAGE);
				})
				.defineEnd(() -> input(EARTHQUAKE_KEYUP));

		Animation<Boss1Parts> roll = assets.getBoss1Animation(Assets.Boss1AnimationName.ROLL)
				.setDuration(ROLL_ANIMATION_DURATION)
				.defineFrameTask(3, () -> rolling = true)
				.defineEnd(() -> input(ROLL_KEYUP));

		animations.map(STANDING, standing)
				.map(WALKING_LEFT, standing)
				.map(WALKING_RIGHT, standing)
				.map(SLAM, slam)
				.map(EARTHQUAKE, earthquake)
				.map(ROLL, roll);
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

	public boolean damageTest(Hitbox hitbox, float damage) {
		if (!isDispose() &&
				(getHitbox(BODY).hitTest(hitbox) ||
						getHitbox(LEFT_LEG).hitTest(hitbox) ||
						getHitbox(RIGHT_LEG).hitTest(hitbox) ||
						getHitbox(LEFT_ARM).hitTest(hitbox) ||
						getHitbox(RIGHT_ARM).hitTest(hitbox))) {
			inflictDamage(damage);
			return true;
		}
		return false;
	}
}
