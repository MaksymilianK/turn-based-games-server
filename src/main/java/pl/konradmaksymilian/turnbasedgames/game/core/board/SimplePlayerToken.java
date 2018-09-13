package pl.konradmaksymilian.turnbasedgames.game.core.board;

public class SimplePlayerToken {

	public final int team;
	
	public int fieldId;

	public SimplePlayerToken(int team, int fieldId) {
		this.team = team;
		this.fieldId = fieldId;
	}

	public int getTeam() {
		return team;
	}
	
	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}
	
	public int getFieldId() {
		return fieldId;
	}
}
