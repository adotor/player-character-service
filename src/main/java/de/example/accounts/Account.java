package de.example.accounts;

import java.time.LocalDate;
import java.util.Objects;

public class Account {
    public Long id;
    public String firstname;
    public String lastname;
    public String email;
    public LocalDate birthday;
    public String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id) && firstname.equals(account.firstname) && lastname.equals(account.lastname) && email.equals(account.email) && birthday.equals(account.birthday) && password.equals(account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, birthday, password);
    }
}
