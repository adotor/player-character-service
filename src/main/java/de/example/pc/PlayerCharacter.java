package de.example.pc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class PlayerCharacter {
    public Long id;
    public String name;
    public Integer level;
    public LocalDate creationDate;
    public PCClass pcClass;

    public PCSpecies pcSpecies;

    public boolean isInTrialPeriod() {
        return this.creationDate.isBefore(LocalDate.now().minusWeeks(2));
    }

    /**
     * Berechnet die Sichtweite je nach Tageszeit.
     *
     * Von 22:00 bis 4:59 sind es 200 Einheiten.
     * Von 20:00 bis 21:59 und von 5:00 sind es 800 Einheiten.
     *
     * Am restlichen Tag sind es 1600 Einheiten.
     *
     * @return Sichtweite in interner Masseinheit
     */
    public int getVisionRange() {
        LocalTime now = LocalTime.now();
        
        if (now.isAfter(LocalTime.of(21,59))
                && now.isBefore(LocalTime.of(5,0))) {
            return 200;
        }

        if (now.isAfter(LocalTime.of(19,59))
                && now.isBefore(LocalTime.of(22,0)) ||
                now.isAfter(LocalTime.of(4,59))
                        && now.isBefore(LocalTime.of(7,0))) {
            return 800;
        }

        return 1600;
    }
}
