package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.BoardSituation;
import wooteco.chess.domain.position.Position;

import java.util.List;

public interface MoveStrategy {

    List<Position> getMovablePositions(Position source, BoardSituation boardSituation);

}
