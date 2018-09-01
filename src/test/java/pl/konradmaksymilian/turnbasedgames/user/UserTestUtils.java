package pl.konradmaksymilian.turnbasedgames.user;

import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.dto.UserResponseDto;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

public class UserTestUtils {
	
	private UserTestUtils() {}
	
	public static User mockUser(int id, Role role) {
		var user = new User("nick" + id, "email" + id, "password" + id, role);
		user.setId(id);
		return user;
	}
	
	public static User mockUser(int id) {
		return mockUser(id, Role.PLAYER);
	}
	
	public static User mockGuest() {
		return mockGuest(-235);
	}
	
	public static User mockGuest(int id) {
		var user = new User("guest" + (-id), "", "", Role.GUEST);
		user.setId(id);
		return user;
	}
	
	public static UserResponseDto mockUserResponseDto(int id, Role role) {
		return new UserResponseDto(id, "nick" + id, role);
	}
	
	public static UserResponseDto mockUserResponseDto(User user) {
		return new UserResponseDto(user.getId(), user.getNick(), user.getRole());
	}
}
