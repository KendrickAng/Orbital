package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Data structure to store cooldowns for the Tank.
 */
public class TankState {
    protected static final long PRIMARY_COOLDOWN = 0; // primary skill cd in ms
    protected static final long SECONDARY_COOLDOWN = 1000; // secondary skill cd in ms
    protected static final long TERTIARY_COOLDOWN = 2000; // tertiary skill cd in ms

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
    public void updatePrimary() { this.primary = TimeUtils.millis(); }
    public void updateSecondary() { this.secondary = TimeUtils.millis(); }
    public void updateTertiary() { this.tertiary = TimeUtils.millis(); }

    public long getPrimary() { return this.primary; }
    public long getSecondary() { return this.secondary; }
    public long getTertiary() { return this.tertiary; }
}
