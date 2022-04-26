package chess.domain.board;

import chess.dao.Movement;
import chess.domain.position.Position;

public final class ChessGame {

    private String id;
    private String name;
    private boolean isEnd;
    private final ChessBoard chessBoard;

    private ChessGame(final String gameName, final BoardGenerator boardGenerator) {
        this.chessBoard = new ChessBoard(boardGenerator);
        this.name = gameName;
    }

    private ChessGame(final BoardGenerator boardGenerator) {
        this.chessBoard = new ChessBoard(boardGenerator);
    }

    public static ChessGame fromName(String gameName) {
        return new ChessGame(gameName, new ChessBoardGenerator());
    }

    public static ChessGame fromId(final String gameId) {
        ChessGame chessGame = new ChessGame(new ChessBoardGenerator());
        chessGame.id = gameId;
        return chessGame;
    }

    public static ChessGame createChessGame() {
        return new ChessGame(new ChessBoardGenerator());
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
}
