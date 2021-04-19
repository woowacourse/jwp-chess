package chess.service.spring;

import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardGenerator;
import chess.domain.board.Coordinate;
import chess.domain.history.Histories;
import chess.domain.piece.TeamType;
import chess.domain.result.Result;
import chess.domain.result.Scores;
import chess.repository.spring.ChessDAO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChessService {
    private static Map<Integer, ChessBoard> CACHED_CHESS_BOARDS = new HashMap<>();

    private final ChessDAO chessDAO;

    public ChessService(ChessDAO chessDAO) {
        this.chessDAO = chessDAO;
    }

    public ChessBoard findChessBoardByRoomId(int id) {
        return CACHED_CHESS_BOARDS.computeIfAbsent(id, key -> {
            ChessBoard chessBoard = new ChessBoard(ChessBoardGenerator.generateDefaultChessBoard());
            Histories histories = new Histories(chessDAO.findAllHistoriesByRoomId(id));
            histories.restoreChessBoardAsLatest(chessBoard);
            CACHED_CHESS_BOARDS.put(id, chessBoard);
            return chessBoard;
        });
    }

    public TeamType findCurrentTeamTypeByRoomId(int id) {
        Histories histories = new Histories(chessDAO.findAllHistoriesByRoomId(id));
        return histories.findNextTeamType();
    }

    public void moveByRoomId(String current, String destination, String teamType, int id) {
        ChessBoard chessBoard = findChessBoardByRoomId(id);
        chessBoard.move(Coordinate.from(current), Coordinate.from(destination), TeamType.valueOf(teamType));
        chessDAO.insertHistoryByRoomId(current, destination, teamType, id);
    }

    public Result calculateResultByRoomId(int id) {
        ChessBoard chessBoard = findChessBoardByRoomId(id);
        Scores scores = chessBoard.calculateScores();
        TeamType winnerTeamType = chessBoard.findWinnerTeam();
        return new Result(scores, winnerTeamType);
    }

    public void deleteAllHistoriesByRoomId(int id) {
        chessDAO.deleteAllHistoriesByRoomId(id);
        CACHED_CHESS_BOARDS.remove(id);
    }
}
