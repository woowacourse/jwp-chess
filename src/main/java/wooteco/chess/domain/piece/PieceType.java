package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.move.BishopMoveStrategy;
import wooteco.chess.domain.move.KingMoveStrategy;
import wooteco.chess.domain.move.KnightMoveStrategy;
import wooteco.chess.domain.move.MoveStrategy;
import wooteco.chess.domain.move.PawnMoveStrategy;
import wooteco.chess.domain.move.QueenMoveStrategy;
import wooteco.chess.domain.move.RookMoveStrategy;

public enum PieceType {
    KING("k", 0, new KingMoveStrategy()),
    QUEEN("q", 9, new QueenMoveStrategy()),
    ROOK("r", 5, new RookMoveStrategy()),
    BISHOP("b", 3, new BishopMoveStrategy()),
    KNIGHT("n", 2.5, new KnightMoveStrategy()),
    PAWN("p", 1, new PawnMoveStrategy());

    private final String symbol;
    private final double score;
    private final MoveStrategy moveStrategy;

    PieceType(String symbol, double score, MoveStrategy moveStrategy) {
        this.symbol = symbol;
        this.score = score;
        this.moveStrategy = moveStrategy;
    }

    public boolean movable(Position source, Position target, Board board) {
        return this.moveStrategy.movable(source, target, board);
    }

    public boolean isKing() {
        return this == KING;
    }

    public boolean isPawn() {
        return this == PAWN;
    }

    public double getScore() {
        return score;
    }

    public String getSymbol() {
        return symbol;
    }
}