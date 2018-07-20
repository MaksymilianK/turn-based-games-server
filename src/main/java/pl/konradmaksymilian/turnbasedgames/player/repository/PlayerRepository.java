package pl.konradmaksymilian.turnbasedgames.player.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import pl.konradmaksymilian.turnbasedgames.player.entity.Player;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Integer> {

	Optional<Player> findByNick(String nick);
	
	Optional<Player> findByEmail(String email);
	
	@Query("select p from Player p where p.role between 2 and 5")
	Set<Player> findAdministration();

	boolean existsByNickIgnoreCase(String lowerCase);
	
	boolean existsByEmailIgnoreCase(String lowerCase);
}