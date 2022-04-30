package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.player.Player;
import chess.domain.position.Position;
import chess.dto.PieceDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PieceDao {

    private JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAllPieces(final Player player, final long roomId) {
        final String SQL = "insert into piece (position, name, team, room_id) values (?, ?, ?, ?)";
        final List<Piece> pieces = player.findAll();
        for (Piece piece : pieces) {
            jdbcTemplate.update(SQL, toPositionString(piece.getPosition()), String.valueOf(piece.getName()),
                    player.getTeamName(), roomId);
        }
    }

    private String toPositionString(final Position position) {
        final char file = position.getFile().getValue();
        final int rank = position.getRank().getValue();
        return String.valueOf(file) + rank;
    }

    public List<PieceDto> findAllPieceByIdAndTeam(final long roomId, final String team) {
        final String SQL = "select * from piece where room_id = (?) and team = (?)";
        return jdbcTemplate.query(SQL,
                (resultSet, count) -> new PieceDto(
                        resultSet.getString("position"),
                        resultSet.getString("name")
                ), roomId, team);
    }

    public void deleteAllPiecesByRoomId(final long roomId) {
        final String SQL = "delete from piece where room_id = (?)";
        jdbcTemplate.update(SQL, roomId);
    }

    public void deletePieceByRoomIdAndPositionAndTeam(final long roomId, final String position, final String team) {
        final String SQL = "delete from piece where room_id = (?) and position = (?) and team = (?)";
        jdbcTemplate.update(SQL, roomId, position, team);
    }

    public void updatePiecePositionByRoomIdAndTeam(final long roomId, final String current,
                                                   final String destination, final String team) {
        final String SQL = "update piece set position = (?) where room_id = (?) and position = (?) and team = (?)";
        jdbcTemplate.update(SQL, destination, roomId, current, team);
    }
}
