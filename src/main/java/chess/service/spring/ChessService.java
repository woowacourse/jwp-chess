package chess.service.spring;

import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardGenerator;
import chess.domain.history.Histories;
import chess.domain.history.History;
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

    private final UserService userService;
    private final RoomService roomService;
    private final ChessDAO chessDAO;

    public ChessService(UserService userService, RoomService roomService, ChessDAO chessDAO) {
        this.userService = userService;
        this.roomService = roomService;
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

    public void move(History history, int roomId, String password) {
        TeamType teamType = findCurrentTeamTypeByRoomId(roomId);
        userService.validateUserTurn(roomId, password, teamType);
        ChessBoard chessBoard = findChessBoardByRoomId(roomId);
        history.updateChessBoard(chessBoard);
        chessDAO.insertHistoryByRoomId(history, roomId);
    }

    public TeamType findCurrentTeamTypeByRoomId(int id) {
        Histories histories = new Histories(chessDAO.findAllHistoriesByRoomId(id));
        return histories.findNextTeamType();
    }

    public Result calculateResultByRoomId(int id) {
        ChessBoard chessBoard = findChessBoardByRoomId(id);
        Scores scores = chessBoard.calculateScores();
        TeamType winnerTeamType = chessBoard.findWinnerTeam();
        return new Result(scores, winnerTeamType);
    }

    public void deleteGame(int id) {
        chessDAO.deleteAllHistoriesByRoomId(id);
        userService.deleteAllUsersByRoomId(id);
        roomService.deleteRoomById(id);
        CACHED_CHESS_BOARDS.remove(id);
    }
}
