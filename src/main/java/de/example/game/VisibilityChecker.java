package de.example.game;

import de.example.monster.Monster;
import de.example.pc.PlayerCharacter;

import java.time.LocalTime;
import java.util.List;

public class VisibilityChecker {

    public boolean isMonsterVisible(PlayerCharacter pc, List<Monster> enemies) {
        LocalTime now = LocalTime.now();

        for (Monster monster :  enemies) {
            if (monster.distance <= pc.getVisionRange(now)) {
                return true;
            }
        }

        return false;
    }
}