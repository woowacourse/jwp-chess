package chess.domain;

import chess.domain.board.Board;
import chess.domain.board.InitializedBoard;
import chess.domain.board.Point;
import chess.domain.board.Position;
import chess.domain.exceptions.WrongTurnException;
import chess.domain.password.BlackPassword;
import chess.domain.password.EmptyPassword;
import chess.domain.password.Password;
import chess.domain.password.WhitePassword;
import chess.domain.piece.PieceColor;

public class Game {
    private Board board;
    private Point point;
    private GameState gameState;
    private PieceColor turnColor;
    private Password whitePassword;
    private Password blackPassword;

    public static Game game(Board board, String pieceColor, String whitePw) {
        Point point = new Point(board);
        PieceColor turnColor = PieceColor.pieceColorByName(pieceColor);
        return new Game(board, point, GameState.START, turnColor, whitePw);
    }

    public static Game newGame(String whitePw) {
        InitializedBoard initializedBoard = new InitializedBoard();
        Board board = new Board(initializedBoard.board());

        return game(board, "W", whitePw);
    }

    private Game(Board board, Point point,
                 GameState gameState, PieceColor turnColor, String whitePw) {
        this.board = board;
        this.point = point;
        this.gameState = gameState;
        this.turnColor = turnColor;
        whitePassword = new WhitePassword(whitePw);
        blackPassword = new EmptyPassword();
    }

    public void init() {
        InitializedBoard initializedBoard = new InitializedBoard();
        this.board = new Board(initializedBoard.board());
        this.point = new Point(this.board);
        this.gameState = GameState.START;
        this.turnColor = PieceColor.WHITE;
    }

    public void end() {
        gameState = GameState.END;
    }

    public boolean isEnd() {
        return gameState.isEnd();
    }

    public boolean isNotStart() {
        return !gameState.isStart();
    }

    public void move(String source, String target, String password) {
        Position sourcePosition = Position.from(source);
        Position targetPosition = Position.from(target);

        if (turnColor == PieceColor.WHITE) {
            if (!whitePassword.isSame(password)) {
                throw new WrongTurnException();
            }
        }

        if (turnColor == PieceColor.BLACK) {
            if (!blackPassword.isSame(password)) {
                throw new WrongTurnException();
            }
        }

        board.move(sourcePosition, targetPosition, turnColor);
        if (board.kingIsDead()) {
            gameState = GameState.END;
        }

        this.turnColor = turnColor.oppositeColor();
    }

    public void setBlackPassword(String password) {
        this.blackPassword = new BlackPassword(password);
    }

    public boolean emptyBlackPlayer() {
        return blackPassword.isEmptyPassword();
    }

    public PieceColor turnColor() {
        return turnColor;
    }

    public double computeWhitePoint() {
        return point.whitePoint();
    }

    public double computeBlackPoint() {
        return point.blackPoint();
    }

    public Board getBoard() {
        return board;
    }

    public PieceColor winnerColor() {
        return board.winnerColor();
    }
}
