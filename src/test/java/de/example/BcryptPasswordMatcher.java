package de.example;

import io.quarkus.elytron.security.common.BcryptUtil;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


public class BcryptPasswordMatcher extends TypeSafeMatcher<String> {

    private final String password;

    public BcryptPasswordMatcher(String password) {
        this.password = password;
    }

    @Override
    protected boolean matchesSafely(String passwordHash) {
        return BcryptUtil.matches(password, passwordHash);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("hashed bcrypt password of " + password);
    }

    public static Matcher<String> bcryptHashOf(String password) {
        return new BcryptPasswordMatcher(password);
    }
}
