package com.mygdx.game.entity.boss1;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.Boss1AnimationName;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.LivingEntity;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.character.Character;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.entity.rock.Rock;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;
import com.mygdx.game.screens.GameScreen;

import static com.mygdx.game.UntitledGame.FLOOR_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_WIDTH;
import static com.mygdx.game.entity.EntityManager.BOSS_RENDER_PRIORITY;
import static com.mygdx.game.entity.boss1.Boss1Input.CROWD_CONTROL;
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
import static com.mygdx.game.entity.boss1.Boss1Parts.BODY;
import static com.mygdx.game.entity.boss1.Boss1Parts.LEFT_ARM;
import static com.mygdx.game.entity.boss1.Boss1Parts.LEFT_LEG;
import static com.mygdx.game.entity.boss1.Boss1Parts.RIGHT_ARM;
import static com.mygdx.game.entity.boss1.Boss1Parts.RIGHT_LEG;
import static com.mygdx.game.entity.boss1.Boss1Parts.SHOCKWAVE;
import static com.mygdx.game.entity.boss1.Boss1States.EARTHQUAKE;
import static com.mygdx.game.entity.boss1.Boss1States.ROLL;
import static com.mygdx.game.entity.boss1.Boss1States.SLAM;
import static com.mygdx.game.entity.boss1.Boss1States.STANDING;
import static com.mygdx.game.entity.boss1.Boss1States.WALKING_LEFT;
import static com.mygdx.game.entity.boss1.Boss1States.WALKING_RIGHT;

/*
Responsibilities: Defines abilities, maps Ability states to Ability instances, handles
sprite position, direction, motion.
 */
public class Boss1 extends LivingEntity<Boss1Input, Boss1States, Boss1Parts> {
	private static final float HEALTH = 2000;
	private static final float DAMAGED_DURATION = 0f;
	private static final float WALKING_SPEED = 1f;

	// Ability animation duration
	private static final float STANDING_ANIMATION_DURATION = 2f;
	private static final float SLAM_ANIMATION_DURATION = 1f;
	private static final float EARTHQUAKE_ANIMATION_DURATION = 1f;
	private static final float ROLL_ANIMATION_DURATION = 1f;

	// Ability cooldowns
	private static final float SLAM_COOLDOWN = 1f;
	private static final float EARTHQUAKE_COOLDOWN = 1f;
	private static final float ROLL_COOLDOWN = 1f;

	// Ability modifiers
	private static final float SLAM_DAMAGE = 20;
	private static final float EARTHQUAKE_DAMAGE = 20;
	private static final int EARTHQUAKE_ROCK_COUNT = 4;
	private static final float EARTHQUAKE_ROCK_DAMAGE = 20;
	private static final float ROLL_SPEED = 4f;
	private static final float ROLL_DAMAGE = 20;

	// Scores
	private static final int DAMAGE_SCORE_MULTIPLIER = 1;
	private static final int TRUE_DAMAGE_SCORE_MULTIPLIER = 4;

	private boolean rolling;

	public Boss1(GameScreen game) {
		super(game, BOSS_RENDER_PRIORITY);
		getPosition().x = WINDOW_WIDTH - getHitbox(BODY).getTextureWidth();
		getPosition().y = FLOOR_HEIGHT;
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
						.addEdge(LEFT_KEYUP, STANDING)
						.addEdge(CROWD_CONTROL, STANDING))

				.add(new State<Boss1Input, Boss1States>(WALKING_RIGHT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(() -> {
							getPosition().x += WALKING_SPEED;
							checkWithinMap();
						})
						.addEdge(RIGHT_KEYUP, STANDING)
						.addEdge(CROWD_CONTROL, STANDING))

				.add(new State<Boss1Input, Boss1States>(SLAM)
						.addEdge(SLAM_KEYUP, STANDING)
						.addEdge(CROWD_CONTROL, STANDING))

				.add(new State<Boss1Input, Boss1States>(EARTHQUAKE)
						.addEdge(EARTHQUAKE_KEYUP, STANDING)
						.addEdge(CROWD_CONTROL, STANDING))

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
								character.damageTest(this, getHitbox(BODY), ROLL_DAMAGE);
							}
						})
						.addEdge(ROLL_KEYUP, STANDING)
						.addEdge(CROWD_CONTROL, STANDING));
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
	protected void beginCrowdControl() {
		input(CROWD_CONTROL);
	}

	@Override
	public void endCrowdControl() {

	}

	@Override
	public float getMiddleX() {
		return getPosition().x + getHitbox(BODY).getOffsetX() + getHitbox(BODY).getWidth() / 2;
	}

	@Override
	protected float getTopY() {
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
					Character character = getGame().getCharacter();
					if (character.damageTest(this, getHitbox(RIGHT_ARM), SLAM_DAMAGE) ||
							character.damageTest(this, getHitbox(LEFT_ARM), SLAM_DAMAGE)) {
						character.inflictDebuff(new Debuff(DebuffType.STUN, 0, 2f));
					}
				})
				.defineEnd(() -> input(SLAM_KEYUP));

		Animation<Boss1Parts> earthquake = assets.getBoss1Animation(Boss1AnimationName.EARTHQUAKE)
				.setDuration(EARTHQUAKE_ANIMATION_DURATION)
				.defineFrameTask(1, () -> {
					Character character = getGame().getCharacter();
					character.damageTest(this, getHitbox(SHOCKWAVE), EARTHQUAKE_DAMAGE);
					for (int i = 0; i < EARTHQUAKE_ROCK_COUNT; i++) {
						new Rock(getGame(), EARTHQUAKE_ROCK_DAMAGE);
					}
				})
				.defineEnd(() -> input(EARTHQUAKE_KEYUP));

		Animation<Boss1Parts> roll = assets.getBoss1Animation(Boss1AnimationName.ROLL)
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

		if (getPosition().x > WINDOW_WIDTH - x - width) {
			getPosition().x = WINDOW_WIDTH - x - width;
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

			inflictTrueDamage(damage);
			return true;
		}
		return false;
	}
}
