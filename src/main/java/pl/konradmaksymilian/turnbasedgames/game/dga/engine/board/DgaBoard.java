package pl.konradmaksymilian.turnbasedgames.game.dga.engine.board;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pl.konradmaksymilian.turnbasedgames.core.exception.BadOperationException;
import pl.konradmaksymilian.turnbasedgames.game.core.business.engine.board.IdentifiablePlayerToken;

public final class DgaBoard {
	
	private int size;
	private final Map<Integer, Map<Integer, IdentifiablePlayerToken>> tokens = new HashMap<>();
	private final Map<Integer, Map<Integer, DgaField>> baseFields = new HashMap<>();
	private final Map<Integer, Map<Integer, DgaField>> finishFields = new HashMap<>();
	private final Map<Integer, DgaField> mainFields = new HashMap<>();
	
	public DgaBoard(int size) {
		this.size = size;
	}
	
	public void enterToken(int team, int tokenId) {
		var field = getBaseField(team, tokenId);
		if (!field.containsToken()) {
			new BadOperationException("Cannot enter a token; there is no token in a base field");
		}
		var startingField = getStartingField(team);
		throwExceptionIfFieldContainsTokenOfTeam(startingField, team);
		
		moveToken(field, startingField);
	}
	
	public void move(int team, int tokenId, int by) {
		var tokenToMove = getToken(team, tokenId);
		int fieldId = tokenToMove.getFieldId();
		if (fieldId < size) {
			moveTokenFromMainField(getMainField(fieldId), fieldId + by);
		} else {
			moveTokenFromFinishField(team, fieldId, by);
			int finishFieldNumber = calculateFinishFieldNumber(fieldId);
			var field = getFinishField(team, finishFieldNumber);
			finishFieldNumber += by;
			if (finishFieldNumber > 3) {
				throw new BadOperationException("Cannot move a token; the finishField does not exist");
			}
			
			var newField = getFinishField(team, finishFieldNumber);
			if (newField.containsToken()) {
				throw new BadOperationException("Cannot move a token; there is another token on the finish field");
			}
			
			moveToken(field, newField);
		}
	}
	
	public boolean areAllTokensOnFinishFields(int team) {
		return finishFields.get(team).values().stream()
				.allMatch(field -> field.containsToken());
	}
	
	public Map<Integer, Map<Integer, IdentifiablePlayerToken>> initialize(Collection<Integer> teams) {
		clear();
		initializeFields(teams);
		initializeTokens();
		return tokens;
	}
	
	public int countFields() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void removeTeam(int team) {
		tokens.get(team).values().forEach(token -> {
			if (token.getFieldId() >= 0 && token.getFieldId() < size) {
				getMainField(token.getFieldId()).removeToken();
			}
		});
		
		tokens.remove(team);
		baseFields.remove(team);
		finishFields.remove(team);
	}
	
	public Map<Integer, Map<Integer, Integer>> getTokensPositions() {
		var tokensPositions = new HashMap<Integer, Map<Integer, Integer>>();
		this.tokens.forEach((team, tokens) -> {
			var teamTokens = new HashMap<Integer, Integer>();
			tokens.forEach((tokenId, token) -> teamTokens.put(tokenId, token.getFieldId()));
			tokensPositions.put(team, Collections.unmodifiableMap(teamTokens));
		});
		return Collections.unmodifiableMap(tokensPositions);
	}

	private void clear() {
		if (!tokens.isEmpty()) {
			tokens.clear();
		}
		
		if (!baseFields.isEmpty()) {
			baseFields.clear();
		}
		
		if (!finishFields.isEmpty()) {
			finishFields.clear();
		}
		
		if (!mainFields.isEmpty()) {
			mainFields.clear();
		}
	}
	
	private void moveTokenFromMainField(DgaField mainField, int newField) {
		int fieldId = mainField.getId();
		int team = mainField.getToken().get().getTeam();
		int lastField = getLastField(team);
		if (fieldId < lastField && newField > lastField) {
			moveTokenFromMainFieldToFinishField(mainField, newField, lastField);
		} else {
			moveTokenFromMainFieldToMainField(mainField, newField);
		}
	}

	private void moveTokenFromMainFieldToFinishField(DgaField mainField, int newField, int lastField) {
		newField -= (lastField + 1);
		if (newField > 3) {
			throw new BadOperationException("Cannot move a token; the finish field does not exist");
		}
		var newFinishField = getFinishField(mainField.getToken().get().getTeam(), newField);
		if (newFinishField.containsToken()) {
			throw new BadOperationException("Cannot move a token; there is another token on the finish field");
		}
		
		moveToken(mainField, newFinishField);
	}
	
	private void moveTokenFromMainFieldToMainField(DgaField mainField, int newField) {
		if (newField >= size) {
			newField -= size;
		}
		var newMainField = getMainField(newField);
		newMainField.getToken().ifPresent(token -> {
			if (token.getTeam() == mainField.getToken().get().getTeam()) {
				throw new BadOperationException("Cannot move a token; there is another token of the same team on"
						+ " the finish field");
			} else {
				returnTokenToBase(newMainField);
			}
		});
		
		moveToken(mainField, newMainField);
	}
	
	private void moveTokenFromFinishField(int team, int fieldId, int by) {
		int finishFieldNumber = calculateFinishFieldNumber(fieldId);
		var currentField = getFinishField(team, finishFieldNumber);
		var newField = finishFieldNumber + by;
		if (newField > 3) {
			throw new BadOperationException("Cannot move a token; the finish field does not exist");
		}
		
		var newFinishField = getFinishField(team, newField);
		if (newFinishField.containsToken()) {
			throw new BadOperationException("Cannot move a token; there is another token on the finish field");
		}
		
		moveToken(currentField, newFinishField);
	}

	private void returnTokenToBase(DgaField field) {
		var token = field.removeToken();
		var baseField = getBaseField(token.getTeam(), token.getId());
		baseField.addToken(token);
		token.setFieldId(field.getId());
	}
	
	private void throwExceptionIfFieldContainsTokenOfTeam(DgaField field, int team) {
		if (field.containsTokenOf(team)) {
			throw new BadOperationException("Cannot move a token; the new field is occupied by another token of the same"
					+ " team");
		}
	}
	
	private void moveToken(DgaField currentField, DgaField newField) {
		var token = currentField.removeToken();
		newField.addToken(token);
		token.setFieldId(newField.getId());
	}
	
	private IdentifiablePlayerToken getToken(int team, int id) {
		return tokens.get(team).get(id);
	}
	
	private int getLastField(int team) {
		if (team == 0) {
			return size - 1;
		} else {
			return (size / 4 * team) - 1;
		}
	}
	
	private void initializeFields(Collection<Integer> teams) {
		teams.forEach(team -> {
			 var baseFields = new HashMap<Integer, DgaField>();
			 var finishFields = new HashMap<Integer, DgaField>();
			 for (int i = 0; i < 4; i++) {
				 var field = new DgaField(-i - 1, DgaFieldType.BASE);
				 baseFields.put(i, field);
				 finishFields.put(i, new DgaField(size + i, DgaFieldType.FINISH));
			 }
			this.baseFields.put(team, Collections.unmodifiableMap(baseFields));
			this.finishFields.put(team, Collections.unmodifiableMap(finishFields));
		});
		
		for (int i = 0; i < size; i++) {
			mainFields.put(i, new DgaField(i, DgaFieldType.MAIN));
		}
	}
	
	private void initializeTokens() {
		baseFields.forEach((Integer team, Map<Integer, DgaField> fields) -> {
			var playerTokens = new HashMap<Integer, IdentifiablePlayerToken>();
			fields.forEach((Integer fieldNumber, DgaField field) -> {
				var token = new IdentifiablePlayerToken(team, field.getId(), fieldNumber);
				playerTokens.put(token.getId(), token);
				field.addToken(token);
			});
			tokens.put(team, Collections.unmodifiableMap(playerTokens));
		});
	}
	
	private DgaField getStartingField(int team) {
		return getMainField(size / 4 * team);
	}
	
	private DgaField getBaseField(int team, int number) {
		return baseFields.get(team).get(number);
	}
	
	private DgaField getMainField(int id) {
		return mainFields.get(id);
	}
	
	private DgaField getFinishField(int team, int number) {
		return finishFields.get(team).get(number);
	}
	
	private int calculateFinishFieldNumber(int id) {
		return id - size - 1;
	}
}
