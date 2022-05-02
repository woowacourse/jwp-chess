package chess.model.game;

import chess.model.Color;
import chess.model.board.Board;
import chess.model.board.ChessInitializer;
import chess.model.board.EmptyBoardInitializer;
import chess.model.board.MoveResult;
import chess.model.board.Square;
import chess.model.piece.Piece;
import java.util.Map;

public class ChessGame {
    private Color turn;
    private Status status;
    private Board board;

    public ChessGame(final Color turn, final Status status, final Board board) {
        this.turn = turn;
        this.status = status;
        this.board = board;
    }

    public static ChessGame getReadyInstance() {
        return new ChessGame(Color.WHITE, Status.READY, new Board(new EmptyBoardInitializer()));
    }

    public void start() {
        this.status = Status.PLAYING;
        this.turn = Color.WHITE;
        this.board = new Board(new ChessInitializer());
    }

    public MoveResult move(final Square from, final Square to) {
        if (!status.isPlaying()) {
            throw new IllegalArgumentException("체스 말을 움직일 수 없는 상태입니다.");
        }
        checkTurn(from);
        MoveResult moveResult = board.move(from, to);
        updateGame(moveResult);
        return moveResult;
    }

    private void checkTurn(Square from) {
        if (isInvalidTurn(board.findPieceBySquare(from))) {
            throw new IllegalArgumentException(
                    String.format("해당 기물을 움직일 권한이 없습니다. 현재 %s의 차례입니다.", turn.name()));
        }
    }

    private boolean isInvalidTurn(Piece pieceForMove) {
        return !pieceForMove.isSameColor(turn);
    }

    private void updateGame(MoveResult moveResult) {
        turn = turn.changeToOpposite();
        if (moveResult.isKingAttacked()) {
            status = Status.END;
        }
    }

    public void end() {
        if (!status.hasPlayed()) {
            throw new IllegalArgumentException("게임을 종료할 수 없는 상태입니다.");
        }
        status = Status.END;
    }

    public GameResult getResult() {
        if (!status.hasPlayed()) {
            throw new IllegalArgumentException("게임 결과를 조회할 수 없는 상태입니다.");
        }
        return board.getResult();
    }

    public boolean isPlaying() {
        return status.isPlaying();
    }

    public boolean isEnd() {
        return status.isEnd();
    }

    public Map<Square, Piece> getBoard() {
        return Map.copyOf(board.getValue());
    }

    public Color getTurn() {
        return turn;
    }

    public Status getStatus() {
        return status;
    }
}
