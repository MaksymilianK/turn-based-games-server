package pl.konradmaksymilian.turnbasedgames.user.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.konradmaksymilian.turnbasedgames.user.dto.UserSecurityDataUpdateDto;
import pl.konradmaksymilian.turnbasedgames.user.validator.NotEmptyPlayerSecurityDataUpdateDtoValidator;

public class NotEmptyPlayerSecurityDataUpdateDtoValidatorTest {
	
	private NotEmptyPlayerSecurityDataUpdateDtoValidator validator;
	
	@BeforeEach
	public void setUp() {
		validator = new NotEmptyPlayerSecurityDataUpdateDtoValidator();
	}
	
	@Test
	public void isValid_givenFullDto_returnsTrue() {
		var dto = new UserSecurityDataUpdateDto();
		dto.setNewNick("nick");
		dto.setNewEmail("email");
		dto.setNewPassword("password");
		
		assertThat(validator.isValid(dto, null)).isTrue();
	}
	
	@Test
	public void isValid_givenOnlyNewEmail_returnsTrue() {
		var dto = new UserSecurityDataUpdateDto();
		dto.setNewEmail("email");
		
		assertThat(validator.isValid(dto, null)).isTrue();
	}
	
	@Test
	public void isValid_givenNull_returnsTrue() {
		assertThat(validator.isValid(null, null)).isTrue();
	}
	
	@Test
	public void isValid_givenEmptyDto_returnsFalse() {
		var dto = new UserSecurityDataUpdateDto();
		assertThat(validator.isValid(dto, null)).isFalse();
	}
}
