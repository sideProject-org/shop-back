package toy.shop.cmmn.valid.rate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RateValidator implements ConstraintValidator<RateValid, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return false;

        return value >= 1 && value <= 5 && (value * 10) % 5 == 0;
    }
}
