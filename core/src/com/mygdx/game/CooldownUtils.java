package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Helper class for checking cooldowns
 */
public class CooldownUtils {

    // returns true if cooldown < current time - prev
    public boolean isSkillAvailable(long prev, long cooldown) {
        if(TimeUtils.timeSinceMillis(prev) < 0) {
            throw new IllegalStateException("Cooldown check returning negative cd!");
        } else {
            return TimeUtils.timeSinceMillis(prev) >= cooldown;
        }
    }

    // returns true if persistTime < current time - startCast
    public boolean isSkillPersisting(long startCast, long persistTime) {
        if(TimeUtils.timeSinceMillis(startCast) < 0) {
            throw new IllegalStateException("Skill persist check returning negative time!");
        } else {
            return TimeUtils.timeSinceMillis(startCast) <= persistTime;
        }
    }
}
