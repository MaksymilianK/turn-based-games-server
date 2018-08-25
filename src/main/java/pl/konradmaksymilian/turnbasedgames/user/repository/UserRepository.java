package pl.konradmaksymilian.turnbasedgames.user.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import pl.konradmaksymilian.turnbasedgames.user.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	Optional<User> findByNick(String nick);
	
	Optional<User> findByEmail(String email);
	
	@Query("select u from User u where u.role between 2 and 5")
	Set<User> findAdministration();

	boolean existsByNickIgnoreCase(String lowerCase);
	
	boolean existsByEmailIgnoreCase(String lowerCase);
}