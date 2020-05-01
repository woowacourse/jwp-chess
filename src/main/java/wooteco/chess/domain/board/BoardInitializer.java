package wooteco.chess.domain.board;

import java.util.Map;

import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.position.Position;

public interface BoardInitializer {

    Map<Position, PieceState> create();

}
