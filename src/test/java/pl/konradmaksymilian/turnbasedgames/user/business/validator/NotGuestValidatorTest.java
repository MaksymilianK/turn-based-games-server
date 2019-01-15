package pl.konradmaksymilian.turnbasedgames.user.validator;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.validator.NotGuestValidator;

public class NotGuestValidatorTest {
	
	private NotGuestValidator validator;
	
	@BeforeEach
	public void setUp() {
		validator = new NotGuestValidator();
	}
	
	@Test
	public void isValid_givenPlayerRole_returnsTrue() {
		assertThat(validator.isValid(Role.PLAYER, null)).isTrue();
	}
	
	@Test
	public void isValid_givenModeratorRole_returnsTrue() {
		assertThat(validator.isValid(Role.MODERATOR, null)).isTrue();
	}
	
	@Test
	public void isValid_givenHeadAdminRole_returnsTrue() {
		assertThat(validator.isValid(Role.HEAD_ADMINISTRATOR, null)).isTrue();
	}
	
	@Test
	public void isValid_givenNull_returnsTrue() {
		assertThat(validator.isValid(null, null)).isTrue();
	}
	
	@Test
	public void isValid_givenGuestRole_returnsFalse() {
		assertThat(validator.isValid(Role.GUEST, null)).isFalse();
	}
}

