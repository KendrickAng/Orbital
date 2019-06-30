package com.mygdx.game.entity.boss1;

import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.LivingEntity;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
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

	private static final float PRIMARY_DAMAGE = 10;
	private static final float SECONDARY_DAMAGE = 20;
	private static final float TERTIARY_DAMAGE = 30;

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

		/*
				.addAbilityTask(() -> {
					Character c = super.getGame().getCharacter();
					if (c instanceof Assassin) {
						if (this.getHitbox(RIGHT_ARM).hitTest(c.getHitbox(AssassinParts.BODY)) ||
								this.getHitbox(LEFT_ARM).hitTest(c.getHitbox(AssassinParts.BODY))) {
							Gdx.app.log("Boss1.java", "Assassin was hit by primary!");
							c.damage(PRIMARY_DAMAGE);
						}
					}
					if (c instanceof Tank) {
						// TODO: Game crashes when slam used for (PRIMARY_ANIMATION_DURATION / 2). Nullpointerexception. Why?
						if (this.getHitbox(RIGHT_ARM).hitTest(c.getHitbox(TankParts.BODY)) ||
								this.getHitbox(LEFT_ARM).hitTest(c.getHitbox(TankParts.BODY))) {
							Gdx.app.log("Boss1.java", "Tank was hit by primary!");
							c.damage(PRIMARY_DAMAGE);
						}
					}

				}, PRIMARY_ANIMATION_DURATION / 2f);

				.addAbilityTask(() -> {
					Character c = super.getGame().getCharacter();
					if (c instanceof Assassin) {
						if (this.getHitbox(RIGHT_LEG).hitTest(c.getHitbox(AssassinParts.BODY)) ||
								this.getHitbox(SHOCKWAVE).hitTest(c.getHitbox(AssassinParts.LEFT_LEG)) ||
								this.getHitbox(SHOCKWAVE).hitTest(c.getHitbox(AssassinParts.RIGHT_LEG))) {
							Gdx.app.log("Boss1.java", "Assassin was hit by secondary!");
							c.damage(PRIMARY_DAMAGE);
						}
					}
					if (c instanceof Tank) {
						if (this.getHitbox(RIGHT_LEG).hitTest(c.getHitbox(TankParts.BODY)) ||
								// TODO: Game crashes on shockwave. Nullpointer exception. Why?
								this.getHitbox(SHOCKWAVE).hitTest(c.getHitbox(TankParts.LEFT_LEG)) ||
								this.getHitbox(SHOCKWAVE).hitTest(c.getHitbox(AssassinParts.RIGHT_LEG))) {
							Gdx.app.log("Boss1.java", "Tank was hit by secondary!");
							c.damage(SECONDARY_DAMAGE);
						}
					}
				}, SECONDARY_ANIMATION_DURATION * (3f / 4));

				.setAbilityUsing(() -> {
					Character c = super.getGame().getCharacter();
					if (c instanceof Assassin) {
						if (!super.getDebuffs().getInflicted().get(ROLLING).isEmpty() &&
								this.getHitbox(BODY).hitTest(c.getHitbox(AssassinParts.BODY))) {
							Gdx.app.log("Boss1.java", "Assassin was hit by tertiary!");
							c.damage(TERTIARY_DAMAGE);
						}
					}
					// TODO: Nullpointerexception again. What's going on?
					if (c instanceof Tank) {
						if (!super.getDebuffs().getInflicted().get(ROLLING).isEmpty() &&
								this.getHitbox(BODY).hitTest(c.getHitbox(TankParts.BODY))) {
							Gdx.app.log("Boss1.java", "Tank was hit by tertiary!");
							c.damage(TERTIARY_DAMAGE);
						}
					}
				})
				.addAbilityTask(() -> {
					inflictDebuff(ROLLING, 0, TERTIARY_ANIMATION_DURATION - 0.3f);
				}, 0.3f);
		*/
	}

	@Override
	protected void defineDebuffs(Debuffs debuffs) {

	}

	/* Animations */
	@Override
	protected void defineAnimations(Animations<Boss1States, Boss1Parts> animations) {
		HashMap<String, Boss1Parts> filenames = new HashMap<>();
		filenames.put("RightArm", RIGHT_ARM);
		filenames.put("Body", BODY);
		filenames.put("RightLeg", RIGHT_LEG);
		filenames.put("LeftLeg", LEFT_LEG);
		filenames.put("LeftArm", LEFT_ARM);
		filenames.put("Shockwave", SHOCKWAVE);

		Animation<Boss1Parts> standing =
				new Animation<>(STANDING_ANIMATION_DURATION, "Boss1/Standing", filenames)
						.loop();

		Animation<Boss1Parts> slam =
				new Animation<>(SLAM_ANIMATION_DURATION, "Boss1/Smash", filenames)
						.defineEnd(() -> input(SLAM_KEYUP));

		Animation<Boss1Parts> earthquake =
				new Animation<>(EARTHQUAKE_ANIMATION_DURATION, "Boss1/Earthquake", filenames)
						.defineEnd(() -> input(EARTHQUAKE_KEYUP));

		Animation<Boss1Parts> roll =
				new Animation<>(ROLL_ANIMATION_DURATION, "Boss1/Roll", filenames)
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
}
