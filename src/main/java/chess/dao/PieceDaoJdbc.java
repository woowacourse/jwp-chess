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
public class PieceDaoJdbc implements PieceDao {

    private final RowMapper<PieceDto> pieceRowMapper = (resultSet, rowNum) -> new PieceDto(
            resultSet.getString("position"),
            resultSet.getString("name")
    );

    private final JdbcTemplate jdbcTemplate;

    public PieceDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initializePieces(Player player) {
        final String sql = "insert into piece (position, team, name) values (?, ?, ?)";
        List<PieceDto> pieces = player.findAll()
                .stream()
                .map(PieceDto::from)
                .collect(Collectors.toUnmodifiableList());
        for (PieceDto piece : pieces) {
            jdbcTemplate.update(sql, piece.getPosition(), player.getTeamName(), piece.getName());
        }
    }

    @Override
    public List<PieceDto> findPiecesByTeam(final Team team) {
        final String sql = "select * from piece where piece.team = ?";
        return jdbcTemplate.query(sql, pieceRowMapper, team.getName());
    }

    @Override
    public void updatePiece(final MoveDto moveDto) {
        final String sql = "update piece set position = ? where position = ?";
        jdbcTemplate.update(sql, moveDto.getDestinationPosition(), moveDto.getCurrentPosition());
    }

    @Override
    public void removePieceByCaptured(final MoveDto moveDto) {
        final String sql = "delete from piece where position = ?";
        jdbcTemplate.update(sql, moveDto.getDestinationPosition());
    }

    @Override
    public void endPieces() {
        final String sql = "truncate table piece";
        jdbcTemplate.execute(sql);
    }
}
