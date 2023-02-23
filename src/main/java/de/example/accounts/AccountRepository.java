package de.example.accounts;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.quarkus.elytron.security.common.BcryptUtil.bcryptHash;

@ApplicationScoped
public class AccountRepository {

    public List<Account> listAll() {
        List<AccountEntity> listAll = AccountEntity.listAll();
        return listAll.stream().map(AccountEntity::asAccount).toList();
    }

    public Account get(Long id) {
        Optional<AccountEntity> optionalAccount = AccountEntity.findByIdOptional(id);
        AccountEntity accountEntity = optionalAccount.orElseThrow(NotFoundException::new);
        return accountEntity.asAccount();
    }

    @Transactional
    public Long create(String firstname, String lastname, String email, LocalDate birthday, String password) {
        AccountEntity accountEntity = AccountEntity.with(firstname, lastname, email, birthday, bcryptHash(password));
        accountEntity.persist();
        return accountEntity.id;
    }

    @Transactional
    public void update(Long id, Account update) {
        Optional<AccountEntity> optionalAccount = AccountEntity.findByIdOptional(id);
        AccountEntity existingAccount = optionalAccount.orElseThrow(NotFoundException::new);

        existingAccount.firstname = update.firstname;
        existingAccount.lastname = update.lastname;
        existingAccount.email = update.email;
        existingAccount.birthday = update.birthday;
        existingAccount.password = bcryptHash(update.password);

        existingAccount.persist();
    }
}

