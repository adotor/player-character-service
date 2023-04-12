package de.example.pc.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaximumLevelValidator.class)
@Target( { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLevel {
    String message() default "Level too high.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

