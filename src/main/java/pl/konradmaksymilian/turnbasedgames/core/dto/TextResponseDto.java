package pl.konradmaksymilian.turnbasedgames.core.dto;

import org.springframework.http.HttpStatus;

public class TextResponseDto {
	
	private String description;
	
	private TextResponseDto(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public static TextResponseDto with(String description) {
		return new TextResponseDto(description);
	}
}
