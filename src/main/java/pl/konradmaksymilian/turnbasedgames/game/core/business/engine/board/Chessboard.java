package pl.konradmaksymilian.turnbasedgames.game.core.business.engine.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Chessboard<T extends SimplePlayerToken> {

	protected final Map<Integer, T> whiteTokens;
	protected final Map<Integer, T> blackTokens;
	protected final Map<Integer, Map<Integer, ChessboardField<T>>> fields;
	
	public Chessboard() {
		fields = createFields();
		whiteTokens = initializeWhiteTokens();
		blackTokens = initializeBlackTokens();
	}
	
	public final Map<Integer, Map<Integer, ChessboardField<T>>> createFields() {
		var fields = new HashMap<Integer, Map<Integer, ChessboardField<T>>>();
		for (int i = 0; i < 8; i++) {
			var fieldsColumn = new HashMap<Integer, ChessboardField<T>>(); 
			for (int j = 0; j < 8; j++) {
				fieldsColumn.put(j, new ChessboardField<>(i, j));
			}
			fields.put(i, Collections.unmodifiableMap(fieldsColumn));
		}
		return Collections.unmodifiableMap(fields);
	}
	
	public abstract Map<Integer, T> initializeWhiteTokens();
	
	public abstract Map<Integer, T> initializeBlackTokens();
}
