package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.player.Player;
import chess.domain.position.Position;
import chess.dto.PieceDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePieces(final Player player, final int chessGameId) {
        final String sql = "insert into piece (position, name, team, chess_game_id) values (?, ?, ?, ?)";
        final List<Piece> pieces = player.findAll();
        for (Piece piece : pieces) {
            jdbcTemplate.update(sql, toPositionString(piece.getPosition()), String.valueOf(piece.getName()),
                    player.getTeamName(), chessGameId);
        }
    }

    private String toPositionString(final Position position) {
        final char file = position.getFile().getValue();
        final int rank = position.getRank().getValue();
        return String.valueOf(file) + rank;
    }

    public List<PieceDto> findAllPieceByIdAndTeam(final int chessGameId, final String team) {
        final String sql = "select * from piece where chess_game_id = (?) and team = (?)";
        return jdbcTemplate.query(sql,
                (resultSet, count) -> new PieceDto(
                        resultSet.getString("position"),
                        resultSet.getString("name")
                ), chessGameId, team);
    }

    public void deletePieces(final int chessGameId) {
        final String sql = "delete from piece where chess_game_id = (?)";
        jdbcTemplate.update(sql, chessGameId);
    }

    public void updatePiece(final int gameId, final String current, final String destination,
            final String currentTeam, final String opponentTeam) {
        deletePieceByGameIdAndPositionAndTeam(gameId, destination, opponentTeam);
        updatePiecePositionByGameId(gameId, current, destination, currentTeam);
    }

    public void deletePieceByGameIdAndPositionAndTeam(final int gameId, final String position, final String team) {
        final String sql = "delete from piece where chess_game_id = (?) and position = (?) and team = (?)";
        jdbcTemplate.update(sql, gameId, position, team);
    }

    public void updatePiecePositionByGameId(final int gameId, final String current, final String destination,
            final String team) {
        final String sql = "update piece set position = (?) where chess_game_id = (?) and position = (?) and team = (?)";
        jdbcTemplate.update(sql, destination, gameId, current, team);
    }
}
