package wooteco.chess.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import wooteco.chess.domain.Game;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;

public interface ChessService {
    Map<Integer, Map<Side, Player>> addGame(Player white, Player black) throws SQLException;

    Game findGameById(int id) throws SQLException;

    Board findBoardById(int id) throws SQLException;

    Board resetGameById(int id) throws SQLException;

    boolean finishGameById(int id) throws SQLException;

    double getScoreById(int id, Side side) throws SQLException;

    Map<Integer, Map<Side, Player>> getPlayerContexts() throws SQLException;

    Map<Integer, Map<Side, Double>> getScoreContexts() throws SQLException;

    Map<Side, Double> getScoresById(int id) throws SQLException;

    boolean addMoveByGameId(int id, String start, String end) throws SQLException;

    List<String> findAllAvailablePath(int id, String start) throws SQLException;

    boolean isWhiteTurn(int id) throws SQLException;

    boolean isGameOver(int id) throws SQLException;
}
