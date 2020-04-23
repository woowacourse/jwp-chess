package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.strategy.BishopStrategy;
import wooteco.chess.domain.piece.strategy.KingStrategy;
import wooteco.chess.domain.piece.strategy.KnightStrategy;
import wooteco.chess.domain.piece.strategy.PawnStrategy;
import wooteco.chess.domain.piece.strategy.QueenStrategy;
import wooteco.chess.domain.piece.strategy.RookStrategy;
import wooteco.chess.domain.piece.strategy.Strategy;

public enum Type {
    KING("k", new KingStrategy(), 0.0),
    QUEEN("q", new QueenStrategy(), 9.0),
    ROOK("r", new RookStrategy(), 5.0),
    BISHOP("b", new BishopStrategy(), 3.0),
    KNIGHT("n", new KnightStrategy(), 2.5),
    PAWN("p", new PawnStrategy(), 1.0),
    EMPTY(".", Strategy.empty(), 0.0);

    private String name;
    private Strategy strategy;
    private double score;

    Type(final String name, final Strategy strategy, final double score) {
        this.name = name;
        this.strategy = strategy;
        this.score = score;
    }

    public boolean isNotEmpty() {
        return this != EMPTY;
    }

    public boolean initPosition(Position position, Side side) {
        return strategy.shouldBePlacedOn(position, side);
    }

    public boolean canMoveBetween(Path path) {
        if (path.distanceSquare() == 0) {
            return false;
        }
        return strategy.isMovableWithin(path);
    }

    public String getName() {
        return name;
    }

    public String getNameBy(Side side) {
        if (side == Side.WHITE) {
            return name;
        }
        return name.toUpperCase();
    }

    public double getScore() {
        return score;
    }
}
