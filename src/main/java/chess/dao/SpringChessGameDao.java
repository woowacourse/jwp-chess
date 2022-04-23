package chess.dao;

import chess.domain.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.domain.position.Position;
import chess.dto.ChessGameUpdateDto;
import chess.dto.PieceDto;
import chess.exception.ExecuteQueryException;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpringChessGameDao {

    private JdbcTemplate jdbcTemplate;

    public SpringChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int findChessGameIdByName(final String gameName) {
        final String sql = "select id from chess_game where name = (?)";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, gameName);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("체스 게임 정보 불러오는데 실패했습니다.");
        }
    }

    public boolean isDuplicateGameName(final String gameName) {
        final String sql = "select id from chess_game where name = (?)";
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, gameName);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public void createNewChessGame(final ChessGame chessGame, final String gameName) {
        saveChessGame(gameName, chessGame.getTurn());
        final int chessGameId = findChessGameIdByName(gameName);
        savePieces(chessGame.getCurrentPlayer(), chessGameId);
        savePieces(chessGame.getOpponentPlayer(), chessGameId);
    }

    private void saveChessGame(final String gameName, final Team turn) {
        final String sql = "insert into chess_game (name, turn) values (?, ?)";
        try {
            jdbcTemplate.update(sql, gameName, turn.getName());
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("게임을 저장할 수 없습니다.");
        }
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

    public ChessGameUpdateDto findChessGame(final int chessGameId) {
        final String turn = findCurrentTurn(chessGameId);
        final List<PieceDto> whitePieces = findAllPieceByIdAndTeam(chessGameId, Team.WHITE.getName());
        final List<PieceDto> blackPieces = findAllPieceByIdAndTeam(chessGameId, Team.BLACK.getName());
        return new ChessGameUpdateDto(turn, whitePieces, blackPieces);
    }

    private String findCurrentTurn(final int chessGameId) {
        final String sql = "select turn from chess_game where id = (?)";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, chessGameId);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("현재 턴 정보 불러오기가 실패했습니다.");
        }
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

    public void deleteChessGame(final int chessGameId) {
        final String sql = "delete from chess_game where id = (?)";
        try {
            jdbcTemplate.update(sql, chessGameId);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("게임을 삭제할 수 없습니다.");
        }
    }

    public void updateGameTurn(final int gameId, final Team nextTurn) {
        final String sql = "update chess_game set turn = (?) where id = (?)";
        try {
            jdbcTemplate.update(sql, nextTurn.getName(), gameId);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("턴 정보 업데이트에 실패했습니다.");
        }
    }

    public void updatePiece(final int gameId, final String current, final String destination,
            final String currentTeam, final String opponentTeam) {
        deletePieceByGameIdAndPositionAndTeam(gameId, destination, opponentTeam);
        updatePiecePositionByGameId(gameId, current, destination, currentTeam);
    }

    private void deletePieceByGameIdAndPositionAndTeam(final int gameId, final String position, final String team) {
        final String sql = "delete from piece where chess_game_id = (?) and position = (?) and team = (?)";
        try {
            jdbcTemplate.update(sql, gameId, position, team);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("해당 위치의 체스말을 삭제할 수 없습니다.");
        }
    }

    private void updatePiecePositionByGameId(final int gameId, final String current, final String destination,
            final String team) {
        final String sql = "update piece set position = (?) where chess_game_id = (?) and position = (?) and team = (?)";
        try {
            jdbcTemplate.update(sql, destination, gameId, current, team);
        } catch (DataAccessException e) {
            throw new ExecuteQueryException("체스말 위치 업데이트에 실패했습니다.");
        }
    }
}
