package chess.domain.piece;

import static chess.domain.Color.BLACK;
import static chess.domain.Color.WHITE;

import chess.domain.ChessBoard;
import chess.domain.Color;
import chess.domain.Position;

public final class Piece {

    private final long chessGameId;
    private final Color color;
    private final PieceRule pieceRule;

    public Piece(long chessGameId, Color color, PieceRule pieceRule) {
        this.chessGameId = chessGameId;
        this.color = color;
        this.pieceRule = pieceRule;
    }

    public static Piece createWhitePiece(long chessGameId, PieceRule pieceRule) {
        return new Piece(chessGameId, WHITE, pieceRule);
    }

    public static Piece createBlackPiece(long chessGameId, PieceRule pieceRule) {
        return new Piece(chessGameId, BLACK, pieceRule);
    }

    public Piece move(Position source, Position target, ChessBoard chessBoard) {
        return new Piece(chessGameId, color, pieceRule.move(source, target, chessBoard));
    }

    public String convertedName() {
        return pieceRule.convertedName(color);
    }

    public boolean isSameColor(Color color) {
        return this.color == color;
    }

    public boolean isSameTeamPiece(Piece piece) {
        return isSameColor(piece.color);
    }

    public double score() {
        return pieceRule.score();
    }

    public boolean isPawn() {
        return pieceRule.isPawn();
    }

    public boolean isKing() {
        return pieceRule.isKing();
    }

    public Color color() {
        return color;
    }

    public String name() {
        return pieceRule.name();
    }
}
