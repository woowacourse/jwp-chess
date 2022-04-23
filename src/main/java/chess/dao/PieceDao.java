package chess.dao;

import chess.domain.ChessBoard;
import chess.domain.Color;
import chess.domain.PieceConverter;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceRule;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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

		return jdbcTemplate.batchUpdate(sql,
				pieceSaveBatchSetter(chessGameId, new ArrayList<>(pieces.entrySet()))).length;
	}

	private BatchPreparedStatementSetter pieceSaveBatchSetter(long chessGameId,
															  List<Entry<Position, Piece>> pieceEntries) {
		return new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Entry<Position, Piece> pieceEntry = pieceEntries.get(i);
				Position position = pieceEntry.getKey();
				Piece piece = pieceEntry.getValue();

				ps.setString(1, piece.name());
				ps.setString(2, piece.color().name());
				ps.setString(3, String.valueOf(position.column()));
				ps.setString(4, String.valueOf(position.row()));
				ps.setLong(5, chessGameId);
			}

			@Override
			public int getBatchSize() {
				return pieceEntries.size();
			}
		};
	}

	public ChessBoard findChessBoardByChessGameId(long chessGameId) {
		String sql = "select * from piece where chess_game_id = ?";
		return jdbcTemplate.query(sql, this::chessBoardRowMapper, chessGameId);
	}

	private ChessBoard chessBoardRowMapper(ResultSet resultSet) throws SQLException {
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
		return new ChessBoard(pieces);
	}

	public int updatePiecePosition(long chessGameId, Position source, Position target) {
		String sql = "update piece set position_col = ?, position_row = ? "
				+ "where position_col = ? and position_row = ? and chess_game_id = ?";

		return jdbcTemplate.update(sql, target.column(), target.row(), source.column(), source.row(), chessGameId);
	}

	public int updatePieceRule(long chessGameId, Position source, PieceRule pieceRule) {
		String sql = "update piece set type = ? "
				+ "where position_col = ? and position_row = ? and chess_game_id = ?";

		return jdbcTemplate.update(sql, pieceRule.name(), source.column(), source.row(), chessGameId);
	}

	public int delete(long chessGameId, Position source) {
		String sql = "delete from piece where position_col = ? and position_row = ? and chess_game_id = ?";

		return jdbcTemplate.update(sql, source.column(), source.row(), chessGameId);
	}
}
