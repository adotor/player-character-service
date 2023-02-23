package de.example.accounts;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class AccountEntityTest {

    @Test
    void itIsInitialized() {

        AccountEntity sut = AccountEntity.with(aFirstname(),
                aLastname(),
                anEmail(),
                aDate(),
                aPassword());

        // Discussion: Test vs. Spec (Single Assert)
        assertThat(sut.firstname, is(aFirstname()));
        assertThat(sut.lastname, is(aLastname()));
        assertThat(sut.email, is(anEmail()));
        assertThat(sut.birthday, is(aDate()));
        assertThat(sut.password, is(aPassword()));
    }

    @Test
    void asAccount() {
        AccountEntity sut = new AccountEntity();
        sut.id = anID();
        sut.firstname = aFirstname();
        sut.lastname = aLastname();
        sut.email = anEmail();
        sut.birthday = aDate();
        sut.password = aPassword();

        Account account = sut.asAccount();

        // Discussion: Test vs. Spec (Single Assert)
        assertThat(account.id, is(anID()));
        assertThat(account.firstname, is(aFirstname()));
        assertThat(account.lastname, is(aLastname()));
        assertThat(account.email, is(anEmail()));
        assertThat(account.birthday, is(aDate()));
        assertThat(account.password, is(aPassword()));
    }

    private static long anID() {
        return 42L;
    }

    private static String aPassword() {
        return "TopSecrets";
    }

    private static LocalDate aDate() {
        return LocalDate.parse("1940-03-10");
    }

    private static String anEmail() {
        return "cuck@norris.com";
    }

    private static String aLastname() {
        return "Norris";
    }

    private static String aFirstname() {
        return "Chuck";
    }
}