package com.mygdx.game.entity.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.Shuriken;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.part.AssassinParts;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;

import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.character.AssassinStates.PRIMARY;
import static com.mygdx.game.entity.character.AssassinStates.PRIMARY_LEFT;
import static com.mygdx.game.entity.character.AssassinStates.PRIMARY_LEFT_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.PRIMARY_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.PRIMARY_UP;
import static com.mygdx.game.entity.character.AssassinStates.PRIMARY_UP_LEFT;
import static com.mygdx.game.entity.character.AssassinStates.PRIMARY_UP_LEFT_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.PRIMARY_UP_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.SECONDARY;
import static com.mygdx.game.entity.character.AssassinStates.SECONDARY_LEFT;
import static com.mygdx.game.entity.character.AssassinStates.SECONDARY_LEFT_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.SECONDARY_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.SECONDARY_UP;
import static com.mygdx.game.entity.character.AssassinStates.SECONDARY_UP_LEFT;
import static com.mygdx.game.entity.character.AssassinStates.SECONDARY_UP_LEFT_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.SECONDARY_UP_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.STANDING;
import static com.mygdx.game.entity.character.AssassinStates.STANDING_LEFT_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.STANDING_UP;
import static com.mygdx.game.entity.character.AssassinStates.STANDING_UP_LEFT_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.TERTIARY;
import static com.mygdx.game.entity.character.AssassinStates.WALKING_LEFT;
import static com.mygdx.game.entity.character.AssassinStates.WALKING_RIGHT;
import static com.mygdx.game.entity.character.AssassinStates.WALKING_UP_LEFT;
import static com.mygdx.game.entity.character.AssassinStates.WALKING_UP_RIGHT;
import static com.mygdx.game.entity.character.CharacterInput.LEFT_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.LEFT_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.PRIMARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.PRIMARY_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.RIGHT_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.RIGHT_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.SECONDARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.SECONDARY_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.SWITCH_CHARACTER;
import static com.mygdx.game.entity.character.CharacterInput.TERTIARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.TERTIARY_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.UP_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.UP_KEYUP;
import static com.mygdx.game.entity.part.AssassinParts.BODY;
import static com.mygdx.game.entity.part.AssassinParts.LEFT_ARM;
import static com.mygdx.game.entity.part.AssassinParts.LEFT_LEG;
import static com.mygdx.game.entity.part.AssassinParts.RIGHT_ARM;
import static com.mygdx.game.entity.part.AssassinParts.RIGHT_LEG;


public class Assassin extends Character<AssassinStates, AssassinParts> {
	private static final float MOVESPEED = 2f;
	// Movespeed is multiplied by this constant in air
	private static final float AIR_MOVESPEED = 0.1f;

	// Velocity is multiplied by these constants
	private static final float FRICTION = 0.6f;
	private static final float AIR_FRICTION = 0.95f;

	private static final float HEALTH = 10;

	// Skill cooldown in seconds.
	private static final float PRIMARY_COOLDOWN = 0;
	private static final float SECONDARY_COOLDOWN = 1;
	private static final float TERTIARY_COOLDOWN = 2;

	// Skill animation duration in seconds.
	private static final float STANDING_ANIMATION_DURATION = 1f;
	private static final float WALKING_ANIMATION_DURATION = 1f;
	private static final float PRIMARY_ANIMATION_DURATION = 0.05f;
	private static final float SECONDARY_ANIMATION_DURATION = 0.5f;
	private static final float TERTIARY_ANIMATION_DURATION = 2f;

	// Dodge speed
	private static final float DODGE_SPEED = 20;
	private static final float DODGE_DIAGONAL_SPEED = 15;

	private Vector2 velocity;
	private boolean falling;
	private Ability<AssassinStates> primary;
	private boolean primaryGround;

	public Assassin(GameScreen game) {
		super(game);
		velocity = new Vector2();
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	@Override
	protected void defineStates(States<CharacterInput, AssassinStates> states) {
		states.add(new State<CharacterInput, AssassinStates>(STANDING)
				.defineUpdate(this::updatePhysics)
				.addEdge(LEFT_KEYDOWN, WALKING_LEFT)
				.addEdge(RIGHT_KEYDOWN, WALKING_RIGHT)
				.addEdge(UP_KEYDOWN, STANDING_UP)
				.addEdge(SECONDARY_KEYDOWN, SECONDARY)
				.addEdge(TERTIARY_KEYDOWN, TERTIARY)
				.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<CharacterInput, AssassinStates>(STANDING_LEFT_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(LEFT_KEYUP, WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, WALKING_LEFT)
						.addEdge(UP_KEYDOWN, STANDING_UP_LEFT_RIGHT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_LEFT_RIGHT)
						.addEdge(SECONDARY_KEYDOWN, SECONDARY_LEFT_RIGHT)
						.addEdge(SWITCH_CHARACTER, STANDING))


				.add(new State<CharacterInput, AssassinStates>(WALKING_LEFT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(() -> {
							addVelocityX(-MOVESPEED * (1 - getSlow()));
							updatePhysics();
						})
						.addEdge(LEFT_KEYUP, STANDING)
						.addEdge(RIGHT_KEYDOWN, STANDING_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, WALKING_UP_LEFT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_LEFT)
						.addEdge(SECONDARY_KEYDOWN, SECONDARY_LEFT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<CharacterInput, AssassinStates>(WALKING_RIGHT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(() -> {
							addVelocityX(MOVESPEED * (1 - getSlow()));
							updatePhysics();
						})
						.addEdge(RIGHT_KEYUP, STANDING)
						.addEdge(LEFT_KEYDOWN, STANDING_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, WALKING_UP_RIGHT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_RIGHT)
						.addEdge(SECONDARY_KEYDOWN, SECONDARY_RIGHT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<CharacterInput, AssassinStates>(STANDING_UP)
						.defineUpdate(this::updatePhysics)
						.addEdge(UP_KEYUP, STANDING)
						.addEdge(LEFT_KEYDOWN, WALKING_UP_LEFT)
						.addEdge(RIGHT_KEYDOWN, WALKING_UP_RIGHT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_UP)
						.addEdge(SECONDARY_KEYDOWN, SECONDARY_UP)
						.addEdge(TERTIARY_KEYDOWN, TERTIARY)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<CharacterInput, AssassinStates>(STANDING_UP_LEFT_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(UP_KEYUP, STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, WALKING_UP_RIGHT)
						.addEdge(RIGHT_KEYUP, WALKING_UP_LEFT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_UP_LEFT_RIGHT)
						.addEdge(SECONDARY_KEYDOWN, SECONDARY_UP_LEFT_RIGHT)
						.addEdge(SWITCH_CHARACTER, STANDING))


				.add(new State<CharacterInput, AssassinStates>(WALKING_UP_LEFT)
						.defineBegin(() -> getFlipX().set(true))
						.defineUpdate(() -> {
							addVelocityX(-MOVESPEED * (1 - getSlow()));
							updatePhysics();
						})
						.addEdge(UP_KEYUP, WALKING_LEFT)
						.addEdge(LEFT_KEYUP, STANDING_UP)
						.addEdge(RIGHT_KEYDOWN, STANDING_UP_LEFT_RIGHT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_UP_LEFT)
						.addEdge(SECONDARY_KEYDOWN, SECONDARY_UP_LEFT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				.add(new State<CharacterInput, AssassinStates>(WALKING_UP_RIGHT)
						.defineBegin(() -> getFlipX().set(false))
						.defineUpdate(() -> {
							addVelocityX(MOVESPEED * (1 - getSlow()));
							updatePhysics();
						})
						.addEdge(UP_KEYUP, WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, STANDING_UP)
						.addEdge(LEFT_KEYDOWN, STANDING_UP_LEFT_RIGHT)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY_UP_RIGHT)
						.addEdge(SECONDARY_KEYDOWN, SECONDARY_UP_RIGHT)
						.addEdge(SWITCH_CHARACTER, STANDING))

				/* Dodge */
				.add(new State<CharacterInput, AssassinStates>(PRIMARY)
						.defineUpdate(this::updatePosition)
						.addEdge(LEFT_KEYDOWN, PRIMARY_LEFT)
						.addEdge(RIGHT_KEYDOWN, PRIMARY_RIGHT)
						.addEdge(UP_KEYDOWN, PRIMARY_UP)
						.addEdge(PRIMARY_KEYUP, STANDING))

				.add(new State<CharacterInput, AssassinStates>(PRIMARY_LEFT)
						.defineUpdate(this::updatePosition)
						.addEdge(LEFT_KEYUP, PRIMARY)
						.addEdge(RIGHT_KEYDOWN, PRIMARY_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, PRIMARY_UP_LEFT)
						.addEdge(PRIMARY_KEYUP, WALKING_LEFT))

				.add(new State<CharacterInput, AssassinStates>(PRIMARY_RIGHT)
						.defineUpdate(this::updatePosition)
						.addEdge(RIGHT_KEYUP, PRIMARY)
						.addEdge(LEFT_KEYDOWN, PRIMARY_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, PRIMARY_UP_RIGHT)
						.addEdge(PRIMARY_KEYUP, WALKING_RIGHT))

				.add(new State<CharacterInput, AssassinStates>(PRIMARY_LEFT_RIGHT)
						.defineUpdate(this::updatePosition)
						.addEdge(LEFT_KEYUP, PRIMARY_RIGHT)
						.addEdge(RIGHT_KEYUP, PRIMARY_LEFT)
						.addEdge(UP_KEYDOWN, PRIMARY_UP_LEFT_RIGHT)
						.addEdge(PRIMARY_KEYUP, STANDING_LEFT_RIGHT))

				.add(new State<CharacterInput, AssassinStates>(PRIMARY_UP)
						.defineUpdate(this::updatePosition)
						.addEdge(UP_KEYUP, PRIMARY)
						.addEdge(LEFT_KEYDOWN, PRIMARY_UP_LEFT)
						.addEdge(RIGHT_KEYDOWN, PRIMARY_UP_RIGHT)
						.addEdge(PRIMARY_KEYUP, STANDING_UP))

				.add(new State<CharacterInput, AssassinStates>(PRIMARY_UP_LEFT)
						.defineUpdate(this::updatePosition)
						.addEdge(UP_KEYUP, PRIMARY_LEFT)
						.addEdge(LEFT_KEYUP, PRIMARY_UP)
						.addEdge(RIGHT_KEYDOWN, PRIMARY_UP_LEFT_RIGHT)
						.addEdge(PRIMARY_KEYUP, WALKING_UP_LEFT))

				.add(new State<CharacterInput, AssassinStates>(PRIMARY_UP_RIGHT)
						.defineUpdate(this::updatePosition)
						.addEdge(UP_KEYUP, PRIMARY_RIGHT)
						.addEdge(RIGHT_KEYUP, PRIMARY_UP)
						.addEdge(LEFT_KEYDOWN, PRIMARY_UP_LEFT_RIGHT)
						.addEdge(PRIMARY_KEYUP, WALKING_UP_RIGHT))

				.add(new State<CharacterInput, AssassinStates>(PRIMARY_UP_LEFT_RIGHT)
						.defineUpdate(this::updatePosition)
						.addEdge(UP_KEYUP, PRIMARY_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, PRIMARY_UP_RIGHT)
						.addEdge(RIGHT_KEYUP, PRIMARY_UP_LEFT)
						.addEdge(PRIMARY_KEYUP, STANDING_UP_LEFT_RIGHT))

				/* Stars */
				.add(new State<CharacterInput, AssassinStates>(SECONDARY)
						.defineUpdate(this::updatePhysics)
						.addEdge(SECONDARY_KEYUP, STANDING)
						.addEdge(LEFT_KEYDOWN, SECONDARY_LEFT)
						.addEdge(RIGHT_KEYDOWN, SECONDARY_RIGHT)
						.addEdge(UP_KEYDOWN, SECONDARY_UP))

				.add(new State<CharacterInput, AssassinStates>(SECONDARY_LEFT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SECONDARY_KEYUP, WALKING_LEFT)
						.addEdge(LEFT_KEYUP, SECONDARY)
						.addEdge(RIGHT_KEYDOWN, SECONDARY_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, SECONDARY_UP_LEFT))

				.add(new State<CharacterInput, AssassinStates>(SECONDARY_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SECONDARY_KEYUP, WALKING_RIGHT)
						.addEdge(RIGHT_KEYUP, SECONDARY)
						.addEdge(LEFT_KEYDOWN, SECONDARY_LEFT_RIGHT)
						.addEdge(UP_KEYDOWN, SECONDARY_UP_RIGHT))

				.add(new State<CharacterInput, AssassinStates>(SECONDARY_LEFT_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SECONDARY_KEYUP, STANDING_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, SECONDARY_RIGHT)
						.addEdge(RIGHT_KEYUP, SECONDARY_LEFT)
						.addEdge(UP_KEYDOWN, SECONDARY_UP_LEFT_RIGHT))

				.add(new State<CharacterInput, AssassinStates>(SECONDARY_UP)
						.defineUpdate(this::updatePhysics)
						.addEdge(SECONDARY_KEYUP, STANDING_UP)
						.addEdge(UP_KEYUP, SECONDARY)
						.addEdge(LEFT_KEYDOWN, SECONDARY_UP_LEFT)
						.addEdge(RIGHT_KEYDOWN, SECONDARY_UP_RIGHT))

				.add(new State<CharacterInput, AssassinStates>(SECONDARY_UP_LEFT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SECONDARY_KEYUP, WALKING_UP_LEFT)
						.addEdge(UP_KEYUP, SECONDARY_LEFT)
						.addEdge(LEFT_KEYUP, SECONDARY_UP)
						.addEdge(RIGHT_KEYDOWN, SECONDARY_UP_LEFT_RIGHT))

				.add(new State<CharacterInput, AssassinStates>(SECONDARY_UP_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SECONDARY_KEYUP, WALKING_UP_RIGHT)
						.addEdge(UP_KEYUP, SECONDARY_RIGHT)
						.addEdge(RIGHT_KEYUP, SECONDARY_UP)
						.addEdge(LEFT_KEYDOWN, SECONDARY_UP_LEFT_RIGHT))

				.add(new State<CharacterInput, AssassinStates>(SECONDARY_UP_LEFT_RIGHT)
						.defineUpdate(this::updatePhysics)
						.addEdge(SECONDARY_KEYUP, STANDING_UP_LEFT_RIGHT)
						.addEdge(UP_KEYUP, SECONDARY_LEFT_RIGHT)
						.addEdge(LEFT_KEYUP, SECONDARY_UP_RIGHT)
						.addEdge(RIGHT_KEYUP, SECONDARY_UP_LEFT))

				/* Cleanse */
				.add(new State<CharacterInput, AssassinStates>(TERTIARY)
						.addEdge(TERTIARY_KEYUP, STANDING));
	}

	@Override
	protected void defineAnimations(Animations<AssassinStates, AssassinParts> animations) {
		HashMap<String, AssassinParts> filenames = new HashMap<>();
		filenames.put("Body", BODY);
		filenames.put("LeftArm", LEFT_ARM);
		filenames.put("LeftLeg", LEFT_LEG);
		filenames.put("RightArm", RIGHT_ARM);
		filenames.put("RightLeg", RIGHT_LEG);

		Animation<AssassinParts> standing =
				new Animation<>(STANDING_ANIMATION_DURATION, "Assassin/Standing", filenames)
						.loop();

		Animation<AssassinParts> walking =
				new Animation<>(WALKING_ANIMATION_DURATION, "Assassin/Walking", filenames)
						.loop();

		Animation<AssassinParts> primary =
				new Animation<>(PRIMARY_ANIMATION_DURATION, "Assassin/Primary", filenames)
						.defineEnd(() -> input(PRIMARY_KEYUP));

		Animation<AssassinParts> secondary =
				new Animation<>(SECONDARY_ANIMATION_DURATION, "Assassin/Secondary", filenames)
						.defineEnd(() -> input(SECONDARY_KEYUP));

		animations.map(STANDING, standing)
				.map(STANDING_LEFT_RIGHT, standing)
				.map(WALKING_LEFT, walking)
				.map(WALKING_RIGHT, walking)
				.map(STANDING_UP, standing)
				.map(STANDING_UP_LEFT_RIGHT, standing)
				.map(WALKING_UP_LEFT, walking)
				.map(WALKING_UP_RIGHT, walking)

				.map(PRIMARY_LEFT, primary)
				.map(PRIMARY_RIGHT, primary)
				.map(PRIMARY_UP, primary)
				.map(PRIMARY_UP_LEFT, primary)
				.map(PRIMARY_UP_RIGHT, primary)
				.map(PRIMARY_UP_LEFT_RIGHT, primary)

				.map(SECONDARY, secondary)
				.map(SECONDARY_LEFT, secondary)
				.map(SECONDARY_RIGHT, secondary)
				.map(SECONDARY_LEFT_RIGHT, secondary)
				.map(SECONDARY_UP, secondary)
				.map(SECONDARY_UP_LEFT, secondary)
				.map(SECONDARY_UP_RIGHT, secondary)
				.map(SECONDARY_UP_LEFT_RIGHT, secondary);
	}

	@Override
	protected void defineAbilities(Abilities<AssassinStates> abilities) {
		primary = new Ability<AssassinStates>(PRIMARY_COOLDOWN)
				.defineBegin((state) -> {
					primaryGround = false;
					switch (state) {
						case PRIMARY_LEFT:
							velocity.x = -DODGE_SPEED;
							primaryGround = true;
							break;
						case PRIMARY_RIGHT:
							velocity.x = DODGE_SPEED;
							primaryGround = true;
							break;
						case PRIMARY_UP_LEFT:
							velocity.x = -DODGE_DIAGONAL_SPEED;
							break;
						case PRIMARY_UP_RIGHT:
							velocity.x = DODGE_DIAGONAL_SPEED;
							break;
					}

					falling = true;
					switch (state) {
						case PRIMARY_UP:
						case PRIMARY_UP_LEFT_RIGHT:
							velocity.y = DODGE_SPEED;
							break;
						case PRIMARY_UP_LEFT:
							velocity.y = DODGE_DIAGONAL_SPEED;
							break;
						case PRIMARY_UP_RIGHT:
							velocity.y = DODGE_DIAGONAL_SPEED;
							break;
						default:
							falling = false;
					}
				})
				.defineEnd(() -> {
					if (primaryGround) {
						primary.reset();
					}
				});

		Ability<AssassinStates> secondary = new Ability<AssassinStates>(SECONDARY_COOLDOWN)
				.defineBegin((state) -> {
					Hitbox body = getHitbox(BODY);
					float x = body.getX() + body.getWidth() / 2;
					float y = body.getY() + body.getHeight() / 2;
					new Shuriken(getGame(), x, y, getFlipX().get());
				});

		Ability<AssassinStates> tertiary = new Ability<AssassinStates>(TERTIARY_COOLDOWN)
				.defineBegin((state) -> Gdx.app.log("Assassin.java", "Tertiary"));

		abilities.addBegin(PRIMARY_LEFT, primary)
				.addBegin(PRIMARY_RIGHT, primary)
				.addBegin(PRIMARY_UP, primary)
				.addBegin(PRIMARY_UP_LEFT, primary)
				.addBegin(PRIMARY_UP_RIGHT, primary)
				.addBegin(PRIMARY_UP_LEFT_RIGHT, primary)
				.addEnd(WALKING_LEFT, primary)
				.addEnd(WALKING_RIGHT, primary)
				.addEnd(STANDING_UP, primary)
				.addEnd(WALKING_UP_LEFT, primary)
				.addEnd(WALKING_UP_RIGHT, primary)
				.addEnd(STANDING_UP_LEFT_RIGHT, primary)

				.addBegin(SECONDARY, secondary)
				.addBegin(SECONDARY_LEFT, secondary)
				.addBegin(SECONDARY_RIGHT, secondary)
				.addBegin(SECONDARY_LEFT_RIGHT, secondary)
				.addBegin(SECONDARY_UP, secondary)
				.addBegin(SECONDARY_UP_LEFT, secondary)
				.addBegin(SECONDARY_UP_RIGHT, secondary)
				.addBegin(SECONDARY_UP_LEFT_RIGHT, secondary)
				.addEnd(STANDING, secondary)
				.addEnd(WALKING_LEFT, secondary)
				.addEnd(WALKING_RIGHT, secondary)
				.addEnd(STANDING_LEFT_RIGHT, secondary)
				.addEnd(STANDING_UP, secondary)
				.addEnd(WALKING_UP_LEFT, secondary)
				.addEnd(WALKING_UP_RIGHT, secondary)
				.addEnd(STANDING_UP_LEFT_RIGHT, secondary)

				.addBegin(TERTIARY, tertiary)
				.addEnd(STANDING, tertiary);
	}

	private void addVelocityX(float movespeed) {
		if (falling) {
			velocity.x += movespeed * AIR_MOVESPEED;
		} else {
			velocity.x += movespeed;
		}
	}

	private void updatePhysics() {
		if (falling) {
			velocity.y += GRAVITY;
			velocity.x *= AIR_FRICTION;
			if (getPosition().y < MAP_HEIGHT) {
				falling = false;
				velocity.y = 0;
				getPosition().y = MAP_HEIGHT;
				primary.reset();
			}
		} else {
			velocity.x *= FRICTION;
		}
		updatePosition();
	}

	private void updatePosition() {
		getPosition().x += velocity.x;
		getPosition().y += velocity.y;
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
		if (keydown) {
			input(UP_KEYDOWN);
		} else {
			input(UP_KEYUP);
		}
	}

	@Override
	protected void usePrimary(boolean keydown) {
		if (keydown) {
			input(PRIMARY_KEYDOWN);
		}
	}

	@Override
	protected void useSecondary(boolean keydown) {
		if (keydown) {
			input(SECONDARY_KEYDOWN);
		}
	}

	@Override
	protected void useTertiary(boolean keydown) {
		if (keydown) {
			input(TERTIARY_KEYDOWN);
		} else {
			input(TERTIARY_KEYUP);
		}
	}

	@Override
	public boolean useSwitchCharacter() {
		if (input(SWITCH_CHARACTER)) {
			velocity.x = 0;
			velocity.y = 0;
			falling = false;
			primary.reset();
			return true;
		}
		return false;
	}
}
