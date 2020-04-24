package wooteco.chess.dao;

import static wooteco.chess.dao.Connector.*;

import org.springframework.stereotype.Repository;

import wooteco.chess.domain.Turn;

@Repository("TurnDAO")
public class TurnDAO {
	private static final String SELECT_FROM_TURN = "SELECT * FROM turn";
	private static final String INSERT_INTO_TURN_VALUES = "INSERT INTO turn(isWhiteTurn) VALUES (?)";
	private static final String TRUNCATE_TURN = "TRUNCATE turn";
	private static final String UPDATE_TURN_SET_IS_WHITE_TURN = "UPDATE turn SET isWhiteTurn = ? WHERE id = 1";

	public Turn find() {
		return executeQuery(SELECT_FROM_TURN, new TurnRowMapper());
	}

	public void removeAll() {
		executeUpdate(TRUNCATE_TURN);
	}

	public void addTurn(boolean isWhiteTurn) {
		Connector.executeUpdate(INSERT_INTO_TURN_VALUES, isWhiteTurn);
	}

	public void changeTurn(boolean isWhiteTurn) {
		Connector.executeUpdate(UPDATE_TURN_SET_IS_WHITE_TURN, isWhiteTurn);
	}
}
