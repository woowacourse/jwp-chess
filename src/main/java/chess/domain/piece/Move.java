package chess.domain.piece;

import chess.domain.piece.condition.BishopMoveCondition;
import chess.domain.piece.condition.CatchingPieceBlackPawnMoveCondition;
import chess.domain.piece.condition.CatchingPieceWhitePawnMoveCondition;
import chess.domain.piece.condition.FirstTurnBlackPawnMoveCondition;
import chess.domain.piece.condition.FirstTurnWhitePawnMoveCondition;
import chess.domain.piece.condition.KingMoveCondition;
import chess.domain.piece.condition.KnightMoveCondition;
import chess.domain.piece.condition.MoveCondition;
import chess.domain.piece.condition.NormalBlackPawnMoveCondition;
import chess.domain.piece.condition.NormalWhitePawnMoveCondition;
import chess.domain.piece.condition.QueenMoveCondition;
import chess.domain.piece.condition.RookMoveCondition;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Move {
    P("P", Arrays.asList(new FirstTurnBlackPawnMoveCondition(), new NormalBlackPawnMoveCondition(),
        new CatchingPieceBlackPawnMoveCondition())),
    K("K", Collections.singletonList(new KingMoveCondition())),
    N("N", Collections.singletonList(new KnightMoveCondition())),
    B("B", Collections.singletonList(new BishopMoveCondition())),
    Q("Q", Collections.singletonList(new QueenMoveCondition())),
    R("R", Collections.singletonList(new RookMoveCondition())),
    p("p", Arrays.asList(new FirstTurnWhitePawnMoveCondition(), new NormalWhitePawnMoveCondition(),
        new CatchingPieceWhitePawnMoveCondition())),
    k("k", Collections.singletonList(new KingMoveCondition())),
    n("n", Collections.singletonList(new KnightMoveCondition())),
    b("b", Collections.singletonList(new BishopMoveCondition())),
    q("q", Collections.singletonList(new QueenMoveCondition())),
    r("r", Collections.singletonList(new RookMoveCondition()));

    private final List<MoveCondition> moveConditions;
    private String notation;

    Move(String notation, List<MoveCondition> moveConditions) {
        this.notation = notation;
        this.moveConditions = moveConditions;
    }

    public List<MoveCondition> getMoveCondition() {
        return moveConditions;
    }
}
