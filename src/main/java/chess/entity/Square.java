package chess.entity;

import chess.domain.chessboard.ChessBoard;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Square {

    private String position;
    private String symbol;
    private String color;
    private int gameId;

    public Square(String position, Piece piece, int gameId) {
        this.position = position;
        this.symbol = piece.getSymbol().name();
        this.color = piece.getColor().name();
        this.gameId = gameId;
    }

    public Square(String position, String symbol, String color, int gameId) {
        this.position = position;
        this.symbol = symbol;
        this.color = color;
        this.gameId = gameId;
    }

    public Square(String position, String symbol, String color) {
        this.position = position;
        this.symbol = symbol;
        this.color = color;
    }

    public static List<Square> from(ChessBoard chessBoard, int gameId) {
        Map<Position, Piece> pieces = chessBoard.getPieces();
        List<Square> squares = pieces.entrySet().stream()
                .map(entry -> new Square(entry.getKey().getValue(), entry.getValue().getSymbol().name(),
                        entry.getValue().getColor().name(), gameId))
                .collect(Collectors.toList());
        return squares;
    }

    public String getPosition() {
        return position;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }

    public int getGameId() {
        return gameId;
    }

    @Override
    public String toString() {
        return "Square{" +
                "position='" + position + '\'' +
                ", symbol='" + symbol + '\'' +
                ", color='" + color + '\'' +
                ", gameId=" + gameId +
                '}';
    }
}
