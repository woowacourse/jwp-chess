package wooteco.chess.domain.piece;

import java.util.List;

import wooteco.chess.domain.board.BoardSituation;
import wooteco.chess.domain.position.Position;

public interface MoveStrategy {

    List<Position> getMovablePositions(Position source, BoardSituation boardSituation);

}
