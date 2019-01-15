package pl.konradmaksymilian.turnbasedgames.game.core.web.dto.message.request;

public abstract class HostOnlyGameRequest extends GameRequest {

	@Override
	public final boolean isHostOnly() {
		return true;
	}
}
