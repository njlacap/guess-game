package njslcp.project.guess_game.helper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValuesAllowedValidator implements ConstraintValidator<ValuesAllowed, String> {

    private List<String> expectedValues;
    private String message;

    @Override
    public void initialize(ValuesAllowed constraintAnnotation) {
        expectedValues = Arrays.asList(constraintAnnotation.values());
        message = constraintAnnotation.message().concat(expectedValues.toString());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = expectedValues.contains(s);
        if(!valid) {
            constraintValidatorContext
                    .disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }
        return valid;
    }
}
