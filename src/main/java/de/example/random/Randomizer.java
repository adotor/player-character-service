package de.example.random;

import java.util.random.RandomGenerator;

public class Randomizer implements RandomNumberProvider {
    public int randomize() {
        return RandomGenerator.getDefault().nextInt();
    }
}
