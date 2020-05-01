package wooteco.chess.domain.piece;

import java.util.List;

import wooteco.chess.domain.board.BoardSituation;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public interface PieceState {

    PieceState move(Position target, BoardSituation boardSituation);

    List<Position> getMovablePositions(BoardSituation boardSituation);

    PieceType getPieceType();

    Team getTeam();

    double getPoint(BoardSituation boardSituation);
}
