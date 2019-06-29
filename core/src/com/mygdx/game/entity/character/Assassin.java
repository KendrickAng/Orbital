package com.mygdx.game.entity.character;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.Direction;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.part.AssassinParts;
import com.mygdx.game.entity.state.State;
import com.mygdx.game.entity.state.States;

import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.character.AssassinStates.PRIMARY;
import static com.mygdx.game.entity.character.AssassinStates.SECONDARY;
import static com.mygdx.game.entity.character.AssassinStates.STANDING;
import static com.mygdx.game.entity.character.AssassinStates.TERTIARY;
import static com.mygdx.game.entity.character.AssassinStates.WALKING_LEFT;
import static com.mygdx.game.entity.character.AssassinStates.WALKING_RIGHT;
import static com.mygdx.game.entity.character.CharacterInput.LEFT_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.LEFT_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.PRIMARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.PRIMARY_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.RIGHT_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.RIGHT_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.SECONDARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.SECONDARY_KEYUP;
import static com.mygdx.game.entity.character.CharacterInput.TERTIARY_KEYDOWN;
import static com.mygdx.game.entity.character.CharacterInput.TERTIARY_KEYUP;
import static com.mygdx.game.entity.part.AssassinParts.BODY;

public class Assassin extends Character<CharacterInput, AssassinStates, AssassinParts> {
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
	private static final float TERTIARY_ANIMATION_DURATION = 0.5f;

	// Dodge speed
	private static final float DODGE_SPEED = 20;
	private static final float DODGE_DIAGONAL_SPEED = 15;

	private Direction dodgeDirection;

	public Assassin(GameScreen game) {
		super(game);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	@Override
	protected void defineStates(States<CharacterInput, AssassinStates> states) {
		states.add(new State<CharacterInput, AssassinStates>(STANDING)
				.defineUpdateVelocity((velocity) -> velocity.x *= FRICTION)
				.addEdge(LEFT_KEYDOWN, WALKING_LEFT)
				.addEdge(RIGHT_KEYDOWN, WALKING_RIGHT)
				.addEdge(PRIMARY_KEYDOWN, PRIMARY)
				.addEdge(SECONDARY_KEYDOWN, SECONDARY)
				.addEdge(TERTIARY_KEYDOWN, TERTIARY))

				.add(new State<CharacterInput, AssassinStates>(WALKING_LEFT)
						.defineBegin(() -> setFlipX(true))
						.defineUpdateVelocity((velocity) -> {
							velocity.x -= MOVESPEED * (1 - getSlow());
							velocity.x *= FRICTION;
						})
						.addEdge(LEFT_KEYUP, STANDING)
						.addEdge(RIGHT_KEYDOWN, STANDING)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY))

				.add(new State<CharacterInput, AssassinStates>(WALKING_RIGHT)
						.defineBegin(() -> setFlipX(false))
						.defineUpdateVelocity((velocity) -> {
							velocity.x += MOVESPEED * (1 - getSlow());
							velocity.x *= FRICTION;
						})
						.addEdge(RIGHT_KEYUP, STANDING)
						.addEdge(LEFT_KEYDOWN, STANDING)
						.addEdge(PRIMARY_KEYDOWN, PRIMARY))

				/* Dodge */
				.add(new State<CharacterInput, AssassinStates>(PRIMARY)
						.defineUpdateVelocity((velocity) -> {
							if (isFlipX()) {
								velocity.x = -DODGE_SPEED;
							} else {
								velocity.x = DODGE_SPEED;
							}
						})
						.addEdge(PRIMARY_KEYUP, STANDING))

				/* Stars */
				.add(new State<CharacterInput, AssassinStates>(SECONDARY)
						.addEdge(SECONDARY_KEYUP, STANDING))

				/* Cleanse */
				.add(new State<CharacterInput, AssassinStates>(TERTIARY)
						.addEdge(TERTIARY_KEYUP, STANDING));
	}

	@Override
	protected void defineAnimations(Animations<AssassinStates, AssassinParts> animations) {
		HashMap<String, AssassinParts> filenames = new HashMap<>();
		filenames.put("Body", BODY);

		Animation<AssassinParts> standing = new Animation<>(STANDING_ANIMATION_DURATION, true);
		standing.load("Assassin/Standing", filenames);

		animations.map(STANDING, standing);
	}

	@Override
	protected void defineAbilities(Abilities<AssassinStates> abilities) {

	}

	/*
	@Override
	protected Ability initPrimary() {
		return new Ability(PRIMARY_ANIMATION_DURATION, PRIMARY_COOLDOWN)
				.defineBegin(() -> {
					dodgeDirection = getInputDirection();
					inflictDebuff(DebuffType.IGNORE_FRICTION, 1, PRIMARY_ANIMATION_DURATION);
				})
				.setAbilityUsing(() -> {
					float velX = 0;
					float velY = 0;
					switch (dodgeDirection) {
						case RIGHT:
							velX = DODGE_SPEED;
							break;
						case LEFT:
							velX = -DODGE_SPEED;
							break;
						case UP:
							velY = DODGE_SPEED;
							break;
						case UP_RIGHT:
							velX = DODGE_DIAGONAL_SPEED;
							velY = DODGE_DIAGONAL_SPEED;
							break;
						case UP_LEFT:
							velX = -DODGE_DIAGONAL_SPEED;
							velY = DODGE_DIAGONAL_SPEED;
							break;
					}
					setVelocity(velX, velY);
				})
				.setResetCondition(isOnCooldown -> !isFalling());
	}

	@Override
	protected Ability initSecondary() {
		return new Ability(SECONDARY_ANIMATION_DURATION, SECONDARY_COOLDOWN)
				.defineBegin(() -> {
					Entity shuriken = new Shuriken(getGame());
					Hitbox body = getHitbox(BODY);
					float x = body.getX() + body.getWidth() / 2;
					float y = body.getY() + body.getHeight() / 2;
					int x_velocity = 0;
					switch (isFlipX()) {
						case RIGHT:
							x_velocity = Shuriken.FLYING_SPEED;
							break;
						case LEFT:
							x_velocity = -Shuriken.FLYING_SPEED;
							break;
					}
					shuriken.setPosition(x, y);
					shuriken.setVelocity(x_velocity, 0);
				});
	}

	@Override
	protected Ability initTertiary() {
		return new Ability(TERTIARY_ANIMATION_DURATION, TERTIARY_COOLDOWN)
				.defineBegin(() -> Gdx.app.log("Assassin.java", "Tertiary"));
	}
	*/

	@Override
	protected void updatePosition(Vector2 position) {
		float x = getHitbox(BODY).getOffsetX();
		float width = getHitbox(BODY).getWidth();
		if (position.x < -x) {
			position.x = -x;
		}

		if (position.x > GAME_WIDTH - x - width) {
			position.x = GAME_WIDTH - x - width;
		}
	}

	/*
	@Override
	protected void updateVelocity(Vector2 position, Vector2 velocity) {
		switch (getState()) {
			case WALKING_LEFT:
				if (isFlipX()) {
					velocity.x -= MOVESPEED;
				} else {
					velocity.x += MOVESPEED;
				}
				velocity.x *= FRICTION;
				break;
			case PRIMARY:
				if (isFlipX()) {
					velocity.x -= MOVESPEED * AIR_MOVESPEED;
				} else {
					velocity.x += MOVESPEED * AIR_MOVESPEED;
				}
				break;
		}

		if (position.y > MAP_HEIGHT) {
			falling = true;
		}

		// Airborne
		if (falling) {
			if (position.y > MAP_HEIGHT) {
				if (getState() == WALKING_LEFT) {
					if (isFlipX()) {
						velocity.x -= movespeed * AIR_MOVESPEED;
					} else {
						velocity.x += movespeed * AIR_MOVESPEED;
					}
				}
				velocity.x *= AIR_FRICTION;
				velocity.y += GRAVITY;
			} else {
				// Touched ground
				velocity.y = 0;
				position.y = MAP_HEIGHT;
				falling = false;
			}

			// Not Airborne
		} else {
			if (getState() == WALKING_LEFT) {
				if (isFlipX()) {
					velocity.x -= movespeed;
				} else {
					velocity.x += movespeed;
				}
			}
			velocity.x *= friction;
		}
	}
	*/



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
	protected void usePrimary(boolean keydown) {
		if (keydown) {
			input(PRIMARY_KEYDOWN);
		} else {
			input(PRIMARY_KEYUP);
		}
	}

	@Override
	protected void useSecondary(boolean keydown) {
		if (keydown) {
			input(SECONDARY_KEYDOWN);
		} else {
			input(SECONDARY_KEYUP);
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
}
