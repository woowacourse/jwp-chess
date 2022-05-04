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

    public ChessGame(Board board, boolean whiteTurn) {
        this.board = board;
        this.whiteTurn = whiteTurn;
    }

    public static ChessGame create() {
        return new ChessGame(BoardInitializer.get(), true);
    }

    public static ChessGame load(Map<Position, Piece> board, boolean whiteTurn) {
        return new ChessGame(new Board(board), whiteTurn);
    }

    public void move(Position sourcePosition, Position targetPosition) {
        if(this.board.isPieceWhiteIn(sourcePosition) != whiteTurn) {
            throw new IllegalArgumentException(ERROR_NOT_YOUR_TURN);
        }
        this.board.move(sourcePosition, targetPosition);
        whiteTurn = !whiteTurn;
    }

    public boolean isRunning() {
        return !this.board.hasKingCaptured();
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
