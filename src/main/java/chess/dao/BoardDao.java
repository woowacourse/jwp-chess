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

    public void update(int gameId, Map<Position, Piece> board) {
        deletePieceByGameId(gameId);
        final String sql = "insert into piece (game_no, type, white, position) values (?, ?, ?, ?)";

        for (Entry<Position, Piece> entry : board.entrySet()) {
            savePiece(gameId, sql, entry);
        }
    }

    private void deletePieceByGameId(int gameId) {
        final String sql = "delete from piece where game_no = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public void update(Map<Position, Piece> board) {
//        final String sql = chooseSaveSql();
//
//        for (Entry<Position, Piece> entry : board.entrySet()) {
//            savePiece(sql, entry);
//        }
    }

    private void savePiece(int gameId, String sql, Entry<Position, Piece> entry) {
        Piece piece = entry.getValue();
        String type = piece.getType().toString();
        boolean isWhite = piece.isCamp(Camp.WHITE);
        String position = entry.getKey().toString();

        jdbcTemplate.update(sql, gameId, type, isWhite, position);
    }

    public List<PieceDto> load() {
        final String sql = "select type, white, position from piece";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> PieceDto.of(
                resultSet.getString("type"),
                resultSet.getBoolean("white"),
                resultSet.getString("position"))
        );
    }

    public List<PieceDto> loadById(int id) {
        final String sql = "select type, white, position from piece where game_no = ?";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> PieceDto.of(
                resultSet.getString("type"),
                resultSet.getBoolean("white"),
                resultSet.getString("position")
        ), id);
    }

    public void deleteAllByGameId(int gameId) {
        final String sql = "delete from piece where game_no = ?";

        jdbcTemplate.update(sql, gameId);
    }
}
