package de.example.pc;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class PlayerCharacterEntityTest {

    @Test
    void itIsInitialized() {

        PlayerCharacterEntity sut = PlayerCharacterEntity.with(aName(),
                aLevel(),
                aDate(),
                aClass());

        // Discussion: Test vs. Spec (Single Assert)
        assertThat(sut.name, is(aName()));
        assertThat(sut.level, is(aLevel()));
        assertThat(sut.creationDate, is(aDate()));
        assertThat(sut.pcClass, is(aClass()));
    }

    @Test
    void asAccount() {
        PlayerCharacterEntity sut = new PlayerCharacterEntity();
        sut.id = anID();
        sut.name = aName();
        sut.level = aLevel();
        sut.creationDate = aDate();
        sut.pcClass = aClass();

        PlayerCharacter playerCharacter = sut.asPlayerCharacter();

        // Discussion: Test vs. Spec (Single Assert)
        assertThat(playerCharacter.id, is(anID()));
        assertThat(playerCharacter.name, is(aName()));
        assertThat(playerCharacter.level, is(aLevel()));
        assertThat(playerCharacter.creationDate, is(aDate()));
        assertThat(playerCharacter.pcClass, is(aClass()));
    }

    private static long anID() {
        return 42L;
    }

    private static PCClass aClass() {
        return PCClass.FIGHTER;
    }

    private static LocalDate aDate() {
        return LocalDate.parse("2022-01-22");
    }

    private static Integer aLevel() {
        return 100;
    }

    private static String aName() {
        return "Orgrim";
    }
}