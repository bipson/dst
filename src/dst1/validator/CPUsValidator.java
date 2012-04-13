package dst1.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CPUsValidator implements ConstraintValidator<CPUs, Integer> {
	
	private int min;
	private int max;

	public void initialize(CPUs constraintAnnotation) {
		this.min = constraintAnnotation.min();
		this.max = constraintAnnotation.max();
	}
	
	public boolean isValid(Integer value, ConstraintValidatorContext constraintContext) {
	
		if (value == null)
			return true;
		
		if ((value < min) || (value > max)) {
			constraintContext.disableDefaultConstraintViolation();
			constraintContext.buildConstraintViolationWithTemplate(
					"{dst1.constraints.CPUs.message}"  ).addConstraintViolation();
			return false;
		}
	
		return true;
		
	}
}