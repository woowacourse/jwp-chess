package wooteco.chess.domain.board;

import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.position.Position;

import java.util.Map;

public interface BoardInitializer {

    Map<Position, PieceState> create();

}
