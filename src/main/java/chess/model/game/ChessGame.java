package chess.model.game;

import chess.model.Color;
import chess.model.board.MoveResult;
import chess.model.board.Square;
import chess.model.gamestatus.Playing;
import chess.model.gamestatus.Status;
import chess.model.piece.Piece;
import java.util.Map;

public class ChessGame {
    private Color turn;
    private Status status;

    public ChessGame() {
        init();
    }

    public ChessGame(final Color turn, final Status status) {
        this.turn = turn;
        this.status = status;
    }

    public void init() {
        this.status = Playing.init();
        this.turn = Color.WHITE;
    }

    public MoveResult move(final Square from, final Square to) {
        MoveResult moveResult = status.move(from, to, turn);
        turn = turn.changeToOpposite();
        if (moveResult.isKingAttacked()) {
            status = status.end();
        }
        return moveResult;
    }

    public boolean isEnd() {
        return status.isEnd();
    }

    public GameResult getResult() {
        return status.getResult();
    }

    public void end() {
        status = status.end();
    }

    public Map<Square, Piece> getBoard() {
        return status.getBoard();
    }

    public Color getTurn() {
        return turn;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isPlaying() {
        return status.isPlaying();
    }
}
