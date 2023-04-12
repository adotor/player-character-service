package de.example.pc;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;

@QuarkusTest
public class PlayerCharacterResourceTest {

    @Inject
    PlayerCharacterResource sut;

    @InjectMock
    PlayerCharacterRepository playerCharacterRepository;

    @Test
    public void itListsTheUsers() {
        List<PlayerCharacter> expectedPlayerCharacters = asList(anAccount(), anotherAccount());
        Mockito.when(playerCharacterRepository.listAll()).thenReturn(expectedPlayerCharacters);

        List<PlayerCharacter> playerCharacters = sut.listUsers();

        assertThat(playerCharacters, is(expectedPlayerCharacters));
        Mockito.verify(playerCharacterRepository).listAll();
    }

    @Test
    public void itGetsAUser() {
        Mockito.when(playerCharacterRepository.get(1L)).thenReturn(anAccount());

        PlayerCharacter playerCharacter = sut.listUsers(1L);

        assertThat(playerCharacter, is(anAccount()));
        Mockito.verify(playerCharacterRepository).get(1L);
    }

    @Test
    public void itReportsAMissingUser() {
        Mockito.when(playerCharacterRepository.get(123L)).thenThrow(new NotFoundException());

        assertThrows(NotFoundException.class, () -> sut.listUsers(123L));

        Mockito.verify(playerCharacterRepository).get(123L);
    }

    @Test
    public void itUpdatesAnUser() {
        Mockito.doNothing().when(playerCharacterRepository).update(eq(1L), isA(PlayerCharacter.class));

        sut.updateUser(1L, null, anAccount());

        Mockito.verify(playerCharacterRepository).update(eq(1L), eq(anAccount()));
    }

    @Test
    public void itCreatesAUser() {
        UriInfo uriInfoMock = mock(UriInfo.class);
        Mockito.when(uriInfoMock.getAbsolutePathBuilder())
                .thenReturn(UriBuilder.fromPath("test/users"));

        RestResponse restResponse = sut.createUser(uriInfoMock, aNewAccount());

        Mockito.verify(playerCharacterRepository).create("Orgrim Doomhammer",
                100,
                LocalDate.parse("2022-01-22"),
                PCClass.FIGHTER);
        assertThat(restResponse.getLocation().getRawPath(), is("test/users/0"));
    }

    private PlayerCharacter anAccount() {
        return anAccountWith(0L,
                "Orgrim Doomhammer",
                100,
                "2022-01-22",
                "FIGHTER");
    }

    private PlayerCharacter anotherAccount() {
        return anAccountWith(1L,
                "Jaina Proudmoore",
                101,
                "2023-01-23",
                "WIZARD");
    }

    private PlayerCharacter anAccountWith(Long id, String name, Integer level, String creationDate, String pcClass) {
        PlayerCharacter playerCharacter = new PlayerCharacter();
        playerCharacter.id = id;
        playerCharacter.name = name;
        playerCharacter.level = level;
        playerCharacter.creationDate = LocalDate.parse(creationDate);
        playerCharacter.pcClass = PCClass.valueOf(pcClass);
        return playerCharacter;
    }

    private PlayerCharacterResource.NewCharacter aNewAccount() {
        PlayerCharacterResource.NewCharacter account = new PlayerCharacterResource.NewCharacter();
        account.name = "Orgrim Doomhammer";
        account.level = 100;
        account.creationDate = LocalDate.parse("2022-01-22");
        account.pcClass = PCClass.FIGHTER;
        return account;
    }
}