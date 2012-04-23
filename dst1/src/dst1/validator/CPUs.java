package dst1.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CPUsValidator.class)
@Documented
public @interface CPUs {
	
    String message() default "{dst1.constraints.CPUs}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
	int min();
	int max();
	
}
