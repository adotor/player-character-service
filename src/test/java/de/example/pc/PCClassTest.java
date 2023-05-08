package de.example.pc;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class PCClassTest {
    @Test
    public void testClericsAreSpellcasters() {
        PCClass sut = PCClass.CLERIC;

        boolean spellcaster = sut.isSpellcaster;

        assertThat(spellcaster, is(true));
    }
}