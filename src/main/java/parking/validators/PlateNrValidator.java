package parking.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PlateNrValidator implements ConstraintValidator<ProperPlate, String> {

    @Override
    public void initialize(ProperPlate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String plateNr, ConstraintValidatorContext constraintValidatorContext) {
        StringBuilder stringBuilder = new StringBuilder();

        plateNr.codePoints()
                .filter(c -> Character.isAlphabetic(c) || Character.isDigit(c))
                .forEach(stringBuilder::appendCodePoint);

        return Objects.equals(plateNr, stringBuilder.toString());
    }
}