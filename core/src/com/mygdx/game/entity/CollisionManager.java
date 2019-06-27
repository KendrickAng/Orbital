package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.boss1.Boss1;
import com.mygdx.game.entity.character.Assassin;
import com.mygdx.game.entity.character.Tank;
import com.mygdx.game.entity.part.AssassinParts;
import com.mygdx.game.entity.part.Boss1Parts;
import com.mygdx.game.entity.part.TankParts;
import com.mygdx.game.entity.state.Boss1States;
import com.mygdx.game.entity.state.CharacterStates;

import java.util.Collection;

/**
 * Detects collisions between two classes.
 */
public class CollisionManager {
    private GameScreen game;

    public CollisionManager(GameScreen game) {
        this.game = game;
    }

    // Check all hitboxes for one LivingEntity for collision with all hitboxes of the other LivingEntity.
    // Called every delta second in the main gamescreen loop.
    public boolean colliding(LivingEntity character, LivingEntity<Boss1States, Boss1Parts> boss) {
        if (character instanceof Assassin) {
            for (AssassinParts a1 : AssassinParts.values()) {
                for (Boss1Parts b1 : Boss1Parts.values()) {
                    Hitbox h1 = character.getHitbox(a1);
                    Hitbox h2 = boss.getHitbox(b1);
                    if ((!character.getIsIdle() && !boss.getIsIdle()) &&
                            h1 != null && h2 != null && h1.hitTest(h2)) {
                        Gdx.app.log("CollisionManager.java", "Hit detected between Assassin and Boss1");
                        return true;
                    }
                }
            }
        }
        else if (character instanceof Tank) {
            for (TankParts t1 : TankParts.values()) {
                for (Boss1Parts b1 : Boss1Parts.values()) {
                    Hitbox h1 = character.getHitbox(t1);
                    Hitbox h2 = boss.getHitbox(b1);
                    if ((!character.getIsIdle() && !boss.getIsIdle()) &&
                        h1 != null && h2 != null && h1.hitTest(h2)) {
                        Gdx.app.log("CollisionManager.java", "Hit detected between Tank and Boss1");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
