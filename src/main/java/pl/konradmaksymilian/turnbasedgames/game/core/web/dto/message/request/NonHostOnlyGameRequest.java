package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request;

public abstract class NonHostOnlyGameRequest extends GameRequest {

	@Override
	public final boolean isHostOnly() {
		return false;
	}
}
