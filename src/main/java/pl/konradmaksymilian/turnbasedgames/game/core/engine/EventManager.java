package pl.konradmaksymilian.turnbasedgames.game.core.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.gameroom.service.RoomMessageSender;

public final class EventManager {
	
	private int roomId;
	
	private final List<GameEvent> events = new ArrayList<>();
	
	private final RoomMessageSender messageSender;
	
	public EventManager(int roomId, RoomMessageSender messageSender) {
		this.roomId = roomId;
		this.messageSender = messageSender;
	}
	
	public void publish(GameEvent event) {
		messageSender.sendGameEvent(roomId, event);
	}
	
	public void publish(GameEvent event, int... usersIds) {
		for (int userId : usersIds) {
			messageSender.sendGameEvent(userId, event);
		}
	}
	
	public void store(GameEvent event) {
		events.add(event);
	}
	
	public void publishAndStore(GameEvent event) {
		publish(event);
		store(event);
	}
	
	public void publishAndStore(GameEvent event, int... usersIds) {
		publish(event, usersIds);
		store(event);
	}
		
	public Optional<GameEvent> findFromTail(int which) {
		int index = events.size() - 1 - which;
		if (index < 0) {
			return Optional.of(events.get(index));
		} else {
			return Optional.empty();
		}
	}
	
	public Optional<GameEvent> findLast() {
		return findFromTail(0);
	}
	
	public Optional<GameEvent> find(int which) {
		if (which > 0 && which < events.size()) {
			return Optional.of(events.get(which));
		} else {
			return Optional.empty();
		}
	}
}
