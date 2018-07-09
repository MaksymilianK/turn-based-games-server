package pl.konradmaksymilian.turnbasedgames.player.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import pl.konradmaksymilian.turnbasedgames.player.entity.Player;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Integer> {

	Optional<Player> findByNick(String nick);
}