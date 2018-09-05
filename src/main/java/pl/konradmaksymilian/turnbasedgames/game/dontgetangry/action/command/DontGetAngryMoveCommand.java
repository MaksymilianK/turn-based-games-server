package pl.konradmaksymilian.turnbasedgames.game.dontgetangry.action.command;

import com.fasterxml.jackson.annotation.JsonProperty;

import pl.konradmaksymilian.turnbasedgames.game.core.action.command.NotHostGameCommand;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.CommonGameActionName;

public final class DontGetAngryMoveCommand extends NotHostGameCommand {

	private final int token;
	
	public DontGetAngryMoveCommand(@JsonProperty("token") int token) {
		this.token = token;
	}
	
	public int getToken() {
		return token;
	}
	
	@Override
	public int getCode() {
		return CommonGameActionName.MOVE.code();
	}
}
