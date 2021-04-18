package chess.domain.chess;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.position.MovePosition;
import chess.web.dto.PieceDto;
import java.util.List;

public class Chess {

    private static final String ERROR_GAME_IS_NOT_RUNNING = "게임 진행 중이 아닙니다.";

    private final Board board;
    private final Status status;
    private final Color turn;

    public Chess(Board board) {
        this(board, Status.RUNNING, Color.WHITE);
    }

    public Chess(Board board, Status status, Color turn) {
        this.board = board;
        this.status = status;
        this.turn = turn;
    }

    public static Chess createWithEmptyBoard() {
        return new Chess(BoardFactory.EmptyBoard.create(), Status.STOP, Color.WHITE);
    }

    public static Chess of(List<PieceDto> pieceDtos, String status, String turn) {
        Board board = Board.from(pieceDtos);
        return new Chess(board, Status.valueOf(status), Color.valueOf(turn));
    }

    public Board getBoard() {
        return board;
    }

    public double score(Color color) {
        return board.score(color);
    }

    public Color winner() {
        return turn.next();
    }

    public Chess start() {
        return new Chess(BoardFactory.InitializedBoard.create(), Status.RUNNING, Color.WHITE);
    }

    public Chess move(MovePosition movePosition) {
        if (!status.isRunning()) {
            throw new IllegalStateException(ERROR_GAME_IS_NOT_RUNNING);
        }

        Status status = board.move(movePosition, turn);
        return new Chess(board, status, turn.next());
    }

    public Chess end() {
        return new Chess(board, Status.STOP, turn);
    }

    public Chess exit() {
        return new Chess(board, Status.TERMINATED, turn);
    }

    public boolean isTerminated() {
        return status.isTerminated();
    }

    public boolean isKindDead() {
        return status.isKingDead();
    }

    public boolean isRunning() {
        return status.isRunning();
    }

    public boolean isStop() {
        return status.isStop();
    }

    public String status() {
        return status.name();
    }

    public String color() {
        return turn.name();
    }
}
