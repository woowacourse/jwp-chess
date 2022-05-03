package chess.model.dao;

import chess.entity.PieceEntity;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PieceDao {
    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static String getPieceName(Piece piece) {
        return (piece.getTeam().name() + "-" + piece.getName()).toLowerCase();
    }

    public void init(Board board, Long gameId) {
        String query = "insert into piece (position, name, game_id) values (?, ?, ?)";
        for (Map.Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            jdbcTemplate.update(query, entry.getKey().getPosition(), getPieceName(entry.getValue()), gameId);
        }
    }

    public List<PieceEntity> findAllPieces() {
        String sql = "select position, name from piece";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    PieceEntity pieceEntity = new PieceEntity(
                            resultSet.getString("name"),
                            resultSet.getString("position")
                    );
                    return pieceEntity;
                });
    }

    public List<PieceEntity> findAllByGameId(Long gameId) {
        String sql = "select position, name from piece where game_id = ?";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    PieceEntity pieceEntity = new PieceEntity(
                            resultSet.getString("name"),
                            resultSet.getString("position")
                    );
                    return pieceEntity;
                }, gameId);
    }

    public String findPieceNameByPositionAndGameId(String source, Long gameId) {
        String sql = "select name from piece where position = ? and game_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, source, gameId);
    }

    public void updateByPositionAndGameId(String position, String pieceName, Long gameId) {
        String sql = "UPDATE piece SET name = (?) WHERE position = (?) and game_id = ?";
        jdbcTemplate.update(sql, pieceName, position, gameId);
    }

    public void deleteAll() {
        String query = "DELETE FROM piece";
        jdbcTemplate.update(query);
    }

    public void deleteByGameId(Long id) {
        String sql = "DELETE FROM piece WHERE game_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
