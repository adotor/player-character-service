package de.example.pc;

public enum PCSpecies {
    HUMAN(false),
    ELF(true),
    HALFLING(false);

    // TODO: Darkvision bei Sichtweite berücksichtigen:
    //  Nachts 400 Einheiten, bei Dämmerung wie am hellichten Tag.
    public final boolean darkvision;

    PCSpecies(boolean darkvision) {
        this.darkvision = darkvision;
    }
}
