package de.example.pc.validation;

import de.example.pc.PlayerCharacterResource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaximumLevelValidator implements
        ConstraintValidator<ValidLevel, PlayerCharacterResource.NewCharacter> {

    private final MaximumLevelChecker maximumLevelChecker = new MaximumLevelChecker();
    @Override
    public void initialize(ValidLevel constraint) {
    }

    @Override
    public boolean isValid(PlayerCharacterResource.NewCharacter character,
                           ConstraintValidatorContext cxt) {
        return character != null && maximumLevelChecker.check(character.pcClass, character.level);
    }

}