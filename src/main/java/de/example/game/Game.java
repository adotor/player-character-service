package de.example.game;

import de.example.monster.Monster;
import de.example.pc.PlayerCharacter;

import java.util.List;

import static de.example.game.GameState.ACTIVE;
import static de.example.game.GameState.PAUSE;

public class Game {
    public PlayerCharacter activePC;
    private GameState state;
    public GameState getState() {
        return state;
    }
    public Game(PlayerCharacter pc) {
        this.activePC = pc;
        this.state = ACTIVE;
    }
    public void afterMovement(List<Monster> updatedMonsters, boolean isAutoPauseEnabled) {
        if (isAutoPauseEnabled && VisibilityChecker.shared.isMonsterVisible(this.activePC, updatedMonsters)) {
            this.state = PAUSE;
        }
    }
}
