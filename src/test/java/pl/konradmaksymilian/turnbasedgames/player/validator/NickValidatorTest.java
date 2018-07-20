package pl.konradmaksymilian.turnbasedgames.player.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NickValidatorTest {
	
	private NickValidator nickValidator;
	
	@BeforeEach
	public void setUp() {
		nickValidator = new NickValidator();
	}
	
	@Test
	public void isValid_givenCorrectNick_returnsTrue() {
		assertThat(nickValidator.isValid("correctNick", null)).isTrue();
	}
	
	@Test
	public void isValid_givenNull_returnsTrue() {
		assertThat(nickValidator.isValid(null, null)).isTrue();
	}
	
	@Test
	public void isValid_givenNonAlphabeticNick_returnsFalse() {
		assertThat(nickValidator.isValid("incorre_ct", null)).isFalse();
	}
	
	@Test
	public void isValid_givenTooShortNick_returnsFalse() {
		assertThat(nickValidator.isValid("a", null)).isFalse();
	}
	
	@Test
	public void isValid_givenTooLongNick_returnsFalse() {
		assertThat(nickValidator.isValid("tooLongSuperNick", null)).isFalse();
	}
	
	@Test
	public void isValid_givenNickWithGuestPrefix_returnsFalse() {
		assertThat(nickValidator.isValid("gUeStSuperPlayer", null)).isFalse();
	}
	
	@Test
	public void isValid_givenNickWithHeadAdminPrefix_returnsFalse() {
		assertThat(nickValidator.isValid("heAdadmiNSuperPlayer", null)).isFalse();
	}
	
	@Test
	public void isValid_givenNickWithAdminPrefix_returnsFalse() {
		assertThat(nickValidator.isValid("admiNSuperPlayer", null)).isFalse();
	}
	
	@Test
	public void isValid_givenNickWithModeratorPrefix_returnsFalse() {
		assertThat(nickValidator.isValid("moDeRAtorSuperPlayer", null)).isFalse();
	}
	
	@Test
	public void isValid_givenNickWithHelperPrefix_returnsFalse() {
		assertThat(nickValidator.isValid("HelpErSuperPlayer", null)).isFalse();
	}
}
