package wooteco.chess.domain.piece;

import static wooteco.chess.domain.piece.Direction.*;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.board.Column;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.board.Row;
import wooteco.chess.domain.player.PlayerColor;

public class Knight extends GamePiece {

    private static final String NAME = "n";
    private static final double SCORE = 2.5;
    private static final int MOVE_COUNT = 1;
    private static List<Position> originalPositions = Arrays.asList(Position.of(Column.B, Row.ONE),
            Position.of(Column.G, Row.ONE));

    public Knight(PlayerColor playerColor) {
        super(NAME, SCORE, playerColor, Arrays.asList(NNE, NEE, SEE, SSE, SSW, SWW, NWW, NNW), MOVE_COUNT);
    }

    @Override
    public List<Position> getOriginalPositions() {
        return playerColor.reviseInitialPositions(originalPositions);
    }
}
