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
    private static final Map<Integer, ChessBoard> CACHED_CHESS_BOARDS = new HashMap<>();

    private final UserService userService;
    private final RoomService roomService;
    private final ChessDAO chessDAO;

    public ChessService(UserService userService, RoomService roomService, ChessDAO chessDAO) {
        this.userService = userService;
        this.roomService = roomService;
        this.chessDAO = chessDAO;
    }

    public ChessBoard findChessBoard(int roomId) {
        return CACHED_CHESS_BOARDS.computeIfAbsent(roomId, key -> {
            ChessBoard chessBoard = new ChessBoard(ChessBoardGenerator.generateDefaultChessBoard());
            Histories histories = new Histories(chessDAO.findAllByRoomId(roomId));
            histories.restoreChessBoardAsLatest(chessBoard);
            CACHED_CHESS_BOARDS.put(roomId, chessBoard);
            return chessBoard;
        });
    }

    public void move(History history, int roomId, String password) {
        TeamType teamType = findCurrentTeamType(roomId);
        userService.validateUserTurn(roomId, password, teamType);
        ChessBoard chessBoard = findChessBoard(roomId);
        history.updateChessBoard(chessBoard);
        chessDAO.insertHistory(history, roomId);
    }

    public TeamType findCurrentTeamType(int roomId) {
        Histories histories = new Histories(chessDAO.findAllByRoomId(roomId));
        return histories.findNextTeamType();
    }

    public Result calculateResult(int roomId) {
        ChessBoard chessBoard = findChessBoard(roomId);
        Scores scores = chessBoard.calculateScores();
        TeamType winnerTeamType = chessBoard.findWinnerTeam();
        return new Result(scores, winnerTeamType);
    }

    public void deleteGame(int roomId, int userRoomId) {
        if (roomId != userRoomId || !roomService.isRoomExists(roomId)) {
            throw new IllegalStateException("방을 삭제할 수 없습니다.");
        }
        chessDAO.deleteAllByRoomId(roomId);
        userService.deleteAllByRoomId(roomId);
        roomService.deleteById(roomId);
        CACHED_CHESS_BOARDS.remove(roomId);
    }
}
