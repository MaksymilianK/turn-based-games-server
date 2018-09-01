package pl.konradmaksymilian.turnbasedgames.user.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pl.konradmaksymilian.turnbasedgames.user.Role;
import pl.konradmaksymilian.turnbasedgames.user.entity.User;

@Repository
public class LoggedUserManager {

	private final Map<Integer, User> loggedUsers = new HashMap<>();
	
	public Optional<User> find(int id) {
		return Optional.ofNullable(loggedUsers.get(id));
	}
	
	public Optional<User> findByNick(String nick) {
		var entrySetStream = loggedUsers.entrySet().stream();
		if (nick.startsWith("guest")) {
			entrySetStream = entrySetStream.filter(entry -> entry.getKey() < 0);
					
		} else {
			entrySetStream = entrySetStream.filter(entry -> entry.getKey() >= 0);
		}
		return entrySetStream
				.filter(entry -> entry.getValue().getNick().equals(nick))
				.map(entry -> entry.getValue())
				.findAny();
	}
	
	public synchronized void add(User user) {
		if (loggedUsers.containsKey(user.getId())) {
			throw new LoggedUserManagerException("Cannot store the user: " + user.toString() + "; the user is already "
					+ "stored as logged in");
			
		} else {
			if (user.getRole().equals(Role.GUEST)) {
				user.setId(generateGuestId());
				user.setNick("guest" + (-user.getId()));
			}
			loggedUsers.put(user.getId(), user);
		}
	}
	
	public synchronized void remove(int userId) {
		if (loggedUsers.containsKey(userId)) {
			loggedUsers.remove(userId);
		} else {
			throw new LoggedUserManagerException("Cannot remove the user with the id: " + userId + "; the user is not "
					+ "stored as logged in");
		}
	}
	
	private int generateGuestId() {
		int id = 0;
		do {
			id--;
		} while (loggedUsers.containsKey(id));
		
		return id;
	}
}
