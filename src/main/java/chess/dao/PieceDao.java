package chess.dao;

import chess.domain.Color;
import chess.domain.PieceConverter;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceRule;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDao {

	private final JdbcTemplate jdbcTemplate;

	public PieceDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int savePieces(Map<Position, Piece> pieces) {
		String sql = "insert into piece (type, color, position_col, position_row, chess_game_id) values (?, ?, ?, ?, ?)";
		int count = 0;
		for (Position position : pieces.keySet()) {
			Piece piece = pieces.get(position);
			jdbcTemplate.update(sql, piece.name(), piece.color().name(), position.column(), position.row(), 1);
			count++;
		}
		return count;
	}

	public Map<Position, Piece> findAllPieces() {
		String sql = "select * from piece";
		return jdbcTemplate.query(sql, this::piecesRowMapper);
	}

	private Map<Position, Piece> piecesRowMapper(ResultSet resultSet) throws SQLException {
		Map<Position, Piece> pieces = new HashMap<>();
		while (resultSet.next()) {
			String type = resultSet.getString("type");
			String colorName = resultSet.getString("color");
			char column = resultSet.getString("position_col").charAt(0);
			char row = resultSet.getString("position_row").charAt(0);

			Position position = Position.of(column, row);
			Color color = Color.valueOf(colorName);
			pieces.put(position, PieceConverter.parseToPiece(type, color));
		}
		return pieces;
	}

	public int updatePiecePosition(Position source, Position target) {
		String sql = "update piece set position_col = ?, position_row = ? "
				+ "where position_col = ? and position_row = ?";

		return jdbcTemplate.update(sql, target.column(), target.row(), source.column(), source.row());
	}

	public int updatePieceRule(Position source, PieceRule pieceRule) {
		String sql = "update piece set type = ? "
				+ "where position_col = ? and position_row = ?";

		return jdbcTemplate.update(sql, pieceRule.name(), source.column(), source.row());
	}

	public int delete(Position source) {
		String sql = "delete from piece where position_col = ? and position_row = ?";

		return jdbcTemplate.update(sql, source.column(), source.row());
	}
}
