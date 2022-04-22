package chess.dao;

import chess.domain.ChessBoard;
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

	public int savePieces(long chessGameId, Map<Position, Piece> pieces) {
		String sql = "insert into piece (type, color, position_col, position_row, chess_game_id) values (?, ?, ?, ?, ?)";
		int count = 0;
		for (Position position : pieces.keySet()) {
			Piece piece = pieces.get(position);
			jdbcTemplate.update(sql, piece.name(), piece.color().name(), position.column(), position.row(), chessGameId);
			count++;
		}
		return count;
	}

	public ChessBoard findChessBoardByChessGameId(long chessGameId) {
		String sql = "select * from piece where chess_game_id = ?";
		return jdbcTemplate.query(sql, this::chessBoardRowMapper, chessGameId);
	}

	private ChessBoard chessBoardRowMapper(ResultSet resultSet) throws SQLException {
		Map<Position, Piece> pieces = new HashMap<>();
		while (resultSet.next()) {
			Long chessGameId = resultSet.getLong("chess_game_id");
			String type = resultSet.getString("type");
			String colorName = resultSet.getString("color");
			char column = resultSet.getString("position_col").charAt(0);
			char row = resultSet.getString("position_row").charAt(0);

			Position position = Position.of(column, row);
			Color color = Color.valueOf(colorName);
			pieces.put(position, PieceConverter.parseToPiece(type, chessGameId, color));
		}
		return new ChessBoard(pieces);
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
