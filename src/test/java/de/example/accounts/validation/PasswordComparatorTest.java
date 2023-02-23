package de.example.accounts.validation;

import de.example.accounts.AccountResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
@QuarkusTest
class PasswordComparatorTest {
    @Test
    public void itMatchesTwoEqualPassword() {
        PasswordComparator sut = new PasswordComparator();

        boolean valid = sut.isValid(anAccountWith("Test123", "Test123"), null);

        assertThat(valid, is(true));
    }

    @Test
    public void itMatchesTwoEmptyPassword() {
        PasswordComparator sut = new PasswordComparator();

        boolean valid = sut.isValid(anAccountWith("", ""), null);

        assertThat(valid, is(true));
    }
    @Test
    public void itFailsOnTwoDifferentPassword() {
        PasswordComparator sut = new PasswordComparator();

        boolean valid = sut.isValid(anAccountWith("Test123", "Typo456"), null);

        assertThat(valid, is(false));
    }
    @Test
    public void itFailsOnEmptyPasswordRepetition() {
        PasswordComparator sut = new PasswordComparator();

        boolean valid = sut.isValid(anAccountWith("Test123", ""), null);

        assertThat(valid, is(false));
    }
    @Test
    public void itFailsOnNullPasswordRepetition() {
        PasswordComparator sut = new PasswordComparator();

        boolean valid = sut.isValid(anAccountWith("Test123", null), null);

        assertThat(valid, is(false));
    }
    private AccountResource.NewAccount anAccountWith(String password, String passwordRepetition) {
        AccountResource.NewAccount account = new AccountResource.NewAccount();
        account.firstname = "Chuck";
        account.lastname = "Norris";
        account.email = "chuck@norris.com";
        account.birthday = LocalDate.parse("1940-03-10");
        account.password = password;
        account.passwordRepetition = passwordRepetition;
        return account;
    }

}