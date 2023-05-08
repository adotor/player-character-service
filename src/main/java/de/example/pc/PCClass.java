package de.example.pc;

public enum PCClass {
    FIGHTER(false),
    THIEF(false),
    WIZARD(true),
    CLERIC(true);

    public final boolean isSpellcaster;

    PCClass(boolean isSpellcaster) {
        this.isSpellcaster = isSpellcaster;
    }
}
