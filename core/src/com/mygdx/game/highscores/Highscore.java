package com.mygdx.game.highscores;

public class Highscore {
	private String name;
	private int level;
	private int score;
	private int time;

	public Highscore(String name, int level, int score, int time) {
		this.name = name;
		this.level = level;
		this.score = score;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getScore() {
		return score;
	}

	public int getTime() {
		return time;
	}
}
