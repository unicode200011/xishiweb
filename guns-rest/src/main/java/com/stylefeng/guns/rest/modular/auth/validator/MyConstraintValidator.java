/**
 * 
 */
package com.stylefeng.guns.rest.modular.auth.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {
	
	@Override
	public void initialize(MyConstraint constraintAnnotation) {
		System.out.println("验证器初始化...");
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		Map map = (Map) value;
		if (!map.containsKey("phone")
					|| null == map.get("phone")
					|| "".equals(map.get("phone"))) {
			return false;
		}

		return true;
	}

}
