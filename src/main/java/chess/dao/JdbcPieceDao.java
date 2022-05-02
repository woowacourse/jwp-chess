package chess.dao;

import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.dto.MoveDto;
import chess.dto.PieceDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcPieceDao implements PieceDao {

    private final RowMapper<PieceDto> pieceRowMapper = (resultSet, rowNum) -> {
        return new PieceDto(
                resultSet.getString("position"),
                resultSet.getString("name")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcPieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initializePieces(final long roomId, final Player player) {
        final String sql = "insert into piece (roomId, position, team, name) values (?, ?, ?, ?)";
        List<PieceDto> pieces = player.findAll()
                .stream()
                .map(PieceDto::from)
                .collect(Collectors.toUnmodifiableList());
        for (PieceDto piece : pieces) {
            jdbcTemplate.update(sql, roomId, piece.getPosition(), player.getTeamName(), piece.getName());
        }
    }

    @Override
    public List<PieceDto> findPiecesByTeam(final long roomId, final Team team) {
        final String sql = "select * from piece where roomId = ? and piece.team = ?";
        return jdbcTemplate.query(sql, pieceRowMapper, roomId, team.getName());
    }

    @Override
    public void updatePiece(final long roomId, final MoveDto moveDto) {
        final String sql = "update piece set position = ? where roomId = ? and position = ?";
        jdbcTemplate.update(sql, moveDto.getDestinationPosition(), roomId, moveDto.getCurrentPosition());
    }

    @Override
    public void removePieceByCaptured(final long roomId, final MoveDto moveDto) {
        final String sql = "delete from piece where roomId = ? and position = ?";
        jdbcTemplate.update(sql, roomId, moveDto.getDestinationPosition());
    }

    @Override
    public void endPieces(final long roomId) {
        final String sql = "delete from piece where roomId = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
