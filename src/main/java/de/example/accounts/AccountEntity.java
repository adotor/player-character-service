package de.example.accounts;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class AccountEntity extends PanacheEntity {
    public String firstname;
    public String lastname;
    public String email;
    public LocalDate birthday;

    public String password;

    public static AccountEntity with(String firstname, String lastname, String email, LocalDate birthday, String password) {
        AccountEntity entity = new AccountEntity();
        entity.firstname = firstname;
        entity.lastname = lastname;
        entity.email = email;
        entity.birthday = birthday;
        entity.password = password;
        return entity;
    }

    public Account asAccount() {
        Account account = new Account();
        account.id = this.id;
        account.firstname = this.firstname;
        account.lastname = this.lastname;
        account.email = this.email;
        account.birthday = this.birthday;
        account.password = this.password;
        return account;
    }
}
