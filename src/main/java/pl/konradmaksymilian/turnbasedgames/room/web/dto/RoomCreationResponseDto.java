package pl.konradmaksymilian.turnbasedgames.room.web.dto;

public class RoomCreationResponseDto {

	private final int roomId;
	
	public RoomCreationResponseDto(int roomId) {
		this.roomId = roomId;
	}
	
	public int getRoomId() {
		return roomId;
	}
}
