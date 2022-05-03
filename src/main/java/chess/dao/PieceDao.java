package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Pieces;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createAllByGameId(List<Piece> pieces, long gameId) {
        final String sql = "insert into piece (name, color, position, game_id) values (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, pieces, pieces.size(),
            (statement, piece) -> {
                statement.setString(1, piece.getName());
                statement.setString(2, piece.getColor().getName());
                statement.setString(3, piece.getPosition().getPosition());
                statement.setLong(4, gameId);
            }
        );
    }

    public Pieces findAllByGameId(long gameId) {
        final String sql = "select name, color, position from piece where game_id = ?";

        List<Piece> pieces = jdbcTemplate.query(sql, (resultSet, rowNum) -> PieceFactory.of(
            resultSet.getString("name"),
            resultSet.getString("color"),
            resultSet.getString("position")
        ), gameId);

        return new Pieces(pieces);
    }

    public boolean exists(long gameId, String position) {
        final String sql = "select exists (select game_id from piece where game_id = ? and position = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, gameId, position);
    }

    public void deleteByGameIdAndPosition(long gameId, String position) {
        final String sql = "delete from piece where game_id = ? and position = ?";
        jdbcTemplate.update(sql, gameId, position);
    }

    public void updateByGameIdAndPosition(long gameId, String source, String target) {
        final String sql = "update piece set position = ? where game_id = ? and position = ? ";
        jdbcTemplate.update(sql, target, gameId, source);
    }

}
