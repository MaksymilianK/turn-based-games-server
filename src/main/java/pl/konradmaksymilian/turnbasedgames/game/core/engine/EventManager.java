package pl.konradmaksymilian.turnbasedgames.game.core.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import pl.konradmaksymilian.turnbasedgames.game.core.action.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.action.event.MoveGameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.event.GameEventDto;
import pl.konradmaksymilian.turnbasedgames.game.core.dto.event.GameFinishEventDto;
import pl.konradmaksymilian.turnbasedgames.gameroom.service.RoomMessageSender;

public final class EventManager {
	
	private int roomId = -1;
	
	private final List<GameEvent> events = new ArrayList<>();
	
	private final List<GameEventDto> eventsDtos = new ArrayList<>();
		
	private final RoomMessageSender messageSender;
	
	public EventManager(RoomMessageSender messageSender) {
		this.messageSender = messageSender;
	}
	
	public void injectRoomId(int roomId) {
		if (this.roomId == -1) {
			this.roomId = roomId;
		} else {
			throw new EventManagerException("The room's ID has been already injected");
		}
	}
	
	public List<GameEvent> getAll() {
		return Collections.unmodifiableList(events);
	}
	
	public void publish(GameEventDto eventDto) {
		messageSender.sendGameEvent(roomId, eventDto);
		eventsDtos.add(eventDto);
	}
	
	public void publish(GameEventDto eventDto, int... usersIds) {
		for (int userId : usersIds) {
			messageSender.sendGameEvent(userId, eventDto);
		}
		eventsDtos.add(eventDto);
	}
	
	public void store(GameEvent event) {
		events.add(event);
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
	
	public Optional<MoveGameEvent> findMoveEventFromTail(int which) {
		int i = 0;
		while (i <= events.size()) {
			var event = findFromTail(i).get();
			if (event instanceof MoveGameEvent) {
				if (i == which) {
					return Optional.of((MoveGameEvent) event);
				}
			}
			i++;
		}
		return Optional.empty();
	}
	
	public Optional<MoveGameEvent> findLastMoveEvent() {
		return findMoveEventFromTail(0);
	}

	public void publishGameHistory() {
		messageSender.sendGameEvent(roomId, new GameFinishEventDto(findLast().get().getTime().toEpochMilli(), eventsDtos));
	}

	public void reset() {
		events.clear();
		eventsDtos.clear();
	}
}
