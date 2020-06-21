/**
 * 
 */
package com.stylefeng.guns.rest.modular.auth.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author zhailiang
 *
 */
@Documented
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MyConstraintValidator.class})
public @interface MyConstraint {
	
	String message();

	String [] field() default { };

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
