package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.entity.Assassin;
import com.mygdx.game.entity.Character;
import com.mygdx.game.entity.Direction;
import com.mygdx.game.entity.Tank;

import java.util.HashMap;
import java.util.HashSet;

import static com.mygdx.game.CharacterType.ASSASSIN;
import static com.mygdx.game.CharacterType.TANK;

public class CharacterController implements InputProcessor {
	private HashMap<CharacterType, Character> characters;
	private CharacterType current;
	private MyGdxGame game;
	private HashSet<Direction> inputDirections;

	public CharacterController(MyGdxGame game) {
		Gdx.input.setInputProcessor(this);
		this.characters = new HashMap<CharacterType, Character>();
		this.game = game;
		this.characters.put(TANK, new Tank(game));
		this.characters.put(ASSASSIN, new Assassin(game));
		inputDirections = new HashSet<Direction>();
		setCharacter(TANK);
	}

	public void setCharacter(CharacterType character) {
		this.current = character;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.LEFT:
				inputDirections.add(Direction.LEFT);
				character().setInputDirection(resolveInputDirection());
				break;
			case Input.Keys.RIGHT:
				inputDirections.add(Direction.RIGHT);
				character().setInputDirection(resolveInputDirection());
				break;
			case Input.Keys.Q:
				character().primary();
				break;
			case Input.Keys.W:
				character().secondary();
				break;
			case Input.Keys.E:
				character().tertiary();
				break;
			case Input.Keys.R: // switch characters
				Gdx.app.log("CharacterController.java", "switched characters");
				current = switchCharacter();
				break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.LEFT:
				inputDirections.remove(Direction.LEFT);
				character().setInputDirection(resolveInputDirection());
				break;
			case Input.Keys.RIGHT:
				inputDirections.remove(Direction.RIGHT);
				character().setInputDirection(resolveInputDirection());
				break;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		return true;
	}

	public Character character() {
		Character c = null;
		switch (current) {
			case ASSASSIN:
				c = characters.get(ASSASSIN);
				break;
			case TANK:
				c = characters.get(TANK);
				break;
		}
		return c;
	}

	public CharacterType switchCharacter() {
		CharacterType previous = current;
		CharacterType switched = previous == ASSASSIN ? TANK : ASSASSIN;
		Character prev = characters.get(previous);
		Character next = characters.get(switched);
		next.setPosition(prev.getPosition());
		next.setInputDirection(prev.getInputDirection());
		return switched;
	}

	public MyGdxGame getGame() {
		return this.game;
	}

	private Direction resolveInputDirection() {
		Direction inputDirection = Direction.NONE;
		// Check that inputDirections doesn't contain RIGHT & LEFT
		if (!(inputDirections.contains(Direction.RIGHT) && inputDirections.contains(Direction.LEFT))) {
			if (inputDirections.contains(Direction.RIGHT)) {
				inputDirection = Direction.RIGHT;
			} else if (inputDirections.contains(Direction.LEFT)) {
				inputDirection = Direction.LEFT;
			}
		}
		return inputDirection;
	}
}
