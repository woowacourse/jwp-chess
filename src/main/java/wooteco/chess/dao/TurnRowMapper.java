package wooteco.chess.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import wooteco.chess.domain.Turn;

public class TurnRowMapper implements RowMapper<Turn> {
	@Override
	public Turn mapRow(ResultSet rs) throws SQLException {
		return mapTurn(rs);
	}

	private Turn mapTurn(ResultSet resultSet) throws SQLException {
		if (!resultSet.next()) {
			throw new NoSuchElementException("Turn이 없습니다.");
		}
		boolean isWhiteTurn = resultSet.getBoolean("isWhiteTurn");
		return new Turn(isWhiteTurn);
	}
}
