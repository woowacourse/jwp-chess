package wooteco.chess.domain.gameinfo;

import java.util.List;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.board.Status;
import wooteco.chess.domain.exception.InvalidMovementException;
import wooteco.chess.domain.player.PlayerColor;
import wooteco.chess.domain.result.ChessResult;
import wooteco.chess.domain.result.Score;

public class GameInfo {

    private final Board board;
    private final Status status;
    private final ChessResult chessResult;

    private GameInfo(final Board board, final Status status) {
        this.board = board;
        this.status = status;
        this.chessResult = board.calculateResult();
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

    public List<Position> searchPath(String source) {
        for (Position p : board.searchPath(source)) {
            //System.out.println(p.getName() + " ");
        }
        return board.searchPath(source);
    }

    public Status getStatus() {
        return status;
    }

    public Score getWhiteScore() {
        return chessResult.getScore(PlayerColor.WHITE);
    }

    public Score getBlackScore() {
        return chessResult.getScore(PlayerColor.BLACK);
    }

    public Board getBoard() {
        return board;
    }
}
