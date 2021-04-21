package chess.repository.piece;

import chess.domain.piece.Piece;
import java.sql.PreparedStatement;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class JdbcPieceDao implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPieceDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public long insert(long roomId, Piece piece) {
        String sql = "INSERT INTO pieces (roomid, signature, team, location) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update((con) -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, roomId);
            ps.setString(2, String.valueOf(piece.getSignature()));
            ps.setString(3, piece.getTeam().getValue());
            ps.setString(4, String.valueOf(piece.getX()) + String.valueOf(piece.getY()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(Piece piece) {
        String sql = "UPDATE pieces SET location = ? WHERE id = ?";
        this.jdbcTemplate.update(
                sql,
                String.valueOf(piece.getX()) + String.valueOf(piece.getY()),
                piece.getId());
    }

    @Override
    public Piece findPieceById(long pieceId) {
        String sql = "SELECT * FROM pieces WHERE id = ?";
        return this.jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) -> Piece.generatePiece(
                        resultSet.getLong("id"),
                        resultSet.getLong("roomid"),
                        resultSet.getString("signature").charAt(0),
                        resultSet.getString("team"),
                        resultSet.getString("location")
                ),
                pieceId
        );
    }

    @Override
    public void deletePieceById(long id) {
        String sql = "DELETE FROM pieces WHERE id = ?";
        this.jdbcTemplate.update(sql, Long.valueOf(id));
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM pieces";
        this.jdbcTemplate.update(sql);
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM pieces";
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public List<Piece> findPiecesByRoomId(long roomId) {
        String sql = "SELECT * FROM pieces WHERE roomid = ?";
        return this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> Piece.generatePiece(
                        resultSet.getLong("id"),
                        resultSet.getLong("roomid"),
                        resultSet.getString("signature").charAt(0),
                        resultSet.getString("team"),
                        resultSet.getString("location")
                ),
                roomId
        );
    }
}
