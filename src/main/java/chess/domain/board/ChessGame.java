package chess.domain.board;

import chess.domain.position.Movement;
import chess.domain.position.Position;

public final class ChessGame {

    private String id;
    private String name;
    private boolean isEnd;
    private String password;
    private BoardGenerator boardGenerator;
    private ChessBoard chessBoard;

    private ChessGame(final BoardGenerator boardGenerator) {
        this.chessBoard = new ChessBoard(boardGenerator);
    }

    public ChessGame(final String id, final String name, final String password, final BoardGenerator boardGenerator) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.boardGenerator = boardGenerator;
    }

    public ChessGame(final String id, final String name, final String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.chessBoard = new ChessBoard(new ChessBoardGenerator());
    }

    public static ChessGame fromId(final String gameId) {
        ChessGame chessGame = new ChessGame(new ChessBoardGenerator());
        chessGame.id = gameId;
        return chessGame;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void execute(final Movement movement) {
        chessBoard.move(
                Position.of(movement.getSource()),
                Position.of(movement.getTarget())
        );
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public boolean isGameSet() {
        return !chessBoard.checkKingExist();
    }

    public boolean isMatched(final String password) {
        return this.password.equals(password);
    }
}
