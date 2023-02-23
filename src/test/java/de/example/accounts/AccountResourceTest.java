package de.example.accounts;

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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;

@QuarkusTest
public class AccountResourceTest {

    @Inject
    AccountResource sut;

    @InjectMock
    AccountRepository accountRepository;

    @Test
    public void itListsTheUsers() {
        List<Account> expectedAccounts = asList(anAccount(), anotherAccount());
        Mockito.when(accountRepository.listAll()).thenReturn(expectedAccounts);

        List<Account> accounts = sut.listUsers();

        assertThat(accounts, equalTo(expectedAccounts));
        Mockito.verify(accountRepository).listAll();
    }

    @Test
    public void itGetsAUser() {
        Mockito.when(accountRepository.get(1L)).thenReturn(anAccount());

        Account account = sut.listUsers(1L);

        assertThat(account, equalTo(anAccount()));
        Mockito.verify(accountRepository).get(1L);
    }

    @Test
    public void itReportsAMissingUser() {
        Mockito.when(accountRepository.get(123L)).thenThrow(new NotFoundException());

        assertThrows(NotFoundException.class, () -> sut.listUsers(123L));

        Mockito.verify(accountRepository).get(123L);
    }

    @Test
    public void itUpdatesAnUser() {
        Mockito.doNothing().when(accountRepository).update(eq(1L), isA(Account.class));

        sut.updateUser(1L, null, anAccount());

        Mockito.verify(accountRepository).update(eq(1L), eq(anAccount()));
    }

    @Test
    public void itCreatesAUser() {
        UriInfo uriInfoMock = mock(UriInfo.class);
        Mockito.when(uriInfoMock.getAbsolutePathBuilder())
                .thenReturn(UriBuilder.fromPath("test/users"));

        RestResponse restResponse = sut.createUser(uriInfoMock, aNewAccount());

        Mockito.verify(accountRepository).create("Chuck",
                "Norris",
                "chuck@norris.com",
                LocalDate.parse("1940-03-10"),
                "TopSecret");
        assertThat(restResponse.getLocation().getRawPath(), equalTo("test/users/0"));
    }

    private Account anAccount() {
        return anAccountWith(0L,
                "Chuck",
                "Norris",
                "chuck@norris.com",
                "1940-03-10",
                "TopSecret");
    }

    private Account anotherAccount() {
        return anAccountWith(1L,
                "Linda",
                "Hamilton",
                "linda@hamilton.com",
                "1956-09-26",
                "UltraViolet");
    }

    private Account anAccountWith(Long id, String firstname, String lastname, String email, String birthday, String password) {
        Account account = new Account();
        account.id = id;
        account.firstname = firstname;
        account.lastname = lastname;
        account.email = email;
        account.birthday = LocalDate.parse(birthday);
        account.password = password;
        return account;
    }

    private AccountResource.NewAccount aNewAccount() {
        AccountResource.NewAccount account = new AccountResource.NewAccount();
        account.firstname = "Chuck";
        account.lastname = "Norris";
        account.email = "chuck@norris.com";
        account.birthday = LocalDate.parse("1940-03-10");
        account.password = "TopSecret";
        account.passwordRepetition = "TopSecret";
        return account;
    }
}