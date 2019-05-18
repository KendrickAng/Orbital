package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Data structure to store cooldowns for the Tank.
 */
public class TankState {
    protected static final long PRIMARY_COOLDOWN = 0; // skill cd in ms
    protected static final long SECONDARY_COOLDOWN = 1000;
    protected static final long TERTIARY_COOLDOWN = 2000;
    protected static final long PRIMARY_PERSIST_TIME = 50; // skill lasting time in ms
    protected static final long SECONDARY_PERSIST_TIME = 50;
    protected static final long TERTIARY_PERSIST_TIME= 5000;

    // boolean flags for skill activation
    private boolean primary_pressed = false;
    private boolean secondary_pressed = false;
    private boolean tertiary_pressed = false;

    // time since last skills
    private long primary;
    private long secondary;
    private long tertiary;

    public TankState() {
        this.primary = 0;
        this.secondary = 0;
        this.tertiary = 0;
    }

    // set the timing of the respective skill to current time.
    public void updatePrimaryTimeSince() { this.primary = TimeUtils.millis(); }
    public void updateSecondaryTimeSince() { this.secondary = TimeUtils.millis(); }
    public void updateTertiaryTimeSince() { this.tertiary = TimeUtils.millis(); }

    public long getPrimaryTimeSince() { return this.primary; }
    public long getSecondaryTimeSince() { return this.secondary; }
    public long getTertiaryTimeSince() { return this.tertiary; }

    public boolean isPrimaryPressed() { return this.primary_pressed; }
    public boolean isSecondaryPressed() { return this.secondary_pressed; }
    public boolean isTertiaryPressed() { return this.tertiary_pressed; }

    public void setPrimaryPressed(boolean flag) { this.primary_pressed = flag; }
    public void setSecondaryPressed(boolean flag) { this.secondary_pressed = flag; }
    public void setTertiaryPressed(boolean flag) { this.tertiary_pressed = flag; }
}
