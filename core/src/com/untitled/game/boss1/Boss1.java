package com.untitled.game.boss1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.untitled.UntitledGame;
import com.untitled.assets.Assets;
import com.untitled.assets.Boss1AnimationName;
import com.untitled.game.EntityManager;
import com.untitled.game.Hitbox;
import com.untitled.game.LivingEntity;
import com.untitled.game.ability.Abilities;
import com.untitled.game.ability.Ability;
import com.untitled.game.animation.Animation;
import com.untitled.game.animation.Animations;
import com.untitled.game.character.Character;
import com.untitled.game.debuff.Debuff;
import com.untitled.game.debuff.DebuffType;
import com.untitled.game.debuff.Debuffs;
import com.untitled.game.rock.Rock;
import com.untitled.game.state.State;
import com.untitled.game.state.States;
import com.untitled.screens.GameScreen;

import static com.untitled.game.boss1.Boss1Parts.BODY;
import static com.untitled.game.boss1.Boss1Parts.LEFT_ARM;
import static com.untitled.game.boss1.Boss1Parts.LEFT_LEG;
import static com.untitled.game.boss1.Boss1Parts.RIGHT_ARM;
import static com.untitled.game.boss1.Boss1Parts.RIGHT_LEG;
import static com.untitled.game.boss1.Boss1Parts.SHOCKWAVE;
import static com.untitled.screens.GameScreen.GAME_FLOOR_HEIGHT;

/*
Responsibilities: Defines abilities, maps Ability states to Ability instances, handles
sprite position, direction, motion.
 */
public class Boss1 extends LivingEntity<Boss1Input, Boss1States, Boss1Parts> {
	// Ability animation duration
	private static final float STANDING_ANIMATION_DURATION = 2f;
	public static final float SLAM_ANIMATION_DURATION = 1f;
	public static final float EARTHQUAKE_ANIMATION_DURATION = 1f;
	public static final float ROLL_ANIMATION_DURATION = 1.2f;

	private static final float HEALTH = 3000;
	private static final float DAMAGED_DURATION = 0f;
	private static final float WALKING_SPEED = 1f;

	// Ability cooldowns
	private static final float SLAM_COOLDOWN = 0f;
	private static final float EARTHQUAKE_COOLDOWN = 0f;
	private static final float ROLL_COOLDOWN = 0f;

	// Ability modifiers
	private static final float SLAM_DAMAGE = 20;
	private static final float EARTHQUAKE_DAMAGE = 20;
	private static final float EARTHQUAKE_MIN_DISTANCE = 20f;
	private static final float EARTHQUAKE_MAX_DISTANCE = 50f;
	private static final float EARTHQUAKE_MIN_DELAY = 0.04f;
	private static final float EARTHQUAKE_MAX_DELAY = 0.06f;
	private static final float EARTHQUAKE_ROCK_DAMAGE = 20;
	private static final float ROLL_SPEED = 5.5f;
	private static final float ROLL_DAMAGE = 20;

	// Screen shake
	private static final int SLAM_SHAKE_COUNT = 1;
	private static final float SLAM_SHAKE_OFFSET = 3f;
	private static final float SLAM_SHAKE_INTERVAL = 0.05f;

	private static final int EARTHQUAKE_SHAKE_COUNT = 3;
	private static final float EARTHQUAKE_SHAKE_OFFSET = 1f;
	private static final float EARTHQUAKE_SHAKE_INTERVAL = 0.1f;

	private static final int ROLL_SHAKE_COUNT = 1;
	private static final float ROLL_SHAKE_OFFSET = 1f;
	private static final float ROLL_SHAKE_INTERVAL = 0.05f;

	// Scores
	private static final int DAMAGE_SCORE_MULTIPLIER = 1;
	private static final int TRUE_DAMAGE_SCORE_MULTIPLIER = 4;

	private static final String TRUE_DAMAGE_TEXT = "TRUE DAMAGE";
	private static final Color TRUE_DAMAGE_TEXT_COLOR = Color.valueOf("ef9a9a");

	private boolean rolling;
	private Timer timer;

	public Boss1(GameScreen game) {
		super(game, EntityManager.BOSS_RENDER_PRIORITY);
		this.timer = new Timer();

		getPosition().x = UntitledGame.CAMERA_WIDTH - getHitbox(BODY).getTextureWidth();
		getPosition().y = GAME_FLOOR_HEIGHT;
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	@Override
	protected float damagedDuration() {
		return DAMAGED_DURATION;
	}

	@Override
	protected void defineStates(States<Boss1Input, Boss1States> states) {
		states.add(new State<Boss1Input, Boss1States>(Boss1States.STANDING)
				.addEdge(Boss1Input.LEFT_KEYDOWN, Boss1States.WALKING_LEFT)
				.addEdge(Boss1Input.RIGHT_KEYDOWN, Boss1States.WALKING_RIGHT)
				.addEdge(Boss1Input.SLAM_KEYDOWN, Boss1States.SLAM)
				.addEdge(Boss1Input.EARTHQUAKE_KEYDOWN, Boss1States.EARTHQUAKE)
				.addEdge(Boss1Input.ROLL_KEYDOWN, Boss1States.ROLL))

				.add(new State<Boss1Input, Boss1States>(Boss1States.WALKING_LEFT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(() -> {
							getPosition().x -= WALKING_SPEED * 60 * Gdx.graphics.getRawDeltaTime();
							checkWithinMap();
						})
						.addEdge(Boss1Input.LEFT_KEYUP, Boss1States.STANDING)
						.addEdge(Boss1Input.CROWD_CONTROL, Boss1States.STANDING))

				.add(new State<Boss1Input, Boss1States>(Boss1States.WALKING_RIGHT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(() -> {
							getPosition().x += WALKING_SPEED * 60 * Gdx.graphics.getRawDeltaTime();
							checkWithinMap();
						})
						.addEdge(Boss1Input.RIGHT_KEYUP, Boss1States.STANDING)
						.addEdge(Boss1Input.CROWD_CONTROL, Boss1States.STANDING))

				.add(new State<Boss1Input, Boss1States>(Boss1States.SLAM)
						.addEdge(Boss1Input.SLAM_KEYUP, Boss1States.STANDING)
						.addEdge(Boss1Input.CROWD_CONTROL, Boss1States.STANDING))

				.add(new State<Boss1Input, Boss1States>(Boss1States.EARTHQUAKE)
						.addEdge(Boss1Input.EARTHQUAKE_KEYUP, Boss1States.STANDING)
						.addEdge(Boss1Input.CROWD_CONTROL, Boss1States.STANDING))

				.add(new State<Boss1Input, Boss1States>(Boss1States.ROLL)
						.defineUpdate(() -> {
							if (rolling) {
								if (getFlipX().get()) {
									getPosition().x += ROLL_SPEED * 60 * Gdx.graphics.getRawDeltaTime();
								} else {
									getPosition().x -= ROLL_SPEED * 60 * Gdx.graphics.getRawDeltaTime();
								}
								checkWithinMap();

								Character character = getGame().getCharacter();
								character.damageTest(this, getHitbox(BODY), ROLL_DAMAGE);
							}
						})
						.addEdge(Boss1Input.ROLL_KEYUP, Boss1States.STANDING)
						.addEdge(Boss1Input.CROWD_CONTROL, Boss1States.STANDING));
	}

	@Override
	protected void defineAbilities(Abilities<Boss1States> abilities) {
		Ability<Boss1States> slam = new Ability<>(SLAM_COOLDOWN);
		Ability<Boss1States> earthquake = new Ability<>(EARTHQUAKE_COOLDOWN);
		Ability<Boss1States> roll = new Ability<>(ROLL_COOLDOWN);

		abilities.addBegin(Boss1States.SLAM, slam)
				.addEnd(Boss1States.STANDING, slam)

				.addBegin(Boss1States.EARTHQUAKE, earthquake)
				.addEnd(Boss1States.STANDING, earthquake)

				.addBegin(Boss1States.ROLL, roll)
				.addEnd(Boss1States.STANDING, roll);
	}

	@Override
	protected void defineDebuffs(Debuffs debuffs) {

	}

	@Override
	protected void beginCrowdControl() {
		input(Boss1Input.CROWD_CONTROL);
	}

	@Override
	public void endCrowdControl() {

	}

	@Override
	public float getMiddleX() {
		return getPosition().x + getHitbox(BODY).getOffsetX() + getHitbox(BODY).getWidth() / 2;
	}

	@Override
	public float getTopY() {
		return getPosition().y + getHitbox(BODY).getOffsetY() + getHitbox(BODY).getHeight();
	}

	@Override
	protected void damage() {

	}

	@Override
	protected void debuff(Debuff debuff) {

	}

	/* Animations */
	@Override
	protected void defineAnimations(Animations<Boss1States, Boss1Parts> animations, Assets assets) {
		Animation<Boss1Parts> standing = assets.getBoss1Animation(Boss1AnimationName.STANDING)
				.setDuration(STANDING_ANIMATION_DURATION)
				.setLoop();

		Animation<Boss1Parts> slam = assets.getBoss1Animation(Boss1AnimationName.GROUND_SMASH)
				.setDuration(SLAM_ANIMATION_DURATION)
				.defineFrameTask(1, () -> {
					getGame().screenShake(SLAM_SHAKE_COUNT, SLAM_SHAKE_OFFSET, SLAM_SHAKE_INTERVAL);

					Character character = getGame().getCharacter();
					if (character.damageTest(this, getHitbox(SHOCKWAVE), SLAM_DAMAGE)) {
						character.inflictDebuff(new Debuff(DebuffType.STUN, 0, 2f));
					}
				})
				.defineEnd(() -> input(Boss1Input.SLAM_KEYUP));

		Animation<Boss1Parts> earthquake = assets.getBoss1Animation(Boss1AnimationName.EARTHQUAKE)
				.setDuration(EARTHQUAKE_ANIMATION_DURATION)
				.defineFrameTask(1, () -> {
					getGame().screenShake(EARTHQUAKE_SHAKE_COUNT, EARTHQUAKE_SHAKE_OFFSET, EARTHQUAKE_SHAKE_INTERVAL);

					Character character = getGame().getCharacter();
					character.damageTest(this, getHitbox(SHOCKWAVE), EARTHQUAKE_DAMAGE);

					float delay = 0;
					float leftX = getHitbox(SHOCKWAVE).getX() - 20f;
					float rightX = leftX + getHitbox(SHOCKWAVE).getWidth() + 20f;
					while (true) {
						float distance = MathUtils.random(EARTHQUAKE_MIN_DISTANCE, EARTHQUAKE_MAX_DISTANCE);
						if (leftX < 0 && rightX > UntitledGame.CAMERA_WIDTH) {
							break;
						}

						float finalLeftX = leftX;
						float finalRightX = rightX;
						timer.scheduleTask(new Timer.Task() {
							@Override
							public void run() {
								new Rock(getGame(), finalLeftX, EARTHQUAKE_ROCK_DAMAGE);
								new Rock(getGame(), finalRightX, EARTHQUAKE_ROCK_DAMAGE);
							}
						}, delay);

						leftX -= distance;
						rightX += distance;
						delay += MathUtils.random(EARTHQUAKE_MIN_DELAY, EARTHQUAKE_MAX_DELAY);
					}
				})
				.defineEnd(() -> input(Boss1Input.EARTHQUAKE_KEYUP));

		Animation<Boss1Parts> roll = assets.getBoss1Animation(Boss1AnimationName.ROLL)
				.setDuration(ROLL_ANIMATION_DURATION)
				.defineFrameTask(8, () -> rolling = true)
				.defineFrameTask(13, () -> getGame()
						.screenShake(ROLL_SHAKE_COUNT, ROLL_SHAKE_OFFSET, ROLL_SHAKE_INTERVAL))
				.defineFrameTask(15, () -> rolling = false)
				.defineEnd(() -> input(Boss1Input.ROLL_KEYUP));

		animations.map(Boss1States.STANDING, standing)
				.map(Boss1States.WALKING_LEFT, standing)
				.map(Boss1States.WALKING_RIGHT, standing)
				.map(Boss1States.SLAM, slam)
				.map(Boss1States.EARTHQUAKE, earthquake)
				.map(Boss1States.ROLL, roll);
	}

	@Override
	protected boolean canInput(Boss1Input input) {
		return !isStunned();
	}

	private void checkWithinMap() {
		float x = getHitbox(BODY).getOffsetX();
		float width = getHitbox(BODY).getWidth();
		if (getPosition().x < -x) {
			getPosition().x = -x;
		}

		if (getPosition().x > UntitledGame.CAMERA_WIDTH - x - width) {
			getPosition().x = UntitledGame.CAMERA_WIDTH - x - width;
		}
	}

	private boolean hitTest(LivingEntity entity, Hitbox hitbox) {
		// Not a living entity
		return (entity == null ||
				// Damager is not disposed or cc'ed
				(!entity.isDispose() && !entity.isCrowdControl())) &&
				// Self is not disposed
				!isDispose() &&
				(getHitbox(BODY).hitTest(hitbox) ||
						getHitbox(LEFT_LEG).hitTest(hitbox) ||
						getHitbox(RIGHT_LEG).hitTest(hitbox) ||
						getHitbox(LEFT_ARM).hitTest(hitbox) ||
						getHitbox(RIGHT_ARM).hitTest(hitbox));
	}

	public boolean damageTest(LivingEntity entity, Hitbox hitbox, float damage) {
		if (hitTest(entity, hitbox)) {
			getGame().addScore((int) (damage * DAMAGE_SCORE_MULTIPLIER));

			return inflictDamage(entity, damage);
		}
		return false;
	}

	public boolean trueDamageTest(LivingEntity entity, Hitbox hitbox, float damage) {
		if (hitTest(entity, hitbox)) {
			getGame().addScore((int) (damage * TRUE_DAMAGE_SCORE_MULTIPLIER));

			getGame().getFloatingTextManager()
					.addFloatingText(entity.getMiddleX(), entity.getTopY(), TRUE_DAMAGE_TEXT, TRUE_DAMAGE_TEXT_COLOR);

			inflictTrueDamage(damage);
			return true;
		}
		return false;
	}
}
