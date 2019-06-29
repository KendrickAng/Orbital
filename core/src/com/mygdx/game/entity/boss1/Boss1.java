package com.mygdx.game.entity.boss1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
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

	private static final float SLAM_COOLDOWN = 1f;
	private static final float EARTHQUAKE_COOLDOWN = 1f;
	private static final float ROLL_COOLDOWN = 1f;

	private static final float STANDING_ANIMATION_DURATION = 2f;
	private static final float SLAM_ANIMATION_DURATION = 1f;
	private static final float EARTHQUAKE_ANIMATION_DURATION = 1f;
	private static final float ROLL_ANIMATION_DURATION = 1f;

	public Boss1(GameScreen game) {
		super(game);

		// use width of pixmap
		int xOffset = new Pixmap(Gdx.files.internal("Boss1/Standing/0_Body.png")).getWidth();
		setPosition(GAME_WIDTH - xOffset, MAP_HEIGHT);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	@Override
	protected void defineStates(States<Boss1Input, Boss1States> states) {
		states.add(new State<Boss1Input, Boss1States>(STANDING)
				.defineUpdateVelocity((velocity) -> {
					velocity.x *= FRICTION;
				})
				.addEdge(LEFT_KEYDOWN, WALKING_LEFT)
				.addEdge(RIGHT_KEYDOWN, WALKING_RIGHT)
				.addEdge(SLAM_KEYDOWN, SLAM)
				.addEdge(EARTHQUAKE_KEYDOWN, EARTHQUAKE)
				.addEdge(ROLL_KEYDOWN, ROLL))

				.add(new State<Boss1Input, Boss1States>(WALKING_LEFT)
						.defineBegin(() -> setFlipX(false))
						.defineUpdateVelocity((velocity) -> {
							velocity.x -= WALKING_SPEED;
							velocity.x *= FRICTION;
						})
						.addEdge(LEFT_KEYUP, STANDING))

				.add(new State<Boss1Input, Boss1States>(WALKING_RIGHT)
						.defineBegin(() -> setFlipX(true))
						.defineUpdateVelocity((velocity) -> {
							velocity.x += WALKING_SPEED;
							velocity.x *= FRICTION;
						})
						.addEdge(RIGHT_KEYUP, STANDING))

				.add(new State<Boss1Input, Boss1States>(SLAM)
						.addEdge(SLAM_KEYUP, STANDING))

				.add(new State<Boss1Input, Boss1States>(EARTHQUAKE)
						.addEdge(EARTHQUAKE_KEYUP, STANDING))

				.add(new State<Boss1Input, Boss1States>(ROLL)
						.defineUpdateVelocity((velocity -> {
							if (isFlipX()) {
								velocity.x += ROLL_SPEED;
								velocity.x *= FRICTION;
							} else {
								velocity.x -= ROLL_SPEED;
								velocity.x *= FRICTION;
							}
						}))
						.addEdge(ROLL_KEYUP, STANDING));
	}

	@Override
	protected void defineAbilities(Abilities<Boss1States> abilities) {
		Ability slam = new Ability(SLAM_COOLDOWN);
		Ability earthquake = new Ability(EARTHQUAKE_COOLDOWN);
		Ability roll = new Ability(ROLL_COOLDOWN);

		abilities.defineUse(SLAM, slam)
				.defineUse(EARTHQUAKE, earthquake)
				.defineUse(ROLL, roll);
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

		Animation<Boss1Parts> standing = new Animation<>(STANDING_ANIMATION_DURATION, true);
		Animation<Boss1Parts> slam = new Animation<>(SLAM_ANIMATION_DURATION, false);
		Animation<Boss1Parts> earthquake = new Animation<>(EARTHQUAKE_ANIMATION_DURATION, false);
		Animation<Boss1Parts> roll = new Animation<>(ROLL_ANIMATION_DURATION, false);

		standing.load("Boss1/Standing", filenames);
		slam.load("Boss1/Smash", filenames);
		earthquake.load("Boss1/Earthquake", filenames);
		roll.load("Boss1/Roll", filenames);

		animations.map(STANDING, standing)
				.map(WALKING_LEFT, standing)
				.map(WALKING_RIGHT, standing)
				.map(SLAM, slam)
				.map(EARTHQUAKE, earthquake)
				.map(ROLL, roll);
	}

	@Override
	protected void updatePosition(Vector2 position) {
		// prevent boss from moving off screen
		float x = getHitbox(BODY).getOffsetX();
		float width = getHitbox(BODY).getWidth();
		if (position.x < -x) {
			position.x = -x;
		}

		if (position.x > GAME_WIDTH - x - width) {
			position.x = GAME_WIDTH - x - width;
		}
	}
}
