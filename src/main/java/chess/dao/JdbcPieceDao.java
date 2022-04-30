package chess.dao;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcPieceDao implements PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PieceDto> pieceDtoRowMapper = (resultSet, rowNum) ->
            new PieceDto(
                    resultSet.getString("position"),
                    resultSet.getString("team"),
                    resultSet.getString("name")
            );

    @Override
    public void saveAllPieces(final int roomNumber, final Map<Position, Piece> board) {
        final String sql = "insert into piece (roomNumber, position, team, name) values (?, ?, ?, ?)";
        for (Position position : board.keySet()) {
            final Piece piece = board.get(position);
            jdbcTemplate.update(sql, roomNumber, position.toString(), piece.getTeam(), piece.getName());
        }
    }

    @Override
    public List<PieceDto> findAllPieces(final int roomNumber) {
        final String sql = "select position, team, name from piece where roomnumber = ?";
        return jdbcTemplate.query(sql, pieceDtoRowMapper, roomNumber);
    }

    @Override
    public void removePieceByPosition(final int roomNumber, final String position) {
        final String sql = "delete from piece where position = (?) and roomNumber = (?)";
        jdbcTemplate.update(sql, position, roomNumber);
    }

    @Override
    public void savePiece(final int roomNumber, final String position, final Piece piece) {
        final String sql = "insert into piece (roomnumber, position, team, name) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, roomNumber, position, piece.getTeam(), piece.getName());
    }

    @Override
    public void removeAllPieces(final int roomNumber) {
        final String sql = "delete from piece where roomNumber=?";
        jdbcTemplate.update(sql, roomNumber);
    }
}
