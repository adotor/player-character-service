package de.example.pc.validation;

import de.example.pc.PCClass;

public class MaximumLevelChecker {
    public boolean check(PCClass pcClass, Integer level) {
        System.out.printf("Checking level for (%s, %s)%n", pcClass,level);
        return switch (pcClass) {
            case WIZARD -> level <= 60;
            default -> level <= 100;
        };
    }
}
