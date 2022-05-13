package chess.entity;

import chess.domain.ChessGame;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.Map;

public class GameEntity {
    private final Long id;
    private final String title;
    private final String password;
    private final boolean whiteTurn;
    private final boolean finished;

    public GameEntity(Long id, String title, String password, boolean whiteTurn, boolean finished) {
        this.id = id;
        this.title = title;
        this.password = password;
        this.whiteTurn = whiteTurn;
        this.finished = finished;
    }

    public GameEntity(String title, String password) {
        this(null, title, password, true, false);
    }

    public ChessGame toChessGame(Map<Position, Piece> board) {
        ChessGame chessGame = new ChessGame();
        if (finished) {
            chessGame.end();
            return chessGame;
        }
        chessGame.load(board, whiteTurn);
        return chessGame;
    }

    public boolean incorrectPassword(String password) {
        return !this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean isFinished() {
        return finished;
    }
}
