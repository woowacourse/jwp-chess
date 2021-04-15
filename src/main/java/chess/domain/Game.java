package chess.domain;

import chess.domain.board.Board;
import chess.domain.board.InitializedBoard;
import chess.domain.board.Point;
import chess.domain.board.Position;
import chess.domain.piece.PieceColor;

public class Game {

    private Board board;
    private Point point;
    private GameState gameState;
    private PieceColor turnColor;

    private Game(Board board, Point point, GameState gameState, PieceColor turnColor) {
        this.board = board;
        this.point = point;
        this.gameState = gameState;
        this.turnColor = turnColor;
    }

    public static Game game(Board board, String pieceColor) {
        Point point = new Point(board);
        PieceColor turnColor = PieceColor.pieceColorByName(pieceColor);
        return new Game(board, point, GameState.START, turnColor);
    }

    public static Game newGame() {
        InitializedBoard initializedBoard = new InitializedBoard();
        Board board = new Board(initializedBoard.board());

        return game(board, "W");
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

    public void move(String source, String target) {
        Position sourcePosition = Position.from(source);
        Position targetPosition = Position.from(target);
        board.move(sourcePosition, targetPosition, turnColor);
        if (board.kingIsDead()) {
            gameState = GameState.END;
        }

        this.turnColor = turnColor.oppositeColor();
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
