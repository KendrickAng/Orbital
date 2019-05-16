package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Helper class for checking cooldowns
 */
public class CooldownUtils {

    public boolean isSkillAvailable(long prev, long cooldown) {
        if(TimeUtils.timeSinceMillis(prev) < 0) {
            throw new IllegalStateException("Cooldown check returning negative cd!");
        } else {
            return TimeUtils.timeSinceMillis(prev) >= cooldown;
        }
    }
}
