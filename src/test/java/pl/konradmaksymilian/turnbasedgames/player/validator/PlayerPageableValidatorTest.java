package pl.konradmaksymilian.turnbasedgames.player.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PlayerPageableValidatorTest {

	private PlayerPageableValidator playerPageableValidator;
		
	@BeforeEach
	public void setUp() {
		playerPageableValidator = new PlayerPageableValidator();
	}
	
	@Test
	public void isValid_givenCorrectSortedPageable_returnsTrue() {
		assertThat(playerPageableValidator.isValid(PageRequest.of(5, 100, Sort.by("nick")), null)).isTrue();
	}
	
	@Test
	public void isValid_givenCorrectUnsortedPageable_returnsTrue() {
		assertThat(playerPageableValidator.isValid(PageRequest.of(5, 100), null)).isTrue();
	}
	
	@Test
	public void isValid_givenNull_returnsTrue() {
		assertThat(playerPageableValidator.isValid(null, null)).isTrue();
	}
	
	@Test
	public void isValid_givenTooBigSize_returnsFalse() {
		assertThat(playerPageableValidator.isValid(PageRequest.of(5, 101, Sort.by("nick")), null)).isFalse();
	}
	
	@Test
	public void isValid_givenSortByEmail_returnsFalse() {
		assertThat(playerPageableValidator.isValid(PageRequest.of(5, 100, Sort.by("email")), null)).isFalse();
	}
	
	@Test
	public void isValid_givenSortByPassword_returnsFalse() {
		assertThat(playerPageableValidator.isValid(PageRequest.of(5, 100, Sort.by("password")), null)).isFalse();
	}
}
