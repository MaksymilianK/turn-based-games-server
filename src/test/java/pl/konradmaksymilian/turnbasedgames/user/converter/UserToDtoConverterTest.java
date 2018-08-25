package pl.konradmaksymilian.turnbasedgames.user.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import pl.konradmaksymilian.turnbasedgames.user.UserTestUtils;
import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.converter.UserToDtoConverter;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

public class UserToDtoConverterTest {
	
	private UserToDtoConverter userToDtoConverter;
	
	@BeforeEach
	public void setUp() {
		userToDtoConverter = new UserToDtoConverter();
	}
	
	@Test
	public void convert_returnsResponse() {
		var user = UserTestUtils.mockUser(6, Role.MODERATOR);
		
		UserResponseDto response = userToDtoConverter.convert(user);
		
		assertThat(response.getId()).isEqualTo(6);
		assertThat(response.getNick()).isEqualTo("nick6");
		assertThat(response.getRole()).isEqualTo(Role.MODERATOR);
	}
	
	@Test
	public void convertPage_returnsPageResponseDto() {
		var user = UserTestUtils.mockUser(5, Role.PLAYER);
		var users = new ArrayList<User>();
		for (int i = 0; i < 25; i++) {
			users.add(user);
		}
		var sort = Sort.by("isAccountNonLocked").and(Sort.by("nick").descending());
		var page = new PageImpl<User>(users, PageRequest.of(2, 25, sort), 100);
		
		var dto = userToDtoConverter.convertPage(page);
		
		assertThat(dto.getPage()).isEqualTo(2);
		assertThat(dto.getSize()).isEqualTo(25);
		assertThat(dto.getTotalElements()).isEqualTo(100);
		assertThat(dto.getSort()).hasSize(2);
		assertThat(dto.getSort()).containsEntry("isAccountNonLocked", Sort.Direction.ASC);
		assertThat(dto.getSort()).containsEntry("nick", Sort.Direction.DESC);
		assertThat(dto.getContent()).hasSize(25);
		assertThat(dto.getContent()).allMatch(userDto -> userDto.getId() == 5);
	}
}
