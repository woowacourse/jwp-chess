package chess.domain.board;

import chess.domain.position.Movement;
import chess.domain.position.Position;

public final class ChessGame {

    private String id;
    private String name;
    private boolean isEnd;
    private String password;
    private ChessBoard chessBoard;

    public ChessGame(final String id, final String name, final String password, final boolean isEnd,
                     ChessBoard chessBoard) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.isEnd = isEnd;
        this.chessBoard = chessBoard;
    }

    public void execute(final Movement movement) {
        chessBoard.move(
                Position.of(movement.getSource()),
                Position.of(movement.getTarget())
        );
    }

    public boolean isKingDied() {
        return !chessBoard.checkKingExist();
    }

    public boolean isPasswordMatch(final String password) {
        return this.password.equals(password);
    }

    public boolean isEnd() {
        return isEnd;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }
}
