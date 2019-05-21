package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

public class AssassinState {
    // time since last skills
    private long primary;
    private long secondary;
    private long tertiary;

    // direction facing
    private Direction direction;

    // boolean flags for skill activation
    private boolean primary_pressed = false;
    private boolean secondary_pressed = false;
    private boolean tertiary_pressed = false;

    // boolean flags for assassin state (in air, etc)
    protected boolean in_air = false;

    public AssassinState() {
        this.primary = 0;
        this.secondary = 0;
        this.tertiary = 0;
        this.direction = Direction.RIGHT;
    }

    // set the timing of the respective skill to current time.
    public void updatePrimaryTimeSince() { this.primary = TimeUtils.millis(); }
    public void updateSecondaryTimeSince() { this.secondary = TimeUtils.millis(); }
    public void updateTertiaryTimeSince() { this.tertiary = TimeUtils.millis(); }

    public long getPrimaryTimeSince() { return this.primary; }
    public long getSecondaryTimeSince() { return this.secondary; }
    public long getTertiaryTimeSince() { return this.tertiary; }

    public Direction getDirection() { return this.direction; }
    public void setDirection(Direction d) { this.direction = d; }

    public boolean isPrimaryPressed() { return this.primary_pressed; }
    public boolean isSecondaryPressed() { return this.secondary_pressed; }
    public boolean isTertiaryPressed() { return this.tertiary_pressed; }

    public void setPrimaryPressed(boolean flag) { this.primary_pressed = flag; }
    public void setSecondaryPressed(boolean flag) { this.secondary_pressed = flag; }
    public void setTertiaryPressed(boolean flag) { this.tertiary_pressed = flag; }
}
