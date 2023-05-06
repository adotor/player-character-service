package de.example.pc;

import java.time.LocalDate;
import java.util.Objects;

public class PlayerCharacter {
    public Long id;
    private String name;
    public Integer level;
    public LocalDate creationDate;
    public PCClass pcClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInTrialPeriod() {
        return this.creationDate.isBefore(LocalDate.now().minusWeeks(2));
    }

    /**
     * Ein PC hat aktuell (2. Expansion) einen maximalen Level von 100.
     *
     * PC  aus der 1. Expansion haben ein  Maximum von 80.
     * Noch ältere PC aus dem Basisspiel haben Maximum 60.
     *
     * PC vom 5. Jahrestag kriegen nochmal 5 Level geschenkt.
     *
     * Weil bei einer neuen Expansion die PC immer neu importiert werden, lässt sich die
     * Expansion am creationDate erkennen. TODO: Enum für Expansion einbauen. Max Level als value?
     *
     * @return true, wenn PC maximalen Level hat.
     */
    public boolean isMaximumLevel() {
        return creationDate.isBefore(Dates.FIRST_EXPANSION_DATE) && level == 60
                || creationDate.isBefore(Dates.SECOND_EXPANSION_DATE) && level == 80
                || creationDate.isEqual(Dates.FIFTH_ANNIVERSARY_DATE) && level == 105
                || level == 100;
    }

    // Konnte ich mit der IDE generieren (Generate equals/hashCode). Spart Tipparbeit.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCharacter that = (PlayerCharacter) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(level, that.level) && Objects.equals(creationDate, that.creationDate) && pcClass == that.pcClass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, level, creationDate, pcClass);
    }

    private class Dates {
        private static final LocalDate FIRST_EXPANSION_DATE = LocalDate.of(2021, 4, 1);
        private static final LocalDate SECOND_EXPANSION_DATE = LocalDate.of(2022, 12, 24);
        private static final LocalDate FIFTH_ANNIVERSARY_DATE = LocalDate.of(2023, 3, 3);
    }
}
