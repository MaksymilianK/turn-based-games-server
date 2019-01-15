package pl.konradmaksymilian.turnbasedgames.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.UserTestUtils;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

public class LoggedUserManagerTest {

	private LoggedUserManager loggedUserManager;
	
	@BeforeEach
	public void setUp() {
		loggedUserManager = new LoggedUserManager();
	}
	
	@Test
	public void findByNick_givenNickStartingWithGuest_returnsStoredGuest() {
		var mockGuest = UserTestUtils.mockGuest();
		loggedUserManager.add(mockGuest);
		
		var userOptional = loggedUserManager.findByNick("guest1");
		
		assertThat(userOptional).isPresent();
		assertThat(userOptional.get()).isEqualTo(mockGuest);
	}
	
	@Test
	public void findByNick_givenNickNotStartingWithGuest_returnsStoredGuest() {
		var mockUser = UserTestUtils.mockUser(5);
		loggedUserManager.add(mockUser);
		
		var userOptional = loggedUserManager.findByNick("nick5");
		
		assertThat(userOptional).isPresent();
		assertThat(userOptional.get()).isEqualTo(mockUser);
	}
	
	@Test
	public void add_givenRegisteredUser_storesUser() {
		var mockUser = UserTestUtils.mockUser(6);
		loggedUserManager.add(mockUser);
		
		var userOptional = loggedUserManager.find(6);
		assertThat(userOptional).isPresent();
		assertThat(userOptional.get()).isEqualTo(mockUser);
	}
	
	@Test
	public void add_givenGuest_storesGuest() {
		var mockGuest = UserTestUtils.mockGuest();
		loggedUserManager.add(mockGuest);
		
		var userOptional = loggedUserManager.find(-1);

		assertThat(userOptional).isPresent();
		assertThat(userOptional.get()).isEqualTo(mockGuest);
	}
	
	@Test
	public void add_givenGuest_setsGuestId() {
		var mockGuest = UserTestUtils.mockGuest();
		loggedUserManager.add(UserTestUtils.mockGuest());
		loggedUserManager.add(UserTestUtils.mockGuest());
		loggedUserManager.add(UserTestUtils.mockGuest());
		loggedUserManager.add(UserTestUtils.mockGuest());
		loggedUserManager.remove(-3);
		loggedUserManager.remove(-4);
		loggedUserManager.add(mockGuest);
		
		var user = loggedUserManager.find(-3).get();

		assertThat(user.getId()).isEqualTo(-3);
		assertThat(user.getNick()).isEqualTo("guest3");
		assertThat(user).isEqualTo(mockGuest);
	}
	
	@Test
	public void add_givenAlreadyStoredUser_storesUser() {
		loggedUserManager.add(UserTestUtils.mockUser(6));
		
		assertThatExceptionOfType(LoggedUserManagerException.class)
				.isThrownBy(() -> loggedUserManager.add(UserTestUtils.mockUser(6)))
				.withMessage("Cannot store the user: nick6; the user is already stored as logged in");
	}
	
	@Test
	public void remove_givenStoredUserId_removesUser() {
		loggedUserManager.add(UserTestUtils.mockUser(6));
		
		loggedUserManager.remove(6);
		
		assertThat(loggedUserManager.find(6)).isNotPresent();
	}
	
	@Test
	public void remove_givenNonStoredUserId_throwsException() {				
		assertThatExceptionOfType(LoggedUserManagerException.class)
				.isThrownBy(() -> loggedUserManager.remove(6))
				.withMessage("Cannot remove the user with the id: 6; the user is not stored as logged in");
	}
}
