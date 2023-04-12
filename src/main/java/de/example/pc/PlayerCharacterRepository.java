package de.example.pc;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PlayerCharacterRepository {

    public List<PlayerCharacter> listAll() {
        List<PlayerCharacterEntity> listAll = PlayerCharacterEntity.listAll();
        return listAll.stream().map(PlayerCharacterEntity::asPlayerCharacter).toList();
    }

    public PlayerCharacter get(Long id) {
        Optional<PlayerCharacterEntity> optionalAccount = PlayerCharacterEntity.findByIdOptional(id);
        PlayerCharacterEntity playerCharacterEntity = optionalAccount.orElseThrow(NotFoundException::new);
        return playerCharacterEntity.asPlayerCharacter();
    }

    @Transactional
    public Long create(String name,  Integer level, LocalDate creationDate, PCClass pcClass) {
        PlayerCharacterEntity playerCharacterEntity = PlayerCharacterEntity.with(name, level, creationDate, pcClass);
        playerCharacterEntity.persist();
        return playerCharacterEntity.id;
    }

    @Transactional
    public void update(Long id, PlayerCharacter update) {
        Optional<PlayerCharacterEntity> optionalAccount = PlayerCharacterEntity.findByIdOptional(id);
        PlayerCharacterEntity existingAccount = optionalAccount.orElseThrow(NotFoundException::new);

        existingAccount.name = update.name;
        existingAccount.level = update.level;
        existingAccount.creationDate = update.creationDate;
        existingAccount.pcClass = update.pcClass;

        existingAccount.persist();
    }
}

