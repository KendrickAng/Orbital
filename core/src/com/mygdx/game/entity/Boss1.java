package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.animation.AnimationsGroup;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.state.Boss1States;
import com.mygdx.game.entity.state.States;

import java.util.Collections;
import java.util.HashMap;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.part.Boss1Parts.BODY;
import static com.mygdx.game.entity.part.Boss1Parts.LEFT_ARM;
import static com.mygdx.game.entity.part.Boss1Parts.LEFT_LEG;
import static com.mygdx.game.entity.part.Boss1Parts.RIGHT_ARM;
import static com.mygdx.game.entity.part.Boss1Parts.RIGHT_LEG;
import static com.mygdx.game.entity.state.Boss1States.STANDING;

public class Boss1 extends LivingEntity<Boss1States, Boss1Parts> {
	private static final float HEALTH = 1000;

	public Boss1(GameScreen game) {
		super(game);
		// TODO: Should be width of pixmap
		setPosition(GAME_WIDTH - 320, MAP_HEIGHT);
	}

	@Override
	protected void defineStates(States<Boss1States> states) {
		states.addState(STANDING);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	@Override
	protected void defineAbilities(Abilities<Boss1States> abilities) {

	}

	@Override
	protected void defineDebuffs(Debuffs<DebuffType> debuffs) {

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

		AnimationsGroup<Boss1Parts> standing = new AnimationsGroup<>("Boss1/Standing", filenames);
		standing.setDuration(2);

		animations.map(Collections.singleton(STANDING), standing);
	}

	/* Update */
	@Override
	protected void updateDirection(Direction inputDirection) {

	}

	@Override
	protected void updatePosition(Vector2 position) {

	}

	@Override
	protected void updateVelocity(Vector2 position, Vector2 velocity) {

	}
}
