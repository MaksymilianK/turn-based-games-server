package pl.konradmaksymilian.turnbasedgames.player.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SecurityTokenValidatorTest {
	
	private SecurityTokenValidator validator;
	
	@BeforeEach
	public void setUp() {
		validator = new SecurityTokenValidator();
	}
	
	@Test
	public void isValid_givenCharsBetween33And125_returnsTrue() {		
		assertThat(validator.isValid("a!cDeFghij12345}7", null)).isTrue();
	}
	
	@Test
	public void isValid_givenNull_returnsTrue() {		
		assertThat(validator.isValid(null, null)).isTrue();
	}
	
	@Test
	public void isValid_givenSpace_returnsFalse() {
		assertThat(validator.isValid("a cDeFghij12345}", null)).isFalse();
	}
	
	@Test
	public void isValid_givenTilde_returnsFalse() {
		assertThat(validator.isValid("acDeFghij12345~7", null)).isFalse();
	}
	
	@Test
	public void isValid_givenTooShortToken_returnsFalse() {
		assertThat(validator.isValid("a!cDeFghij12345}", null)).isFalse();
	}
	
	@Test
	public void isValid_givenTooLongToken_returnsFalse() {
		assertThat(validator.isValid("a!cDeFghij12345}78", null)).isFalse();
	}
}