package wooteco.chess.domain.piece;

import static wooteco.chess.domain.piece.Direction.*;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.board.Column;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.board.Row;
import wooteco.chess.domain.player.PlayerColor;

public class Rook extends GamePiece {

    private static final String NAME = "r";
    private static final int SCORE = 5;
    private static final int MOVE_COUNT = 8;
    private static List<Position> originalPositions = Arrays.asList(Position.of(Column.A, Row.ONE),
        Position.of(Column.H, Row.ONE));

    public Rook(PlayerColor playerColor) {
        super(NAME, SCORE, playerColor, Arrays.asList(N, E, W, S), MOVE_COUNT);
    }

    @Override
    public List<Position> getOriginalPositions() {
        return playerColor.reviseInitialPositions(originalPositions);
    }
}
