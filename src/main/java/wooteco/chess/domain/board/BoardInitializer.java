package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.strategy.initialize.BishopInitializer;
import wooteco.chess.domain.strategy.initialize.InitializeStrategy;
import wooteco.chess.domain.strategy.initialize.KingInitializer;
import wooteco.chess.domain.strategy.initialize.KnightInitializer;
import wooteco.chess.domain.strategy.initialize.PawnInitializer;
import wooteco.chess.domain.strategy.initialize.QueenInitializer;
import wooteco.chess.domain.strategy.initialize.RookInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardInitializer {
    private static final List<InitializeStrategy> INITIALIZER;

    static {
        INITIALIZER = new ArrayList<>(Arrays.asList(
                new KingInitializer(),
                new QueenInitializer(),
                new RookInitializer(),
                new KnightInitializer(),
                new BishopInitializer(),
                new PawnInitializer()
        ));
    }

    public static Map<Position, Piece> initializeAll() {
        Map<Position, Piece> board = new HashMap<>();

        for (InitializeStrategy strategy : INITIALIZER) {
            board.putAll(strategy.initialize());
        }

        return board;
    }
}
