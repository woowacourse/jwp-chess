package chess.dao;

import chess.domain.Camp;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDao {
    private final JdbcTemplate jdbcTemplate;

    public BoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(int gameId, Map<Position, Piece> board) {
        for (Entry<Position, Piece> entry : board.entrySet()) {
            savePiece(gameId, entry);
        }
    }

    private void savePiece(int gameId, Entry<Position, Piece> entry) {
        final String sql = "insert into piece (game_no, type, white, position) values (?, ?, ?, ?)";
        Piece piece = entry.getValue();
        String type = piece.getType().toString();
        boolean isWhite = piece.isCamp(Camp.WHITE);
        String position = entry.getKey().toString();

        jdbcTemplate.update(sql, gameId, type, isWhite, position);
    }

    public List<PieceDto> findAllByGameId(int gameId) {
        final String sql = "select type, white, position from piece where game_no = ?";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> PieceDto.of(
                resultSet.getString("type"),
                resultSet.getBoolean("white"),
                resultSet.getString("position")
        ), gameId);
    }

    public void deleteAllByGameId(int gameId) {
        final String sql = "delete from piece where game_no = ?";

        jdbcTemplate.update(sql, gameId);
    }
}
