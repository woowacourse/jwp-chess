package wooteco.chess.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import wooteco.chess.domain.Game;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;
import wooteco.chess.dto.GameResponseDto;

public interface ChessService {
    Map<String, GameResponseDto> addGame(String title, Player white, Player black) throws SQLException;

    Game findGameById(String id) throws SQLException;

    Board findBoardById(String id) throws SQLException;

    Game resetGameById(String id) throws SQLException;

    boolean finishGameById(String id) throws SQLException;

    double getScoreById(String id, Side side) throws SQLException;

    Map<String, Map<Side, Player>> getPlayerContexts() throws SQLException;

    Map<String, Map<Side, Double>> getScoreContexts() throws SQLException;

    Map<Side, Double> getScoresById(String id) throws SQLException;

    boolean moveIfMovable(String id, String start, String end) throws SQLException;

    List<String> findAllAvailablePath(String id, String start) throws SQLException;

    boolean isWhiteTurn(String id) throws SQLException;

    boolean isGameOver(String id) throws SQLException;

    Map<String, GameResponseDto> getBoards();
}
