package pl.konradmaksymilian.turnbasedgames.player;

import pl.konradmaksymilian.turnbasedgames.player.dto.PlayerResponseDto;
import pl.konradmaksymilian.turnbasedgames.player.entity.Player;

public class PlayerTestUtils {
	
	public static Player mockPlayer(int id, Role role) {
		var player = new Player("nick" + id, "email" + id, "password" + id, role);
		player.setId(id);
		
		return player;
	}
	
	public static PlayerResponseDto mockPlayerResponseDto(int id, Role role) {
		return new PlayerResponseDto(id, "nick" + id, role);
	}
	
	public static PlayerResponseDto mockPlayerResponseDto(Player player) {
		return new PlayerResponseDto(player.getId(), player.getNick(), player.getRole());
	}
}
