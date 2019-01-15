package pl.konradmaksymilian.turnbasedgames.game.core.business.engine.event;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import pl.konradmaksymilian.turnbasedgames.game.core.business.converter.GameEventToResponseConverter;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.GameEngineException;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.Resetable;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.GameEvent;
import pl.konradmaksymilian.turnbasedgames.game.core.data.event.PlayerGameEvent;
import pl.konradmaksymilian.turnbasedgames.room.business.RoomMessageSender;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response.RoomResponse;

public final class GameEventManager implements Resetable {
		
	private RoomMessageSender messageSender;
	private GameEventToResponseConverter eventConverter;
	private final List<GameEvent> events = new ArrayList<>();
	
	public GameEventManager(RoomMessageSender messageSender, GameEventToResponseConverter eventConverter) {
		this.messageSender = messageSender;
		this.eventConverter = eventConverter;
	}
	
	public List<GameEvent> getAll() {
		return Collections.unmodifiableList(events);
	}
	
	public void injectMessageSender(RoomMessageSender messageSender) {
		if (this.messageSender == null) {
			this.messageSender = messageSender;
		} else {
			throw new GameEngineException("The message sender has been already injected");
		}
	}
	
	public RoomResponse convert(GameEvent event) {
		return eventConverter.convert(event);
	}
	
	public void store(GameEvent event) {
		events.add(event);
	}
	
	public void publish(RoomResponse response) {
		messageSender.publish(response);
	}
	
	public void sendToUsers(RoomResponse response, String... users) {
		messageSender.sendToUsers(response, users);
	}
	
	public void storeAndPublish(GameEvent event) {
		store(event);
		publish(convert(event));
	}
	
	public void storeAndSendToUsers(GameEvent event, String... users) {
		store(event);
		sendToUsers(convert(event), users);
	}
	
	public Optional<GameEvent> find(int which) {
		if (which > 0 && which < events.size()) {
			return Optional.of(events.get(which));
		} else {
			return Optional.empty();
		}
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
	
	public Optional<PlayerGameEvent> findPlayerEvent(int which) {
		int i = 0;
		for (GameEvent event: events) {
			if (event instanceof PlayerGameEvent) {
				if (i == which) {
					return Optional.of((PlayerGameEvent) event);
				} else {
					i++;
				}
			}
		}
		return Optional.empty();
	}
	
	public Optional<PlayerGameEvent> findPlayerEventFromTail(int which) {
		int i = 0;
		for (int j = 0; j < events.size(); j++) {
			var event = findFromTail(j).get();
			if (event instanceof PlayerGameEvent) {
				if (i == which) {
					return Optional.of((PlayerGameEvent) event);
				} else {
					i++;
				}
			}
		}
		return Optional.empty();
	}
	
	public Optional<PlayerGameEvent> findLastPlayerEvent() {
		return findPlayerEventFromTail(0);
	}

	@Override
	public void reset() {
		
	}
	
	public Deque<GameEvent> getLastEvents(int howMany) {
		var lastEvents = new ArrayDeque<GameEvent>();
		for (int i = events.size() - 1; i >= 0; i--) {
			lastEvents.push(events.get(i));
		}
		return lastEvents;
	}
}
