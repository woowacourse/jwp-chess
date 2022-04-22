package chess.dao;

import chess.domain.Position;
import chess.domain.piece.Piece;
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
}
