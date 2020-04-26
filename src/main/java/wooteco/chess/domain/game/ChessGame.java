package wooteco.chess.domain.game;

import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.pieces.Pieces;
import wooteco.chess.domain.piece.pieces.PiecesInitializer;
import wooteco.chess.domain.position.MovingPosition;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.PositionFactory;
import wooteco.chess.domain.position.positions.Positions;

import java.util.List;
import java.util.stream.Collectors;

public class ChessGame {
    private Pieces pieces;
    private Turn turn;

    public ChessGame() {
        pieces = PiecesInitializer.operate();
        turn = new Turn(Color.WHITE);
    }

    public void move(MovingPosition movingPosition) {
        pieces.move(movingPosition.getStartPosition(), movingPosition.getEndPosition(), turn.getColor());
        turn = turn.change();
    }

    public ScoreResult calculateScore() {
        return new ScoreResult(pieces);
    }

    public boolean isKingDead() {
        return pieces.isKingDead();
    }

    public Color getAliveKingColor() {
        return pieces.getAliveKingColor();
    }

    public Positions findMovablePositions(Position position) {
        Piece piece = pieces.findBy(position, turn.getColor());
        return piece.createMovablePositions(pieces.getPieces());
    }

    public List<String> findMovablePositionNames(String position) {
        return this.findMovablePositions(PositionFactory.of(position))
                .getPositions()
                .stream()
                .map(Position::toString)
                .collect(Collectors.toList());
    }

    public Turn getTurn() {
        return turn;
    }

    public Pieces getPieces() {
        return pieces;
    }
}
