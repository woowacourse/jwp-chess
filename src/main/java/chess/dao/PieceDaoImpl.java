package chess.dao;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDaoImpl implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PieceDto> pieceDtoRowMapper = (resultSet, rowNum) ->
            new PieceDto(
                    resultSet.getString("position"),
                    resultSet.getString("team"),
                    resultSet.getString("name")
            );

    @Override
    public void saveAllPieces(final Map<Position, Piece> board) {
        final String sql = "insert into piece (position, team, name) values (?, ?, ?)";
        for (Position position : board.keySet()) {
            final Piece piece = board.get(position);
            jdbcTemplate.update(sql, position.toString(), piece.getTeam(), piece.getName());
        }
    }

    @Override
    public List<PieceDto> findAllPieces() {
        final String sql = "select position, team, name from piece";
        return jdbcTemplate.query(sql, pieceDtoRowMapper);
    }

    @Override
    public void removePieceByPosition(final String position) {
        final String sql = "delete from piece where position = (?)";
        jdbcTemplate.update(sql, position);
    }

    @Override
    public void savePiece(final String position, final Piece piece) {
        final String sql = "insert into piece (position, team, name) values (?, ?, ?)";
        jdbcTemplate.update(sql, position, piece.getTeam(), piece.getName());
    }

    @Override
    public void removeAllPieces() {
        final String sql = "delete from piece";
        jdbcTemplate.update(sql);
    }
}
