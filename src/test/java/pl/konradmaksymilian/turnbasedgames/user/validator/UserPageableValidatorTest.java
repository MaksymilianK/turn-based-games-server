package pl.konradmaksymilian.turnbasedgames.user.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import pl.konradmaksymilian.turnbasedgames.user.validator.UserPageableValidator;

public class UserPageableValidatorTest {

	private UserPageableValidator userPageableValidator;
		
	@BeforeEach
	public void setUp() {
		userPageableValidator = new UserPageableValidator();
	}
	
	@Test
	public void isValid_givenCorrectSortedPageable_returnsTrue() {
		assertThat(userPageableValidator.isValid(PageRequest.of(5, 100, Sort.by("nick")), null)).isTrue();
	}
	
	@Test
	public void isValid_givenCorrectUnsortedPageable_returnsTrue() {
		assertThat(userPageableValidator.isValid(PageRequest.of(5, 100), null)).isTrue();
	}
	
	@Test
	public void isValid_givenNull_returnsTrue() {
		assertThat(userPageableValidator.isValid(null, null)).isTrue();
	}
	
	@Test
	public void isValid_givenTooBigSize_returnsFalse() {
		assertThat(userPageableValidator.isValid(PageRequest.of(5, 101, Sort.by("nick")), null)).isFalse();
	}
	
	@Test
	public void isValid_givenSortByEmail_returnsFalse() {
		assertThat(userPageableValidator.isValid(PageRequest.of(5, 100, Sort.by("email")), null)).isFalse();
	}
	
	@Test
	public void isValid_givenSortByPassword_returnsFalse() {
		assertThat(userPageableValidator.isValid(PageRequest.of(5, 100, Sort.by("password")), null)).isFalse();
	}
}
