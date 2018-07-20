package pl.konradmaksymilian.turnbasedgames.player.validator;

import java.util.function.Predicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

public class PlayerPageableValidator implements ConstraintValidator<PlayerPageable, Pageable> {
	
	@Override
	public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {
		if (pageable == null) {
			return true;
		} else {
			var sort = pageable.getSort();
			Predicate<Order> isSortedByNonPublicData = order -> order.getProperty().equalsIgnoreCase("email")
					|| order.getProperty().equalsIgnoreCase("password");
			
			return pageable.getPageSize() <= 100 && (sort.isUnsorted() || sort.stream().noneMatch(isSortedByNonPublicData));
		}
	}
}
