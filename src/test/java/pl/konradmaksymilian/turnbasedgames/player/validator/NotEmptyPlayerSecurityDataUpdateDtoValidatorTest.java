package pl.konradmaksymilian.turnbasedgames.player.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerSecurityDataUpdateDto;

public class NotEmptyPlayerSecurityDataUpdateDtoValidatorTest {
	
	private NotEmptyPlayerSecurityDataUpdateDtoValidator validator;
	
	@BeforeEach
	public void setUp() {
		validator = new NotEmptyPlayerSecurityDataUpdateDtoValidator();
	}
	
	@Test
	public void isValid_givenFullDto_returnsTrue() {
		var dto = new PlayerSecurityDataUpdateDto();
		dto.setNewNick("nick");
		dto.setNewEmail("email");
		dto.setNewPassword("password");
		
		assertThat(validator.isValid(dto, null)).isTrue();
	}
	
	@Test
	public void isValid_givenOnlyNewEmail_returnsTrue() {
		var dto = new PlayerSecurityDataUpdateDto();
		dto.setNewEmail("email");
		
		assertThat(validator.isValid(dto, null)).isTrue();
	}
	
	@Test
	public void isValid_givenNull_returnsTrue() {
		assertThat(validator.isValid(null, null)).isTrue();
	}
	
	@Test
	public void isValid_givenEmptyDto_returnsFalse() {
		var dto = new PlayerSecurityDataUpdateDto();
		assertThat(validator.isValid(dto, null)).isFalse();
	}
}
