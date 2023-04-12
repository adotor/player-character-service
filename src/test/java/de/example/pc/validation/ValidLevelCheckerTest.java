package de.example.pc.validation;

import org.junit.jupiter.api.Test;

import static de.example.pc.PCClass.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ValidLevelCheckerTest {
    @Test
    public void testLevel1MageAccepted() {
        MaximumLevelChecker sut = new MaximumLevelChecker();

        boolean result = sut.check(WIZARD, 1);

        assertThat(result, is(true));
    }

    @Test
    public void testLevel60MageAccepted() {
        MaximumLevelChecker sut = new MaximumLevelChecker();

        boolean result = sut.check(WIZARD, 60);

        assertThat(result, is(true));
    }
    @Test
    public void testLevel61MageRejected() {
        MaximumLevelChecker sut = new MaximumLevelChecker();

        boolean result = sut.check(WIZARD, 61);

        assertThat(result, is(false));
    }

    @Test
    public void testLevel1FighterAccepted() {
        MaximumLevelChecker sut = new MaximumLevelChecker();

        boolean result = sut.check(FIGHTER, 1);

        assertThat(result, is(true));
    }
    @Test
    public void testLevel100FighterAccepted() {
        MaximumLevelChecker sut = new MaximumLevelChecker();

        boolean result = sut.check(WIZARD, 1);

        assertThat(result, is(true));
    }

    @Test
    public void testLevel101FighterRejected() {
        MaximumLevelChecker sut = new MaximumLevelChecker();

        boolean result = sut.check(WIZARD, 101);

        assertThat(result, is(false));
    }

//    @Test
    public void testLevel0CharactersRejected() {
        MaximumLevelChecker sut = new MaximumLevelChecker();

        boolean result = sut.check(CLERIC, 0);

        assertThat(result, is(false));
    }
//    @Test
    public void testInvalidClassIsRejected() {
        MaximumLevelChecker sut = new MaximumLevelChecker();

        boolean result = sut.check(null, 1);

        assertThat(result, is(false));
    }

}