package de.example.pc;

import java.time.LocalDate;
import java.util.Objects;

public class PlayerCharacter {
    public Long id;
    public String name;
    public Integer level;
    public LocalDate creationDate;
    public PCClass pcClass;

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
}
