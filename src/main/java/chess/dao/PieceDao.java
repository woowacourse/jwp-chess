package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.player.Player;
import chess.domain.position.Position;
import chess.dto.PieceDto;
import chess.exception.ExecuteQueryException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PieceDao {

    private JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePieces(final Player player, final int chessGameId) {
        final String sql = "insert into piece (position, name, team, chess_game_id) values (?, ?, ?, ?)";
        try {
            final List<Piece> pieces = player.findAll();
            for (Piece piece : pieces) {
                jdbcTemplate.update(sql, toPositionString(piece.getPosition()), String.valueOf(piece.getName()),
                        player.getTeamName(), chessGameId);
            }
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("체스말 저장에 실패했습니다.");
        }
    }

    private String toPositionString(final Position position) {
        final char file = position.getFile().getValue();
        final int rank = position.getRank().getValue();
        return String.valueOf(file) + rank;
    }

    public List<PieceDto> findAllPieceByIdAndTeam(final int chessGameId, final String team) {
        final String sql = "select * from piece where chess_game_id = (?) and team = (?)";
        try {
            return jdbcTemplate.query(sql,
                    (resultSet, count) -> new PieceDto(
                            resultSet.getString("position"),
                            resultSet.getString("name")
                    ), chessGameId, team);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("체스말 불러오기가 실패했습니다.");
        }
    }

    public void deletePieces(final int chessGameId) {
        final String sql = "delete from piece where chess_game_id = (?)";
        try {
            jdbcTemplate.update(sql, chessGameId);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("체스말을 삭제할 수 없습니다.");
        }
    }

    public void deletePieceByGameIdAndPositionAndTeam(final int gameId, final String position, final String team) {
        final String sql = "delete from piece where chess_game_id = (?) and position = (?) and team = (?)";
        try {
            jdbcTemplate.update(sql, gameId, position, team);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("해당 위치의 체스말을 삭제할 수 없습니다.");
        }
    }

    public void updatePiecePositionByGameId(final int gameId, final String current,
                                            final String destination, final String team) {
        final String sql = "update piece set position = (?) where chess_game_id = (?) and position = (?) and team = (?)";
        try {
            jdbcTemplate.update(sql, destination, gameId, current, team);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("체스말 위치 업데이트에 실패했습니다.");
        }
    }
}
