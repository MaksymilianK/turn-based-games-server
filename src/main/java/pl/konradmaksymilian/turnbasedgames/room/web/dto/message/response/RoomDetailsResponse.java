package pl.konradmaksymilian.turnbasedgames.room.web.dto.message.response;

import pl.konradmaksymilian.turnbasedgames.game.core.web.dto.GameDetailsDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.RoomStatusDto;
import pl.konradmaksymilian.turnbasedgames.room.web.dto.SharedRoomMessageName;

public class RoomDetailsResponse extends RoomResponse {
	
	private RoomStatusDto roomDto;
	private GameDetailsDto gameDto;
	
	/**
	 * @param time the message sending time (millis from epoch)
	 */
	public RoomDetailsResponse(long time, RoomStatusDto roomDto, GameDetailsDto gameDto) {
		super(time);
		this.roomDto = roomDto;
		this.gameDto = gameDto;
	}
	
	public RoomDetailsResponse(RoomStatusDto roomDto, GameDetailsDto gameDto) {
		this.roomDto = roomDto;
		this.gameDto = gameDto;
	}
	
	public RoomStatusDto getRoomStatusDto() {
		return roomDto;
	}
	
	public GameDetailsDto getGameStatusDto() {
		return gameDto;
	}
	
	@Override
	public int getCode() {
		return SharedRoomMessageName.ROOM_STATUS.code();
	}
}
