package de.example.pc;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class PlayerCharacterEntity extends PanacheEntity {
    public String name;
    public Integer level;
    public LocalDate creationDate;

    public PCClass pcClass;

    public static PlayerCharacterEntity with(String name, Integer level, LocalDate creationDate, PCClass pcClass) {
        PlayerCharacterEntity entity = new PlayerCharacterEntity();
        entity.name = name;
        entity.level = level;
        entity.creationDate = creationDate;
        entity.pcClass = pcClass;
        return entity;
    }

    public PlayerCharacter asPlayerCharacter() {
        PlayerCharacter playerCharacter = new PlayerCharacter();
        playerCharacter.id = this.id;
        playerCharacter.name = this.name;
        playerCharacter.level = this.level;
        playerCharacter.creationDate = this.creationDate;
        playerCharacter.pcClass = this.pcClass;
        return playerCharacter;
    }
}
