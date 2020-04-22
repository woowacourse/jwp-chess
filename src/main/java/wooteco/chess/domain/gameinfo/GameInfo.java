package chess.domain.gameinfo;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Status;
import chess.domain.exception.InvalidMovementException;
import chess.domain.result.ChessResult;

public class GameInfo {

    private final Board board;
    private final Status status;
    private final ChessResult chessResult;

    private GameInfo(final Board board, final Status status) {
        this.board = board;
        this.status = status;
        this.chessResult = board.calculateResult();
    }

    public static GameInfo start() {
        return new GameInfo(BoardFactory.createInitialBoard(), Status.initialStatus());
    }

    public static GameInfo from(Board board, int turn) {
        return new GameInfo(board, Status.from(turn));
    }

    public GameInfo move(String source, String target) {
        validateStatus(source);
        if (board.isKing(target)) {
            return new GameInfo(board, status.finish());
        }
        Board board = this.board.move(source, target);
        return new GameInfo(board, status.nextTurn());
    }

    private void validateStatus(String source) {
        if (status.isNotProcessing()) {
            throw new UnsupportedOperationException("먼저 게임을 실행해야합니다.");
        }
        if (status.isWhiteTurn() && board.isBlack(source)) {
            throw new InvalidMovementException("해당 플레이어의 턴이 아닙니다.");
        }
        if (status.isBlackTurn() && board.isWhite(source)) {
            throw new InvalidMovementException("해당 플레이어의 턴이 아닙니다.");
        }
    }

    public String searchPath(String source) {
        return board.searchPath(source);
    }

    public int getTurn() {
        return status.getTurn();
    }

    public double getWhiteScore() {
        return chessResult.getWhiteScore()
                .getScore();
    }

    public double getBlackScore() {
        return chessResult.getBlackScore()
                .getScore();
    }

    public boolean isNotFinished() {
        return status.isNotFinished();
    }

    public Board getBoard() {
        return board;
    }

    public ChessResult getChessResult() {
        return chessResult;
    }
}
