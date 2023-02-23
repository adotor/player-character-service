package de.example.accounts.validation;

import de.example.accounts.AccountResource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordComparator implements
        ConstraintValidator<EqualPasswords, AccountResource.NewAccount> {

    @Override
    public void initialize(EqualPasswords password) {
    }

    @Override
    public boolean isValid(AccountResource.NewAccount account,
                           ConstraintValidatorContext cxt) {
        return account != null
                && account.password.equals(account.passwordRepetition);
    }

}