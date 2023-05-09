package de.example.random;

public class FakeRandomizer implements RandomNumberProvider {
    @Override
    public int randomize() {
        return 42;
    }
}
