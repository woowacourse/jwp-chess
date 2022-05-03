package chess.domain;

import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.Map;

public class ChessGame {
    private static final String ERROR_NOT_YOUR_TURN = "상대 진영의 차례입니다.";

    private Board board;
    private boolean whiteTurn;
    private boolean running;

    public ChessGame(Board board, boolean whiteTurn, boolean running) {
        this.board = board;
        this.whiteTurn = whiteTurn;
        this.running = running;
    }

    public static ChessGame create() {
        return new ChessGame(BoardInitializer.get(), true, true);
    }

    public static ChessGame load(Map<Position, Piece> board, boolean whiteTurn, boolean running) {
        return new ChessGame(new Board(board), whiteTurn, running);
    }

    public void move(Position sourcePosition, Position targetPosition) {
        if(this.board.isPieceWhiteIn(sourcePosition) != whiteTurn) {
            throw new IllegalArgumentException(ERROR_NOT_YOUR_TURN);
        }
        this.board.move(sourcePosition, targetPosition);
        whiteTurn = !whiteTurn;
    }

    public void end() {
        this.running = false;
    }

    public boolean isRunning() {
        return !this.board.hasKingCaptured() && this.running;
    }

    public Map<Position, Piece> getBoardSquares() {
        return this.board.getSquares();
    }

    public Map<Camp, Score> getScores() {
        return Score.of(this.board);
    }

    public Camp getWinner() {
        return Score.winnerOf(this.board);
    }

    public boolean isWhiteTurn() {
        return this.whiteTurn;
    }
}
